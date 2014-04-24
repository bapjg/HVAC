package eRegulation;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.xml.parsers.ParserConfigurationException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import HVAC_Messages.*;
import HVAC_Messages.Ctrl_Configuration.Circuit;

public class Global extends DefaultHandler
{
	//===================================================================
	//
	// Create semaphore to thread protect Interfaces.c code
	// This ensures that calls via ADC, Relay(s), LCD, Buttons
	// to i2c and spi channels is thread safe. This could probably be avoided
	// by changing C code to open ic2/spi channel once (and then keeping it open
	// Current code does open, read/write, close
	//
	public static 	Semaphore	 					interfaceSemaphore 	= new Semaphore("Inteface", true);
	//
	// This ensures that http requests are made ThreadSafe
	//
	public static 	Semaphore 						httpSemaphore 		= new Semaphore("http", true);
	//
	//===================================================================
	
	public static 	Boolean							stopNow;
	public static 	int								exitStatus			= 0;	// 0 = stop app, 1 = restart app, 2 = reboot
	
	public static	PIDs							pids;
	public static 	Thermometers 					thermometers;
	
	public static 	Thermometer 					thermoBoiler;
	public static 	Thermometer 					thermoBoilerOld;
	public static 	Thermometer 					thermoBoilerOut;
	public static 	Thermometer 					thermoBoilerIn;
	
	public static 	Thermometer 					thermoFloorOut;
	public static 	Thermometer 					thermoFloorIn;
	
	public static 	Thermometer 					thermoRadiatorOut;
	public static 	Thermometer 					thermoRadiatorIn;
	
	public static 	Thermometer 					thermoOutside;
	public static 	Thermometer 					thermoLivingRoom;
	public static 	Thermometer 					thermoHotWater;

	public static 	Integer							tempHotWaterPrevious;
	
	public static 	Relays	 						relays;
	public static 	Relay							burnerPower;
//	public static 	Relay							mixerUp;
//	public static 	Relay							mixerDown;
	
	public static 	Circuits	 					circuits;
	public static 	Circuit_HotWater				circuitHotWater;
	public static 	Circuit_Radiator				circuitGradient;
	public static 	Circuit_Mixer					circuitFloor;
	
	public static	Pumps							pumps;
	public static 	Pump							pumpWater;
	public static 	Pump							pumpFloor;
	public static 	Pump							pumpRadiator;

//	public static 	Mixer							mixer;

	public static 	Integer							summerTemp;
	public static 	Integer							summerPumpDuration;	
	public static 	Long							summerPumpTime;	
	public static 	Boolean							summerWorkDone;
	
	public static	Boiler							boiler;

	public static 	LCD								display;	
	public static 	ADC								burnerVoltages;	
	public static 	Buttons							buttons;	
	
	private 		Circuit_Abstract 				circuit;			// Used during XMLparsing		

	public static 	String							calendarsDateTime;	// Used to dateTime the Calendar version in use

	public Global() throws IOException, SAXException, ParserConfigurationException
	{
		Integer x = 0;
		x++;

		Global.display 																= new LCD();
		Global.buttons 																= new Buttons();	
		Global.pids																	= new PIDs();
		Global.thermometers															= new Thermometers();
		Global.relays																= new Relays();
		Global.pumps 																= new Pumps(); 
		Global.circuits 															= new Circuits(); 

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
			System.out.println(dateTimeDisplay() + " Global.constructor Lock timedout, owned by " + Global.httpSemaphore.owner);
			return;
		}

		HTTP_Request	<Ctrl_Configuration.Request>		httpRequest			= new HTTP_Request <Ctrl_Configuration.Request> ("ManagementXXX");
		
		Ctrl_Configuration.Request	 						messageSend 		= new Ctrl_Configuration().new Request();
			
		Ctrl_Abstract 										messageReceive 		= httpRequest.sendData(messageSend);
			
		if (!(messageReceive instanceof Ctrl_Configuration.Data))
		{
			System.out.println(dateTimeDisplay() + " Global.constructor messageType is : Nack");
			
			try
			{
				File				file				= new File("eRegulator_Json.txt");
				FileInputStream  	fileread			= new FileInputStream (file);
				String 				messageJson 		= "";
				Integer				length				= fileread.read();
				byte[] 				data 				= new byte[(int) file.length()+1];
				fileread.read(data);
				fileread.close();

			    String 				dataIn 				= new String(data);
				
			    System.out.println( "Data is ");
			    System.out.println(dataIn.substring(0, 5));
			    
				
			    Ctrl_Configuration.Data	dataInJson 		= new Gson().fromJson(dataIn, Ctrl_Configuration.Data.class);
				dataInJson.dateTime						= now();												// This avoids rewiting the file at end
				messageReceive							= (Ctrl_Abstract) dataInJson;
			}  
			catch(IOException ex)
			{
				System.out.println("I/O error on open : " + ex);
			}	
		}
		Global.httpSemaphore.semaphoreUnLock();			
		
		Ctrl_Configuration.Data								configurationData	= (Ctrl_Configuration.Data) messageReceive;

		//
		//==================================================================================
		
		//==================================================================================
		//
		// Got message from server
		//
		System.out.println("Ok");
		Global.pids.configure(configurationData.pidList);
		Global.thermometers.configure(configurationData.thermometerList);
		Global.relays.configure(configurationData.relayList);
		Global.pumps.configure(configurationData.pumpList);
		Global.circuits.configure(configurationData.circuitList);
		System.out.println("Ok");


		Global.boiler									= new Boiler(configurationData.boiler);
		
		Global.summerPumpDuration						= 300;
		Global.summerWorkDone							= false;
		Global.summerPumpTime							= 60 * 60 * 1000L;		// 1 a.m.
		Global.summerTemp								= 15000;
		
		//
		//==================================================================================
		
		try
		{
			File				file					= new File("eRegulator_Json.txt");
			if (file.exists())
			{
				Long timeFile							= file.lastModified();
				Long timeData							= configurationData.dateTime;
				
				if (timeData > timeFile)
				{
					System.out.println("Global.constructor writing eRegulator_Json.txt file");
					try
					{
						FileWriter 			filewrite				= new FileWriter("eRegulator_Json.txt");
						
						Gson 				gson 				= new GsonBuilder().setPrettyPrinting().create();
						
						String 				messageJson 		= gson.toJson((Ctrl_Configuration.Data) messageReceive);

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
			System.out.println("Global.constructor Exception = ");
		}

		
//		try 
//		{
//			SAXParserFactory 	saxFactory 			= SAXParserFactory.newInstance();
//			SAXParser 			saxParser 			= saxFactory.newSAXParser();
//			
//			saxParser.parse("eRegulator.xml", this);
//		} 
//		catch (Exception e) 
//		{
//			e.printStackTrace();
//		} 		
		
		display.writeAtPosition(1, 18, "Ok");
		// Other initialisation messages are displayed by Control.java
	}
//	public void startElement(String uri, String localName, String tagName, Attributes attributes) throws SAXException 
//    {
//		if (attributes.getLength() > 0)
//		{
//			if (attributes.getValue("type").equalsIgnoreCase("Collection")) 
//			{
//				if (tagName.equalsIgnoreCase("Thermometers"))
//				{
////					Global.thermometers 				= new Thermometers(); 
//				}
//				else if (tagName.equalsIgnoreCase("Relays"))
//				{
////					Global.relays 						= new Relays(); 
//				}
//				else if (tagName.equalsIgnoreCase("Circuits"))
//				{
////					Global.circuits 					= new Circuits(); 
//				}
//				else if (tagName.equalsIgnoreCase("Pumps"))
//				{
////					Global.pumps 					= new Pumps(); 
//				}
//			}
//			else if (attributes.getValue("type").equalsIgnoreCase("Object"))
//			{
//				if (tagName.equalsIgnoreCase("Thermometer"))
//				{
//					String name 						= attributes.getValue("name");
//					String address 						= attributes.getValue("address");
//					String friendlyName					= attributes.getValue("friendlyName");
//					String pid							= attributes.getValue("pid");
//					
//					if (pid.equalsIgnoreCase("Yes"))
//					{
////						Global.thermometers.add(name, address, friendlyName, true);
//					}
//					else
//					{
////						Global.thermometers.add(name, address, friendlyName, false);
//					}
//				}
//				else if (tagName.equalsIgnoreCase("Circuit"))
//				{
//					String name 						= attributes.getValue("name");
//					String friendlyName					= attributes.getValue("friendlyName");
//					String circuitType					= attributes.getValue("circuitType");
//					String tempMax 						= attributes.getValue("tempMax");
//					String rampUpTime					= attributes.getValue("rampUpTime");
//
////					Global.circuits.add(name, friendlyName, circuitType, tempMax, rampUpTime);
////					this.circuit 						= Global.circuits.fetchcircuit(name);
//				}	
//				else if (tagName.equalsIgnoreCase("tempGradient"))
//				{
//					String outsideLow 					= attributes.getValue("outsideLow");
//					String tempLow 						= attributes.getValue("tempLow");
//					String outsideHigh 					= attributes.getValue("outsideHigh");
//					String tempHigh 					= attributes.getValue("tempHigh");
//					
////					this.circuit.temperatureGradient	= new TemperatureGradient(outsideLow, tempLow, outsideHigh, tempHigh);
//				}
//				else if (tagName.equalsIgnoreCase("Relay"))
//				{
//					String name 						= attributes.getValue("name");
//					String address 						= attributes.getValue("address");
//					String friendlyName					= attributes.getValue("friendlyName");
//
////					Global.relays.addFromXML(name, address, friendlyName);
//				}
//				else if (tagName.equalsIgnoreCase("Mixer"))
//				{
//					String name 						= attributes.getValue("name");
//					String swingTime					= attributes.getValue("swingTime");
//					String lagTime						= attributes.getValue("lagTime");
//					String gainP						= attributes.getValue("gainP");
//					String timeD						= attributes.getValue("timeD");
//					String timeI						= attributes.getValue("timeI");
//					String gainI						= attributes.getValue("gainI");
//
////					this.circuit.mixer					= new Mixer(name, swingTime, lagTime, gainP, timeD, timeI, gainI);
//				}
//				else if (tagName.equalsIgnoreCase("Pump"))
//				{
//					String name 						= attributes.getValue("name");
//					String relayName					= attributes.getValue("relay");
//
////					Global.pumps.addFromXML(name, relayName);
//				}
//				else if (tagName.equalsIgnoreCase("Params"))
//				{
////					Global.summerTemp					= Integer.parseInt(attributes.getValue("summerTemp"));
////					Global.summerPumpDuration			= Integer.parseInt(attributes.getValue("summerPumpDuration"));
//					Global.summerPumpTime				= Global.parseTime(attributes.getValue("summerPumpTime"));	
////					Global.summerWorkDone				= false;	
//				}
//			}
//			else
//			{
//				// Nothing of interest
//			}
//		}
//	}
//	public void endElement(String uri, String localName, String tagName) throws SAXException 
//	{
//	}
//	public void characters(char ch[], int start, int length) throws SAXException
//	{
//	}
	public static Long getTimeAtMidnight()
	{
		// Returns the system time last midnight
		Calendar now		 				= Calendar.getInstance();
		
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		Long todayMidnight					= now.getTimeInMillis();
		
		return todayMidnight;
	}
	public static Long getTimeNowSinceMidnight()
	{
		// Returns the number of milliseconds since last midnight
		Long now							= Calendar.getInstance().getTimeInMillis() - Global.getTimeAtMidnight();		
		return now;
	}
	public static String getDayOfWeek()
	{
		return getDayOfWeek(0);
	}
	public static String getDayOfWeek(Integer extraDays)
	{
		Calendar calendar 					= Calendar.getInstance();
		Integer day 						= calendar.get(Calendar.DAY_OF_WEEK);  	// Sunday = 1, Monday = 2, Tues = 3 ... Sat = 7
	
		day									= day + extraDays;						// Sunday = 1, Monday = 2, Tues = 3 ... Sat = 7
		day--;																		// Sunday = 0, Monday = 1, Tues = 2 ... Sat = 6
		day									= day % 7;								// Modulo to take extra days into account
		if (day == 0)
		{
			day								= 7;									// Sunday = 7, Monday = 1, Tues = 2 ... Sat = 6
		}
		return day.toString();
	}
	public static Long parseTime(String characters)
	{
		// Returns a supplied time in string form "hh:mm" 
		// In milliseconds since last midnight
		String splitCharacters[]			= characters.split(":");
		
		Calendar calendar					= Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 	Integer.parseInt(splitCharacters[0]));
		calendar.set(Calendar.MINUTE,  		Integer.parseInt(splitCharacters[1]));
		calendar.set(Calendar.SECOND, 		0);
		calendar.set(Calendar.MILLISECOND, 	0);
		
		return calendar.getTimeInMillis() - Global.getTimeAtMidnight();
	}
	public static Boolean waitSeconds(Integer seconds)
	{
		// Sleeps a supplied number of seconds
		return waitMilliSeconds(seconds * 1000);
 	}
	public static Boolean waitMilliSeconds(Integer milliSeconds)
	{
		// Sleeps a supplied number of milliseconds
		Boolean interrupted 				= false;
		try
        {
            Thread.sleep(milliSeconds);
        }
        catch (InterruptedException e)
        {
        	interrupted 					= true;
        }
		return interrupted;
 	}
	public static Boolean waitMilliSeconds(Long milliSeconds)
	{
		// Sleeps a supplied number of milliseconds
		Boolean interrupted 				= false;
		try
        {
            Thread.sleep(milliSeconds);
        }
        catch (InterruptedException e)
        {
        	interrupted 					= true;
        }
		return interrupted;
 	}
	public static Boolean isSummer()
	{
		// Determine from outside temperature whether summer or not
		return true;
 	}
	public static Long now()
	{
		// Determine from outside temperature whether summer or not
		return System.currentTimeMillis();
 	}
	public static void burnerPanic(String reason)
	{
		// Need to determine what to bo in burner Panic situations
		// It seems reasonable to do the following :
		// Ensure that burnerRelay is off
		// Evacutae heat (over temperature by any means) hot water is usefull even in summer
		
		System.out.println("Global/burnerPanic called : will stop all" + reason);

		// Should send EMAIL xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
		
		Global.burnerPower.off();
	}
	public static void semaphoreLock(ReentrantLock semaphore)
	{
		Boolean 						lockResult;
		try
		{
			lockResult = semaphore.tryLock(2, TimeUnit.SECONDS);
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
//      Properties properties 			= System.getProperties();
		Properties props 				= new Properties();
		props.setProperty("mail.user", 			"administrateur");
    	props.setProperty("mail.password", 		"llenkcarb");
    	props.setProperty("mail.smtp.host", 	"192.168.5.10");

        Session session 				= Session.getDefaultInstance(props);

        try
        {
            MimeMessage message 		= new MimeMessage(session);

            message.setFrom(new InternetAddress("HVAC@bapjg.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("andre@bapjg.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("brigitte@bapjg.com"));
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
		Boolean threadsAlive										= true;
		
		while (threadsAlive)
		{
			Set <Thread> threadSet = Thread.getAllStackTraces().keySet();
			
			Iterator<Thread> i = threadSet.iterator();
			while(i.hasNext()) 
			{
				Thread j 											= i.next();
				String threadName 									= j.getName();
				if (threadName.substring(0,7).equals("Thread_"))
				{
						Global.waitSeconds(1);
						break;
				}
			}
			threadsAlive											= false;
		}
		
	}
    public static String  dateTimeDisplay()
	{
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		String nowFormatted = dateFormat.format(now);
		return nowFormatted;
	}
    public static String  dateTimeDisplay(Long milliSeconds)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		String nowFormatted = dateFormat.format(milliSeconds);
		return nowFormatted;
	}

}
