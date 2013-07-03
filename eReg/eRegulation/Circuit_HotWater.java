package eRegulation;

public class Circuit_HotWater extends Circuit_Abstract
{
	public final int 				CIRCUIT_STATE_Off 				= 0;
	public final int 				CIRCUIT_STATE_Started 			= 1;
	public final int 				CIRCUIT_STATE_Running 			= 2;
	public final int 				CIRCUIT_STATE_Stopping	 		= 3;
	public final int 				CIRCUIT_STATE_Optimising 		= 4;
	public final int 				CIRCUIT_STATE_Error	 			= -1;

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
				state						= CIRCUIT_STATE_Stopping;
				activeTask.state			= activeTask.TASK_STATE_Completed;
			}
			//
			//===========================================================

			switch (state)
			{
			case CIRCUIT_STATE_Off:
				
				//Nothing to do
				break;
				
			case CIRCUIT_STATE_Started:
				
				LogIt.info("Circuit", "sequencerWater", "Started");	
				Global.pumpWater.on();
				state								= CIRCUIT_STATE_Running;		
				break;
				
			case CIRCUIT_STATE_Running:
				
				// Nothing to do
				this.heatRequired.tempMinimum		= activeTask.tempObjective + 100;
				this.heatRequired.tempMaximum		= tempMax;
				
				if (Global.thermoHotWater.reading > activeTask.tempObjective)
				{
					state = CIRCUIT_STATE_Stopping;
				}
				
				break;
				
			case CIRCUIT_STATE_Stopping:
				
				// If active task.temporary = true
				// need to delete the object to avoid
				// memory leaks
				System.out.println("Single activetasks " + Global.circuits.isSingleActiveCircuit());

				if (Global.thermoBoiler.reading < Global.thermoHotWater.reading + 20)
				{
					//No more heat can be taken out of the system
					Global.pumpWater.off();
					state							= CIRCUIT_STATE_Off;
					activeTask						= null;
				}
				
				break;
				
			case CIRCUIT_STATE_Error:
				
				break;
				
			default:
				
				LogIt.error("Circuit", "sequencerWater", "unknown state detected : " + state);	
			}
		}
	}	
}
