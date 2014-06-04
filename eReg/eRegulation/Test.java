package eRegulation;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.*;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import HVAC_Common.Ctrl_Configuration;
import HVAC_Common.Ctrl__Abstract;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Test
{

	public static void main(String[] args)
	{
		Control 		Me 							= new Control();
		
		//================================================================
		//
		// Json example
		//
		
		// Create object
		
		TimeZone timeZone = TimeZone.getDefault();
		System.out.println(timeZone.getDisplayName());
		System.out.println(timeZone.getID());
		Integer now = ((int) System.currentTimeMillis());
		now = now /3600;
		now = now/ 1000;
		System.out.println(timeZone.getOffset(System.currentTimeMillis())/3600000);
		now++;
		
//		Ctrl_Configuration.Update 			messageSend			= new Ctrl_Configuration().new Update();
//		((Ctrl_Configuration) messageSend).initialise();
//
//		URL										serverURL;
//		URLConnection							servletConnection;
//
//		serverURL													= null;
//		servletConnection											= null;
//		
//		try
//		{
//			serverURL = new URL("http://192.168.5.10:8888/hvac/" + "Management");
//		}
//		catch (MalformedURLException e)
//		{
//			e.printStackTrace();
//		}
//
//		try
//		{
//			servletConnection 										= serverURL.openConnection();
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//
//		servletConnection.setDoOutput(true);
//		servletConnection.setUseCaches(false);
//		servletConnection.setConnectTimeout(1000);
//		servletConnection.setReadTimeout(1000);
//		servletConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");
//
//		messageSend.dateTime 										= System.currentTimeMillis();
//
//			
//		Ctrl_Abstract							messageReceive		= null;
//
//		try
//		{
//			ObjectOutputStream 			outputToServlet;
//			outputToServlet 										= new ObjectOutputStream(servletConnection.getOutputStream());
//			outputToServlet.writeObject(messageSend);
//			outputToServlet.flush();
//			outputToServlet.close();
//		}
//		catch (SocketTimeoutException eTimeOut)
//		{
//    		// System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP TimeOut on write : " + eTimeOut);
//		}
//		catch (Exception eSend) 
//		{
//    		// System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP Send : " + eSend);
//		}
//
//		try
//		{
//			ObjectInputStream 		response 				= new ObjectInputStream(servletConnection.getInputStream());
//			messageReceive 									= (Ctrl_Abstract) response.readObject();
//		}
//    	catch (Exception e) 
//    	{
//    		 System.out.println("Error " + e);
//		}
//
//		Ctrl_Configuration.Request 			messageSend2		= new Ctrl_Configuration().new Request();
//
//
//			
//		messageReceive												= null;
//
//		try
//		{
//			servletConnection 										= serverURL.openConnection();
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//
//		servletConnection.setDoOutput(true);
//		servletConnection.setUseCaches(false);
//		servletConnection.setConnectTimeout(1000);
//		servletConnection.setReadTimeout(1000);
//		servletConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");
//
//		try
//		{
//			ObjectOutputStream 			outputToServlet;
//			outputToServlet 										= new ObjectOutputStream(servletConnection.getOutputStream());
//			outputToServlet.writeObject(messageSend2);
//			outputToServlet.flush();
//			outputToServlet.close();
//		}
//		catch (SocketTimeoutException eTimeOut)
//		{
//    		// System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP TimeOut on write : " + eTimeOut);
//		}
//		catch (Exception eSend) 
//		{
//    		// System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP Send : " + eSend);
//		}
//
//		try
//		{
//			ObjectInputStream 		response 				= new ObjectInputStream(servletConnection.getInputStream());
//			messageReceive 									= (Ctrl_Abstract) response.readObject();
//		}
//    	catch (Exception e) 
//    	{
//    		 System.out.println("Error " + e);
//		}
//		
//		Gson gson = new GsonBuilder().setPrettyPrinting().create();
//		
//		String z = gson.toJson((Ctrl_Configuration.Data) messageReceive);
//		System.out.println(z);
		
		// Convert Json string(z) back to object(xyx) 

		
//		Ctrl_Configuration_New.Update xyz		= gson.fromJson(z, Ctrl_Configuration_New.Update.class);

		//
		//================================================================

		

    }
}
