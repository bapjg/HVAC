package eRegulation;

import HVAC_Common.*;
import HVAC_Common.Ctrl__Abstract.Ack;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Thread_BackgroundTasks implements Runnable
{
	public static final int										SUMMER_PUMPS_Waiting		= 0;
	public static final int										SUMMER_PUMPS_Running		= 1;
	public static final int										SUMMER_PUMPS_FinishedToDay	= 2;
	public static final Long									SIX_HOURS					= 6 * 60 * 60 * 1000L;

	
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

		Calendars.TasksBackGround								tasksBackGround				= Global.tasksBackGround;
		Long													pumpCleanDateLast			= Global.Date.now();						// Initialise at midnight
		
		Long 													oldD;
		Long 													newD;
		Long 													diffD;
		
	
		
		while (!Global.stopNow)
		{
			//=========================================================================================================================================
			//
			// CleanPumps : particularly in summer
			//
			Long							timeNow					= Global.Time.now();
			
			if ( (timeNow		> 	tasksBackGround.pumpCleanTime						) 		// time to do it has arrived		
			&&   (timeNow		< 	tasksBackGround.pumpCleanTime + (30 * 60 * 1000L)	) 		// & within 30 min window
			&&   (timeNow		> 	pumpCleanDateLast									) 	)	// Last run was yesterday ie < Today.midnight
			{
				LogIt.action("Summer Pumps", "Action being considered");
				
				Boolean mustWait = false;
				
				for (Circuit_Abstract circuit 					: Global.circuits.circuitList)
				{
					Pump		thisPump						= circuit.circuitPump;
					
					if (thisPump.dateTimeLastOperated < (Global.DateTime.now() - 23 * 3600 * 1000L) )									// Last pump use was earlier than 11 hours ago
					{
						if (! circuit.circuitPump.isOn())			// Not really possible otherwise
						{
							LogIt.info("Thread_Background", "Run", "Clean pump !isOn " + circuit.circuitPump.name, true);
							circuit.circuitPump.relay.on();			// circuitPump.on() updates timeLastOperated, whereas circuitPump.relay.on() does not.
							Global.waitMilliSeconds(1000);			// Avoid switch all the relays at the same time
							mustWait 							= true;
						}
					}
					else
					{
						LogIt.info("Thread_Background", "Run", "Clean pump DISCARDED "	+ circuit.circuitPump.name, 			true);
					}
				}

				if (mustWait)		// i.e. at least one pump has been turned on
				{
					LogIt.info("Thread_Background", "Run", "Summer Pumps Wait time " + tasksBackGround.pumpCleanDurationSeconds, true);
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
								Global.waitMilliSeconds(1000);			// Avoid switching off all the relays at the same time
							}
						}
					}
				}
				pumpCleanDateLast															= Global.Date.now();
				LogIt.action("Summer Pumps", "Finished");
				LogIt.info("Thread_Background", "Run", "Clean pump finished", true);
			}
			//
			//=========================================================================================================================================

			//=========================================================================================================================================
			//
			// Ensure no freezing : Particularly in winter
			// TODO what about Thermo Read errors
			//
			Long						now							= Global.Time.now();
			Circuit_Abstract 			circuit						= null;
			
			if (	(Global.thermoHotWater.reading 		!= null)
			&& 		(Global.thermoHotWater.reading 		< tasksBackGround.antiFreeze)	)
			{
				circuit												= Global.circuits.fetchcircuit("Hot_Water");
				circuit.taskActive									= new CircuitTask(	now, 						// Time Start
																			now + 30 * 60 * 1000, 					// TimeEnd
																			10000,									// TempObjective in millidesrees (10]C)
																			true,									// StopOnObjective
																			"1, 2, 3, 4, 5, 6, 7");					// Days
				circuit.start();
			}
			if (((Global.thermoBoiler.reading    != null) && (Global.thermoBoiler.reading 		< tasksBackGround.antiFreeze))
			||  ((Global.thermoBoilerIn.reading  != null) && (Global.thermoBoilerIn.reading 	< tasksBackGround.antiFreeze))
			||  ((Global.thermoBoilerOut.reading != null) && (Global.thermoBoilerOut.reading 	< tasksBackGround.antiFreeze))	)
			{
				circuit												= Global.circuits.fetchcircuit("Radiator");
				circuit.taskActive									= new CircuitTask(	now, 						// Time Start
																			now + 30 * 60 * 1000, 					// 30 mins
																			30000,									// TempObjective in millidegrees 30°C
																			false,									// StopOnObjective
																			"1, 2, 3, 4, 5, 6, 7");					// Days
				circuit.start();
			}
			if (((Global.thermoLivingRoom.reading   != null) && (Global.thermoLivingRoom.reading 	< tasksBackGround.antiFreeze))
			||  ((Global.thermoFloorIn.reading  	!= null) && (Global.thermoFloorIn.reading 		< tasksBackGround.antiFreeze))
			||  ((Global.thermoFloorOut.reading 	!= null) && (Global.thermoFloorOut.reading 		< tasksBackGround.antiFreeze))	)
			{
				circuit												= Global.circuits.fetchcircuit("Floor");
				circuit.taskActive									= new CircuitTask(	now, 						// Time Start
																			now + 30 * 60 * 1000, 					// 30 mins
																			10000,									// TempObjective in millidegrees 10°C
																			false,									// StopOnObjective
																			"1, 2, 3, 4, 5, 6, 7");					// Days
				circuit.start();
			}
			if (((Global.thermoRadiatorIn.reading   != null) && (Global.thermoRadiatorIn.reading 	< tasksBackGround.antiFreeze))
			||  ((Global.thermoRadiatorOut.reading  != null) && (Global.thermoRadiatorOut.reading 	< tasksBackGround.antiFreeze)) )
			{
				circuit												= Global.circuits.fetchcircuit("Floor");
				circuit.taskActive									= new CircuitTask(	now, 						// Time Start
																			now + 30 * 60 * 1000, 					// 30 mins
																			30000,									// TempObjective in millidegrees 30°C
																			false,									// StopOnObjective
																			"1, 2, 3, 4, 5, 6, 7");					// Days
				circuit.start();
			}
			//
			//=========================================================================================================================================

			//=========================================================================================================================================
			//
			// Optimise : Particularly hot water in summer and floor in winter
			
			// evry hour
			// if floor not on
			// set mixer cold
			// run pump 5 mins
			// take floor temp and put in pid or elsewhere
			
			//
			//
			//=========================================================================================================================================

			//=========================================================================================================================================
			//
			// Optimise : Particularly hot water in summer and floor in winter
			//
			
			// Must also run floor pump for a certain time to get good reading of concrete/floor temp
			if (Global.circuits.activeCircuitCount() > 0)
			{
				// TODO 
				// if summer
				//		do something
				// else if winter
				//		do something
				// elseif inbetween
				//		do something
				//
				
				
				
				// TODO
				// If boilertemp > hwtemp
				//		do something
				// elseif boilertemp > radiatortemp 
				//		do something
				// elseif boilertemp > floortemp (need to get accurate floor temp before)
				//		do something
				// and all this depends on summer/winter/in between

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
				Global.circuitFloor.optimiseFloor();
			}
			//
			//=========================================================================================================================================

			//=========================================================================================================================================
			//
			// Get the weather forecast after startup (= null) OR last forecast before latest 6hour interval within the day
			//
			Long Inc_6h_Number		= Global.Time.now()/SIX_HOURS;									// Number of 6hour increments since last midnight
			Long Inc_6h_Time		= Global.Date.now() + SIX_HOURS * Inc_6h_Number;				// DateTime of last increment
			

			if ( (Global.weatherData == null                       )
			||	 (Global.weatherData.dateTimeObtained == null      )								// dateTimeObtained can be null if previous attempt had http failure
			||	 (Global.weatherData.dateTimeObtained < Inc_6h_Time)   ) 							// Latest 6 hour interval in day
			{
				Integer 												efectiveTempCorrection 		= -100;
				Integer 												efectiveTempCalculatedMax 	= -100;
				Long 													efectiveTempCalculatedTime 	= -1L;
				
				LogIt.info("Thread_Background", "Run", "Weather : get It", true);
				try
				{
					Global.weatherData																= new Ctrl_WeatherData();
					
					if (Global.weatherData.forecasts != null)			// TODO need to check
					{
						Global.weatherData.dateTimeObtained											= Global.DateTime.now();
						LogIt.info("Thread_Background", "Run", "Weather : fetched", true);
				        for (Ctrl_WeatherData.Forecast forecastItem : Global.weatherData.forecasts)
				        {
				        	if  ((forecastItem.dateTime.from > Global.Date.now())							// timeStamp > last midnight
				        	&& 	 (forecastItem.dateTime.from < Global.Date.now() + 24 * 60 * 60 * 1000L))	// timeStamp < next midnight
				        	{
				            	String 									time_from					= Global.Time.display(forecastItem.dateTime.from);
				            	String 									time_to						= Global.Time.display(forecastItem.dateTime.to);
				        		// TODO
				         		efectiveTempCorrection 												= Global.tasksBackGround.sunshineInfluence * (100 - forecastItem.clouds.all) / 100;
				         		Integer 								efectiveTempCalculated 		= forecastItem.temperature.value.intValue() * 1000 + efectiveTempCorrection;
				         		if (efectiveTempCalculated > efectiveTempCalculatedMax)
				         		{
				         			efectiveTempCalculatedMax										= efectiveTempCalculated;
				         			efectiveTempCalculatedTime										= (forecastItem.dateTime.from + forecastItem.dateTime.to) / 2;
				         		}
				        	}
				        }
				        // TODO
		            	LogIt.display("Thread_Background", "Run", "GOT HERE");
				        // The maximum temperature of the day may be behind us
				        if (Global.temperatureMaxTodayTime > Global.DateTime.now())					// Use the newer prediction
				        {
			            	LogIt.display("Thread_Background", "Run", "GOT HERE 1");
							LogIt.info("Thread_Background", "Run", "Maximum temperature today corrected");
			            	LogIt.display("Thread_Background", "Run", "GOT HERE 2");
				        	Global.temperatureMaxTodayPredicted										= efectiveTempCalculatedMax;
			            	LogIt.display("Thread_Background", "Run", "GOT HERE 3");
				        	Global.temperatureMaxTodayTime											= efectiveTempCalculatedTime;
			            	LogIt.display("Thread_Background", "Run", "GOT HERE 4");
				        }
		            	LogIt.display("Thread_Background", "Run", "GOT HERE 5");
						LogIt.info("Thread_Background", "Run", "Maximum temperature (corrected) today " + efectiveTempCalculatedMax + ", at " + Global.Time.display(efectiveTempCalculatedTime), true);
		            	LogIt.display("Thread_Background", "Run", "GOT HERE 6");
					}
				}
				catch (Exception e)
				{
					LogIt.info("Thread_Background", "Run", "Weather : getIt returned error " + e, true);
					Global.weatherData																= null;
					// If there's a problem getting weatherData, leave Global.temperatureMaxTodayPredicted as it is
					// there is high probability that today's max temp will be the same as yesterday's
				}
			}
			//
			//=========================================================================================================================================

			Global.waitSeconds(300);							// Wait 5 mins
		}
		LogIt.info("Thread_Background", "Run", "Stopping", true);		
	}
}
