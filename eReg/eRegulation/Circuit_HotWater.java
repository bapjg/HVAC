package eRegulation;

public class Circuit_HotWater extends Circuit_Abstract
{
	public Circuit_HotWater(String name, String friendlyName, String circuitType, String tempMax, String rampUpTime)
	{	
		super(name, friendlyName, circuitType, tempMax, rampUpTime);
	}
	public void sequencer_OLD()
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
				this.stop();
				this.taskActive.state				= CircuitTask.TASK_STATE_Completed;
			}
			//
			//===========================================================

			switch (this.state)
			{
			case CIRCUIT_STATE_Off:
				
				//Nothing to do
				break;
				
			case CIRCUIT_STATE_Starting:
				
				LogIt.info("Circuit", "sequencerWater", "Started");	
				
				// Switching pump on can cause heat being pumped the wrong way
				// Leave as is on the basis that 1) short lived and 2) easier than doing it later 
				// while avoiding repeated pump action
				
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

	public Long getRampUpTime()
	{
		// System.out.println("Calculating rampUpTime for HW");
		Integer hwTempTarget						= this.taskNext.tempObjective;
		Integer hwTempCurrent 						= Global.thermoHotWater.reading;
		Integer hwTempDifference 					= hwTempTarget - hwTempCurrent;
		
		//System.out.println("hwTempDifference : " + hwTempDifference);
		
		// Boiler went from 300 -> 700 (Delta = 400) including over shoot of 70
		// HW     went from 300 -> 460 (Delta = 160)
		// And it took 40 mins ie 10 mins = 4 degrees
		//    or                  600 s   = 4 degrees
		//    or                  150 s   = 1 degree
		//    or                   15 s   = 0.1 degree
		
		Integer  boilerTempDifference 				= hwTempDifference * 400 / 160;
		Integer  boilerTempCutoff					= boilerTempDifference - 70; 			//expect a 7 degree overshoot		
		
		// Need to put cutoff in the boiler temp somewhere
		
		Long	rampUpTime							= hwTempDifference * 15L * 1000L; 		//15000 ms per decidegree 
				
		if (rampUpTime > 0)
		{
			return rampUpTime;
		}
		else
		{
			return 0L;
		}
	}
	@Override
	public Long calculatePerformance()
	{
		Integer hwTempTarget						= this.taskNext.tempObjective;
		Integer hwTempCurrent 						= Global.thermoHotWater.reading;
		Integer hwTempDifference 					= hwTempTarget - hwTempCurrent;
		
		// Boiler went from 300 -> 700 (Delta = 400) including over shoot of 70
		// HW     went from 300 -> 460 (Delta = 160)
		// And it took 40 mins ie 10 mins = 4 degrees
		//    or                  600 s   = 4 degrees
		//    or                  150 s   = 1 degree
		//    or                   15 s   = 0.1 degree
		
		Integer  boilerTempDifference 				= hwTempDifference * 400 / 160;
		Integer  boilerTempCutoff					= boilerTempDifference - 70; 			//expect a 7 degree overshoot		
		
		// Need to put cutoff in the boiler temp somewhere
		
		Long	rampUpTime							= hwTempDifference * 15L * 1000L; 		//15000 ms per decidegree 
				
		return rampUpTime;
		
		// To go from 300 -> 460 (delta 160)
		// We need boiler to go to 300 + 2xdelta = 620
		// Boiler will go up to 620 + 70 (overshoot)
		
		// Single circuit
		
		// boilerMin = hwTempTarget
		// boilerMax = hwTempCurrent + hwTempDifference x 2 (2 multiplier can be modified)
		//   boilerMax will improve by 70 overshoot
		// hwTarget = hwTempCurrent + hwDifference x 0.5  (0.5 multiplier can be modified)
	}
	@Override
	public void start()
	{
	}
	@Override
	public void stop()
	{
		// Either the boiler isn't hot enough or there are other circuits active
		// so we should stop now
		
		// We should call super().stop()
		
		this.heatRequired.tempMinimum	= -1;
		this.heatRequired.tempMaximum	= -1;
		System.out.println("Circuit_HotWater/stop called : heatRequired set to zero, pumpwater off, active task = null");
		Global.pumpWater.off();
		this.taskActive					= null;
	}
	@Override
	public void sequencer()
	{
		// Note that this wont pass midnight
		// Whould need to stop automatically at 23:55
		
		this.heatRequired.tempMinimum				= -1000;
		this.heatRequired.tempMaximum				= -1000;
		
		if (this.taskActive != null)
		{
			Boolean		tempObjectiveAttained		= (Global.thermoHotWater.reading > taskActive.tempObjective);
			Boolean		timeUp						= (Global.getTimeNowSinceMidnight() > this.taskActive.timeEnd);
			Boolean		boilerHotEnough				= (Global.thermoBoiler.reading > Global.thermoHotWater.reading);

			System.out.println("=============================================");
			System.out.println("boilerTemp              " + Global.thermoBoiler.reading);
			System.out.println("waterTemp               " + Global.thermoHotWater.reading);
			
			System.out.println("tempObjectiveAttained : " + tempObjectiveAttained);
			System.out.println("timeUp :                " + timeUp);
			System.out.println("boilerHotEnough :       " + boilerHotEnough);
			
			if (this.taskActive.stopOnObjective)
			{
				if (tempObjectiveAttained)
				{
					if (Global.circuits.isSingleActiveCircuit() && boilerHotEnough && !timeUp)
					{
						System.out.println("stopOnObjective : running free");
						// Water is up to temp
						// We have a single circuit and the boiler still has extra heat
						// so keep pumping
					}
					else
					{
						// Either the boiler isn't hot enough or there are other circuits active
						// so we should stop now
						System.out.println("stopOnObjective : it's the end");
						System.out.println("=============================================");
						stop();
					}
				}
				else
				{
					System.out.println("stopOnObjective We need heat");
					this.heatRequired.tempMinimum	= this.taskActive.tempObjective + 100;
					this.heatRequired.tempMaximum	= this.tempMax;
				}
				
				if ((boilerHotEnough) && (this.taskActive != null)) // We might just have stopped due to temp misread
				{
					System.out.println("stopOnObjective Boiler hot enough : pump on");
					Global.pumpWater.on();
				}
				else
				{
					System.out.println("stopOnObjective Boiler not hot enough : pump off");
					Global.pumpWater.off();
				}
			}
			else
			{
				if (timeUp)
				{
					if (Global.circuits.isSingleActiveCircuit() && boilerHotEnough)
					{
						System.out.println("TimeEnd timeUp : running free");
						// We have a single circuit and the boiler still has extra heat
						// so keep pumping
					}
					else
					{
						// Either the boiler isn't hot enough or there are other circuits active
						// so we should stop now
						System.out.println("TimeEnd timeUp : it's the end");
						System.out.println("=============================================");
						stop();
					}
				}
				else
				{
					if (!tempObjectiveAttained)
					{
						System.out.println("TimeEnd time not Up : need heat");

						this.heatRequired.tempMinimum	= this.taskActive.tempObjective;
						this.heatRequired.tempMaximum	= this.tempMax;
					}
					if (boilerHotEnough)
					{
						System.out.println("TimeEnd time not Up : pump on");
						Global.pumpWater.on();
					}
					else
					{
						System.out.println("TimeEnd time not Up : pump off");
						// Not sure that this is correct
						// boiler aint hot enough but time isnt necessarily up
						stop();
					}
				}
			}
		}
	}	
}
