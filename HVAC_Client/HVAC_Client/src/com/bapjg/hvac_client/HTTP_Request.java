package com.bapjg.hvac_client;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;

import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class HTTP_Request
{
	public URL						serverURL;
	public URLConnection			servletConnection;

	public HTTP_Request()
	{
	}
	public boolean ping()
	{
		Mgmt_Msg_Abstract				messageReceive;
		Mgmt_Msg_Ping					messageSend			= new Mgmt_Msg_Ping();
		Global.serverURL									= "http://192.168.5.20:8080/hvac/Management";
		messageReceive										= sendData(messageSend);
		if (messageReceive instanceof Mgmt_Msg_Ping.Ack)
		{
			return true;
		}

		Global.serverURL									= "http://home.bapjg.com:8080/hvac/Management";
		messageReceive										= sendData(messageSend);
		if (messageReceive instanceof Mgmt_Msg_Ping.Ack)
		{
			return true;
		}
		return false;
	}
	public Mgmt_Msg_Abstract sendData(Mgmt_Msg_Abstract messageSend)
	{
		servletConnection									= null;
		Mgmt_Msg_Abstract				messageReceive		= null;
		
		try
		{
			serverURL = new URL(Global.serverURL);
		}
		catch (MalformedURLException eMUE)
		{
			eMUE.printStackTrace();
		}

		try
		{
			servletConnection = serverURL.openConnection();
		}
		catch (IOException eIO)
		{
			eIO.printStackTrace();
		}
		
		servletConnection.setDoOutput(true);
		servletConnection.setUseCaches(false);
		servletConnection.setConnectTimeout(3000);
		servletConnection.setReadTimeout(3000);
		servletConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");

		try
		{
			ObjectOutputStream 			outputToServlet;
			outputToServlet 								= new ObjectOutputStream(servletConnection.getOutputStream());
			outputToServlet.writeObject(messageSend);
			outputToServlet.flush();
			outputToServlet.close();
    		System.out.println(" HTTP_Request Sent ");
		}
		catch (SocketTimeoutException eTimeOut)
		{
    		System.out.println(" HTTP_Request TimeOut on write : " + eTimeOut);
		}
		catch (Exception eSend) 
		{
    		System.out.println(" HTTP_Request Send : " + eSend);
		}

		try
		{
			ObjectInputStream 		response 				= new ObjectInputStream(servletConnection.getInputStream());
			messageReceive 									= (Mgmt_Msg_Abstract) response.readObject();
		}
    	catch (ClassNotFoundException eClassNotFound) 
    	{
    		System.out.println(" HTTP_Request ClassNotFound : " + eClassNotFound);
    		return new Mgmt_Msg_Nack();
		}
		catch (SocketTimeoutException eTimeOut)
		{
			System.out.println("eTimeOut");
			// Consider retries
			System.out.println(" HTTP_Request TimeOut on read or write : " + eTimeOut);
			return new Mgmt_Msg_Nack();	
		}
		catch (Exception e) 
		{
    		System.out.println(" HTTP_Request Send or read : " + e);
			return new Mgmt_Msg_Nack();	
		}
		return messageReceive;			
	}
}