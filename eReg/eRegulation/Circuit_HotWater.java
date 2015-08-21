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
				Global.eMailMessage("Circuit_HotWater/sequencer", "This may be very bad");
			}
			
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
				if (this.taskActive.stopOnObjective)
				{
					if (Global.circuits.isSingleActiveCircuit())		// Use optimisation
					{
						if (Global.thermoHotWater.reading > this.taskActive.tempObjective - 7000)
						{
							stop();	// stop sets heatRequired to null	// Will then go into optimisation mode
						}
					}
					else												// No optimisation
					{
						if (Global.thermoHotWater.reading > this.taskActive.tempObjective)
						{
							stop();	// stop sets heatRequired to null // Will then go stop
						}
					}
				}
				else
				{
					if (Global.circuits.isSingleActiveCircuit())		// Use optimisation
					{
						if (Global.thermoHotWater.reading > this.taskActive.tempObjective - 7000)
						{
							LogIt.info("Circuit_" + this.name, "sequencer", "Optimising");
							optimise();	// Sets heatRequired to zero (not null) Will then go into optimisation mode
						}
					}
					else												// No optimisation
					{
						if (Global.thermoHotWater.reading > this.taskActive.tempObjective)
						{
							LogIt.info("Circuit_" + this.name, "sequencer", "Suspending");
							circuitPump.off();
							suspend();	// Sets heatRequired to zero (not null)
						}
					}
				}
				break;
			case Optimising:
				if (Global.circuits.isSingleActiveCircuit())		// Use optimisation
				{
					if (Global.thermoBoiler.reading > Global.thermoHotWater.reading + 3000)
					{
						// Stay in optimisation mode
					}
					else
					{
						LogIt.info("Circuit_" + this.name, "sequencer", "Suspending");
						LogIt.action("PumpHotWater", "Off");
						circuitPump.off();
						suspend();									// Sets heatRequired to zero (not null)
					}
				}
				else												// A new circuit has been scheduled we are no longer in singleCircuit mode
				{
					if (Global.thermoHotWater.reading > this.taskActive.tempObjective)
					{
						LogIt.info("Circuit_" + this.name, "sequencer", "Suspending");
						LogIt.action("PumpHotWater", "Off");
						circuitPump.off();
						suspend();									// Sets heatRequired to zero (not null)
					}
					else
					{
						// Stay in optimisation mode
					}
				}
				break;
			case Suspended:
				// In this state the circuitPump is switched off
		
				if (Global.thermoBoiler.reading < this.taskActive.tempObjective - 5000) // If 5 degrees less than objective	
				{
					resume();
				}
				break;
			case Resuming:
				LogIt.info("Circuit_" + this.name, "sequencer", "Resuming");
				// Setting state to starting will setup heat required etc.
				state																		= CIRCUIT.STATE.Starting;
				break;
			case Stop_Requested:
				LogIt.info("Circuit_" + this.name, "sequencer", "Stop Requested");
				state													= CIRCUIT.STATE.Stopping;
				//Now fall through
			case Stopping:
				if 	(	(Global.circuits.isSingleActiveCircuit())
				&& 		(Global.thermoBoiler.reading > Global.thermoHotWater.reading + 3000) ) 	
				{
					// Require at least 3 degrees difference otherwise it takes ages
					// We are alone, so as long as there is heat to get out of the system
					// carry on
				}
				else
				{
					LogIt.action("PumpHotWater", "Off");
					circuitPump.off();
					this.shutDown();					// shutDown sets state to off. Threadmixer looks at this as signal to stop
				}
				break;
			case Error:
				break;
			default:
				LogIt.error("Circuit_" + this.name, "sequencer", "unknown state detected : " + state);	
			}
		}
	}
	public Long getRampUpTime()
	{
		// TODO calculate time function of HW temp and previous performance
		Long 													rampUpMilliSeconds			= 15 * 60 * 1000L;		// 15 mins
		return rampUpMilliSeconds;
	}
}
