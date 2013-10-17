package eRegulation;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;

import java.io.*;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

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
	
	public static 	Thermometers 					thermometers;
	
	public static 	Thermometer 					thermoBoiler;
	public static 	Thermometer 					thermoBoilerIn;
	
	public static 	Thermometer 					thermoFloorOut;
	public static 	Thermometer 					thermoFloorCold;
	public static 	Thermometer 					thermoFloorHot;
	
	public static 	Thermometer 					thermoRadiatorOut;
	public static 	Thermometer 					thermoRadiatorIn;
	
	public static 	Thermometer 					thermoOutside;
	public static 	Thermometer 					thermoLivingRoom;
	public static 	Thermometer 					thermoHotWater;

	public static 	Integer							tempHotWaterPrevious;
	
	public static 	Relays	 						relays;
	public static 	Relay							burnerPower;
	public static 	Relay							mixerUp;
	public static 	Relay							mixerDown;
	
	public static 	Circuits	 					circuits;
	public static 	Circuit_HotWater				circuitHotWater;
	public static 	Circuit_Radiator				circuitGradient;
	public static 	Circuit_Mixer					circuitFloor;
	
	public static	Pumps							pumps;
	public static 	Pump							pumpWater;
	public static 	Pump							pumpFloor;
	public static 	Pump							pumpRadiator;

	public static 	Mixer							mixer;

	public static 	Integer							summerTemp;
	public static 	Integer							summerPumpDuration;	
	public static 	Long							summerPumpTime;	
	public static 	Boolean							summerWorkDone;

	public static 	LCD								display;	
	public static 	ADC								burnerVoltages;	
	public static 	Buttons							buttons;	
	
	private 		Circuit_Abstract 				circuit;			// Used during XMLparsing		

	public static 	String							calendarsDateTime;	// Used to dateTime the Calendar version in use

	public Global() throws IOException, SAXException, ParserConfigurationException
	{
		Global.display 								= new LCD();
		Global.buttons 								= new Buttons();	

		display.clear();
		display.writeAtPosition(0, 0, "Initialising");
		display.writeAtPosition(1, 0, " Reading params");
		
		try 
		{
			SAXParserFactory 	saxFactory 			= SAXParserFactory.newInstance();
			SAXParser 			saxParser 			= saxFactory.newSAXParser();
			
			saxParser.parse("eRegulator.xml", this);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 		
		
		display.writeAtPosition(1, 18, "Ok");
		// Other initialisation messages are displayed by Control.java
	}
	public void startElement(String uri, String localName, String tagName, Attributes attributes) throws SAXException 
    {
		if (attributes.getLength() > 0)
		{
			if (attributes.getValue("type").equalsIgnoreCase("Collection")) 
			{
				if (tagName.equalsIgnoreCase("Thermometers"))
				{
					Global.thermometers 				= new Thermometers(); 
				}
				else if (tagName.equalsIgnoreCase("Relays"))
				{
					Global.relays 						= new Relays(); 
				}
				else if (tagName.equalsIgnoreCase("Circuits"))
				{
					Global.circuits 					= new Circuits(); 
				}
				else if (tagName.equalsIgnoreCase("Pumps"))
				{
					Global.pumps 					= new Pumps(); 
				}
			}
			else if (attributes.getValue("type").equalsIgnoreCase("Object"))
			{
				if (tagName.equalsIgnoreCase("Thermometer"))
				{
					String name 						= attributes.getValue("name");
					String address 						= attributes.getValue("address");
					String friendlyName					= attributes.getValue("friendlyName");

					Global.thermometers.add(name, address, friendlyName);
				}
				else if (tagName.equalsIgnoreCase("Circuit"))
				{
					String name 						= attributes.getValue("name");
					String friendlyName					= attributes.getValue("friendlyName");
					String circuitType					= attributes.getValue("circuitType");
					String tempMax 						= attributes.getValue("tempMax");
					String rampUpTime					= attributes.getValue("rampUpTime");

					Global.circuits.add(name, friendlyName, circuitType, tempMax, rampUpTime);
					this.circuit 						= Global.circuits.fetchcircuit(name);
				}	
				else if (tagName.equalsIgnoreCase("tempGradient"))
				{
					String outsideLow 					= attributes.getValue("outsideLow");
					String tempLow 						= attributes.getValue("tempLow");
					String outsideHigh 					= attributes.getValue("outsideHigh");
					String tempHigh 					= attributes.getValue("tempHigh");
					
					this.circuit.temperatureGradient	= new TemperatureGradient(outsideLow, tempLow, outsideHigh, tempHigh);
				}
				else if (tagName.equalsIgnoreCase("Relay"))
				{
					String name 						= attributes.getValue("name");
					String address 						= attributes.getValue("address");
					String friendlyName					= attributes.getValue("friendlyName");

					Global.relays.addFromXML(name, address, friendlyName);
				}
				else if (tagName.equalsIgnoreCase("Mixer"))
				{
					String name 						= attributes.getValue("name");
					String swingTime					= attributes.getValue("swingTime");
					String lagTime						= attributes.getValue("lagTime");
					String gainP						= attributes.getValue("gainP");
					String timeD						= attributes.getValue("timeD");
					String timeI						= attributes.getValue("timeI");
					String gainI						= attributes.getValue("gainI");

					this.circuit.mixer					= new Mixer(name, swingTime, lagTime, gainP, timeD, timeI, gainI);
				}
				else if (tagName.equalsIgnoreCase("Pump"))
				{
					String name 						= attributes.getValue("name");

					Global.pumps.addFromXML(name);
				}
				else if (tagName.equalsIgnoreCase("Params"))
				{
					Global.summerTemp					= Integer.parseInt(attributes.getValue("summerTemp"));
					Global.summerPumpDuration			= Integer.parseInt(attributes.getValue("summerPumpDuration"));
					Global.summerPumpTime				= Global.parseTime(attributes.getValue("summerPumpTime"));	
					Global.summerWorkDone				= false;	
				}
			}
			else
			{
				// Nothing of interest
			}
		}
	}
	public void endElement(String uri, String localName, String tagName) throws SAXException 
	{
	}
	public void characters(char ch[], int start, int length) throws SAXException
	{
	}
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
	public static void waitSeconds(Integer seconds)
	{
		// Sleeps a supplied number of seconds
		waitMilliSeconds(seconds * 1000);
 	}
	public static void waitMilliSeconds(Integer milliSeconds)
	{
		// Sleeps a supplied number of milliseconds
		try
        {
            Thread.sleep(milliSeconds);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
 	}
	public static void waitMilliSeconds(Long milliSeconds)
	{
		// Sleeps a supplied number of milliseconds
		try
        {
            Thread.sleep(milliSeconds);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
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
}
