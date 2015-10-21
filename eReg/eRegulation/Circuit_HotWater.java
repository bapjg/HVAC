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
		this.heatRequired.set(this.taskActive.tempObjective + 10000, this.tempMax);
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
		// Check for work
		if (taskActive == null)									return;			//Nothing to do				

		// Check for security / errors
		if (	(Global.thermoBoiler.reading 	== null) 
		||		(Global.thermoHotWater.reading 	== null)	)
		{
			shutDown();											// This bypasses stopRequested
			circuitPump.off();
			state																			= HVAC_STATES.Circuit.Error;
			Global.eMailMessage("Circuit_HotWater/sequencer", "A Thermometer cannont be read");
		}

		// Do normal activity
		// DeltaHotWater > 0 means not yet at target
		// DeltaBoiler   > 0 means Boiler is over target temp
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
			if 		(Global.thermoBoiler.reading 	> Global.thermoHotWater.reading)
			{
				LogIt.action("PumpHotWater", "On");
				circuitPump.on();
				stateChange(HVAC_STATES.Circuit.Running);
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
					if 		(deltaHotWater  < 0           )			this.state 				= HVAC_STATES.Circuit.Idle;			// Over target
					else if (deltaRatio 	> deltaMinimum)			this.state 				= HVAC_STATES.Circuit.Idle;			// Enough energy left
					// Otherwise									keep running
				}
				else																		// No optimisation
				{
					if 		(deltaHotWater < 0)						this.state 				= HVAC_STATES.Circuit.Stopping;
				}
			}
			else																			// Task must continue until time up
			{
				if (Global.circuits.isSingleActiveCircuit())								// Use optimisation
				{
					if 		(deltaHotWater  < 0			  )			optimise();				// Over target
					else if (deltaRatio 	> deltaMinimum)			optimise();				// Enough energy left
					// Otherwise									keep running
				}
				else																		// Other circuits active
				{
					if 		(deltaHotWater < 0)						suspend();
				}
			}
			break;
			
		case Idle:
			// Pump is kept on
		case Optimising:
			
			//	singleCircuit		stopOnObjective
			//		Yes					Yes					deltaRatio > deltaMinimum => optimise()   deltaHotWater < 0 => optimise()
			//		Yes					No			*		deltaRatio > deltaMinimum => optimise()   deltaHotWater < 0 => optimise()
			//		No					Yes					deltaHotWater < 0 => stop()
			//		No					No			*		deltaHotWater < 0 => suspend()
			
			if (Global.circuits.isSingleActiveCircuit())									// Use optimisation
			{
				if (Global.thermoBoiler.reading > Global.thermoHotWater.reading + 3000)
				{
					// Stay in optimisation mode
				}
				else																		// No useful energy left
				{
					if (this.taskActive.stopOnObjective)			stop();
					else 											suspend();				// Sets heatRequired to zero (not null)
				}
			}
			else																			// Several circuits running
			{
				if (deltaHotWater < 0)														// Target reached
				{
					if (this.taskActive.stopOnObjective)			stop();
					else 											suspend();				// Sets heatRequired to zero (not null)
				}
				else
				{
					// Stay in optimisation mode
				}
			}
			break;
		case Suspended:																		// In this state the circuitPump has been switched off
			if (Global.thermoBoiler.reading < this.taskActive.tempObjective - 5000) 		// If 5 degrees less than objective	
			{
				resume();
			}
			break;
		case Resuming:																		// Setting state to starting will setup heat required etc.
			LogIt.info("Circuit_" + this.name, "sequencer", "Resuming");
			state																			= HVAC_STATES.Circuit.Starting;
			break;
		case Stopping:
			LogIt.action(this.name, "Closing down completely");
			LogIt.action("PumpHotWater", "Off");
			circuitPump.off();
			this.heatRequired.setZero();
			this.state																		= HVAC_STATES.Circuit.Off;
			break;
		case Error:
			break;
		default:
			LogIt.error("Circuit_" + this.name, "sequencer", "unknown state detected : " + state);	
		}
	}	// sequencer
	//
	//===========================================================================================================================================================
}
