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
			
			
			if (	(Global.getTimeNowSinceMidnight() 	> Global.summerPumpTime)
//					&&
//					(Global.pumps.dateLastClean 		< Global.summerPumpTime + 1000L * 60 * 2) // 2 min window 
			   )
			{
				LogIt.action("Summer Pumps", "On");
				
				
				
				for (Circuit_Abstract circuit 					: Global.circuits.circuitList)
				{
					Pump		thisPump						= circuit.circuitPump;
					
					if (thisPump.dateLastOperated < Global.pumps.dateLastClean + Global.summerPumpTime)		// pump not used since 24hours
					{
						if (!circuit.circuitPump.isOn())
						{
							circuit.circuitPump.relay.on();			// circuitPump.on() updates timeLastOperated, 
						}											// whereas circuitPump.relay.on() does not.
					}
				}

				Global.pumps.dateLastClean						= Global.today();

				for (i = 0; (i < Global.summerPumpDuration) && (!Global.stopNow); i++)
				{
					Global.waitSeconds(1);
				}
				
				for (Circuit_Abstract circuit 					: Global.circuits.circuitList)
				{
					if (circuit.taskActive == null)		// pump not used since 24hours
					{
						if (!circuit.circuitPump.isOn())
						{
							circuit.circuitPump.off();
						}
					}
				}
				LogIt.action("Summer Pumps", "Off");
			}

			
			if (Global.thermoHotWater.reading < Global.antiFreeze)
			{
				// Start HW temp objective antiFreeze + 2000
			}
			if ((Global.thermoBoiler.reading < Global.antiFreeze   )
			||  (Global.thermoBoilerIn.reading < Global.antiFreeze )
			||  (Global.thermoBoilerOut.reading < Global.antiFreeze))
			{
				
			}
			if ((Global.thermoLivingRoom.reading < Global.antiFreeze)
			||  (Global.thermoFloorIn.reading < Global.antiFreeze   )
			||  (Global.thermoFloorOut.reading < Global.antiFreeze  ))
			{
				// Start Floor temp objective antiFreeze + 2000
			}
			if ((Global.thermoRadiatorIn.reading < Global.antiFreeze )
			||  (Global.thermoRadiatorOut.reading < Global.antiFreeze))
			{
				// Start Radiator temp objective antiFreeze + 2000
			}
			Global.waitSeconds(300);							// Wait 5 mins
		}
		LogIt.info("Thread_Background", "Run", "Stopping", true);		
	}
}
