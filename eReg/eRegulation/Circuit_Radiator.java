package eRegulation;

public class Circuit_Radiator extends Circuit_Abstract
{

	public Circuit_Radiator(String name, String friendlyName, String circuitType, String tempMax, String rampUpTime)
	{	
		super(name, friendlyName, circuitType, tempMax, rampUpTime);
	}
	public Long getRampUpTime()
	{
		return 0L;
	}
	@Override
	public Long calculatePerformance()
	{
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
			case CIRCUIT_STATE_Off:
				//Nothing to do
				break;
			case CIRCUIT_STATE_Start_Requested:
				LogIt.info("Circuit_" + this.name, "sequencer", "Start Requested");
				state												= CIRCUIT_STATE_Starting;
				//Now fall through
			case CIRCUIT_STATE_Starting:
				if (temperatureGradient == null)
				{
					LogIt.error("Circuit_" + this.name, "sequencer", "temperatureGradient is null");
					state										= CIRCUIT_STATE_Error;
				}
				else
				{
					Integer temp							= temperatureGradient.getTempToTarget();
					this.heatRequired.tempMinimum			= temp - 75;
					this.heatRequired.tempMaximum			= temp + 75;
					state									= CIRCUIT_STATE_AwaitingHeat;
				}
				break;
			case CIRCUIT_STATE_AwaitingHeat:
				if (Global.thermoBoiler.reading > this.heatRequired.tempMinimum)
				{
					LogIt.action("PumpRadiator", "On");
					Global.pumpRadiator.on();
					state										= CIRCUIT_STATE_Running;
				}
				break;
			case CIRCUIT_STATE_Running:
				Integer temp							= temperatureGradient.getTempToTarget();
				this.heatRequired.tempMinimum			= temp - 75;
				this.heatRequired.tempMaximum			= temp + 75;
				break;
			case CIRCUIT_STATE_Stop_Requested:
				LogIt.info("Circuit_" + this.name, "sequencer", "Stop Requested");
				state												= CIRCUIT_STATE_Stopping;
				//Now fall through
			case CIRCUIT_STATE_Stopping:
				if 	(	(Global.circuits.isSingleActiveCircuit())
				&& 		(Global.thermoBoiler.reading > 400) ) //Might as well get as much heat out of it as possible
				{
					// We are alone, so as long as there is heat to get out of the system
					// carry on
				}
				else
				{
					LogIt.action("PumpRadiator", "Off");
					Global.pumpRadiator.off();
					this.shutDown();
				}
				break;
			case CIRCUIT_STATE_Error:
				break;
			default:
				LogIt.error("Circuit_" + this.name, "sequencer", "unknown state detected : " + state);	
			}
		}
	}
}
