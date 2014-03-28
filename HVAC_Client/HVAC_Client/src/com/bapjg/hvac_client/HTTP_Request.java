package com.bapjg.hvac_client;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import HVAC_Messages.*;




public class HTTP_Request
{
	public URL						serverURL;
	public URLConnection			servletConnection;

	public HTTP_Request()
	{
	}
	//===========================================================================================================================
	//
	//
	public Mgmt_Msg_Abstract ping()
	{
		System.out.println("TCPRequest");
		
		try
		{
			System.out.println("Step 1");
			Socket clientSocket 							= new Socket("192.168.5.51", 8889);
//			Socket clientSocket 							= new Socket("home.bapjg.com", 8889);
			System.out.println("Step 2");
			ObjectOutputStream output 						= new ObjectOutputStream(clientSocket.getOutputStream());
			System.out.println("Step 3");
		
			Ctrl_Temperatures.Request send 					= new Ctrl_Temperatures().new Request();
			System.out.println("Step 4");

			output.writeObject(send);
			System.out.println("Step 5");

	        output.flush();
	        output.close();

			ObjectInputStream input 						= new ObjectInputStream(clientSocket.getInputStream());
			System.out.println("Step 3.1");

	        Object data_in									= input.readObject();
			Ctrl_Temperatures.Response message_in 			= new Ctrl_Temperatures().new Response();
			message_in										= (Ctrl_Temperatures.Response) data_in;
			
			System.out.println("Date : " + message_in.date);
			System.out.println("Time : " + message_in.time);
			System.out.println("tempBoiler : " + message_in.tempBoiler);
		}
		catch(Exception e)
		{
			System.out.print("Whoops! It didn't work!\n" + e);
        	e.printStackTrace();
		}
		
		System.out.println("Ping Started");
		Mgmt_Msg_Abstract				messageReceive;
		Mgmt_Msg_Abstract.Ping			messageSend			= (new Mgmt_Msg_Abstract()).new Ping();
		messageReceive										= sendIt(messageSend, "http://192.168.5.20:8888/hvac/Management");
		System.out.println("Ping sent/replied local");

		if (messageReceive instanceof Mgmt_Msg_Abstract.Ack)
		{
			System.out.println("Ping Ack retruned from local");
			Global.serverURL								= "http://192.168.5.20:8080/hvac/Management";
			return new Mgmt_Msg_Abstract().new Ack();	
		}
		System.out.println("Ping sent remote");

		messageReceive										= sendIt(messageSend, "http://home.bapjg.com:8888/hvac/Management");
		System.out.println("Ping sent/replied retruned from remote");
		if (messageReceive instanceof Mgmt_Msg_Abstract.Ack)
		{
			System.out.println("Ping Ack remote");
			Global.serverURL								= "http://home.bapjg.com:8888/hvac/Management";
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
		servletConnection									= null;
		Mgmt_Msg_Abstract				messageReceive		= null;
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
			ObjectOutputStream 			outputToServlet;
			outputToServlet 								= new ObjectOutputStream(servletConnection.getOutputStream());
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
			ObjectInputStream 		response 				= new ObjectInputStream(servletConnection.getInputStream());
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