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
			//===========================================================
			// Here we detect that a task has just finished its time slot
			//
			if (Global.getTimeNowSinceMidnight() > taskActive.timeEnd)
			{
				state										= CIRCUIT_STATE_Stopping;
				taskActive.state							= taskActive.TASK_STATE_Completed;
			}
			//
			//===========================================================
			switch (state)
			{
			case CIRCUIT_STATE_Off:
				//Nothing to do
				break;
			case CIRCUIT_STATE_Starting:
				LogIt.info("Circuit", "sequencerRadiator", "Started");	
				LogIt.action("PumpRadiator", "On");
				Global.pumpRadiator.on();
				state										= CIRCUIT_STATE_Running;		
				break;
			case CIRCUIT_STATE_Running:
				// Nothing to do
				//The temps will depend on circuit type (h/w, radiator etc.
				//Will also depend on outside temp
				//Will also depend on loi d'eau

				//Radiator Type has temperature gradient
				Integer temp							= temperatureGradient.getTempToTarget();
				this.heatRequired.tempMinimum			= temp - 75;
				this.heatRequired.tempMaximum			= temp + 75;
				break;
			case CIRCUIT_STATE_Stopping:
				LogIt.info("Circuit", "sequencerRadiator", "Stopping");	
				LogIt.action("PumpRadiator", "Off");
				Global.pumpRadiator.off();
				state									= CIRCUIT_STATE_Off;
				taskActive								= null;
				break;
			case CIRCUIT_STATE_Error:
				break;
			default:
				LogIt.error("Circuit", "sequencerRadiator", "unknown state detected : " + state);	
			}
		}
	}
}
