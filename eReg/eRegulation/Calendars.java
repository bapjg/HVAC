package eRegulation;

import HVAC_Messages.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;


public class Calendars
{
	private Circuit_Abstract	circuit;
	
	public Calendars() throws IOException
    {
		//==================================================================================
		//
		// Get message from server
		//
		if (!Global.httpSemaphore.semaphoreLock("LogIt.logMessage"))
		{
			System.out.println(Global.dateTimeDisplay() + " Calendars.constructor Lock timedout, owned by " + Global.httpSemaphore.owner);
			return;
		}

		HTTP_Request	<Ctrl_Calendars.Request>			httpRequest			= new HTTP_Request <Ctrl_Calendars.Request> ("Management");
		
		Ctrl_Calendars.Request	 							messageSend 		= new Ctrl_Calendars.Request();
		Ctrl_Abstract 										messageReceive 		= httpRequest.sendData(messageSend);
			
		if (!(messageReceive instanceof Ctrl_Calendars.Data))
		{
			System.out.println(Global.dateTimeDisplay() + " Calendars.constructor messageType is : Nack");
			// There is a problem, so read the last file received
			try
			{
				File										file				= new File("eCalendars_Json.txt");
				FileInputStream  							fileread			= new FileInputStream (file);
				byte[] 										data 				= new byte[(int) file.length()];
				fileread.read(data);
				fileread.close();

			    String 										dataIn 				= new String(data);
				
			    Ctrl_Calendars.Data							dataInJson 			= new Gson().fromJson(dataIn, Ctrl_Calendars.Data.class);
				messageReceive													= (Ctrl_Abstract) dataInJson;
			}  
			catch(IOException ex)
			{
				System.out.println("I/O error on open : " + ex);
			}	
		}
		else
		{
			// All is Ok, so see if we need to write a copy locally
			try
			{
				File										file				= new File("eCalendars_Json.txt");
				if (file.exists())
				{
					Long timeFile												= file.lastModified();
					Ctrl_Calendars.Data thisData								= (Ctrl_Calendars.Data) messageReceive;
					Long timeData												= thisData.dateTime;
					
					if (timeData > timeFile)
					{
						System.out.println("Calendars.constructor writing eRegulator_Json.txt file");
						try
						{
							FileWriter 						filewrite			= new FileWriter("eRegulator_Json.txt");
							
							Gson 							gson 				= new GsonBuilder().setPrettyPrinting().create();
							
							String 							messageJson 		= gson.toJson((Ctrl_Calendars.Data) messageReceive);

							filewrite.write(messageJson);
							filewrite.flush();
							filewrite.close();
						}  
						catch(IOException ex)
						{
							System.out.println("I/O error on open : " + ex);
						}	
					}
				}
			}
			catch (Exception e)
			{
				System.out.println("Calendars.constructor Exception = ");
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

		
//		this.vocabulary.configure(calendarData.wordList);
		for (Ctrl_Calendars.Word 					word 				: calendarData.wordList) 
		{
			LogIt.info("Vocabulary Entry", word.name, "Days " + word.days);
		}
		
		for (Ctrl_Calendars.Circuit 						paramCircuit 		: calendarData.circuitList)
		{
			Circuit_Abstract								circuit				= Global.circuits.fetchcircuit(paramCircuit.name);
		
			for (Ctrl_Calendars.Calendar 					paramCalendar 		: paramCircuit.calendarList)
			{
				if (paramCalendar.active)
				{
					for (Ctrl_Calendars.Word 				word 				: calendarData.wordList) 
					{
						paramCalendar.days 										= paramCalendar.days.replace(word.name, word.days);
					}
					circuit.addCircuitTask(paramCalendar);
					LogIt.info("Calendar Entry", circuit.name, "Time start/end " + paramCalendar.timeStart + "/" + paramCalendar.stopCriterion.timeEnd + " Days " + paramCalendar.days);
				}
			}
		}

		for (Ctrl_Calendars.Away 							paramAway 			: calendarData.awayList)
		{
			Global.awayList.add(new Away(paramAway));
		}
		
		Global.antiFreeze														= calendarData.antiFreeze;
		//
		//==================================================================================
	}
	public class Away
	{
		public Long 							dateTimeStart;
		public Long 							dateTimeEnd;
		
		public Away(Ctrl_Calendars.Away away)
		{
			this.dateTimeStart										= away.dateTimeStart;
			this.dateTimeEnd										= away.dateTimeEnd;
		}
	}
}
