package eRegulation;

import HVAC_Common.CIRCUIT;
import HVAC_Common.Ctrl_Configuration;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Circuit_Radiator extends Circuit_Abstract
{
	public Circuit_Radiator(Ctrl_Configuration.Circuit				paramCircuit)			// New
	{	
		super(paramCircuit);

		this.temperatureGradient				= new TemperatureGradient(paramCircuit.tempGradient);
	}
	public Long getRampUpTime()
	{
		Long 													rampUpMilliSeconds			= 30 * 60 * 1000L;		// 30 mins
		return rampUpMilliSeconds;
	}
	@Override
	public Long calculatePerformance()
	{
		// TODO Is this required
		return 10000L;
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
				state																		= CIRCUIT.STATE.Starting;
				//Now fall through
			case Starting:
				if (temperatureGradient == null)
				{
					LogIt.error("Circuit_" + this.name, "sequencer", "temperatureGradient is null");
					state																	= CIRCUIT.STATE.Error;
				}
				else
				{
					Integer 									temp						= temperatureGradient.getTempToTarget();
					this.heatRequired.tempMinimum											= temp - 7500;
					this.heatRequired.tempMaximum											= temp + 7500;
					state																	= CIRCUIT.STATE.AwaitingHeat;
				}
				break;
			case AwaitingHeat:
				if (Global.thermoBoiler.reading > this.heatRequired.tempMinimum)
				{
					LogIt.action("PumpRadiator", "On");
					circuitPump.on();
					state																	= CIRCUIT.STATE.Running;
				}
				break;
			case Running:
				Integer 										temp						= temperatureGradient.getTempToTarget();
				this.heatRequired.tempMinimum												= temp - 7500;
				this.heatRequired.tempMaximum												= temp + 7500;
				break;
			case Stop_Requested:
				LogIt.info("Circuit_" + this.name, "sequencer", "Stop Requested");
				state																		= CIRCUIT.STATE.Stopping;
				//Now fall through
			case Stopping:
				if 	(	(Global.circuits.isSingleActiveCircuit())
				&& 		(Global.thermoBoiler.reading > 40000) ) //Might as well get as much heat out of it as possible
				{
					// We are alone, so as long as there is heat to get out of the system
					// carry on
				}
				else
				{
					LogIt.action("PumpRadiator", "Off");
					circuitPump.off();
					this.shutDown();
				}
				break;
			case Error:
				break;
			default:
				LogIt.error("Circuit_" + this.name, "sequencer", "unknown state detected : " + state);	
			}
		}
	}
}
