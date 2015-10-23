package eRegulation;

import HVAC_Common.CIRCUIT;
import HVAC_Common.Ctrl_Configuration;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Circuit_Mixer extends Circuit_Abstract
{
	private Integer												lastAccurateFloorInTemp		= 25000;
	
	public Circuit_Mixer(Ctrl_Configuration.Circuit paramCircuit)
	{
		super(paramCircuit);

		this.temperatureGradient															= new TemperatureGradient(paramCircuit.tempGradient);
		this.mixer																			= new Mixer(paramCircuit.mixer);
	}
	
	//===========================================================================================================================================================
	//
	// Performance methods
	//
	public Long getRampUpTime(Integer tempObjective)
	{
		Integer													tempNow						= Global.thermoLivingRoom.reading;
		if (tempNow != null)
		{
			Integer													tempDifference			= tempObjective - tempNow;
	
			// Work on basis of 		:	6               hours   per degree
			// or						:   6 x 60 x 60     seconds per degree
			// or                       :   6 x 3600 x 1000 milliSeconds per degree
			// or                       :   6 x 3600        milliSeconds per milliDegree
			
			if (tempDifference > 0)
			{
				Long												rampUpMilliSeconds		= 6L * 3600 * tempDifference;		// 6 hours per degree
				return rampUpMilliSeconds;
			}
			return 0L;
		}
		else
		{
			this.state = HVAC_STATES.Circuit.Error;
			return 0L;
		}
	}
	public Boolean canOptimise()
	{
		return (Global.thermoBoiler.reading > lastAccurateFloorInTemp + 3000);
	}	
	//
	//===========================================================================================================================================================

	//===========================================================================================================================================================
	//
	// Activity/State Change methods
	//
	@Override
	public void start()
	{
		super.start();
		this.heatRequired.set(55000, 80000);																// this.taskActive.tempObjective + 10000;
		// this.heatRequired.tempMaximum														= 80000;		// this.tempMax;
	}
	@Override
	public void shutDown()
	{
		this.mixer.positionZero();
		super.shutDown();
	}
	//
	//===========================================================================================================================================================

	//===========================================================================================================================================================
	//
	// Sequencer
	//
	@Override
	public void sequencer()		// Called from main loop
	{
		//===========================================================================================================================================================
		//
		// Check for work
		//
		
		if (taskActive == null)									return;			//Nothing to do	
		
		// end of Check for work
		//
		//===========================================================================================================================================================

		//===========================================================================================================================================================
		//
		// Check for security / errors
		//
		if (	(Global.thermoBoiler.reading 		== null) 
		||		(Global.thermoLivingRoom.reading 	== null)	)
		{
			shutDown();											// This bypasses stopRequested
			// TODO Should we not close the mixer
			circuitPump.off();
			state 																			= HVAC_STATES.Circuit.Error;
			Global.eMailMessage("Circuit_Mixer/sequencer", "A Thermometer cannont be read");
		}
		// end of Check for security / errors
		//
		//===========================================================================================================================================================

		//===========================================================================================================================================================
		//
		// List of possible states
		//
		//		Off,				// Inactive circuit. taskActive should be null
		//		Starting,			// Setup up heatRequired and sets state to AwaitingHeat		
		//		Resuming,			// Hot_Water : hwTemp is below minimum, so reactivates heatRequired
		//		RampingUp,			// Floor : FloorOut is at max temp to shorten rampUp Time. When close to target normal tempControl used.
		//		Running,			// Kepps on running until some sort of event occurs
		//		Optimising,			// Unclear : is this an inTask initiative or a Background task initiative
		//		Stopping,			// Switches off the circuitPump and calls shutDown (sets heatRequired to null; and sets state to Off)
		//
		//		Idle,				// State for pump on but no heatRequired. Used for for floor circuit inlineOptimise
		//		Suspended,			// Hot_Water : if not stop on objective, suspends all activity but surveys hwTemp
		//							// resume is called to set the state to Resuming
		//
		//		Error				// Some sort of error has occured		
		//
		// end of State List
		//
		//===========================================================================================================================================================

		//===========================================================================================================================================================
		//
		// Do normal activity
		//

		switch (state)
		{
		case Off:
			//Nothing to do
			break;
		case Starting:
			if (Global.isSummer())					suspend();
			this.heatRequired.tempMinimum												= 55000;		// Avoid condensation
			this.heatRequired.tempMaximum												= 80000;

			if (Global.thermoLivingRoom.reading > this.taskActive.tempObjective)
			{
				circuitPump.on();														// CircuitPump must be on in order to obtain correct temperature readings
				idle();
			}
			if (Global.thermoBoiler.reading > this.heatRequired.tempMinimum)
			{
				circuitPump.on();														// Switch on circuit pump now
				state 																	= HVAC_STATES.Circuit.RampingUp;
			}
			break;
		case RampingUp:
			lastAccurateFloorInTemp														= Global.thermoFloorIn.reading;
			if (Global.thermoLivingRoom.reading > this.taskActive.tempObjective - 1000)	// Otherwise Stay in rampUp mode
			{
				nowRunning();
			}
			break;
		case Running:
			//TODO if(temp > summerTemp)
//		{
//			suspend();
//			break;
//		}
			lastAccurateFloorInTemp														= Global.thermoFloorIn.reading;
			if (Global.thermoLivingRoom.reading > this.taskActive.tempObjective)
			{
				idle();			// This keeps the floor pump going
			}
			break;
		case Idle:
			lastAccurateFloorInTemp														= Global.thermoFloorIn.reading;
			if (Global.thermoLivingRoom.reading < this.taskActive.tempObjective) // OR Floor return temp too cold
			{
				resume();			// This keeps the floor pump going
			}
			break;
		case Resuming:
			lastAccurateFloorInTemp														= Global.thermoFloorIn.reading;
			start();
			break;
		case Optimising:
			lastAccurateFloorInTemp														= Global.thermoFloorIn.reading;
			if 		(! Global.circuits.isSingleActiveCircuit())		this.shutDown();
			else if	(! this.canOptimise())   						this.shutDown();	//  Continue while boilerTemp more than 3 degrees than return temp
			break;	// Continue as singlecircuit AND canOptimise = true
		case Stopping:
			if 	 	(Global.circuits.isSingleActiveCircuit())		this.optimise();							//  Continue while boilerTemp more than 3 degrees than return temp
			else 													this.shutDown();
			break;
		case Suspended:
		case Error:
		default:
			LogIt.error("Circuit_" + this.name, "sequencer", "state error detected : " + state.toString());
			break;
		}
		// end of Normal Activity
		//
		//===========================================================================================================================================================
	}
	// end of Sequencer
	//
	//===========================================================================================================================================================

	//===========================================================================================================================================================
	//
	// Other methods
	//
/**
 * Starts the circuit in optimisation mode:
 * Supplied circuitTask becomes taskActive
 * State set to Start_Requested
 * heatRequired set to zero valued object
 */
	public void optimiseFloor()
	{
		LogIt.action(this.name, "----------------------------------optimiseFloor called");
		Long											now									= Global.Time.now();
		Integer											targetTemperature					= Global.thermoLivingRoom.reading + 2000;	// Go for 2 degrees above current temperature
		CircuitTask										task								= new CircuitTask(	
																												now, 	// Time Start
																												now + 5L * 60L * 1000L, 	// TimeEnd in 5 mins
																												targetTemperature,	// TempObjective in millidesrees
																												false,	// StopOnObjective
																												"1, 2, 3, 4, 5, 6, 7",					// Days
																												HVAC_TYPES.CircuitTask.DontKnow
				);
		this.taskActive																		= task;
		this.circuitPump.on();
		this.state = HVAC_STATES.Circuit.Optimising;
		this.heatRequired.setZero();
	}
	//
	//===========================================================================================================================================================
}
