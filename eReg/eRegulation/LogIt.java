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
	public static Boolean 						logDisplay 			= true;
	
	public LogIt()
	{
		logDisplay													= true;
	}

	public static void  logMessage(String messageType, String className, String methodName, String message)
	{
		if (!Global.httpSemaphore.semaphoreLock("LogIt.logMessage"))
		{
			System.out.println(dateTimeStamp() + " LogIt.logMessage Lock timedout, owned by " + Global.httpSemaphore.owner);
			return;
		}

		HTTP_Request	<Message_Report>		httpRequest			= new HTTP_Request <Message_Report> ("Monitor");
		
		Message_Report	 						messageSend 		= new Message_Report();
		messageSend.dateTime 										= System.currentTimeMillis();
		messageSend.reportType 										= messageType;
		messageSend.className 										= className;
		messageSend.methodName 										= methodName;
		messageSend.reportText 										= message;
			
		Message_Abstract 						messageReceive 		= httpRequest.sendData(messageSend);
			
		if (!(messageReceive instanceof Message_Abstract.Ack))
		{
			// System.out.println(dateTimeStamp() + " Logit.logMessage" + messageType + "  is : Nack");
		}

		Global.httpSemaphore.semaphoreUnLock();			
	}
	
	public static void  display(String className, String methodName, String message)
	{
		System.out.println(dateTimeStamp() + " : Display: " + className + "/" + methodName + " - " + message);

	}
	public static void  info(String className, String methodName, String message)
	{
		logMessage("Info", className, methodName, message);

		if (logDisplay)
		{
			System.out.println(dateTimeStamp() + " : Info   : " + className + "/" + methodName + " - " + message);
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
	public static void  error(String className, String methodName, String message)
	{
		logMessage("Error", className, methodName, message);

		if (logDisplay)
		{
			System.out.println(dateTimeStamp() + " : Error  : " + className + "/" + methodName + " - " + message);
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
	public static void pidData
		(
				Integer target, 
				Float 	proportional,
				Float 	differential,
				Float 	integral,
				Float 	kP,
				Float 	kD,
				Float 	kI,
				Float 	result,
				Integer tempOut,
				Integer tempBoiler	
		)
    {
		if (!Global.httpSemaphore.semaphoreLock("LogIt.pidData"))
		{
			System.out.println(dateTimeStamp() + " LogIt.pidData Lock timedout, owned by " + Global.httpSemaphore.owner);
			return;
		}

		HTTP_Request 							httpRequest			= new HTTP_Request <Message_PID.Update> ("Monitor");
		
		Message_PID.Data 						messageSend 		= (new Message_PID()).new Update();
		messageSend.dateTime 										= System.currentTimeMillis();
		messageSend.target		 									= target; 
		messageSend.proportional		 							= proportional; 
		messageSend.differential		 							= differential; 
		messageSend.integral		 								= integral; 
		messageSend.kP		 										= kP; 
		messageSend.kD		 										= kD; 
		messageSend.kI		 										= kI; 
		messageSend.result		 									= result; 
		messageSend.tempOut		 									= tempOut;
		messageSend.tempBoiler		 								= tempBoiler;
			
		Message_Abstract 						messageReceive 		= httpRequest.sendData(messageSend);
		
		if (!(messageReceive instanceof Message_Abstract.Ack))
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

		HTTP_Request <Message_Temperatures>		httpRequest			= new HTTP_Request <Message_Temperatures> ("Monitor");
		
		Message_Temperatures 					messageSend 		= new Message_Temperatures();
		messageSend.dateTime 										= System.currentTimeMillis();
		
		messageSend.tempBoiler 										= Global.thermoBoiler.reading;
		messageSend.tempBoilerIn 									= Global.thermoBoilerIn.reading;
		messageSend.tempBoilerOut									= Global.thermoBoilerOut.reading;
		
		messageSend.tempFloorIn 									= Global.thermoFloorIn.reading;
		messageSend.tempFloorOut 									= Global.thermoFloorOut.reading;
		
		messageSend.tempRadiatorIn 									= Global.thermoRadiatorIn.reading;
		messageSend.tempRadiatorOut 								= Global.thermoRadiatorOut.reading;
		
		messageSend.tempHotWater 									= Global.thermoHotWater.reading; 
		messageSend.tempOutside 									= Global.thermoOutside.reading;
		messageSend.tempLivingRoom 									= Global.thermoLivingRoom.reading;
			
		messageSend.pidMixerTarget									= Global.thermoFloorOut.pidControler.target;
		messageSend.tempLivingRoomTarget							= 190;
		
		messageSend.pidMixerDifferential							= Global.thermoFloorOut.pidControler.dTdt();
		messageSend.pidBoilerOutDifferential						= Global.thermoBoilerOut.pidControler.dTdt();

		Message_Abstract 						messageReceive 		= httpRequest.sendData(messageSend);
		
		if (!(messageReceive instanceof Message_Abstract.Ack))
		{
			// System.out.println(dateTimeStamp() + " Temp data  is : Nack");
		}

		Global.httpSemaphore.semaphoreUnLock();			
    }
	public static void mixerData(Long dateTimeStart,Integer positionTrackedStart, Long dateTimeEnd, Integer positionTrackedEnd)
    {
		if (!Global.httpSemaphore.semaphoreLock("LogIt.mixerData"))
		{
			System.out.println(dateTimeStamp() + " LogIt.mixerData Lock timedout, owned by " + Global.httpSemaphore.owner);
			return;
		}

		HTTP_Request <Message_MixerMouvement>	httpRequest			= new HTTP_Request <Message_MixerMouvement> ("Monitor");
		
		Message_MixerMouvement 					messageSend 		= new Message_MixerMouvement();
		messageSend.dateTimeStart 									= dateTimeStart;
		messageSend.positionTrackedStart 							= positionTrackedStart;
		messageSend.dateTimeEnd 									= dateTimeEnd;
		messageSend.positionTrackedEnd								= positionTrackedEnd;
		
//		System.out.println("Start : " + dateTimeStart);
//		System.out.println("End   : " + dateTimeEnd);
		
		Message_Abstract 						messageReceive 		= httpRequest.sendData(messageSend);
		
		if (!(messageReceive instanceof Message_Abstract.Ack))
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

		HTTP_Request							httpRequest			= new HTTP_Request <Message_Fuel.Update> ("Monitor");
			
		Message_Fuel.Update	 					messageSend 		= (new Message_Fuel()).new Update();
		messageSend.dateTime 										= System.currentTimeMillis();
		messageSend.fuelConsumed 									= fuelConsumed;
			
		Message_Abstract 						messageReceive	 	= httpRequest.sendData(messageSend);
			
		if (!(messageReceive instanceof Message_Abstract.Ack))
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

		HTTP_Request <Message_Action>			httpRequest			= new HTTP_Request <Message_Action> ("Monitor");

		Message_Action	 						messageSend 		= new Message_Action();
		messageSend.dateTime 										= System.currentTimeMillis();
		messageSend.device 											= device;
		messageSend.action 											= action;
			
		Message_Abstract 						messageReceive 		= httpRequest.sendData(messageSend);
			
		if (!(messageReceive instanceof Message_Abstract.Ack))
		{
			// System.out.println(dateTimeStamp() + " Action data  is : Nack");
		}

		Global.httpSemaphore.semaphoreUnLock();			
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
