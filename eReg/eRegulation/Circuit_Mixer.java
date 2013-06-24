package eRegulation;

public class Circuit_Mixer extends Circuit_Abstract
{
	public final int 				STATE_Off 				= 0;
	public final int 				STATE_Started 			= 1;
	public final int 				STATE_Running 			= 2;
	public final int 				STATE_Stopping	 		= 3;
	public final int 				STATE_Optimising 		= 4;
	public final int 				STATE_Error	 			= -1;

	public Circuit_Mixer(String name, String friendlyName, String circuitType, String tempMax, String rampUp, String rampDown)
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
				state										= STATE_Stopping;
				activeTask.state							= activeTask.STATE_Completed;
			}
			//
			//===========================================================
			switch (state)
			{
			case STATE_Off:
				//Nothing to do
				break;
			case STATE_Started:
				LogIt.info("Circuit", "sequencerFloor", "Thread Started");	
				Thread thread_mixer 						= new Thread(new Thread_Mixer(mixer, this), "Mixer");
				thread_mixer.start();
				// Need to ensure that pump and mixer dont go on at the same time
				
				Global.waitSeconds(1);
				state										= STATE_Running;		
				break;
			case STATE_Running:
				//The temps will depend on circuit type (h/w, radiator etc.
				//Will also depend on outside temp
				//Will also depend on loi d'eau

				//Mixer Type has temperature gradient
				if (temperatureGradient == null)
				{
					System.out.println("temperatureGradient is null");
				}
				Integer temp									= temperatureGradient.getTempToTarget();
				this.heatRequired.tempMinimum			= 500;
				this.heatRequired.tempMaximum			= 800;
				// Nothing to do
				break;
			case STATE_Stopping:
				LogIt.info("Circuit", "sequencerFloor", "Stopping");
				//
				// Need to figure out how to stop a thread
				// xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
				//
				Global.pumpFloor.off();
				state										= STATE_Off;
				activeTask									= null;
				break;
			case STATE_Error:
				break;
			default:
				LogIt.error("Circuit", "sequencerFloor", "unknown state detected : " + state);	
			}
		}
	}
}
