package eRegulation;

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
			switch (state)
			{
			case Off:
				//Nothing to do
				break;
			case Start_Requested:
				LogIt.info("Circuit_" + this.name, "sequencer", "Start Requested");
				state												= CIRCUIT.STATE.Starting;
				//Now fall through
			case Starting:
				this.heatRequired.tempMinimum						= this.taskActive.tempObjective + 10000;
				this.heatRequired.tempMaximum						= this.tempMax;
				state												= CIRCUIT.STATE.AwaitingHeat;
				break;
			case AwaitingHeat:
				if (Global.thermoBoiler.reading > Global.thermoHotWater.reading)
				{
					LogIt.action("PumpHotWater", "On");
					circuitPump.on();
					state											= CIRCUIT.STATE.Running;
				}
				break;
			case Running:
				
				// This needs to be reapraised while running and if only circuit we can optimise based on statistics
				
				if (	(this.taskActive.stopOnObjective)
				&&		(Global.thermoHotWater.reading > this.taskActive.tempObjective)     )
				{
					this.heatRequired.tempMinimum						= 0;
					this.heatRequired.tempMaximum						= 0;
					stop();
				}
				else
				{
					this.heatRequired.tempMinimum						= this.taskActive.tempObjective + 10000;
					this.heatRequired.tempMaximum						= this.tempMax;
				}
				break;
			case Stop_Requested:
				LogIt.info("Circuit_" + this.name, "sequencer", "Stop Requested");
				state													= CIRCUIT.STATE.Stopping;
				//Now fall through
			case Stopping:
				if 	(	(Global.circuits.isSingleActiveCircuit())
				&& 		(Global.thermoBoiler.reading > Global.thermoHotWater.reading + 3000) ) 	
				{
					// Require atleast 3 degrees difference otherwise it takes ages
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
