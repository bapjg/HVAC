package eRegulation;

public class Thread_BackgroundTasks implements Runnable
{
	public static final int			SUMMER_PUMPS_Waiting			= 0;
	public static final int			SUMMER_PUMPS_Running			= 1;
	public static final int			SUMMER_PUMPS_FinishedToDay		= 2;

	private	Integer	summerPumpsState;
	
	
	
	public Thread_BackgroundTasks()
	{
	}
	public void run()
	{
		Integer i;
		
		// This task must handle :
		//   Summer pump running
		//   Antifreeze
		//   Optimisation
		//   Getting expected weather predictions

		summerPumpsState											= SUMMER_PUMPS_Waiting;
		
		LogIt.info("Thread_Background", "Run", "Starting", true);
		
		while (!Global.stopNow)
		{
			// We should only do this if no circuit active otherwise we will be heating the house in mid summer
			// This should be changed to run continuously
			
			// We should log last pump run it will be cleaner/easier		xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
			
			/*
			 * for each pump
			 * 		if pump.lastrun - now > intervalAllowed
			 * 			if singlecircuit
			 * 				switchon pump
			 * 				pumpstate running
			 * 			end
			 * 		end
			 * 
			 * 
			 * next
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * if SummerPUMPSState = Waiting
			 * 		if time reached
			 * 			IF temp requies running
			 * 				switchon
			 * 				logtimestart
			 * 				SummerPUMPSState = running
			 * 			else
			 * 				SummerPUMPSState = finidhedtoday     /its too cold
			 * 			end
			 * 		end
			 * 
			 * else if SummerPUMPSState = Running
			 * 		if now > logtimestart + duration
			 * 			switchoff
			 * 			SummerPUMPSState = finidhedtoday
			 * 		end
			 * else if SummerPUMPSState = finidhedtoday
			 * 		do nought wait for tomorrow
			 * 		log last date run
			 * endif
			 * 
			 * 
			 */
			
			
			if (Global.thermoOutside.reading > Global.summerTemp)
			{
				if ((Global.getTimeNowSinceMidnight() > Global.summerPumpTime) 
				&& (Global.getTimeNowSinceMidnight() < Global.summerPumpTime + 30 * 60 * 1000L)) // 30 mins
				{
					if (!Global.summerWorkDone)
					{
						Global.summerWorkDone					= true;
						LogIt.action("Summer Pumps", "On");
						Global.pumps.fetchPump("Pump_Floor").on();
						Global.pumps.fetchPump("Pump_Radiator").on();
						
						for (i = 0; (i < Global.summerPumpDuration) && (!Global.stopNow); i++)
						{
							Global.waitSeconds(1);
						}
						
						LogIt.action("Summer Pumps", "Off");
						Global.pumps.fetchPump("Pump_Floor").off();
						Global.pumps.fetchPump("Pump_Radiator").off();
				
					}
				}
			}
			Global.waitSeconds(10);
		}
		LogIt.info("Thread_Background", "Run", "Stopping", true);		
	}
}
