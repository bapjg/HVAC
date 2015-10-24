package eRegulation;

import HVAC_Common.CIRCUIT;
import HVAC_Common.Ctrl_Configuration;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Circuit_HotWater extends Circuit_Abstract
{
	public Circuit_HotWater(Ctrl_Configuration.Circuit paramCircuit)
	{
		super(paramCircuit);
	}
	
	//===========================================================================================================================================================
	//
	// Performance methods
	//
	@Override
	public Long getRampUpTime(Integer tempObjective)
	{
		// TODO calculate time function of HW temp and previous performance
		Long 													rampUpMilliSeconds			= 15 * 60 * 1000L;		// 15 mins
		return rampUpMilliSeconds;
	}
	public Boolean canOptimise()
	{
		return (Global.thermoBoiler.reading > Global.thermoHotWater.reading + 3000);
	}	
	//
	//===========================================================================================================================================================

	//===========================================================================================================================================================
	//
	// Activity/State Change methods
	//

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
		if (	(Global.thermoBoiler.reading 	== null) 
		||		(Global.thermoHotWater.reading 	== null)	)
		{
			super.shutDown();											// This bypasses stopRequested
			state 																			= HVAC_STATES.Circuit.Error;
			Global.eMailMessage("Circuit_HotWater/sequencer", "A Thermometer cannont be read");
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

		// DeltaHotWater > 0 means not yet at target
		// DeltaBoiler   > 0 means Boiler is over targetTemp
		Integer										deltaBoiler								= Global.thermoBoiler.reading - this.taskActive.tempObjective;
		Integer										deltaHotWater							= this.taskActive.tempObjective - Global.thermoHotWater.reading;	// -ve if overTempTarget
		Float										deltaRatio								= deltaBoiler.floatValue() / deltaHotWater.floatValue();
		Float 										deltaMinimum							= 10000F/7000F;		// ie more than enough to get to temperature
		
		switch (state)
		{
		case Off:
			//Nothing to do
			break;
		case Starting:
			this.heatRequired.set(this.taskActive.tempObjective + 10000, this.tempMax);
			if 		(Global.thermoBoiler.reading 	> Global.thermoHotWater.reading)
			{
				circuitPump.on();
				super.nowRunning();
			}
			break;
		case Running:
			
			// This is inline optimisation. 
			
			//	singleCircuit		stopOnObjective
			//		Yes					Yes					deltaRatio > deltaMinimum => optimise()   deltaHotWater < 0 => optimise()
			//		Yes					No			*		deltaRatio > deltaMinimum => optimise()   deltaHotWater < 0 => optimise()
			//		No					Yes					deltaHotWater < 0 => stop()
			//		No					No			*		deltaHotWater < 0 => suspend()
			
			if (this.taskActive.stopOnObjective)											// Stop On Objective
			{
				if (Global.circuits.isSingleActiveCircuit())								// Use optimisation
				{
					if 		(deltaHotWater  < 0           )			super.optimise();				// Over target
					else if (deltaRatio 	> deltaMinimum)			super.optimise();				// Enough energy left
					// Otherwise									keep running
				}
				else																		// No optimisation ass multiple circuits
				{
					if 		(deltaHotWater < 0)						super.stop();
				}
			}
//			else	// Task must continue until time up
//			{
//				if (Global.circuits.isSingleActiveCircuit())								// Use optimisation
//				{
//					if 		(deltaHotWater  < 0			  )			optimise();				// Over target
//					else if (deltaRatio 	> deltaMinimum)			optimise();				// Enough energy left
//					// Otherwise									keep running
//				}
//				else																		// Other circuits active
//				{
//					if 		(deltaHotWater < 0)						suspend();				// We are over target, but there are otheer tasks
//				}
//			}
			break;
			
		case Optimising:
			
			//	singleCircuit		stopOnObjective			deltaRatio > deltaMinimum		deltaHotWater < 0 (ie over temp)
			//		Yes					Yes					       optimise()    						optimise()
			//		Yes					No			*		       optimise()   					    optimise()
			//		No					Yes																stop()
			//		No					No			*													suspend()
			
			if 		(! Global.circuits.isSingleActiveCircuit())								super.shutDown();
			else if	(Global.thermoBoiler.reading < Global.thermoHotWater.reading + 3000)   						
																							super.shutDown();//  Continue while boilerTemp more than 3 degrees than return temp
			break;
		case Suspended:																		// In this state the circuitPump has been switched off If 5 degrees less than objective
			if (Global.thermoBoiler.reading < this.taskActive.tempObjective - 5000) 		resume();	
			break;
		case Resuming:																		// Setting state to starting will setup heat required etc, and turn on pump at the right time.
			this.start();
			break;
		case Stopping:
			if 	 	(Global.circuits.isSingleActiveCircuit())								this.optimise();							//  Continue while boilerTemp more than 3 degrees than return temp
			else 																			this.shutDown();
			break;
		case RampingUp:			// Only applies to floor
		case Idle:			// We only idle circuits when temp reached and keep pump on. Not applicable to HW
		case Error:
		default:
			LogIt.error("Circuit_" + this.name, "sequencer", "unknown state detected : " + state);	
		}
		// end of Normal Activity
		//
		//===========================================================================================================================================================
	}
	// end of Sequencer
	//
	//===========================================================================================================================================================
}
