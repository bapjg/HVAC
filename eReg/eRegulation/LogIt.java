package eRegulation;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.text.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class LogIt
{
	public static Boolean 				logDisplay 				= true;
	public static String 				owner	 				= "";
	
	public LogIt()
	{
		logDisplay												= true;
	}

	public static void  logMessage(String messageType, String className, String methodName, String message)
	{
		Boolean 						lockResult;
		try
		{
			lockResult = Global.httpSemaphore.tryLock(2, TimeUnit.SECONDS);
			owner = Thread.currentThread().getName();
		}
		catch (InterruptedException e1)
		{
			System.out.println(dateTimeStamp() + " LogIt.logMessage.tryLock failed");
			return;
		}
		
		if (!lockResult)
		{
			System.out.println(dateTimeStamp() + " LogIt.logMessage.tryLock timedout, owned by " + owner);
			return;
		}
		
		try 
		{
			URL 						serverURL 				= new URL("http://192.168.5.20:8080/hvac/Monitor");
			URLConnection 				servletConnection 		= serverURL.openConnection();
			servletConnection.setDoOutput(true);
			servletConnection.setUseCaches(false);
			servletConnection.setConnectTimeout(1000);
			servletConnection.setReadTimeout(1000);
			servletConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");
			
			Message_Report	 			messageSend 			= new Message_Report();
			messageSend.dateTime 								= System.currentTimeMillis();
			messageSend.reportType 								= messageType;
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
	    		System.out.println(dateTimeStamp() + " Logit." + messageType + " is : ClassNotFoundException " + e);
			}
			
			if (messageReceive instanceof Message_Ack)
			{
				//System.out.println("Temp data  is : Ack");
			}
			else
			{
				System.out.println(dateTimeStamp() + " Logit." + messageType + "  is : Nack");
			}
		} 
		catch (SocketTimeoutException ste)
		{
    		System.out.println(dateTimeStamp() + " Logit." + messageType + " Timeout occured : " + ste);
		}
		catch (Exception e) 
		{
    		System.out.println(dateTimeStamp() + " Logit." + messageType + "  : httpSend Error" + e);
			System.out.println(dateTimeStamp() + " : " + messageType  + " : " + className + "/" + methodName + " - " + message);
		}
		finally
		{
			Global.httpSemaphore.unlock();			
			owner = "";
		}
	}
	
	public static void  info(String className, String methodName, String message)
	{
		logMessage("Info", className, methodName, message);

		if (logDisplay)
		{
			System.out.println(dateTimeStamp() + " : Info   : " + className + "/" + methodName + " - " + message);
		}
	}
	public static void  error(String className, String methodName, String message)
	{
		logMessage("Error", className, methodName, message);

		if (logDisplay)
		{
			System.out.println(dateTimeStamp() + " : Error  : " + className + "/" + methodName + " - " + message);
		}
	}
	public static void  info(String className, String methodName, String message, Boolean display)
	{
		logMessage("Info", className, methodName, message);

		if (display)
		{
			System.out.println(dateTimeStamp() + " : Info   : " + className + "/" + methodName + " - " + message);
		}
	}
	public static void  error(String className, String methodName, String message, Boolean display)
	{
		logMessage("Error", className, methodName, message);

		if (display)
		{
			System.out.println(dateTimeStamp() + " : Error  : " + className + "/" + methodName + " - " + message);
		}
	}
	public static void tempData_OLD()
    {
		Boolean 						lockResult;
		try
		{
			lockResult = Global.httpSemaphore.tryLock(2, TimeUnit.SECONDS);
			owner = Thread.currentThread().getName();
		}
		catch (InterruptedException e1)
		{
			System.out.println(dateTimeStamp() + " LogIt.tempData.tryLock failed");
			return;
		}
		
		if (!lockResult)
		{
			System.out.println(dateTimeStamp() + " LogIt.tempData.tryLock timedout, owned by " + owner);
			return;
		}

		try 
		{
			URL 						serverURL 				= new URL("http://192.168.5.20:8080/hvac/Monitor");
			URLConnection 				servletConnection 		= serverURL.openConnection();
			servletConnection.setDoOutput(true);
			servletConnection.setUseCaches(false);
			servletConnection.setConnectTimeout(1000);
			servletConnection.setReadTimeout(1000);
			servletConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");
			
			Message_Temperatures 		messageSend 			= new Message_Temperatures();
			messageSend.dateTime 								= System.currentTimeMillis();
			messageSend.tempHotWater 							= Global.thermoHotWater.reading; 
			messageSend.tempBoiler 								= Global.thermoBoiler.reading;
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
	    		System.out.println(dateTimeStamp() + " Error 1 received");
			}
			
			if (messageReceive instanceof Message_Ack)
			{
				//System.out.println("Temp data  is : Ack");
			}
			else
			{
				System.out.println(dateTimeStamp() + " Temp data  is : Nack");
			}
		} 
		catch (SocketTimeoutException ste)
		{
    		System.out.println(dateTimeStamp() + " TempData : Timeout occured : " + ste);
		}
		catch (Exception e) 
		{
    		System.out.println(dateTimeStamp() + " TempData : httpSend Error" + e);
		}
		finally
		{
			Global.httpSemaphore.unlock();			
			owner = "";
		}
    }
	public static void tempData()
    {
		Boolean 						lockResult;
		try
		{
			lockResult = Global.httpSemaphore.tryLock(2, TimeUnit.SECONDS);
			owner = Thread.currentThread().getName();
		}
		catch (InterruptedException e1)
		{
			System.out.println(dateTimeStamp() + " LogIt.tempData.tryLock failed");
			return;
		}
		
		if (!lockResult)
		{
			System.out.println(dateTimeStamp() + " LogIt.tempData.tryLock timedout, owned by " + owner);
			return;
		}

		LogIt_HTTP	<Message_Temperatures>	httpRequest				= new LogIt_HTTP <Message_Temperatures> ("Monitor");
		
		Message_Temperatures 				messageSend 			= new Message_Temperatures();
		messageSend.dateTime 										= System.currentTimeMillis();
		messageSend.tempHotWater 									= Global.thermoHotWater.reading; 
		messageSend.tempBoiler 										= Global.thermoBoiler.reading;
		messageSend.tempBoilerIn 									= Global.thermoBoilerIn.reading;
		messageSend.tempFloorOut 									= Global.thermoFloorOut.reading;
		messageSend.tempFloorCold 									= Global.thermoFloorCold.reading;
		messageSend.tempFloorHot 									= Global.thermoFloorHot.reading;
		messageSend.tempRadiatorOut 								= Global.thermoRadiatorOut.reading;
		messageSend.tempRadiatorIn 									= Global.thermoRadiatorIn.reading;
		messageSend.tempOutside 									= Global.thermoOutside.reading;
		messageSend.tempLivingRoom 									= Global.thermoLivingRoom.reading;
			
		Message_Abstract 					messageReceive 			= httpRequest.sendData(messageSend);

		if (messageReceive instanceof Message_Ack)
		{
		}
		else
		{
			System.out.println(dateTimeStamp() + " Temp data  is : Nack");
		}

		Global.httpSemaphore.unlock();			
		owner = "";
    }
	public static void fuelData(Long fuelConsumed)
    {
		//Global.httpSemaphore.lock();
		Boolean 						lockResult;
		try
		{
			lockResult = Global.httpSemaphore.tryLock(2, TimeUnit.SECONDS);
			owner = Thread.currentThread().getName();
		}
		catch (InterruptedException e1)
		{
			System.out.println(dateTimeStamp() + " LogIt.fuelData.tryLock failed");
			return;
		}
		
		if (!lockResult)
		{
			System.out.println(dateTimeStamp() + " LogIt.fuelData.tryLock timedout, owned by " + owner);
			return;
		}

		try 
		{
			URL 						serverURL 				= new URL("http://192.168.5.20:8080/hvac/Monitor");
			URLConnection 				servletConnection 		= serverURL.openConnection();
			servletConnection.setDoOutput(true);
			servletConnection.setUseCaches(false);
			servletConnection.setConnectTimeout(1000);
			servletConnection.setReadTimeout(1000);
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
	    		System.out.println(dateTimeStamp() + " Fuel Error 1 received");
			}
			
			if (messageReceive instanceof Message_Ack)
			{
				//System.out.println("Fuel data  is : Ack");
			}
			else
			{
				System.out.println(dateTimeStamp() + " Fuel data  is : Nack");
			}
		} 
		catch (SocketTimeoutException ste)
		{
    		System.out.println(dateTimeStamp() + " FuelData : Timeout occured : " + ste);
		}
		catch (Exception e) 
		{
    		System.out.println(dateTimeStamp() + " FuelData : httpSend Error" + e);
		}
		finally
		{
			Global.httpSemaphore.unlock();			
			owner = "";
		}
    }
	public static void action(String device, String action)
    {
		//Global.httpSemaphore.lock();
		Boolean 						lockResult;
		try
		{
			lockResult = Global.httpSemaphore.tryLock(2, TimeUnit.SECONDS);
			owner = Thread.currentThread().getName();
		}
		catch (InterruptedException e1)
		{
			System.out.println(dateTimeStamp() + " LogIt.action.tryLock failed");
			return;
		}
		
		if (!lockResult)
		{
			System.out.println(dateTimeStamp() + " LogIt.action.tryLock timedout, owned by " + owner);
			return;
		}

		try 
		{
			URL 						serverURL 				= new URL("http://192.168.5.20:8080/hvac/Monitor");
			URLConnection 				servletConnection 		= serverURL.openConnection();
			servletConnection.setDoOutput(true);
			servletConnection.setUseCaches(false);
			servletConnection.setConnectTimeout(1000);
			servletConnection.setReadTimeout(1000);
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
	    		System.out.println(dateTimeStamp() + " Action Error 1 received");
			}
			
			if (messageReceive instanceof Message_Ack)
			{
				//System.out.println("Fuel data  is : Ack");
			}
			else
			{
				System.out.println(dateTimeStamp() + " Action data  is : Nack");
			}
		}
		catch (SocketTimeoutException ste)
		{
    		System.out.println(dateTimeStamp() + " Action : Timeout occured : " + ste);
		}
		catch (Exception e) 
		{
    		System.out.println(dateTimeStamp() + " Action : httpSend Error" + e);
		}
		finally
		{
			Global.httpSemaphore.unlock();
			owner = "";
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
