package eRegulation;

import java.io.*;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
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

import HVAC_Messages.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Z_Initialise_Calendars
{

	public static void main(String[] args)
	{
		Control 								Me 					= new Control();
		
		//================================================================================================================================
		//
		// Create object to be sent
		//
		
		Ctrl_Calendars.Update 					messageSend			= new Ctrl_Calendars().new Update();
		messageSend.initialise();												// This is what sets up the data

		//
		//================================================================================================================================

		//================================================================================================================================
		//
		// Setup URL & Connection to server
		//
		
		URL										serverURL;
		URLConnection							serverConnection;

		serverURL													= null;
		serverConnection											= null;
		
		try
		{
			serverURL = new URL("http://192.168.5.10:8888/hvac/" + "Management");
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}

		try
		{
			serverConnection 										= serverURL.openConnection();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		serverConnection.setDoOutput(true);
		serverConnection.setUseCaches(false);
		serverConnection.setConnectTimeout(1000);
		serverConnection.setReadTimeout(1000);
		serverConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");

		messageSend.dateTime 										= System.currentTimeMillis();
			
		Ctrl_Abstract							messageReceive		= null;

		//
		//================================================================================================================================

		//================================================================================================================================
		//
		// Setup send message to server
		//

		try
		{
			ObjectOutputStream 			outputToServlet;
			outputToServlet 										= new ObjectOutputStream(serverConnection.getOutputStream());

			System.out.println("Sending message " + messageSend.getClass().toString());
			
			outputToServlet.writeObject(messageSend);
			outputToServlet.flush();
			outputToServlet.close();
		}
		catch (SocketTimeoutException eTimeOut)
		{
    		// System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP TimeOut on write : " + eTimeOut);
		}
		catch (Exception eSend) 
		{
    		// System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP Send : " + eSend);
		}

		//
		//================================================================================================================================

		//================================================================================================================================
		//
		// Setup get Response which should be an Ack
		//

		try
		{
			ObjectInputStream 		response 				= new ObjectInputStream(serverConnection.getInputStream());
			messageReceive 									= (Ctrl_Abstract) response.readObject();
		}
    	catch (Exception e) 
    	{
    		 System.out.println("Error " + e);
		}

		//
		//================================================================================================================================

		//================================================================================================================================
		//
		// Send second message to Server to receive what was just sent
		//

		Ctrl_Calendars.Request 				messageSend2			= new Ctrl_Calendars.Request();
			
		messageReceive												= null;

		try
		{
			serverConnection 										= serverURL.openConnection();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		serverConnection.setDoOutput(true);
		serverConnection.setUseCaches(false);
		serverConnection.setConnectTimeout(1000);
		serverConnection.setReadTimeout(1000);
		serverConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");

		try
		{
			ObjectOutputStream 			outputToServlet;
			outputToServlet 										= new ObjectOutputStream(serverConnection.getOutputStream());
			outputToServlet.writeObject(messageSend2);
			outputToServlet.flush();
			outputToServlet.close();
		}
		catch (SocketTimeoutException eTimeOut)
		{
    		// System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP TimeOut on write : " + eTimeOut);
		}
		catch (Exception eSend) 
		{
    		// System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP Send : " + eSend);
		}

		//
		//================================================================================================================================

		//================================================================================================================================
		//
		// Get the return message
		//

		try
		{
			ObjectInputStream 		response 				= new ObjectInputStream(serverConnection.getInputStream());
			messageReceive 									= (Ctrl_Abstract) response.readObject();
			System.out.println("Class " + messageReceive.getClass().toString());
		}
    	catch (Exception e) 
    	{
    		 System.out.println("Error " + e);
		}

		//
		//================================================================================================================================

		//================================================================================================================================
		//
		// Convert the response to Json and display
		//
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		String z = gson.toJson((Ctrl_Calendars.Data) messageReceive);
		System.out.println(z);
		
		//
		//================================================================================================================================
    }
}