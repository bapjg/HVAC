package eRegulation;

import HVAC_Common.CIRCUIT;
import HVAC_Common.Ctrl_Configuration;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Circuit_Radiator extends Circuit_Abstract
{
	public Circuit_Radiator(Ctrl_Configuration.Circuit				paramCircuit)			// New
	{	
		super(paramCircuit);

		this.temperatureGradient															= new TemperatureGradient(paramCircuit.tempGradient);
	}
	
	//===========================================================================================================================================================
	//
	// Performance methods
	//
	@Override 
	public Long getRampUpTime(Integer tempObjective)
	{
		Long 													rampUpMilliSeconds			= 30 * 60 * 1000L;		// 30 mins
		return rampUpMilliSeconds;
	}
	@Override 
	public Boolean canOptimise()
	{
		return false;
	}
/**
 * Does nothing... Cannot be optimised
 */
//	public void startOptimisation()
//	{
//		return;
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
		Integer 									temp									= temperatureGradient.getTempToTarget();
		this.heatRequired.set(temp - 7500, temp + 7500);
	}
	//
	//===========================================================================================================================================================

	//===========================================================================================================================================================
	//
	// Sequencer
	//
	@Override
	public void sequencer()
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
		if 	(Global.thermoBoiler.reading == null) 
		{
			super.initiateShutDown();																		// This bypasses stopRequested
			state 																			= HVAC_STATES.Circuit.Error;
			Global.eMailMessage("Circuit_Radiator/sequencer", "A Thermometer cannont be read");
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
		Integer 											temp						= temperatureGradient.getTempToTarget();	// Outside temp may change

		switch (state)
		{
		case Off:
			//Nothing to do
			break;
		case Starting:
			this.heatRequired.tempMinimum													= temp - 7500;
			this.heatRequired.tempMaximum													= temp + 7500;
			circuitPump.on();
			state 																			= HVAC_STATES.Circuit.RampingUp;
			break;
		case RampingUp:
			if (Global.thermoBoiler.reading > 0) 											// We could have a temperature condition here
			{
				LogIt.action("PumpRadiator", "On");
				circuitPump.on();
				state 																		= HVAC_STATES.Circuit.Running;
			}
			break;
		case Running:
			this.heatRequired.tempMinimum													= temp - 7500;
			this.heatRequired.tempMaximum													= temp + 7500;
			break;
		case Stopping:
			if 	 	(Global.circuits.isSingleActiveCircuit())								this.initiateOptimisation();							//  Continue while boilerTemp more than 3 degrees than return temp
			else 																			this.initiateShutDown();
			break;
		case BeginningOptimisation :
			this.heatRequired.setZero();
			state 																			= HVAC_STATES.Circuit.Optimising;
			break;
		case Optimising:
			if 		(! Global.circuits.isSingleActiveCircuit())								super.initiateShutDown();
			else if	(Global.thermoBoiler.reading < temp - 7500	)   						super.initiateShutDown();//  Continue while boilerTemp more than 3 degrees than return temp
			break;	// Continue as singleCircuit and some heat left in system
		case Suspended:
		case Resuming:
		case Idle:
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
