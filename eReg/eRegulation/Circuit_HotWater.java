package eRegulation;

public class Circuit_HotWater extends Circuit_Abstract
{
	public Circuit_HotWater(String name, String friendlyName, String circuitType, String tempMax, String rampUp, String rampDown)
	{	
		super(name, friendlyName, circuitType, tempMax, rampUp, rampDown);
	}
	@Override
	public void sequencer()
	{
		this.heatRequired.tempMinimum				= -1;
		this.heatRequired.tempMaximum				= -1;
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
				state								= CIRCUIT_STATE_Stopping;
				taskActive.state					= CircuitTask.TASK_STATE_Completed;
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
				
				// Switching pump on can cause heat being pumped the wrong way
				// Leave as is on the basis that 1) short lived and 2) easier than doing it later 
				// while avoiding repeated pump action
				
				LogIt.action("PumpWater", "On");
				Global.pumpWater.on();
				state								= CIRCUIT_STATE_Running;		
				break;
				
			case CIRCUIT_STATE_Running:
				
				// Setup real energy requirements (preset to sero at beginning
				// This can be adjusted later with a better algorithm based on ernery statistics
				
				this.heatRequired.tempMinimum		= taskActive.tempObjective + 100;
				this.heatRequired.tempMaximum		= tempMax;
				
				if (Global.thermoHotWater.reading > taskActive.tempObjective)
				{
					state 							= CIRCUIT_STATE_Optimising;
				}
				
				break;
				
			case CIRCUIT_STATE_Optimising:
				
				if (Global.circuits.isSingleActiveCircuit())
				{
					// Keep going until all heat is taken out of boiler
					if (Global.thermoBoiler.reading < Global.thermoHotWater.reading + 20)
					{
						// set taskstate as running free as if other heat requirement
						// then this task can be abandoned at no energy cost
						taskActive.state			= CircuitTask.TASK_STATE_RunningFree;
						// Continue as there is still more heat
					}
					else
					{
						state						= CIRCUIT_STATE_Stopping;
					}
				}
				else
				{
					state							= CIRCUIT_STATE_Stopping;
				}
				// Now fall through to CIRCUIT_STATE_Stopping given that heatRequired is correctly set up

			case CIRCUIT_STATE_Stopping:
				
				// If active task.temporary = true
				// need to delete the object to avoid
				// memory leaks
					
				LogIt.action("PumpWater", "Off");
				Global.pumpWater.off();
				state								= CIRCUIT_STATE_Off;
				taskActive.state					= CircuitTask.TASK_STATE_Completed;
				taskActive							= null;
				
				break;
				
			case CIRCUIT_STATE_Error:
				
				LogIt.error("Circuit", "sequencerWater", "uCIRCUIT_STATE_Error detected : " + state);	

				break;
				
			default:
				
				LogIt.error("Circuit", "sequencerWater", "unknown state detected : " + state);	
			}
		}
	}	
}
