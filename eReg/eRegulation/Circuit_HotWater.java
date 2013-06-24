package eRegulation;

public class Circuit_HotWater extends Circuit_Abstract
{
	public final int 				STATE_Off 				= 0;
	public final int 				STATE_Started 			= 1;
	public final int 				STATE_Running 			= 2;
	public final int 				STATE_Stopping	 		= 3;
	public final int 				STATE_Optimising 		= 4;
	public final int 				STATE_Error	 			= -1;

	public Circuit_HotWater(String name, String friendlyName, String circuitType, String tempMax, String rampUp, String rampDown)
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
				state						= STATE_Stopping;
				activeTask.state			= activeTask.STATE_Completed;
			}
			//
			//===========================================================

			switch (state)
			{
			case STATE_Off:
				//Nothing to do
				break;
			case STATE_Started:
				LogIt.info("Circuit", "sequencerWater", "Started");	
				Global.pumpWater.on();
				state								= STATE_Running;		
				break;
			case STATE_Running:
				// Nothing to do
				this.heatRequired.tempMinimum		= activeTask.tempObjective + 100;
				this.heatRequired.tempMaximum		= tempMax;
				break;
			case STATE_Stopping:
				LogIt.info("Circuit", "sequencerWater", "Stopping");	
				
				// If active task.temporary = true
				// need to delete the object to avoid
				// memory leaks
				System.out.println("Single activetasks " + Global.circuits.isSingleActiveCircuit());

				if (Global.thermoBoiler.reading < Global.thermoHotWater.reading + 20)
				{
					//No more heat can be taken out of the system
					Global.pumpWater.off();
					state							= STATE_Off;
					activeTask						= null;
				}
				break;
			case STATE_Error:
				break;
			default:
				LogIt.error("Circuit", "sequencerWater", "unknown state detected : " + state);	
			}
		}
	}	
}
