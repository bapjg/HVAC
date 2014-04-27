package com.bapjg.hvac_client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import HVAC_Messages.*;

//Template										variable			= something
//Template										ext/imp				class

public class HTTP_Request_OLDOLDOLD
{
	public URL									serverURL;
	public URLConnection						servletConnection;

	public HTTP_Request_OLDOLDOLD()
	{
	}
	//===========================================================================================================================
	//
	//
	public Mgmt_Msg_Abstract ping()
	{
		
		System.out.println("Ping Started");
		Mgmt_Msg_Abstract						messageReceive;
		Mgmt_Msg_Abstract.Ping					messageSend			= (new Mgmt_Msg_Abstract()).new Ping();
		messageReceive												= sendIt(messageSend, "http://192.168.5.20:8888/hvac/Management");
		System.out.println("Ping sent/replied local");

		if (messageReceive instanceof Mgmt_Msg_Abstract.Ack)
		{
			System.out.println("Ping Ack retruned from local");
			Global.serverURL										= "http://192.168.5.20:8080/hvac/Management";
			return new Mgmt_Msg_Abstract().new Ack();	
		}
		System.out.println("Ping sent remote");

		messageReceive												= sendIt(messageSend, "http://home.bapjg.com:8888/hvac/Management");
		System.out.println("Ping sent/replied retruned from remote");
		if (messageReceive instanceof Mgmt_Msg_Abstract.Ack)
		{
			System.out.println("Ping Ack remote");
			Global.serverURL										= "http://home.bapjg.com:8888/hvac/Management";
			return new Mgmt_Msg_Abstract().new Ack();	
		}
		System.out.println("Ping Nack giveup");
		return new Mgmt_Msg_Abstract().new Nack();	
	}
	//
	//
	//===========================================================================================================================

	//===========================================================================================================================
	//
	//
	public Mgmt_Msg_Abstract sendData(Mgmt_Msg_Abstract messageSend)
	{
System.out.println("Mgmt_Msg_Abstract/sendData Started");
		servletConnection											= null;
		Mgmt_Msg_Abstract						messageReceive		= null;
		if (Global.serverURL.equalsIgnoreCase(""))
		{
System.out.println("Mgmt_Msg_Abstract/sendData empty serverURL detected, must wait");
			return new Mgmt_Msg_Abstract().new NoConnection();
		}
		try
		{
			serverURL = new URL(Global.serverURL);
		}
		catch (MalformedURLException eMUE)
		{
System.out.println("Mgmt_Msg_Abstract/sendData MalformedURLException : " + eMUE);
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
		servletConnection.setConnectTimeout(5000);
		servletConnection.setReadTimeout(5000);
		servletConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");

		try
		{
			ObjectOutputStream 					outputToServlet;
			outputToServlet 										= new ObjectOutputStream(servletConnection.getOutputStream());
			outputToServlet.writeObject(messageSend);
			outputToServlet.flush();
			outputToServlet.close();
    		System.out.println(" HTTP_Request Sent ");
		}
		catch (SocketTimeoutException eTimeOut)
		{
    		System.out.println("Mgmt_Msg_Abstract/sendData TimeOut on write : " + eTimeOut);
		}
		catch (Exception eSend) 
		{
    		System.out.println("Mgmt_Msg_Abstract/sendData Send : " + eSend);
		}

		try
		{
			ObjectInputStream 					response 			= new ObjectInputStream(servletConnection.getInputStream());
			messageReceive 									= (Mgmt_Msg_Abstract) response.readObject();
		}
    	catch (ClassNotFoundException eClassNotFound) 
    	{
System.out.println("Mgmt_Msg_Abstract/sendData ClassNotFound : " + eClassNotFound);
    		return new Mgmt_Msg_Abstract().new Nack();
		}
		catch (SocketTimeoutException eTimeOut)
		{
			System.out.println("Mgmt_Msg_Abstract/sendData other");
			// Consider retries
			System.out.println("Mgmt_Msg_Abstract/sendData TimeOut on read or write : " + eTimeOut);
			return new Mgmt_Msg_Abstract().new Nack();	
		}
		catch (Exception e) 
		{
    		System.out.println(" HTTP_Request Send or read : " + e);
    		return new Mgmt_Msg_Abstract().new Nack();	
		}
		return messageReceive;			
	}
	//
	//
	//===========================================================================================================================

	//===========================================================================================================================
	//
	//
	public Mgmt_Msg_Abstract sendIt(Mgmt_Msg_Abstract messageSend, String URL)
	{
System.out.println("Mgmt_Msg_Abstract/sendData Started");
		servletConnection									= null;
		Mgmt_Msg_Abstract				messageReceive		= null;
		try
		{
			serverURL = new URL(URL);
		}
		catch (MalformedURLException eMUE)
		{
System.out.println("Mgmt_Msg_Abstract/sendData MalformedURLException : " + eMUE);
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
		servletConnection.setConnectTimeout(5000);
		servletConnection.setReadTimeout(5000);
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
    		return new Mgmt_Msg_Abstract().new Nack();
		}
		catch (SocketTimeoutException eTimeOut)
		{
			System.out.println("eTimeOut");
			// Consider retries
			System.out.println(" HTTP_Request TimeOut on read or write : " + eTimeOut);
			return new Mgmt_Msg_Abstract().new Nack();	
		}
		catch (Exception e) 
		{
    		System.out.println(" HTTP_Request Send or read : " + e);
    		return new Mgmt_Msg_Abstract().new Nack();	
		}
		return messageReceive;			
	}
	//
	//
	//===========================================================================================================================
}