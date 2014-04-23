package eRegulation;

public class Circuit_HotWater extends Circuit_Abstract
{
	public Circuit_HotWater(String name, String friendlyName, Integer circuitType, String tempMax, String rampUpTime)
	{	
		super(name, friendlyName, circuitType, tempMax, rampUpTime);
	}
	public Circuit_HotWater(String name, Integer circuitType, String pumpName, String thermometerName)			// New
	{	
		super(name, circuitType, pumpName, thermometerName);
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
			switch (state)
			{
			case CIRCUIT_STATE_Off:
				//Nothing to do
				break;
			case CIRCUIT_STATE_Start_Requested:
				LogIt.info("Circuit_" + this.name, "sequencer", "Start Requested");
				state												= CIRCUIT_STATE_Starting;
				//Now fall through
			case CIRCUIT_STATE_Starting:
				this.heatRequired.tempMinimum						= this.taskActive.tempObjective + 10000;
				this.heatRequired.tempMaximum						= this.tempMax;
				state												= CIRCUIT_STATE_AwaitingHeat;
				break;
			case CIRCUIT_STATE_AwaitingHeat:
				if (Global.thermoBoiler.reading > Global.thermoHotWater.reading)
				{
					LogIt.action("PumpHotWater", "On");
					circuitPump.on();
					state											= CIRCUIT_STATE_Running;
				}
				break;
			case CIRCUIT_STATE_Running:
				
				// This needs to be reapraised while running and if only circuit we can optimise based on statistics
				
				if (	(this.taskActive.stopOnObjective)
				&&		(Global.thermoHotWater.reading > this.taskActive.tempObjective)     )
//				&&		(Global.thermoBoiler.reading > this.taskActive.tempObjective)		)
				{
					this.heatRequired.tempMinimum						= 0;
					this.heatRequired.tempMaximum						= 0;
					stop();
				}
				else
				{
					this.heatRequired.tempMinimum						= this.taskActive.tempObjective + 10000;
					this.heatRequired.tempMaximum						= this.tempMax;
				}
				break;
			case CIRCUIT_STATE_Stop_Requested:
				LogIt.info("Circuit_" + this.name, "sequencer", "Stop Requested");
				state												= CIRCUIT_STATE_Stopping;
				//Now fall through
			case CIRCUIT_STATE_Stopping:
				if 	(	(Global.circuits.isSingleActiveCircuit())
				&& 		(Global.thermoBoiler.reading > Global.thermoHotWater.reading + 3000) ) 	
				{
					// Require atleast 3 degrees difference otherwise it takes ages
					// We are alone, so as long as there is heat to get out of the system
					// carry on
				}
				else
				{
					LogIt.action("PumpHotWater", "Off");
					circuitPump.off();
					this.shutDown();					// shutDown sets state to off. Threadmixer looks at this as signal to stop
				}
				break;
			case CIRCUIT_STATE_Error:
				break;
			default:
				LogIt.error("Circuit_" + this.name, "sequencer", "unknown state detected : " + state);	
			}
		}
	}
//	public Long getRampUpTime()
//	{
//		// System.out.println("Calculating rampUpTime for HW");
//		Integer hwTempTarget						= this.taskNext.tempObjective;
//		Integer hwTempCurrent 						= Global.thermoHotWater.reading;
//		Integer hwTempDifference 					= hwTempTarget - hwTempCurrent;
//		
//		//System.out.println("hwTempDifference : " + hwTempDifference);
//		
//		// Boiler went from 300 -> 700 (Delta = 400) including over shoot of 70
//		// HW     went from 300 -> 460 (Delta = 160)
//		// And it took 40 mins ie 10 mins = 4 degrees
//		//    or                  600 s   = 4 degrees
//		//    or                  150 s   = 1 degree
//		//    or                   15 s   = 0.1 degree
//		
//		Integer  boilerTempDifference 				= hwTempDifference * 400 / 160;
//		Integer  boilerTempCutoff					= boilerTempDifference - 70; 			//expect a 7 degree overshoot		
//		
//		// Need to put cutoff in the boiler temp somewhere
//		
//		Long	rampUpTime							= hwTempDifference * 15L * 1000L; 		//15000 ms per decidegree 
//				
//		if (rampUpTime > 0)
//		{
//			return rampUpTime;
//		}
//		else
//		{
//			return 0L;
//		}
//		return 0L;
//	}
//	@Override
//	public Long calculatePerformance()
//	{
//		Integer hwTempTarget						= this.taskNext.tempObjective;
//		Integer hwTempCurrent 						= Global.thermoHotWater.reading;
//		Integer hwTempDifference 					= hwTempTarget - hwTempCurrent;
//		
//		// Boiler went from 300 -> 700 (Delta = 400) including over shoot of 70
//		// HW     went from 300 -> 460 (Delta = 160)
//		// And it took 40 mins ie 10 mins = 4 degrees
//		//    or                  600 s   = 4 degrees
//		//    or                  150 s   = 1 degree
//		//    or                   15 s   = 0.1 degree
//		
//		Integer  boilerTempDifference 				= hwTempDifference * 400 / 160;
//		Integer  boilerTempCutoff					= boilerTempDifference - 70; 			//expect a 7 degree overshoot		
//		
//		// Need to put cutoff in the boiler temp somewhere
//		
//		Long	rampUpTime							= hwTempDifference * 15L * 1000L; 		//15000 ms per decidegree 
//				
//		return rampUpTime;
//		
//		// To go from 300 -> 460 (delta 160)
//		// We need boiler to go to 300 + 2xdelta = 620
//		// Boiler will go up to 620 + 70 (overshoot)
//		
//		// Single circuit
//		
//		// boilerMin = hwTempTarget
//		// boilerMax = hwTempCurrent + hwTempDifference x 2 (2 multiplier can be modified)
//		//   boilerMax will improve by 70 overshoot
//		// hwTarget = hwTempCurrent + hwDifference x 0.5  (0.5 multiplier can be modified)
//
//	}
//	public void sequencer_Revised()
//	{
//		// Note that this wont pass midnight
//		// Whould need to stop automatically at 23:55
//		
//		if (this.taskActive != null)
//		{
//			Boolean		tempObjectiveAttained		= (Global.thermoHotWater.reading > taskActive.tempObjective);
//			Boolean		timeUp						= (Global.getTimeNowSinceMidnight() > this.taskActive.timeEnd);
//			Boolean		boilerHotEnough				= (Global.thermoBoiler.reading > Global.thermoHotWater.reading);
//
//			System.out.println("=============================================");
//			System.out.println("boilerTemp              " + Global.thermoBoiler.reading);
//			System.out.println("waterTemp               " + Global.thermoHotWater.reading);
//			
//			System.out.println("tempObjectiveAttained : " + tempObjectiveAttained);
//			System.out.println("timeUp :                " + timeUp);
//			System.out.println("boilerHotEnough :       " + boilerHotEnough);
//			
//			if (this.taskActive.stopOnObjective)
//			{
//				if (tempObjectiveAttained)
//				{
//					if (Global.circuits.isSingleActiveCircuit() && boilerHotEnough && !timeUp)
//					{
//						System.out.println("stopOnObjective : running free");
//						// Water is up to temp
//						// We have a single circuit and the boiler still has extra heat
//						// so keep pumping
//					}
//					else
//					{
//						// Either the boiler isn't hot enough or there are other circuits active
//						// so we should stop now
//						System.out.println("stopOnObjective : it's the end");
//						System.out.println("=============================================");
//						stop();
//						Global.pumpWater.off();
//						this.taskActive					= null;
//					}
//				}
//				else
//				{
//					System.out.println("stopOnObjective We need heat");
//					this.heatRequired.tempMinimum	= this.taskActive.tempObjective + 100;
//					this.heatRequired.tempMaximum	= this.tempMax;
//				}
//				
//				if ((boilerHotEnough) && (this.taskActive != null)) // We might just have stopped due to temp misread
//				{
//					System.out.println("stopOnObjective Boiler hot enough : pump on");
//					Global.pumpWater.on();
//				}
//				else
//				{
//					System.out.println("stopOnObjective Boiler not hot enough : pump off");
//					Global.pumpWater.off();
//				}
//			}
//			else
//			{
//				if (timeUp)
//				{
//					if (Global.circuits.isSingleActiveCircuit() && boilerHotEnough)
//					{
//						System.out.println("TimeEnd timeUp : running free");
//						// We have a single circuit and the boiler still has extra heat
//						// so keep pumping
//					}
//					else
//					{
//						// Either the boiler isn't hot enough or there are other circuits active
//						// so we should stop now
//						System.out.println("TimeEnd timeUp : it's the end");
//						System.out.println("=============================================");
//						stop();
//						Global.pumpWater.off();
//						this.taskActive					= null;
//
//					}
//				}
//				else
//				{
//					if (!tempObjectiveAttained)
//					{
//						System.out.println("TimeEnd time not Up : need heat");
//
//						this.heatRequired.tempMinimum	= this.taskActive.tempObjective;
//						this.heatRequired.tempMaximum	= this.tempMax;
//					}
//					if (boilerHotEnough)
//					{
//						System.out.println("TimeEnd time not Up : pump on");
//						Global.pumpWater.on();
//					}
//					else
//					{
//						System.out.println("TimeEnd time not Up : pump off");
//						// Not sure that this is correct
//						// boiler aint hot enough but time isnt necessarily up
//						stop();
//						Global.pumpWater.off();
//						this.taskActive					= null;
//
//					}
//				}
//			}
//		}
//	}	
}
