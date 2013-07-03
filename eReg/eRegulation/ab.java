package eRegulation;

public class ab extends aa
{

	public ab(String name, String friendlyName, String circuitType, String tempMax, String rampUp, String rampDown)
	{
		super(name, friendlyName, circuitType, tempMax, rampUp, rampDown);
	}
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
				state						= CIRCUIT_STATE_Stopping;
				activeTask.state			= activeTask.TASK_STATE_Completed;
			}
			//
			//===========================================================

			switch (state)
			{
			case CIRCUIT_STATE_Off:


			}

		}
		state = CIRCUIT_STATE_Off;
	}

}
