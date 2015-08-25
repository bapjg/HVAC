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
	@Override
	public void sequencer()
	{
		if (taskActive == null)
		{
			//Nothing to do
		}
		else
		{
			if (	(Global.thermoBoiler.reading 	== null) 
			||		(Global.thermoHotWater.reading 	== null)	)
			{
				shutDown();											// This bypasses stopRequested
				circuitPump.off();
				state																		= CIRCUIT.STATE.Error;
				Global.eMailMessage("Circuit_HotWater/sequencer", "A Thermometer cannont be read");
			}

			// DeltaHotWater > 0 means not yet at target
			// DeltaBoiler   > 0 means Boiler is over target temp
			Integer										deltaBoiler							= Global.thermoBoiler.reading - this.taskActive.tempObjective;
			Integer										deltaHotWater						= this.taskActive.tempObjective - Global.thermoHotWater.reading;
			Float										deltaRatio							= deltaBoiler.floatValue() / deltaHotWater.floatValue();
			Float 										deltaMinimum						= 10000F/7000F;
			
			switch (state)
			{
			case Off:
				//Nothing to do
				break;
			case Start_Requested:
				LogIt.info("Circuit_" + this.name, "sequencer", "Start Requested");
				state																		= CIRCUIT.STATE.Starting;
				//Now fall through
			case Starting:
				this.heatRequired.tempMinimum												= this.taskActive.tempObjective + 10000;
				this.heatRequired.tempMaximum												= this.tempMax;
				state																		= CIRCUIT.STATE.AwaitingHeat;
				break;
			case AwaitingHeat:
				if 		(Global.thermoBoiler.reading 	> Global.thermoHotWater.reading)
				{
					LogIt.action("PumpHotWater", "On");
					circuitPump.on();
					state																	= CIRCUIT.STATE.Running;
				}
				break;
				
			case Running:
				
				//	singleCircuit		stopOnObjective
				//		Yes					Yes					deltaRatio > deltaMinimum => optimise()   deltaHotWater < 0 => optimise()
				//		Yes					No			*		deltaRatio > deltaMinimum => optimise()   deltaHotWater < 0 => optimise()
				//		No					Yes					deltaHotWater < 0 => stop()
				//		No					No			*		deltaHotWater < 0 => suspend()
				
				
				
				
				if (this.taskActive.stopOnObjective)										// Stop On Objective
				{
					if (Global.circuits.isSingleActiveCircuit())							// Use optimisation
					{
						if 		(deltaHotWater  < 0           )		optimise();				// Over target
						else if (deltaRatio 	> deltaMinimum)		optimise();				// Enough energy left
						// Otherwise								keep running
					}
					else																	// No optimisation
					{
						if 		(deltaHotWater < 0)					stop();
					}
				}
				else																		// Task must continue until time up
				{
					if (Global.circuits.isSingleActiveCircuit())							// Use optimisation
					{
						if 		(deltaHotWater  < 0			  )		optimise();				// Over target
						else if (deltaRatio 	> deltaMinimum)		optimise();				// Enough energy left
						// Otherwise								keep running
					}
					else																	// Other circuits active
					{
						if 		(deltaHotWater < 0)					suspend();
					}
				}
				break;
				
			case Optimising:
				
				//	singleCircuit		stopOnObjective
				//		Yes					Yes					deltaRatio > deltaMinimum => optimise()   deltaHotWater < 0 => optimise()
				//		Yes					No			*		deltaRatio > deltaMinimum => optimise()   deltaHotWater < 0 => optimise()
				//		No					Yes					deltaHotWater < 0 => stop()
				//		No					No			*		deltaHotWater < 0 => suspend()
				
				if (Global.circuits.isSingleActiveCircuit())								// Use optimisation
				{
					if (Global.thermoBoiler.reading > Global.thermoHotWater.reading + 3000)
					{
						// Stay in optimisation mode
					}
					else																	// No useful energy left
					{
						if (this.taskActive.stopOnObjective)		stop();
						else 										suspend();				// Sets heatRequired to zero (not null)
					}
				}
				else																		// Several circuits running
				{
					if (deltaHotWater < 0)													// Target reached
					{
						if (this.taskActive.stopOnObjective)		stop();
						else 										suspend();				// Sets heatRequired to zero (not null)
					}
					else
					{
						// Stay in optimisation mode
					}
				}
				break;
			case Suspended:																	// In this state the circuitPump has been switched off
				if (Global.thermoBoiler.reading < this.taskActive.tempObjective - 5000) // If 5 degrees less than objective	
				{
					resume();
				}
				break;
			case Resuming:																	// Setting state to starting will setup heat required etc.
				LogIt.info("Circuit_" + this.name, "sequencer", "Resuming");
				state																		= CIRCUIT.STATE.Starting;
				break;
			case Stop_Requested:
				LogIt.info("Circuit_" + this.name, "sequencer", "Stop Requested");
				state																		= CIRCUIT.STATE.Stopping;
				//Now fall through
			case Stopping:
				LogIt.action("PumpHotWater", "Off");
				circuitPump.off();
				this.shutDown();					// shutDown sets state to off. Threadmixer looks at this as signal to stop
				break;
			case Error:
				break;
			default:
				LogIt.error("Circuit_" + this.name, "sequencer", "unknown state detected : " + state);	
			}
		}
	}
	@Override
	public Long getRampUpTime(Integer tempObjective)
	{
		// TODO calculate time function of HW temp and previous performance
		Long 													rampUpMilliSeconds			= 15 * 60 * 1000L;		// 15 mins
		System.out.println("Circuit_HW/rampUpTime : " + rampUpMilliSeconds);
		return rampUpMilliSeconds;
	}
}
