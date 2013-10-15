package eRegulation;

public class Circuit_Gradient extends Circuit_Abstract
{

	public Circuit_Gradient(String name, String friendlyName, String circuitType, String tempMax, String rampUpTime)
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
			case CIRCUIT_STATE_Starting:
				if (temperatureGradient == null)
				{
					LogIt.error("Circuit_Mixer", "sequencer", "temperatureGradient is null");
				}
				else
				{
					Integer temp							= temperatureGradient.getTempToTarget();
					this.heatRequired.tempMinimum			= temp - 75;
					this.heatRequired.tempMaximum			= temp + 75;
					if (Global.thermoBoiler.reading > this.heatRequired.tempMinimum)
					{
						LogIt.action("PumpRadiator", "On");
						Global.pumpRadiator.on();
						state										= CIRCUIT_STATE_Running;
					}
				}
				break;
			case CIRCUIT_STATE_Running:
				Integer temp							= temperatureGradient.getTempToTarget();
				this.heatRequired.tempMinimum			= temp - 75;
				this.heatRequired.tempMaximum			= temp + 75;
				break;
			case CIRCUIT_STATE_Stopping:
				if (Global.circuits.isSingleActiveCircuit())
				{
					this.optimise();
				}
				else
				{
					Global.pumpRadiator.off();
					this.shutDown();
				}
				break;
			case CIRCUIT_STATE_Error:
				break;
			default:
				LogIt.error("Circuit", "sequencerRadiator", "unknown state detected : " + state);	
			}
		}
	}
}
