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
		if 	(Global.thermoBoiler.reading 		== null) 
		{
			shutDown();																		// This bypasses stopRequested
			circuitPump.off();
			state = HVAC_STATES.Circuit.Error;
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
			if (Global.thermoBoiler.reading > Global.thermoHotWater.reading) 				// We can start pumping heat (was : > this.heatRequired.tempMinimum)
			{
				LogIt.action("PumpRadiator", "On");
				circuitPump.on();
				super.nowRunning();
//				state 																		= HVAC_STATES.Circuit.Running;
			}
			break;
		case Running:
			this.heatRequired.tempMinimum													= temp - 7500;
			this.heatRequired.tempMaximum													= temp + 7500;
			break;
		case Stopping:
			if 	 	(Global.circuits.isSingleActiveCircuit())								this.optimise();							//  Continue while boilerTemp more than 3 degrees than return temp
			else 																			this.shutDown();
			break;
		case Optimising:
			if 		(! Global.circuits.isSingleActiveCircuit())								super.shutDown();
			else if	(Global.thermoBoiler.reading < temp - 7500	)   						super.shutDown();//  Continue while boilerTemp more than 3 degrees than return temp
			break;	// Continue as singleCircuit and some heat left in system
		case Resuming:
		case RampingUp:
		case Idle:
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
}
