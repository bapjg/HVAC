package eRegulation;

public class Circuit_Mixer extends Circuit_Abstract
{
	public final int 				CIRCUIT_STATE_Off 				= 0;
	public final int 				CIRCUIT_STATE_Started 			= 1;
	public final int 				CIRCUIT_STATE_Running 			= 2;
	public final int 				CIRCUIT_STATE_Stopping	 		= 3;
	public final int 				CIRCUIT_STATE_Optimising 		= 4;
	public final int 				CIRCUIT_STATE_Error	 			= -1;

	public Circuit_Mixer(String name, String friendlyName, String circuitType, String tempMax, String rampUpTime)
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
			case CIRCUIT_STATE_Started:
				LogIt.info("Circuit", "sequencerFloor", "Thread Started");	
				Thread thread_mixer 						= new Thread(new Thread_Mixer(mixer, this), "Mixer");
				thread_mixer.start();
				// Need to ensure that pump and mixer dont go on at the same time
				
				Global.waitSeconds(1);
				state										= CIRCUIT_STATE_Running;		
				break;
			case CIRCUIT_STATE_Running:
				//The temps will depend on circuit type (h/w, radiator etc.
				//Will also depend on outside temp
				//Will also depend on loi d'eau

				//Mixer Type has temperature gradient
				if (temperatureGradient == null)
				{
					System.out.println("temperatureGradient is null");
				}
				Integer temp								= temperatureGradient.getTempToTarget();
				this.heatRequired.tempMinimum				= 500;
				this.heatRequired.tempMaximum				= 800;
				break;
			case CIRCUIT_STATE_Stopping:
				LogIt.info("Circuit", "sequencerFloor", "Stopping");
				//
				// Need to figure out how to stop a thread
				// xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
				//
				LogIt.action("PumpFloor", "Off");
				Global.pumpFloor.off();
				state										= CIRCUIT_STATE_Off;
				taskActive									= null;
				break;
			case CIRCUIT_STATE_Error:
				break;
			default:
				LogIt.error("Circuit", "sequencerFloor", "unknown state detected : " + state);	
			}
		}
	}
}
