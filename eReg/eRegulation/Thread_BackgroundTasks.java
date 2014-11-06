package eRegulation;

import HVAC_Common.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Thread_BackgroundTasks implements Runnable
{
	public static final int			SUMMER_PUMPS_Waiting			= 0;
	public static final int			SUMMER_PUMPS_Running			= 1;
	public static final int			SUMMER_PUMPS_FinishedToDay		= 2;
	public static final Long		SIX_HOURS						= 6 * 60 * 60 * 1000L;

	
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

		Calendars.TasksBackGround			tasksBackGround			= Global.tasksBackGround;
		
		while (!Global.stopNow)
		{
			//=========================================================================================================================================
			//
			// CleanPumps : particularly in summer
			//

			if ( (tasksBackGround.pumpCleanTime		> 	Global.getTimeNowSinceMidnight()						) 		// time to do it has arrived		
			&&   (tasksBackGround.pumpCleanTime		< 	Global.getTimeNowSinceMidnight() + (30 * 60 * 1000L)	) 	)	// & within 30 min window
			{
				LogIt.action("Summer Pumps", "On");
				LogIt.info("Thread_Background", "Run", "Summer Pumps On", true);
				
				for (Circuit_Abstract circuit 					: Global.circuits.circuitList)
				{
					Pump		thisPump						= circuit.circuitPump;
					
					if (thisPump.dateTimeLastOperated < (Global.now() - 23 * 3600 * 1000L) )									// Last pump use was earlier than 11 hours ago
					{
						if (!circuit.circuitPump.isOn())			// Not really possible otherwise
						{
							LogIt.info("Thread_Background", "Run", "Clean pump " + circuit.circuitPump.name, true);
							circuit.circuitPump.relay.on();			// circuitPump.on() updates timeLastOperated, whereas circuitPump.relay.on() does not.
							Global.waitMilliSeconds(500);			// Avoid switch all the relays at the same time
						}
					}
				}

//				Global.pumps.dateTimeLastClean					= Global.now(); // This value will be higher than dateLastOperated, ensuring a run next day even if unused

				LogIt.info("Thread_Background", "Run", "Summer Pumps Wait", true);
				// This is a wait which allows loop exit if stopButton pressed
				for (i = 0; (i < tasksBackGround.pumpCleanDurationSeconds) && (!Global.stopNow); i++)			
				{
					Global.waitSeconds(1);
				}
				
				// Switch off all pumps but inspect each circuit to see if a task is now active
				for (Circuit_Abstract circuit 					: Global.circuits.circuitList)
				{
					if (circuit.taskActive == null)	
					{
						if (circuit.circuitPump.isOn())
						{
							LogIt.info("Thread_Background", "Run", "Clean pump off " + circuit.circuitPump.name, true);
							circuit.circuitPump.off();
							Global.waitMilliSeconds(500);			// Avoid switch all the relays at the same time
						}
					}
				}
				LogIt.action("Summer Pumps", "Off");
				LogIt.info("Thread_Background", "Run", "Clean pump finished", true);
			}
			//
			//=========================================================================================================================================

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

			//=========================================================================================================================================
			//
			// Optimise : Particularly hot water in summer and floor in winter
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
					// ensure mixer is in the correct position
				}

			}
			else
			{
				// Are there any circuit that we were optimising and are left on
			}
			//
			//=========================================================================================================================================

			//=========================================================================================================================================
			//
			// Get the weather forecast after startup (= null) OR last forecast before latest 6hour interval within the day
			//
			Long Inc_6h_Number		= Global.getTimeNowSinceMidnight()/SIX_HOURS;					// Number of 6hour increments since last midnight
			Long Inc_6h_Time		= Global.getTimeAtMidnight() + SIX_HOURS * Inc_6h_Number;		// DateTime of last increment
			

			if ( (Global.weatherData == null                       )
			||	 (Global.weatherData.dateTimeObtained == null      )								// dateTimeObtained can be null if previous attempt had http failure
			||	 (Global.weatherData.dateTimeObtained < Inc_6h_Time)   ) 							// Latest 6 hour interval in day
			{
				Integer efectiveTempCorrection = 0;
				Integer efectiveTempCalculatedMax = 0;
				
				LogIt.info("Thread_Background", "Run", "Weather : get It", true);
				try
				{
					Global.weatherData							= new Ctrl_WeatherData();
					Global.weatherData.dateTimeObtained			= Global.now();
					LogIt.info("Thread_Background", "Run", "Weather : fetched", true);
			        for (Ctrl_WeatherData.Forecast forecastItem : Global.weatherData.forecasts)
			        {
			        	if  ((forecastItem.dateTime.from > Global.getTimeAtMidnight())							// timeStamp > last midnight
			        	&& 	 (forecastItem.dateTime.from < Global.getTimeAtMidnight() + 24 * 60 * 60 * 1000L))	// timeStamp < next midnight
			        	{
			            	String 										time_from					= Global.displayTimeShort(forecastItem.dateTime.from);
			            	String 										time_to						= Global.displayTimeShort(forecastItem.dateTime.to);
			        		// TODO
						    
			         		efectiveTempCorrection = Global.tasksBackGround.sunshineInfluence * (100 - forecastItem.clouds.all) / 100;
			         		Integer efectiveTempCalculated = forecastItem.temperature.value.intValue() * 1000 + efectiveTempCorrection;
			         		if (efectiveTempCalculated > efectiveTempCalculatedMax)
			         		{
			         			efectiveTempCalculatedMax											= efectiveTempCalculated;
			         		}
			        	}
			        }
					LogIt.info("Thread_Background", "Run", "Maximum temperature (corrected) today " + efectiveTempCalculatedMax, true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					LogIt.info("Thread_Background", "Run", "Weather : getIt returned error " + e, true);
					Global.weatherData							= null;
				}
			}
			//
			//=========================================================================================================================================

			Global.waitSeconds(300);							// Wait 5 mins

		}
		LogIt.info("Thread_Background", "Run", "Stopping", true);		
	}
}
