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
	public Long getRampUpTime(Integer tempObjective)
	{
		Long 													rampUpMilliSeconds			= 30 * 60 * 1000L;		// 30 mins
		return rampUpMilliSeconds;
	}
//	@Override
//	public Long calculatePerformance()
//	{
//		// TODO Is this required
//		return 10000L;
//	}
	@Override
	public void sequencer()
	{
		if (taskActive == null)
		{
			//Nothing to do
		}
		else
		{
			if 	(Global.thermoBoiler.reading 		== null) 
			{
				shutDown();											// This bypasses stopRequested
				circuitPump.off();
				state												= HVAC_STATES.Circuit.Error;
				Global.eMailMessage("Circuit_Radiator/sequencer", "A Thermometer cannont be read");
			}

			
			switch (state)
			{
			case Off:
				//Nothing to do
				break;
			case Start_Requested:
				LogIt.info("Circuit_" + this.name, "sequencer", "Start Requested");
				state																		= HVAC_STATES.Circuit.Starting;
				//Now fall through
			case Starting:
				if (temperatureGradient == null)
				{
					LogIt.error("Circuit_" + this.name, "sequencer", "temperatureGradient is null");
					state																	= HVAC_STATES.Circuit.Error;
				}
				else
				{
					Integer 									temp						= temperatureGradient.getTempToTarget();
					this.heatRequired.tempMinimum											= temp - 7500;
					this.heatRequired.tempMaximum											= temp + 7500;
					state																	= HVAC_STATES.Circuit.AwaitingHeat;
				}
				break;
			case AwaitingHeat:
				if (Global.thermoBoiler.reading > this.heatRequired.tempMinimum)
				{
					LogIt.action("PumpRadiator", "On");
					circuitPump.on();
					state																	= HVAC_STATES.Circuit.Running;
				}
				break;
			case Running:
				Integer 										temp						= temperatureGradient.getTempToTarget();
				this.heatRequired.tempMinimum												= temp - 7500;
				this.heatRequired.tempMaximum												= temp + 7500;
				break;
			case Stop_Requested:
				LogIt.info("Circuit_" + this.name, "sequencer", "Stop Requested");
				state																		= HVAC_STATES.Circuit.Stopping;
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
