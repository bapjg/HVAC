package eRegulation;

import HVAC_Messages.Ctrl_WeatherData;

public class Thread_BackgroundTasks implements Runnable
{
	public static final int			SUMMER_PUMPS_Waiting			= 0;
	public static final int			SUMMER_PUMPS_Running			= 1;
	public static final int			SUMMER_PUMPS_FinishedToDay		= 2;

	
	public Thread_BackgroundTasks()
	{
	}
	public void run()
	{
		int i;
		// This task must handle :
		//   Summer pump running
		//   Antifreeze
		//   Optimisation
		//   Getting expected weather predictions

		LogIt.info("Thread_Background", "Run", "Starting", true);
		try
		{
			Calendars.TasksBackGround			tasksBackGround			= Global.tasksBackGround;
		}
		catch (Exception e)
		{
			System.out.println("e " + e);
		}
		
		
		LogIt.info("Thread_Background", "Run", "Started", true);
		
		while (!Global.stopNow)
		{
			LogIt.info("Thread_Background", "Run", "Looping 1", true);
			//=========================================================================================================================================
			//
			// CleanPumps : particularly in summer
			//
			if ( (Global.pumps.dateTimeLastClean 	< 	Global.today()											)	 		// last run was yerterday
			&&   (tasksBackGround.pumpCleanTime		> 	Global.getTimeNowSinceMidnight()						) 		// time to do it has arrived		
			&&   (tasksBackGround.pumpCleanTime		< 	Global.getTimeNowSinceMidnight() + (30 * 60 * 1000L)	) 	)	// but not too late	(allow 30 mins	
			{
				LogIt.action("Summer Pumps", "On");
				
				for (Circuit_Abstract circuit 					: Global.circuits.circuitList)
				{
					Pump		thisPump						= circuit.circuitPump;
					
					if (thisPump.dateLastOperated <= Global.pumps.dateTimeLastClean)		// pump not used since last clean
					{
						if (!circuit.circuitPump.isOn())			// Not really possible otherwise
						{
							circuit.circuitPump.relay.on();			// circuitPump.on() updates timeLastOperated, whereas circuitPump.relay.on() does not.
							Global.waitMilliSeconds(500);			// Avoid switch all the relays at the same time
						}
					}
				}

				Global.pumps.dateTimeLastClean					= Global.now(); // This value will be higher then dateLastOperated, ensuring a run next day even if unused

				// This is a wait which allows loop exit if stopButton pressed
				for (i = 0; (i < tasksBackGround.pumpCleanDurationSeconds) && (!Global.stopNow); i++)			
				{
					Global.waitSeconds(1);
				}
				
				// Switch off all pumps but inspect each circuit to see if a task is ow active
				for (Circuit_Abstract circuit 					: Global.circuits.circuitList)
				{
					if (circuit.taskActive == null)		// pump not used since 24hours
					{
						if (!circuit.circuitPump.isOn())
						{
							circuit.circuitPump.off();
							Global.waitMilliSeconds(500);			// Avoid switch all the relays at the same time
						}
					}
				}
				LogIt.action("Summer Pumps", "Off");
			}
			//
			//=========================================================================================================================================
			LogIt.info("Thread_Background", "Run", "Looping after pumps", true);

			//=========================================================================================================================================
			//
			// Ensure no freezing : Particulary in winter
			//
			if (Global.thermoHotWater.reading 		< tasksBackGround.antiFreeze)
			{
				// Start HW temp objective antiFreeze + 2000
			}
			if ((Global.thermoBoiler.reading 		< tasksBackGround.antiFreeze)
			||  (Global.thermoBoilerIn.reading 		< tasksBackGround.antiFreeze)
			||  (Global.thermoBoilerOut.reading 	< tasksBackGround.antiFreeze)	)
			{
				
			}
			if ((Global.thermoLivingRoom.reading 	< tasksBackGround.antiFreeze)
			||  (Global.thermoFloorIn.reading 		< tasksBackGround.antiFreeze)
			||  (Global.thermoFloorOut.reading 		< tasksBackGround.antiFreeze)	)
			{
				// Start Floor temp objective antiFreeze + 2000
			}
			if ((Global.thermoRadiatorIn.reading 	< tasksBackGround.antiFreeze)
			||  (Global.thermoRadiatorOut.reading 	< tasksBackGround.antiFreeze)	)
			{
				// Start Radiator temp objective antiFreeze + 2000
			}
			//
			//=========================================================================================================================================
			LogIt.info("Thread_Background", "Run", "Looping after antifreeze", true);

			//=========================================================================================================================================
			//
			// Optimise : Particulary hot water in summer and floor in winter
			//
			
			
			// Must also run floor pump for a certain time to get good reading of concrete/floor temp
			if (Global.circuits.activeCircuitCount() > 0)
			{
				if (Global.boiler.thermoBoiler.reading > 45000)
				{
					// Hotwater
				}
				else if (Global.boiler.thermoBoiler.reading > 18000)
				{
					// Floor
				}

			}
			else
			{
				// Are there any circuit that we were optimising and are left on
			}
			//
			//=========================================================================================================================================
			LogIt.info("Thread_Background", "Run", "Looping after optimisation", true);

			//=========================================================================================================================================
			//
			// Get the weather forecast after startup (= null) OR last forecast before latest 6hour interval within the day
			//
			if ( (Global.weatherData == null)
			||	 (Global.weatherData.dateTimeObtained < Global.getTimeAtMidnight() + Global.getTimeNowSinceMidnight() / (6 * 60 * 60 * 1000L))   ) // Latest 6 hour interval in day
			{
				try
				{
					Global.weatherData							= new Ctrl_WeatherData();
				}
				catch (Exception e)
				{
				}
			}
			//
			//=========================================================================================================================================

			LogIt.info("Thread_Background", "Run", "Looping after weather and before wait", true);
			Global.waitSeconds(300);							// Wait 5 mins

		}
		LogIt.info("Thread_Background", "Run", "Stopping", true);		
	}
}
