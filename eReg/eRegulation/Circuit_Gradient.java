package eRegulation;

public class Circuit_Gradient extends Circuit_Abstract
{
//	public final int 				CIRCUIT_STATE_Off 				= 0;
//	public final int 				CIRCUIT_STATE_Started 			= 1;
//	public final int 				CIRCUIT_STATE_Running 			= 2;
//	public final int 				CIRCUIT_STATE_Stopping	 		= 3;
//	public final int 				CIRCUIT_STATE_Optimising 		= 4;
//	public final int 				CIRCUIT_STATE_Error	 			= -1;


	public Circuit_Gradient(String name, String friendlyName, String circuitType, String tempMax, String rampUp, String rampDown)
	{	
		super(name, friendlyName, circuitType, tempMax, rampUp, rampDown);
	}
	@Override
	public void sequencer()
	{
		this.heatRequired.tempMinimum			= -1;
		this.heatRequired.tempMaximum			= -1;
		if (activeTask == null)
		{
			//Nothing to do
		}
		else
		{
			//===========================================================
			// Here we detect that a task has just finished its time slot
			//
			if (Global.getTimeNowSinceMidnight() > activeTask.timeEnd)
			{
				state										= CIRCUIT_STATE_Stopping;
				activeTask.state							= activeTask.TASK_STATE_Completed;
			}
			//
			//===========================================================
			switch (state)
			{
			case CIRCUIT_STATE_Off:
				//Nothing to do
				break;
			case CIRCUIT_STATE_Started:
				LogIt.info("Circuit", "sequencerRadiator", "Started");	
				Global.pumpRadiator.on();
				state										= CIRCUIT_STATE_Running;		
				break;
			case CIRCUIT_STATE_Running:
				// Nothing to do
				//The temps will depend on circuit type (h/w, radiator etc.
				//Will also depend on outside temp
				//Will also depend on loi d'eau

				//Radiator Type has temperature gradient
				Integer temp			= temperatureGradient.getTempToTarget();
				this.heatRequired.tempMinimum			= temp - 75;
				this.heatRequired.tempMaximum			= temp + 75;
				break;
			case CIRCUIT_STATE_Stopping:
				LogIt.info("Circuit", "sequencerRadiator", "Stopping");	
				Global.pumpRadiator.off();
				state										= CIRCUIT_STATE_Off;
				activeTask									= null;
				break;
			case CIRCUIT_STATE_Error:
				break;
			default:
				LogIt.error("Circuit", "sequencerRadiator", "unknown state detected : " + state);	
			}
			// This is a comment for git
		}
	}
}
