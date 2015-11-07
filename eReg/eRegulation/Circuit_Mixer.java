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
			this.state 																		= HVAC_STATES.Circuit.Error;
			return 0L;
		}
	}
	public Boolean canOptimise()
	{
		Integer lastAccurate = lastAccurateFloorInTemp + 3000;
		LogIt.debug("Floor/canOptimise thermoBoiler : " + Global.thermoBoiler.reading + ", lastAccurate + 3000 : " + lastAccurate.toString());
		return (Global.thermoBoiler.reading > lastAccurateFloorInTemp + 3000);
	}	
/**
 * Starts the circuit in optimisation mode:
 * Supplied circuitTask becomes taskActive
 * State set to Start_Requested
 * heatRequired set to zero valued object
 */
//	@Override
//	public void startOptimisation()
//	{
//		super.startOptimisation();
//	}
	//
	//===========================================================================================================================================================

	//===========================================================================================================================================================
	//
	// Activity/State Change methods
	//
	@Override
	public void initiateStart()
	{
		super.initiateStart();
		this.heatRequired.setMax();
	}
/**
 * Shuts down the circuit :
 * State = Off.
 * circuitPump = OFF.
 * heatRequired.max/min = ZERO.
 * task will be deactivated by scheduler.
 * Floor specific : positionMixer = ZERO
*/
	@Override
	public void initiateShutDown()
	{
//		this.mixer.positionZero();
		super.initiateShutDown();
	}
/**
 * Optimises the circuit :
 * State = Optimising.
 * circuitPump = UNCHANGED.
 * heatRequired.max/min = ZERO.
 * circuitPump = ON.
 * Floor specific : positionMixer = 20%
 */	
	@Override
	public void optimise()
	{
		// TODO : Kludge
		this.state	= HVAC_STATES.Circuit.Optimising; // Must be done here as mixer positionning takes time, and scheduler will set taskActive to null
		this.mixer.positionPercentage(0.20F);
		super.optimise();
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
			this.initiateShutDown();											// This bypasses stopRequested
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
			if (Global.isSummer())			
			{
				state 																		= HVAC_STATES.Circuit.Suspended;
			}
			else
			{
				this.heatRequired.setMax();				// Avoid condensation
				circuitPump.on();															// CircuitPump must be on in order to obtain correct temperature readings
	
				if (Global.thermoLivingRoom.reading > this.taskActive.tempObjective)
				{
					state 																	= HVAC_STATES.Circuit.Idle;
				}
				if (Global.thermoBoiler.reading > this.heatRequired.tempMinimum)
				{
					state 																	= HVAC_STATES.Circuit.RampingUp;
				}
			}
			break;
		case RampingUp:
			lastAccurateFloorInTemp															= Global.thermoFloorIn.reading;
			if (Global.thermoLivingRoom.reading > this.taskActive.tempObjective - 1000)		// Otherwise Stay in rampUp mode
			{
				state 																		= HVAC_STATES.Circuit.Running;
			}
			break;
		case Running:
			//TODO if(temp > summerTemp)
//		{
//			suspend();
//			break;
//		}
			lastAccurateFloorInTemp															= Global.thermoFloorIn.reading;
			if (Global.thermoLivingRoom.reading > this.taskActive.tempObjective)
			{																				// This keeps the floor pump going
				state 																		= HVAC_STATES.Circuit.Idle;
			}
			break;
		case Stopping:
			if 	 	(Global.circuits.isSingleActiveCircuit())		this.initiateOptimisation();							//  Continue while boilerTemp more than 3 degrees than return temp
			else 													this.initiateShutDown();
			break;
		case BeginningOptimisation :
			this.heatRequired.setZero();
			state 																			= HVAC_STATES.Circuit.Optimising;
			break;
		case Optimising:
			lastAccurateFloorInTemp															= Global.thermoFloorIn.reading;
			if 		(! Global.circuits.isSingleActiveCircuit())		this.initiateShutDown();
			else if	(! this.canOptimise())   						this.initiateShutDown();	//  Continue while boilerTemp more than 3 degrees than return temp
			break;																		// Continue as singlecircuit AND canOptimise = true
		case ShuttingDown:
			circuitPump.off();
			this.heatRequired.setZero();
			state 																			= HVAC_STATES.Circuit.Off;	
			break;
		case Suspended:
			// Used if outSide temp > Summer temp
		case Resuming:
			LogIt.error("Circuit_" + this.name, "sequencer", "state error detected : " + state.toString());
			break;
		case Idle:
			lastAccurateFloorInTemp															= Global.thermoFloorIn.reading;
			if (Global.thermoLivingRoom.reading < this.taskActive.tempObjective) // OR Floor return temp too cold
			{
				state 																		= HVAC_STATES.Circuit.Starting;	
			}
			break;
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
}
