package eRegulation;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

import HVAC_Common.*;
import HVAC_Common.Ctrl_Json.Request;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Global 
{
	//===================================================================
	//
	// Create semaphore to thread protect Interfaces.c code
	// This ensures that calls via ADC, Relay(s), LCD, Buttons
	// to i2c and spi channels is thread safe. This could probably be avoided
	// by changing C code to open ic2/spi channel once (and then keeping it open
	// Current code does open, read/write, close
	//
	public static 	Semaphore	 								interfaceSemaphore 			= new Semaphore("Inteface", true);
	//
	// This ensures that http requests are made ThreadSafe
	//
	public static 	Semaphore 									httpSemaphore 				= new Semaphore("http", true);
	//
	//===================================================================
	
	public static 	Boolean										stopNow;
	public static 	int											exitStatus			= 0;	// 0 = stop app, 1 = restart app, 2 = reboot
	
	public static	PIDs										pids;
	public static 	Thermometers 								thermometers;
	
	public static 	Thermometer 								thermoBoiler;
	public static 	Thermometer 								thermoBoilerOld;
	public static 	Thermometer 								thermoBoilerOut;
	public static 	Thermometer 								thermoBoilerIn;
	
	public static 	Thermometer 								thermoFloorOut;
	public static 	Thermometer 								thermoFloorIn;
	
	public static 	Thermometer 								thermoRadiatorOut;
	public static 	Thermometer 								thermoRadiatorIn;
	
	public static 	Thermometer 								thermoOutside;
	public static 	Thermometer 								thermoLivingRoom;
	public static 	Thermometer 								thermoHotWater;

	public static 	Integer										tempHotWaterPrevious;
	
	public static 	Relays	 									relays;
	public static 	Relay										burnerPower;
	
	public static 	Circuits	 								circuits;
	public static 	Circuit_HotWater							circuitHotWater;
	public static 	Circuit_Radiator							circuitGradient;
	public static 	Circuit_Mixer								circuitFloor;
	
	public static	Pumps										pumps;
	public static 	Pump										pumpWater;
	public static 	Pump										pumpFloor;
	public static 	Pump										pumpRadiator;

	public static	Boiler										boiler;
	public static	Burner										burner;

	public static 	LCD											display;	
	public static 	Buttons										buttons;	

	public static 	ArrayList	<String>						eMails;	

	public static 	String										calendarsDateTime;	// Used to dateTime the Calendar version in use

	public static	ArrayList	<Calendars.Away>				awayList;
	public static	Calendars.TasksBackGround					tasksBackGround;
	public static	Ctrl_WeatherData							weatherData;
	public static	Integer										temperatureMaxTodayPredicted;
	public static	Long										temperatureMaxTodayTime;
	
	public Global()
	{
		Global.display 																		= new LCD();
		Global.buttons 																		= new Buttons();	
		Global.pids																			= new PIDs();
		Global.thermometers																	= new Thermometers();
		Global.relays																		= new Relays();
		Global.pumps 																		= new Pumps(); 
		Global.circuits 																	= new Circuits(); 
		Global.eMails	 																	= new ArrayList<String>(); 
		Global.awayList	 																	= new ArrayList<Calendars.Away>(); 
		Global.temperatureMaxTodayPredicted													= null;

		display.clear();
		display.blinkOff();
		display.writeAtPosition(0, 0, "Initialising");
		display.writeAtPosition(1, 0, " Reading params");
		
		//==================================================================================
		//
		// Get message from server
		//
		if (!Global.httpSemaphore.semaphoreLock("LogIt.logMessage"))
		{
			LogIt.info("Global", "constructor", "Lock timedout, owned by " + Global.httpSemaphore.owner);
			return;
		}

		//==================================================================================================
		// New code
		//
		// HTTP_Send	(new Ctrl_Json().new Request(Ctrl_Json.TYPE_Calendar));				// Fire these async actions as soon as possible
		// HTTP_Request	<Ctrl_Configuration.Request>			httpRequest					= new HTTP_Request <Ctrl_Configuration.Request> ("Management");

		int x = 1;
		int y = 2;
		if (x == 1)
		{

		Ctrl_Json.Request messageSendTest = new Ctrl_Json().new Request(Ctrl_Json.TYPE_Configuration);
		HTTP_Request	<Ctrl_Json.Request>			httpRequestTest					= new HTTP_Request <Ctrl_Json.Request> ("Management");
		Ctrl__Abstract 											messageReceiveTest 				= httpRequestTest.sendData(messageSendTest);
		
		if (messageReceiveTest instanceof Ctrl_Json.Data)
		{
			String													JsonString				= ((Ctrl_Json.Data) messageReceiveTest).json;
			Ctrl_Configuration.Data		data													= new Gson().fromJson(JsonString, Ctrl_Configuration.Data.class);
		}
		
		
		
		}
		
		//
		//
		//==================================================================================================
		
		
		HTTP_Request	<Ctrl_Configuration.Request>			httpRequest					= new HTTP_Request <Ctrl_Configuration.Request> ("Management");
		
		Ctrl_Configuration.Request	 							messageSend 				= new Ctrl_Configuration().new Request();
		Ctrl__Abstract 											messageReceive 				= httpRequest.sendData(messageSend);
			
		if (!(messageReceive instanceof Ctrl_Configuration.Data))
		{
			LogIt.info("Global", "constructor", "MessageType is : Nack using local disk configuration");
			// There is a problem, so read the last file received
			try
			{
				File											file						= new File("/home/pi/HVAC_Data/eRegulator_Json.txt");
				FileInputStream  								fileread					= new FileInputStream (file);
				byte[] 											data 						= new byte[(int) file.length()];
				fileread.read(data);
				fileread.close();

			    String 											dataIn 						= new String(data);
				
			    Ctrl_Configuration.Data							dataInJson 					= new Gson().fromJson(dataIn, Ctrl_Configuration.Data.class);
				messageReceive																= (Ctrl__Abstract) dataInJson;
			}  
			catch(IOException ex)
			{
				System.out.println("I/O error on open : eRegulator_Json " + ex);
				System.exit(Ctrl_Actions_Stop.ACTION_Stop);				// 0 = stop application
			}	
		}
		else
		{
			LogIt.info("Global", "constructor", "MessageType is : Ok using remote configuration");
			// All is Ok, so see if we need to write a copy locally
			try
			{
				File											file						= new File("/home/pi/HVAC_Data/eRegulator_Json.txt");
				if (file.exists())
				{
					Long timeFile															= file.lastModified();
					Ctrl_Configuration.Data 					thisData					 = (Ctrl_Configuration.Data) messageReceive;
					Long timeData															= thisData.dateTime;
					
					if (timeData > timeFile)
					{
						LogIt.info("Global", "constructor", "Over writing eRegulator_Json.txt file");
						try
						{
							FileWriter 							filewrite					= new FileWriter("/home/pi/HVAC_Data/eRegulator_Json.txt");
							Gson 								gson 						= new GsonBuilder().setPrettyPrinting().create();
							String 								messageJson 				= gson.toJson((Ctrl_Configuration.Data) messageReceive);

							filewrite.write(messageJson);
							filewrite.flush();
							filewrite.close();
						}  
						catch(IOException ex)
						{
							LogIt.info("Global", "constructor", "I/O error on open : eRegulator_Json.txt " + ex);
						}	
					}
					else
					{
						LogIt.info("Global", "constructor", "File eRegulator_Json.txt is still up to date");
					}
				}
				else
				{
					LogIt.info("Global", "constructor", "Creating eRegulator_Json.txt file");
					try
					{
						FileWriter 							filewrite						= new FileWriter("/home/pi/HVAC_Data/eRegulator_Json.txt");
						Gson 								gson 							= new GsonBuilder().setPrettyPrinting().create();
						String 								messageJson 					= gson.toJson((Ctrl_Configuration.Data) messageReceive);

						filewrite.write(messageJson);
						filewrite.flush();
						filewrite.close();
					}  
					catch(IOException ex)
					{
						LogIt.info("Global", "constructor", "I/O error on creation/open : eRegulator_Json.txt " + ex);
					}	
				}
			}
			catch (Exception e)
			{
				LogIt.info("Global", "constructor", "I/O error on open : eRegulator_Json.txt " + e);
			}
		}
		Global.httpSemaphore.semaphoreUnLock();			
		
		Ctrl_Configuration.Data								configurationData				= (Ctrl_Configuration.Data) messageReceive;

		//
		//==================================================================================
		
		//==================================================================================
		//
		// Got configuration message from server or locally
		//
		Global.pids.configure			(configurationData.pidList);
		Global.thermometers.configure	(configurationData.thermometerList);
		Global.relays.configure			(configurationData.relayList);
		Global.pumps.configure			(configurationData.pumpList);
		Global.circuits.configure		(configurationData.circuitList);
		Global.burner																		= new Burner(configurationData.burner);
		Global.boiler																		= new Boiler(configurationData.boiler);
		
		for (String eMail : configurationData.eMailList)
		{
			Global.eMails.add(eMail);
		}
		//
		//==================================================================================

		//============================================================
		//
		// Initialise Global
		//
		
		thermoBoiler 																	= Global.thermometers.fetchThermometer("Boiler");
		thermoBoilerOld																	= Global.thermometers.fetchThermometer("Boiler_Old");
		thermoBoilerOut																	= Global.thermometers.fetchThermometer("Boiler_Out");
		thermoBoilerIn																	= Global.thermometers.fetchThermometer("Boiler_In");
												
		thermoFloorOut																	= Global.thermometers.fetchThermometer("Floor_Out");
		thermoFloorIn																	= Global.thermometers.fetchThermometer("Floor_In");
												
		thermoRadiatorOut																= Global.thermometers.fetchThermometer("Radiator_Out");
		thermoRadiatorIn																= Global.thermometers.fetchThermometer("Radiator_In");
												
		thermoOutside																	= Global.thermometers.fetchThermometer("Outside");
		thermoLivingRoom																= Global.thermometers.fetchThermometer("Living_Room");
		thermoHotWater																	= Global.thermometers.fetchThermometer("Hot_Water");

		burnerPower	 																	= Global.relays.fetchRelay("Burner");
											
		circuitFloor																	= (Circuit_Mixer) 		Global.circuits.fetchCircuit("Floor");
		circuitGradient																	= (Circuit_Radiator) 	Global.circuits.fetchCircuit("Radiator");
		circuitHotWater																	= (Circuit_HotWater) 	Global.circuits.fetchCircuit("Hot_Water");

		//
		//============================================================

		display.writeAtPosition(1, 18, "Ok");
		// Other initialisation messages are displayed by Control.java
	}
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
	public static Boolean waitSeconds(Integer seconds)										// Sleeps a supplied number of seconds
	{
		return waitMilliSeconds(seconds * 1000);
 	}
	public static Boolean waitMilliSeconds(Integer milliSeconds)							// Sleeps a supplied number of milliseconds
	{
		Boolean interrupted 																= false;
		try
        {
            Thread.sleep(milliSeconds);
        }
        catch (InterruptedException e)
        {
        	interrupted 																	= true;
        }
		return interrupted;
 	}
	public static Boolean waitMilliSeconds(Long milliSeconds)								// Sleeps a supplied number of milliseconds
	{
		Boolean interrupted 																= false;
		try
        {
            Thread.sleep(milliSeconds);
        }
        catch (InterruptedException e)
        {
        	interrupted 																	= true;
        }
		return interrupted;
 	}
	public static Boolean isSummer()
	{
		// TODO Determine from outside temperature whether summer or not
		if (Global.thermoOutside.reading == null)											return true; // Let's play it safe																	// force summer to keep things safe
		if (Global.thermoOutside.reading > Global.tasksBackGround.summerTemp)				return true;
		else																				return false;
 	}
	public static Boolean isWinter()
	{
		// TODO Determine from outside temperature whether summer or not
		if (Global.thermoOutside.reading == null)											return false; // Let's play it safe																	// force summer to keep things safe
		if (Global.thermoOutside.reading < Global.tasksBackGround.winterTemp)				return true;
		else																				return false;
 	}
	public static Boolean isAway()
    {
		for (Calendars.Away 		awayItem : awayList)
		{
			Long now												= Global.DateTime.now();
			if (	(now > awayItem.dateTimeStart)
			&& 		(now < awayItem.dateTimeEnd)	)
			{
				return true;
			}
		}
		return false;
    }
//	public static Long now()
//	{
//		return Calendar.getInstance().getTimeInMillis();
// 	}
	public static void burnerPanic(String reason)
	{
		// Need to determine what to bo in burner Panic situations
		// It seems reasonable to do the following :
		// Ensure that burnerRelay is off
		// Evacutae heat (over temperature by any means) hot water is usefull even in summer
		
		System.out.println("Global/burnerPanic called : will stop all" + reason);

		eMailMessage("HVAC Panic", "The burner has detected a panic situation, reason : " + reason);
		
		Global.burnerPower.off();
	}
	public static void semaphoreLock(ReentrantLock semaphore)
	{
		Boolean 												lockResult;
		try
		{
			lockResult 																		= semaphore.tryLock(2, TimeUnit.SECONDS);
		}
		catch (InterruptedException e1)
		{
			System.out.println(LogIt.dateTimeStamp() + " TryLock failed");
			return;
		}
		
		if (!lockResult)
		{
			System.out.println(LogIt.dateTimeStamp() + " TryLock timed out ");
			return;
		}
	}
	public static void eMailMessage(String subject, String messageText)
	{
		Properties 												props 						= new Properties();
		props.setProperty("mail.user", 			"administrateur");
    	props.setProperty("mail.password", 		"llenkcarb");
    	props.setProperty("mail.smtp.host", 	"192.168.5.10");

        Session 												session 					= Session.getDefaultInstance(props);

        try
        {
            MimeMessage 										message				 		= new MimeMessage(session);

            message.setFrom(new InternetAddress("HVAC@bapjg.com"));
            
            for (String eMail : Global.eMails)
            {
            	message.addRecipient(Message.RecipientType.TO, new InternetAddress(eMail));
            }
            message.setSubject("HVAC System : " + subject);
            message.setText(messageText);
            Transport.send(message);
        }
        catch (MessagingException mex) 
        {
            mex.printStackTrace();
        }
	}
	public static void waitThreadTermination()
	{
		Boolean 												threadsAlive				= true;
		
		while (threadsAlive)
		{
			Set <Thread> 										threadSet 					= Thread.getAllStackTraces().keySet();
			
			Iterator<Thread> i = threadSet.iterator();
			while(i.hasNext()) 
			{
				Thread j 																	= i.next();
				String threadName 															= j.getName();
				if (threadName.substring(0,7).equals("Thread_"))
				{
						Global.waitSeconds(1);
						break;
				}
			}
			threadsAlive																	= false;
		}
		
	}

   //===========================================================================================================================================
   //
   //																DateTime
   //
   public static class Date
   {
		public static Long now()															// Returns date in milliseconds (ie dateTime at midnight)
		{
			Calendar now		 																= Calendar.getInstance();
			
			now.set(Calendar.HOUR_OF_DAY, 0);
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
			now.set(Calendar.MILLISECOND, 0);
			Long todayMidnight																	= now.getTimeInMillis();
			
			return todayMidnight;
		}
		public static Long 			today()							{return now();												}
		public static Long 			dateOnly(Integer days)			{return now() - 24 * 60 * 60 * 1000L * days;				}
		public static Long 			yesterday()						{return dateOnly(-1);										}
		public static String getDayOfWeek()
		{
			return getDayOfWeek(0);
		}
		public static String getDayOfWeek(Integer extraDays)
		{
			Calendar 												calendar 				= Calendar.getInstance();
			Integer 												day 					= calendar.get(Calendar.DAY_OF_WEEK);  	// Sunday = 1, Monday = 2, Tues = 3 ... Sat = 7
		
			day																				= day + extraDays;						// Sunday = 1, Monday = 2, Tues = 3 ... Sat = 7
			day--;																														// Sunday = 0, Monday = 1, Tues = 2 ... Sat = 6
			day																				= day % 7;								// Modulo to take extra days into account
			if (day == 0)
			{
				day																			= 7;									// Sunday = 7, Monday = 1, Tues = 2 ... Sat = 6
			}
			return day.toString();
		}
   }
   //
   //===========================================================================================================================================

   //===========================================================================================================================================
   //
   //																Time
   //
   public static class Time
   {
		public static Long 			now()													// Returns time part in milliseconds
		{
			//==============================================================
			// Returns the number of milliseconds since last midnight
			//==============================================================
			return Calendar.getInstance().getTimeInMillis() - Global.Date.now();		
		}

		public static String display(Long dateTime)
		{
			//==============================================================
			// Accepts a TimeSinceMidnight argument if UTC and adjusts for local Timezone
			// returns supplied dateTime in the form hh:mm
			//==============================================================
			
			String 													dateTimeString;
			Long													days					= dateTime / 1000 / 3600 / 24;		//millisecs -> secs -> hours -> days
			if (days > 0)																		// We need to use the TimeZoned calendar to workout time
			{
			     SimpleDateFormat 									sdf 					= new SimpleDateFormat("HH:mm");
			     GregorianCalendar 									calendar 				= new GregorianCalendar();
			     calendar.setTimeInMillis(dateTime);	
			     dateTimeString																= sdf.format(dateTime);
			}
			else																					// Must calculate manually as time is since local midnight
			{
				Integer												seconds					= (int) (long) (dateTime / 1000);
				Integer												hours					= seconds / 3600;
				Integer												minutes					= (seconds - hours * 3600)/60;
				dateTimeString																= String.format("%02d", hours)  + ":" +String.format("%02d", minutes);
			}
			return dateTimeString;
		}
		public static Long parseTime(String characters)
		{
			// Returns a supplied time in string form "hh:mm"
			// In milliseconds since last midnight
			String splitCharacters[]														= characters.split(":");
			
			Calendar calendar																= Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 	Integer.parseInt(splitCharacters[0]));
			calendar.set(Calendar.MINUTE,  		Integer.parseInt(splitCharacters[1]));
			calendar.set(Calendar.SECOND, 		0);
			calendar.set(Calendar.MILLISECOND, 	0);
			
			//return calendar.getTimeInMillis() - Global.Date.now();
			
			return (Integer.parseInt(splitCharacters[0]) * 3600 + Integer.parseInt(splitCharacters[1]) * 60 ) * 1000L;
		}
   }
   //
   //===========================================================================================================================================

   //===========================================================================================================================================
   //
   //																DateTime
   //
   public static class DateTime
   {
		public static Long now()															// Returns DateTime in milliseconds
		{
			return Calendar.getInstance().getTimeInMillis();
	 	}
		public static String display()														// Returns DateTime in milliseconds
		{
			Date 													now 					= new Date();
			SimpleDateFormat 										dateFormat 				= new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
			String 													nowFormatted 			= dateFormat.format(now);
			return nowFormatted;
	 	}
	    public static String  display(Long milliSeconds)
		{
			SimpleDateFormat 										dateFormat 				= new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
			String 													nowFormatted 			= dateFormat.format(milliSeconds);
			return nowFormatted;
		}
   }
   //
   //===========================================================================================================================================
}
