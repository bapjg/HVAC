package eRegulation;

import HVAC_Common.CIRCUIT;
import HVAC_Common.Ctrl_Configuration;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Circuit_Mixer extends Circuit_Abstract
{
	private Integer												lastAccurateFloorInTemp		= 21000;
	
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
		return (Global.thermoBoiler.reading > lastAccurateFloorInTemp + 3000);
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
		
//		if (taskActive == null)									return;			//Nothing to do	
		
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
			this.requestShutDown();											// This bypasses stopRequested
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
		//		Off,					// Internal                          	// Inactive circuit. taskActive should be null
		//		StartRequested,			// External via requestStart        	// Setup up heatRequired and sets state to AwaitingHeat		
		//		RampingUp,				// Internal                          	// Floor : FloorOut is at max temp to shorten rampUp Time. When close to target normal tempControl used.
		//		Running,				// Internal                          	// Keeps on running until some sort of event occurs
		//		StopRequested,			// External via requestStop			 	// Decides whether to optimise or shut down
		//		OptimisationRequested,	// External via requestOptimisation 	// 
		//		Optimising,				// Internal 						 	// Unclear : is this an inTask initiative or a Background task initiative
		//		ShutDownRequested,		// Internal via requestShutDown     	// Switches off the circuitPump and calls shutDown (sets heatRequired to null; and sets state to Off)
		//
		//		Suspended,				// Internal 						 	// Hot_Water : if not stop on objective, suspends all activity but surveys hwTemp
		//		 															 	// resume is called to set the state to Resuming
		//		Resuming,				// Internal 						 	// Hot_Water : hwTemp is below minimum, so reactivates heatRequired
		//		Idle,					// Internal 						 	// State for pump on but no heatRequired. Used for for floor circuit inlineOptimise
		//
		//		Error					// Internal    						 	// Some sort of error has occured
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
		case StartRequested:
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
					this.heatRequired.setZero();
					state 																	= HVAC_STATES.Circuit.IdleRequested;
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
			if (Global.thermoLivingRoom.reading > this.taskActive.tempObjective)			// This keeps the floor pump going
			{																				
				state 																		= HVAC_STATES.Circuit.IdleRequested;
			}
			break;
		case StopRequested:
			if 	 	(Global.circuits.isSingleActiveCircuit())		this.requestOptimisation();							//  Continue while boilerTemp more than 3 degrees than return temp
			else 													this.requestShutDown();
			break;
		case OptimisationRequested :
			this.heatRequired.setZero();
			this.circuitPump.on();
			state 																			= HVAC_STATES.Circuit.AwaitingMixer;
			break;
		case AwaitingMixer :
			break;																			// Just wait for Thread_Mixer to do its work
		case MixerReady :
			state 																			= HVAC_STATES.Circuit.Optimising;
			break;																			// Just wait for Thread_Mixer to do its work
		case Optimising:
			lastAccurateFloorInTemp															= Global.thermoFloorIn.reading;
			if 		(! Global.circuits.isSingleActiveCircuit())		this.requestShutDown();
			else if	(! this.canOptimise())   						this.requestShutDown();	//  Continue while boilerTemp more than 3 degrees than return temp
			break;																		// Continue as singlecircuit AND canOptimise = true
		case ShutDownRequested:
			circuitPump.off();
			this.heatRequired.setZero();
			state 																			= HVAC_STATES.Circuit.Off;
			this.taskActive																	= null;
			break;
		case Suspended:
			this.heatRequired.setZero();
			// Used if outSide temp > Summer temp
		case Resuming:
// Corrected 31/01/2017
			
// Was :
//			LogIt.error("Circuit_" + this.name, "sequencer", "state error detected : " + state.toString());
			
// Now is :			
			this.requestStart();
			LogIt.display("Circuit_" + this.name, "sequencer", "State = Resuming, start requested");
			break;
		case IdleRequested:
			LogIt.display("Circuit_" + this.name, "sequencer", "idle requested with LivingRomm at : " + Global.thermoLivingRoom.reading.toString());
			LogIt.display("Circuit_" + this.name, "sequencer", "idle requested with Objective  at : " + this.taskActive.tempObjective.toString());
			state 																			= HVAC_STATES.Circuit.Idle;	
			break;
		case Idle:
			lastAccurateFloorInTemp															= Global.thermoFloorIn.reading;
			if (Global.thermoLivingRoom.reading < this.taskActive.tempObjective) // OR Floor return temp too cold
			{
				state 																		= HVAC_STATES.Circuit.StartRequested;	
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
