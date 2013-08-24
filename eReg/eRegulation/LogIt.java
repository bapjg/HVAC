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
	public static Boolean 				logDisplay 				= true;
	
	public LogIt()
	{
		logDisplay												= true;
	}
	public static void  info(String className, String methodName, String message)
	{
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
    		System.out.println("Info : httpSend Error" + e);
			System.out.println(dateTimeStamp() + " : Info   : " + className + "/" + methodName + " - " + message);
		}
		finally
		{
			Global.httpSemaphore.unlock();			
		}
		if (logDisplay)
		{
			System.out.println(dateTimeStamp() + " : Info   : " + className + "/" + methodName + " - " + message);
		}
	}
	public static void  error(String className, String methodName, String message)
	{
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
    		System.out.println("Error : httpSend Error" + e);
			System.out.println(dateTimeStamp() + " : Error  : " + className + "/" + methodName + " - " + message);
		}
		finally
		{
			Global.httpSemaphore.unlock();			
		}
		if (logDisplay)
		{
			System.out.println(dateTimeStamp() + " : Error  : " + className + "/" + methodName + " - " + message);
		}
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
    		System.out.println("TempData : httpSend Error" + e);
		}
		finally
		{
			Global.httpSemaphore.unlock();			
		}
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
    		System.out.println("FuelData : httpSend Error" + e);
		}
		finally
		{
			Global.httpSemaphore.unlock();			
		}
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
    		System.out.println("Action : httpSend Error" + e);
		}
		finally
		{
			Global.httpSemaphore.unlock();			
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
