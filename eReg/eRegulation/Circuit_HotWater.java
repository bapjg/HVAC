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
		if (this.taskActive == null)
		{
			//Nothing to do
		}
		else
		{
			//===========================================================
			// Here we detect that a task has just finished its time slot
			//
			if (Global.getTimeNowSinceMidnight() > this.taskActive.timeEnd)
			{
				this.state							= CIRCUIT_STATE_Stopping;
				this.taskActive.state				= CircuitTask.TASK_STATE_Completed;
			}
			//
			//===========================================================

			switch (this.state)
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
				
				LogIt.error("Circuit", "sequencerWater", "CIRCUIT_STATE_Error detected : " + state);	

				break;
				
			default:
				
				LogIt.error("Circuit", "sequencerWater", "unknown state detected : " + state);	
			}
		}
	}
	public void sequencer_NEW()
	{
		// Note that this wont pass midnight
		// Whould need to stop automatically at 23:55
		
		
		
		this.heatRequired.tempMinimum				= -1;
		this.heatRequired.tempMaximum				= -1;
		
		if (this.taskActive != null)
		{
			Boolean		tempObjectiveAttained		= (Global.thermoHotWater.reading > taskActive.tempObjective);
			Boolean		timeUp						= (Global.getTimeNowSinceMidnight() > this.taskActive.timeEnd);
			Boolean		boilerHotEnough				= (Global.thermoBoiler.reading > Global.thermoHotWater.reading);
			
			Boolean		nowStop						= false;
			
			if (this.taskActive.stopOnObjective)
			{
				if (!tempObjectiveAttained)
				{
					this.heatRequired.tempMinimum	= this.taskActive.tempObjective + 100;
					this.heatRequired.tempMaximum	= this.tempMax;
				}
				
				if (boilerHotEnough)
				{
					Global.pumpWater.on();
				}
				else
				{
					Global.pumpWater.off();
				}
				
				if (tempObjectiveAttained)
				{
					if (Global.circuits.isSingleActiveCircuit() && boilerHotEnough)
					{
						// Water is up to temp
						// We have a single circuit and the boiler still has extra heat
						// so keep pumping
						nowStop						= false;
					}
					else
					{
						// Either the boiler isn't hot enough or there are other circuits active
						// so we should stop now
						nowStop						= true;
					}
				}
			}
			else
			{
				if (timeUp)
				{
					if (Global.circuits.isSingleActiveCircuit() && boilerHotEnough)
					{
						// We have a single circuit and the boiler still has extra heat
						// so keep pumping
						nowStop						= false;
					}
					else
					{
						// Either the boiler isn't hot enough or there are other circuits active
						// so we should stop now
						nowStop						= true;
					}
				}
				else
				{
					if (!tempObjectiveAttained)
					{
						this.heatRequired.tempMinimum	= this.taskActive.tempObjective + 100;
						this.heatRequired.tempMaximum	= this.tempMax;
					}
					if (boilerHotEnough)
					{
						Global.pumpWater.on();
					}
					else
					{
						Global.pumpWater.off();
					}
				}
			}
			
			if (nowStop)
			{
				Global.pumpWater.off();
				this.taskActive				= null;
			}
		}
	}	
}
