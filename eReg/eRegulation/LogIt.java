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
		Global.httpSemaphore.lock();
		try 
		{
			URL 						serverURL 				= new URL("http://192.168.5.20:8080/hvac/Monitor");
			URLConnection 				servletConnection 		= serverURL.openConnection();
			servletConnection.setDoOutput(true);
			servletConnection.setUseCaches(false);
			servletConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");
			
			Message_Report	 			messageSend 			= new Message_Report();
			messageSend.dateTime 								= System.currentTimeMillis();
			messageSend.reportType 								= "Info";
			messageSend.className 								= className;
			messageSend.methodName 								= methodName;
			messageSend.reportText 								= message;

			
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
				//System.out.println("Temp data  is : Ack");
			}
			else
			{
				System.out.println("Logit.info  is : Nack");
			}
		} 
		catch (Exception e) 
		{
    		System.out.println("Error temp received");
		}
		Global.httpSemaphore.unlock();
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
		Global.httpSemaphore.lock();
		try 
		{
			URL 						serverURL 				= new URL("http://192.168.5.20:8080/hvac/Monitor");
			URLConnection 				servletConnection 		= serverURL.openConnection();
			servletConnection.setDoOutput(true);
			servletConnection.setUseCaches(false);
			servletConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");
			
			Message_Report	 			messageSend 			= new Message_Report();
			messageSend.dateTime 								= System.currentTimeMillis();
			messageSend.reportType 								= "Error";
			messageSend.className 								= className;
			messageSend.methodName 								= methodName;
			messageSend.reportText 								= message;

			
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
				//System.out.println("Temp data  is : Ack");
			}
			else
			{
				System.out.println("Logit.info  is : Nack");
			}
		} 
		catch (Exception e) 
		{
    		System.out.println("Error temp received");
		}
		Global.httpSemaphore.lock();
	}
 	public static void tempData()
    {
		Global.httpSemaphore.lock();
		try 
		{
			URL 						serverURL 				= new URL("http://192.168.5.20:8080/hvac/Monitor");
			URLConnection 				servletConnection 		= serverURL.openConnection();
			servletConnection.setDoOutput(true);
			servletConnection.setUseCaches(false);
			servletConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");
			
			Message_Temperatures 			messageSend 		= new Message_Temperatures();
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
				//System.out.println("Temp data  is : Ack");
			}
			else
			{
				System.out.println("Temp data  is : Nack");
			}
		} 
		catch (Exception e) 
		{
    		System.out.println("Error temp received");
		}
		Global.httpSemaphore.unlock();
    }
	public static void fuelData(Long fuelConsumed)
    {
		Global.httpSemaphore.lock();
		try 
		{
			URL 						serverURL 				= new URL("http://192.168.5.20:8080/hvac/Monitor");
			URLConnection 				servletConnection 		= serverURL.openConnection();
			servletConnection.setDoOutput(true);
			servletConnection.setUseCaches(false);
			servletConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");
			
			Message_Fuel	 			messageSend 			= new Message_Fuel();
			messageSend.dateTime 								= System.currentTimeMillis();
			messageSend.fuelConsumed 							= fuelConsumed;
			
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
	    		System.out.println("Fuel Error 1 received");
			}
			
			if (messageReceive instanceof Message_Ack)
			{
				//System.out.println("Fuel data  is : Ack");
			}
			else
			{
				System.out.println("Fuel data  is : Nack");
			}
		} 
		catch (Exception e) 
		{
    		System.out.println("Error fuel received");
		}
		Global.httpSemaphore.unlock();
    }
	public static void action(String device, String action)
    {
		Global.httpSemaphore.lock();
		try 
		{
			URL 						serverURL 				= new URL("http://192.168.5.20:8080/hvac/Monitor");
			URLConnection 				servletConnection 		= serverURL.openConnection();
			servletConnection.setDoOutput(true);
			servletConnection.setUseCaches(false);
			servletConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");
			
			Message_Action	 			messageSend 			= new Message_Action();
			messageSend.dateTime 								= System.currentTimeMillis();
			messageSend.device 									= device;
			messageSend.action 									= action;
			
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
	    		System.out.println("Action Error 1 received");
			}
			
			if (messageReceive instanceof Message_Ack)
			{
				//System.out.println("Fuel data  is : Ack");
			}
			else
			{
				System.out.println("Action data  is : Nack");
			}
		} 
		catch (Exception e) 
		{
    		System.out.println("Error Action received");
		}
		Global.httpSemaphore.unlock();
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
