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

public class TCP_Connection
{
	public Socket					piSocket;
	public InetAddress				piAddressV4;
	public Boolean					piConnected				= false;

	public TCP_Connection()
	{
		piConnected											= false;
	}

	public Boolean connect()
	{
		if (piConnected)
		{
			return true;
		}
		System.out.println("connect");
		
		try
		{
			piAddressV4										= InetAddress.getByName("192.168.5.51");
			piSocket 										= new Socket(piAddressV4, 8889);
		}
		catch(Exception e)
		{
			// It Failed so now try over the internet
			try
			{
				piAddressV4 								= InetAddress.getByName("home.bapjg.com");
				piSocket 									= new Socket(piAddressV4, 8889);
			}
			catch(Exception e2)
			{
				// Major problem, return a nack.
				piConnected									= false;
				return false;
			}
		}
		return true;
	}
	
	
	public Ctrl_Abstract ping()
	{
		System.out.println("ping");
		
		try
		{
			piAddressV4										= InetAddress.getByName("192.168.5.51");
			piSocket 										= new Socket(piAddressV4, 8889);
		}
		catch(Exception e)
		{
			// It Failed so now try over the internet
			try
			{
				piAddressV4 								= InetAddress.getByName("home.bapjg.com");
				piSocket 									= new Socket(piAddressV4, 8889);
			}
			catch(Exception e2)
			{
				// Major problem, return a nack.
				piConnected									= false;
				return new Ctrl_Abstract().new Nack();
			}
		}
		
		try
		{
			ObjectOutputStream 		piOutput 				= new ObjectOutputStream(piSocket.getOutputStream());
			System.out.println("Step 3");
		
			Ctrl_Abstract.Ping 		piPingSend 				= new Ctrl_Abstract().new Ping();
			System.out.println("Step 4");

			piOutput.writeObject(piPingSend);
			piOutput.flush();
			
			System.out.println("Step 5.1");



			ObjectInputStream 		piInput 				= new ObjectInputStream(piSocket.getInputStream());
			System.out.println("Step 3.1");

	        Object 					data_in					= piInput.readObject();
	        
			if (data_in instanceof Ctrl_Abstract.Ack)
			{
				piConnected									= true;
				return new Ctrl_Abstract().new Ack();
			}
			else
			{
				piConnected									= false;
				return new Ctrl_Abstract().new Nack();
			}
		}
		catch(Exception e)
		{
			System.out.print("Whoops! It didn't work!\n" + e);
        	e.printStackTrace();
			piConnected										= false;
			return new Ctrl_Abstract().new Nack();
		}
	}
	public Ctrl_Abstract piTransaction(Ctrl_Abstract messageSend)
	{
		Ctrl_Abstract					messageReceive		= null;
		ObjectOutputStream 				piOutputStream 		= null;
		ObjectInputStream 				piInputStream		= null;
		
		if (piConnected)
		{
			try
			{
				piOutputStream 									= new ObjectOutputStream(piSocket.getOutputStream());
				piOutputStream.writeObject(messageSend);
				piOutputStream.flush();
				piInputStream									= new ObjectInputStream(piSocket.getInputStream());
		        messageReceive									= (Ctrl_Abstract) piInputStream.readObject();
		        
				return messageReceive;
			}
			catch (SocketTimeoutException eTimeOut)
			{
				System.out.print("Whoops! It didn't work!\n" + eTimeOut);
				eTimeOut.printStackTrace();
				return new Ctrl_Abstract().new Nack();
			}
			catch(Exception e)
			{
				System.out.print("Whoops! It didn't work!\n" + e);
	        	e.printStackTrace();
				return new Ctrl_Abstract().new Nack();
			}
		}
		else
		{
			return new Ctrl_Abstract().new NoConnection();
		}
	}
}