package eRegulation;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.text.*;
import java.io.*;

public class LogIt
{
	public static String 	logFileName 			= "";
	public static Boolean 	logFile 				= false;
	public static Boolean 	logDisplay 				= true;
	
	public LogIt()
	{
		logFileName = "";
		logFile=false;
		logDisplay=true;
	}
	public LogIt(String fileName)
	{
		logFileName = fileName;
		logFile=true;
		logDisplay=false;
	}
	public static void  info(String className, String methodName, String message)
	{
		if (logDisplay)
		{
			System.out.println(dateTimeStamp() + " : Info   : " + className + "/" + methodName + " - " + message);
		}
		if ((logFile) && (logFileName != ""))
		{
			FileWriter out;
            try
            {
	            out = new FileWriter(logFileName, true);
				out.write(dateTimeStamp() + " : "+ className + "/" + methodName + " - " + message + "\r\n");
				out.close();
            }
            catch (IOException e)
            {
	            e.printStackTrace();
            }
		}
	}
	public static void  error(String className, String methodName, String message)
	{
		if (logDisplay)
		{
			System.out.println(dateTimeStamp() + " : Error  : " + className + "/" + methodName + " - " + message);
		}
		if ((logFile) && (logFileName != ""))
		{
			FileWriter out;
            try
            {
	            out = new FileWriter(logFileName, true);
				out.write(dateTimeStamp() + " : "+ className + "/" + methodName + " - " + message + "\r\n");
				out.close();
            }
            catch (IOException e)
            {
	            e.printStackTrace();
            }
		}
	}
    public static void tempHeadings()
    {
    	try 
        {
            PrintWriter temperatureFile = new PrintWriter(new BufferedWriter(new FileWriter("Temperatures.csv", true)));
            temperatureFile.println
            	(
        			"\"dateTime\";" 		+
        			"\"time\";" 			+
        			"\"seconds\";" 			+
        			"\"Boiler\";" 			+
        			"\"Outside\";" 			+
        			"\"HotWater\";" 		+
        			"\"MixerOut\";" 		+
        			"\"MixerCold\";" 		+
        			"\"MixerHot\";" 		+
        			"\"LivingRoom\";" 		+
        			"\"MixerPosition\";"	+
        			"\"Comments\""
            	);
            temperatureFile.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
	public static void tempData()
    {
		try 
		{
			URL 						serverURL 				= new URL("http://192.168.5.20:8080/hvac/Monitor");
			URLConnection 				servletConnection 		= serverURL.openConnection();
			servletConnection.setDoOutput(true);
			servletConnection.setUseCaches(false);
			servletConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");
			
			Message_Readings 			messageSend 			= new Message_Readings();
			messageSend.dateTime 								= System.currentTimeMillis();
			messageSend.tempHotWater 							= Global.thermoBoiler.reading;
			messageSend.tempBoiler 								= Global.thermoHotWater.reading;
			messageSend.tempBoilerIn 							= Global.thermoBoilerIn.reading;
			messageSend.tempFloorOut 							= Global.thermoFloorOut.reading;
			messageSend.tempFloorCold 							= Global.thermoFloorCold.reading;
			messageSend.tempFloorHot 							= Global.thermoFloorHot.reading;
			messageSend.tempRadiatorOut 						= Global.thermoRadiatorOut.reading;
			messageSend.tempRadiatorIn 							= Global.thermoRadiatorIn.reading;
			messageSend.tempOutside 							= Global.thermoOutside.reading;
			messageSend.tempLivingRoom 							= Global.thermoLivingRoom.reading;
			//messageSend.fuelConsumed 							= Global.burner.fuelConsumed;
			messageSend.fuelConsumed 							= 0L;
			
			ObjectOutputStream 			outputToServlet;
			outputToServlet 									= new ObjectOutputStream(servletConnection.getOutputStream());
			outputToServlet.writeObject(messageSend);
			outputToServlet.flush();
			outputToServlet.close();
			
			ObjectInputStream 			response 				= new ObjectInputStream(servletConnection.getInputStream());
			Message_Abstract 			messageReceive 			= null;
			
			try
			{
				messageReceive 									= (Message_Abstract) response.readObject();
			}
	    	catch (ClassNotFoundException e) 
	    	{
	    		System.out.println("Error 1 received");
			}
			
			if (messageReceive instanceof Message_Ack)
			{
				// System.out.println("The data  is : Ack");
			}
			else
			{
				// System.out.println("The data  is : Not ack");
			}
		} 
		catch (Exception e) 
		{
    		System.out.println("Error 2 received");
		}
    }
    public static void tempInfo(String message)
    {
    	System.out.println(dateTimeStamp() + " : Info   : " + "LogIt" + "/" + "tempInfo" + " - " + message);
    	try 
        {
            PrintWriter temperatureFile = new PrintWriter(new BufferedWriter(new FileWriter("Temperatures.csv", true)));
            temperatureFile.println
        	(
        		'"' + dateTimeStamp() 					+ '"' 	+ ';' +
        		'"' + timeStamp() 						+ '"' 	+ ';' + 
        		'"' + Global.getTimeNowSinceMidnight() 	+ '"' 	+ ';' + 
        		Global.thermoBoiler.reading 					+ ';' + 
        		Global.thermoOutside.reading 					+ ';' + 
        		Global.thermoHotWater.reading 					+ ';' + 
        		Global.thermoFloorOut.reading 					+ ';' + 
        		Global.thermoFloorCold.reading 					+ ';' + 
        		Global.thermoFloorHot.reading					+ ';' + 
        		Global.thermoLivingRoom.reading					+ ';' +
            	Global.mixer.positionTracked					+ ';' +	
            	message
        	);
            temperatureFile.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    } 
    public static String  dateTimeStamp()
	{
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		String nowFormatted = dateFormat.format(now);
		return nowFormatted;
	}
    public static String  timeStamp()
	{
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String nowFormatted = dateFormat.format(now);
		return nowFormatted;
	}
}
