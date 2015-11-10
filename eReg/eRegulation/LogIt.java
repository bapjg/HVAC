package eRegulation;

import java.io.FileInputStream;
import java.io.FileWriter;
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

import com.google.gson.Gson;

import HVAC_Common.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class LogIt
{
	public static Boolean 										logDisplay 					= true;
	public static Boolean 										logFile 					= true;
	
	public LogIt()
	{
		logDisplay																			= true;
	}
	public static void toLogFile(String message)
	{
		try
		{
	        FileWriter 											fw 							= new FileWriter("/mnt/DZ/HVAC_LogFile.txt",true); //the true will append the new data
	        fw.write(message + "\n\r");//appends the string to the file
	        fw.flush();
	        fw.close();
		}
		catch (Exception ex) {}
	}
	public static void debug(String message)
	{
		System.out.println(dateTimeStamp() + " LogIt.debug " + message);
	}
	public static void  logMessage(String messageType, String className, String methodName, String message)
	{
		if (!Global.httpSemaphore.semaphoreLock("LogIt.logMessage"))
		{
			System.out.println(dateTimeStamp() + " LogIt.logMessage Lock timedout, owned by " + Global.httpSemaphore.owner);
			return;
		}

		HTTP_Request	<Rpt_Report>							httpRequest					= new HTTP_Request <Rpt_Report> ("Monitor");
		
		Rpt_Report	 											messageSend 				= new Rpt_Report();
		messageSend.dateTime 																= System.currentTimeMillis();
		messageSend.reportType 																= messageType;
		messageSend.className 																= className;
		messageSend.methodName 																= methodName;
		messageSend.reportText 																= message;
			
		Rpt_Abstract 											messageReceive 				= httpRequest.sendData(messageSend);
			
		if (!(messageReceive instanceof Rpt_Abstract.Ack))
		{
			// System.out.println(dateTimeStamp() + " Logit.logMessage" + messageType + "  is : Nack");
		}

		Global.httpSemaphore.semaphoreUnLock();			
	}
	
	public static void  display(String className, String methodName, String message)
	{
		String 													classMethod					= (className + "/" + methodName + "                                             ").substring(0,30);
		System.out.println(dateTimeStamp() + " : Display: " + classMethod + " - " + message);
	}
	public static void  info(String className, String methodName, String message)
	{
		logMessage("Info", className, methodName, message);

		if (logDisplay)
		{
			String 												classMethod					= (className + "/" + methodName + "                                             ").substring(0,30);
			System.out.println(dateTimeStamp() + " : Info   : " + classMethod + " - " + message);
		}
	}
	public static void  info(String className, String methodName, String message, Boolean display)
	{
		logMessage("Info", className, methodName, message);

		if (display)
		{
			String 												classMethod					= (className + "/" + methodName + "                                             ").substring(0,30);
			System.out.println(dateTimeStamp() + " : Info   : " + classMethod + " - " + message);
		}
	}
	public static void  error(String className, String methodName, String message)
	{
		logMessage("Error", className, methodName, message);

		if (logDisplay)
		{
			String 												classMethod					= (className + "/" + methodName + "                                             ").substring(0,30);
			System.out.println(dateTimeStamp() + " : Error  : " + classMethod + " - " + message);
		}
	}
	public static void  error(String className, String methodName, String message, Boolean display)
	{
		logMessage("Error", className, methodName, message);

		if (display)
		{
			String 												classMethod					= (className + "/" + methodName + "                                             ").substring(0,30);
			System.out.println(dateTimeStamp() + " : Error  : " + classMethod + " - " + message);
		}
	}
	public static void pidData	(Rpt_PID.Update	messageSend)
	{
		if (!Global.httpSemaphore.semaphoreLock("LogIt.pidData"))
		{
			System.out.println(dateTimeStamp() + " LogIt.pidData Lock timedout, owned by " + Global.httpSemaphore.owner);
			return;
		}
	
		HTTP_Request 											httpRequest					= new HTTP_Request <Rpt_PID.Update> ("Monitor");
		Rpt_Abstract 											messageReceive 				= httpRequest.sendData(messageSend);
		
		if (!(messageReceive instanceof Rpt_Abstract.Ack))
		{
			// System.out.println(dateTimeStamp() + " pid data  is : Nack");
		}
		Global.httpSemaphore.semaphoreUnLock();			
	}
	public static void tempData()
    {
		if (!Global.httpSemaphore.semaphoreLock("LogIt.tempData"))
		{
			System.out.println(dateTimeStamp() + " LogIt.tempData Lock timedout, owned by " + Global.httpSemaphore.owner);
			return;
		}

		HTTP_Request <Rpt_Temperatures>							httpRequest					= new HTTP_Request <Rpt_Temperatures> ("Monitor");
		
		Rpt_Temperatures 										messageSend 				= new Rpt_Temperatures();
		messageSend.dateTime 																= System.currentTimeMillis();
		
		
		// TODO should we not check for null values
		
		messageSend.tempBoiler 																= Global.thermoBoiler.reading;
		messageSend.tempBoilerIn 															= Global.thermoBoilerIn.reading;
		messageSend.tempBoilerOut															= Global.thermoBoilerOut.reading;
									
		messageSend.tempFloorIn 															= Global.thermoFloorIn.reading;
		messageSend.tempFloorOut 															= Global.thermoFloorOut.reading;
									
		messageSend.tempRadiatorIn 															= Global.thermoRadiatorIn.reading;
		messageSend.tempRadiatorOut 														= Global.thermoRadiatorOut.reading;
									
		messageSend.tempHotWater 															= Global.thermoHotWater.reading; 
		messageSend.tempOutside 															= Global.thermoOutside.reading;
		messageSend.tempLivingRoom 															= Global.thermoLivingRoom.reading;
										
		messageSend.pidMixerTarget															= Global.thermoFloorOut.pidControler.target;
		messageSend.tempLivingRoomTarget													= 19000;
									
		messageSend.pidMixerDifferential													= Global.thermoFloorOut.pidControler.dTdt();
		messageSend.pidBoilerOutDifferential												= Global.thermoBoilerOut.pidControler.dTdt();
		
		Rpt_Abstract 											messageReceive 				= httpRequest.sendData(messageSend);
			
		if (!(messageReceive instanceof Rpt_Abstract.Ack))
		{
			// System.out.println(dateTimeStamp() + " Temp data  is : Nack");
		}

		Global.httpSemaphore.semaphoreUnLock();			
    }
	public static void mixerData(Long dateTimeStart, Integer positionTrackedStart, Long dateTimeEnd, Integer positionTrackedEnd)
    {
		if (!Global.httpSemaphore.semaphoreLock("LogIt.mixerData"))
		{
			System.out.println(dateTimeStamp() + " LogIt.mixerData Lock timedout, owned by " + Global.httpSemaphore.owner);
			return;
		}

		HTTP_Request <Rpt_MixerMouvement>						httpRequest					= new HTTP_Request <Rpt_MixerMouvement> ("Monitor");
				
		Rpt_MixerMouvement 										messageSend 				= new Rpt_MixerMouvement();
		messageSend.dateTimeStart 															= dateTimeStart;
		messageSend.positionTrackedStart 													= positionTrackedStart;
		messageSend.dateTimeEnd 															= dateTimeEnd;
		messageSend.positionTrackedEnd														= positionTrackedEnd;
				
		Rpt_Abstract 											messageReceive 				= httpRequest.sendData(messageSend);
		
		if (!(messageReceive instanceof Rpt_Abstract.Ack))
		{
			// System.out.println(dateTimeStamp() + " Temp data  is : Nack");
		}

		Global.httpSemaphore.semaphoreUnLock();			
    }
	public static void fuelData(Long fuelConsumed)
    {
		if (!Global.httpSemaphore.semaphoreLock("LogIt.fuelData"))
		{
			System.out.println(dateTimeStamp() + " LogIt.fuelData Lock timedout, owned by " + Global.httpSemaphore.owner);
			return;
		}

		HTTP_Request											httpRequest					= new HTTP_Request <Ctrl_Fuel_Consumption.Update> ("Monitor");
			
		Ctrl_Fuel_Consumption.Update	 						messageSend 				= (new Ctrl_Fuel_Consumption()).new Update();
		messageSend.dateTime 																= Global.DateTime.now();
		messageSend.fuelConsumed 															= fuelConsumed;
			
		Ctrl__Abstract 											messageReceive	 			= httpRequest.sendData(messageSend);
			
		if (!(messageReceive instanceof Ctrl__Abstract.Ack))
		{
			// System.out.println(dateTimeStamp() + " Fuel data  is : Nack");
		}

		Global.httpSemaphore.semaphoreUnLock();			
    }
	public static void action(String device, String action)
    {
		if (!Global.httpSemaphore.semaphoreLock("LogIt.action"))
		{
			System.out.println(dateTimeStamp() + " LogIt.action Lock timedout, owned by " + Global.httpSemaphore.owner);
			return;
		}

		HTTP_Request <Rpt_Action>								httpRequest					= new HTTP_Request <Rpt_Action> ("Monitor");

		Rpt_Action	 											messageSend 				= new Rpt_Action();
		messageSend.dateTime 																= System.currentTimeMillis();
		messageSend.device 																	= device;
		messageSend.action 																	= action;
			
		Rpt_Abstract 											messageReceive 				= httpRequest.sendData(messageSend);
			
		if (!(messageReceive instanceof Rpt_Abstract.Ack))
		{
			// System.out.println(dateTimeStamp() + " Action data  is : Nack");
		}

		Global.httpSemaphore.semaphoreUnLock();			
    }
    public static String  dateTimeStamp()
	{
		Date 													now 						= new Date();
		SimpleDateFormat 										dateFormat 					= new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		String 													nowFormatted 				= dateFormat.format(now);
		return nowFormatted;
	}
    public static String  timeStamp()
	{
		Date 													now 						= new Date();
		SimpleDateFormat 										dateFormat 					= new SimpleDateFormat("HH:mm:ss");
		String 													nowFormatted 				= dateFormat.format(now);
		return nowFormatted;
	}
}
