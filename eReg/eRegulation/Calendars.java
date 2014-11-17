package eRegulation;

import HVAC_Common.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Calendars
{
	private Circuit_Abstract	circuit;
	public  Ctrl_Calendars.Data	calendars;
	
	public Calendars() throws IOException
    {
		//==================================================================================
		//
		// Get message from server
		//
		if (!Global.httpSemaphore.semaphoreLock("LogIt.logMessage"))
		{
			LogIt.info("Calendars", "constructor", "Lock timedout, owned by " + Global.httpSemaphore.owner);
			return;
		}

		HTTP_Request	<Ctrl_Calendars.Request>			httpRequest			= new HTTP_Request <Ctrl_Calendars.Request> ("Management");
		
		Ctrl_Calendars.Request	 							messageSend 		= new Ctrl_Calendars().new Request();
		Ctrl__Abstract 										messageReceive 		= httpRequest.sendData(messageSend);
			
		if (!(messageReceive instanceof Ctrl_Calendars.Data))
		{
			LogIt.info("Calendars", "constructor", "Calendars.constructor messageType is : Nack");
			// There is a problem, so read the last file received
			try
			{
				File										file				= new File("/home/pi/HVAC_Data/eCalendars_Json.txt");
				FileInputStream  							fileread			= new FileInputStream (file);
				byte[] 										data 				= new byte[(int) file.length()];
				fileread.read(data);
				fileread.close();

			    String 										dataIn 				= new String(data);
				
			    Ctrl_Calendars.Data							dataInJson 			= new Gson().fromJson(dataIn, Ctrl_Calendars.Data.class);
				messageReceive													= (Ctrl__Abstract) dataInJson;
			}  
			catch(IOException ex)
			{
				LogIt.info("Calendars", "constructor", "I/O error on open : eCalendars_Json.txt " + ex);		//Probably file dont exist. Can only bomb out
				System.exit(Ctrl_Actions_Stop.ACTION_Stop);				// 0 = stop application
			}	
		}
		else
		{
			// All is Ok, so see if we need to write a copy locally
			try
			{
				File										file				= new File("/home/pi/HVAC_Data/eCalendars_Json.txt");
				if (file.exists())
				{
					Long timeFile												= file.lastModified();
					Ctrl_Calendars.Data thisData								= (Ctrl_Calendars.Data) messageReceive;
					Long timeData												= thisData.dateTime;
					
					if (timeData > timeFile)
					{
						LogIt.info("Calendars", "constructor", "Over writing eCalendars_Json.txt");
						try
						{
							FileWriter 						filewrite			= new FileWriter("/home/pi/HVAC_Data/eCalendars_Json.txt");
							
							Gson 							gson 				= new GsonBuilder().setPrettyPrinting().create();
							
							String 							messageJson 		= gson.toJson((Ctrl_Calendars.Data) messageReceive);
	
							filewrite.write(messageJson);
							filewrite.flush();
							filewrite.close();
						}  
						catch(IOException ex)
						{
							LogIt.info("Calendars", "constructor", "I/O error on open : eCalendars_Json.txt " + ex);
						}	
					}
					else
					{
						LogIt.info("Calendars", "constructor", "Data in eCalendars_Json.txt file is still up to date");
					}
				}
				else
				{
					LogIt.info("Calendars", "constructor", "Creating eCalendars_Json.txt");
					try
					{
						FileWriter 						filewrite			= new FileWriter("/home/pi/HVAC_Data/eCalendars_Json.txt");
						
						Gson 							gson 				= new GsonBuilder().setPrettyPrinting().create();
						
						String 							messageJson 		= gson.toJson((Ctrl_Calendars.Data) messageReceive);

						filewrite.write(messageJson);
						filewrite.flush();
						filewrite.close();
					}  
					catch(IOException ex)
					{
						LogIt.info("Calendars", "constructor", "I/O error on create/open : eCalendars_Json.txt " + ex);
					}	
				}
			}
			catch (Exception e)
			{
				LogIt.info("Calendars", "constructor", "Exception " + e);
			}
		}
		Global.httpSemaphore.semaphoreUnLock();			
		
		Ctrl_Calendars.Data									calendarData		= (Ctrl_Calendars.Data) messageReceive;

		//
		//==================================================================================
		
		//==================================================================================
		//
		// Got message from server
		//

		
		//		get words/wordList/vocabulary
		for (Ctrl_Calendars.Word 							word 				: calendarData.wordList) 
		{
			LogIt.info("Vocabulary Entry", word.name, "Days " + word.days);
		}
		//		get circuit tasks
		for (Ctrl_Calendars.Circuit 						paramCircuit 		: calendarData.circuitList)
		{
			Circuit_Abstract								circuit				= Global.circuits.fetchcircuit(paramCircuit.name);
		
			for (Ctrl_Calendars.Calendar 					paramCalendar 		: paramCircuit.calendarList)
			{
				for (Ctrl_Calendars.Word 					word 				: calendarData.wordList) 
				{
					paramCalendar.days 											= paramCalendar.days.replace(word.name, word.days);
				}
				// TODO Clanedar days are not printed correctly
				circuit.addCircuitTask(paramCalendar);
				LogIt.info("Calendar Entry", circuit.name, "Time start/end " + paramCalendar.timeStart.displayShort() + "/" + paramCalendar.timeEnd.displayShort() + " Days " + paramCalendar.days);
			}
		}
		//		get away calendar
		for (Ctrl_Calendars.Away 							paramAway 			: calendarData.awayList)
		{
			Global.awayList.add(new Away(paramAway));
		}
		//		get tasksBackGround information
		Global.tasksBackGround													= new TasksBackGround(calendarData.tasksBackGround);
		//
		//==================================================================================
	}
	public class Away
	{
		public Long 							dateTimeStart;
		public Long 							dateTimeEnd;
		
		public Away(Ctrl_Calendars.Away 							away)
		{
			this.dateTimeStart										= away.dateTimeStart;
			this.dateTimeEnd										= away.dateTimeEnd;
		}
	}
	public class TasksBackGround
	{
		public Long 							pumpCleanTime;
		public Integer 							pumpCleanDurationSeconds;
		public Integer							antiFreeze;
		public Integer							summerTemp;
		public Integer							winterTemp;
		public Integer							sunshineInfluence;

		public TasksBackGround(Ctrl_Calendars.TasksBackGround 		tasksBackGround)
		{
			this.pumpCleanTime										= tasksBackGround.pumpCleanTime.milliSeconds;
			this.pumpCleanDurationSeconds							= tasksBackGround.pumpCleanDurationMinutes * 60;
			this.antiFreeze											= tasksBackGround.antiFreeze.milliDegrees;
			this.summerTemp											= tasksBackGround.summerTemp.milliDegrees;
			this.winterTemp											= tasksBackGround.winterTemp.milliDegrees;
			this.sunshineInfluence									= tasksBackGround.sunshineInfluence.milliDegrees;
			LogIt.info("Calendar.Background", "Tasks", "Pump Clean Time     : " + tasksBackGround.pumpCleanTime.displayShort());
			LogIt.info("Calendar.Background", "Tasks", "Pump Clean Duration : " + tasksBackGround.pumpCleanDurationMinutes + " mins");
			LogIt.info("Calendar.Background", "Tasks", "Antifreeze          : " + tasksBackGround.antiFreeze.displayInteger());
			LogIt.info("Calendar.Background", "Tasks", "Summer temperature  : " + tasksBackGround.summerTemp.displayInteger());
			LogIt.info("Calendar.Background", "Tasks", "Winter temperature  : " + tasksBackGround.winterTemp.displayInteger());
			LogIt.info("Calendar.Background", "Tasks", "Sunshine influence  : " + tasksBackGround.sunshineInfluence.displayInteger());
		}
	}

}
