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
	public ObjectOutputStream 		piOutputStream 			= null;
	public ObjectInputStream 		piInputStream			= null;

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
		System.out.println("TCP must connect");
		
		try
		{
			piAddressV4										= InetAddress.getByName("192.168.5.51");
			piSocket 										= new Socket(piAddressV4, 8889);
			piSocket.setKeepAlive(true);
			piOutputStream 									= new ObjectOutputStream(piSocket.getOutputStream());
			System.out.println("TCP got socket local");
		}
		catch(Exception e)
		{
			// It Failed so now try over the internet
			try
			{
				piAddressV4 								= InetAddress.getByName("home.bapjg.com");
				piSocket 									= new Socket(piAddressV4, 8889);
				piSocket.setKeepAlive(true);
				piOutputStream 								= new ObjectOutputStream(piSocket.getOutputStream());
				System.out.println("TCP got socket remote");
			}
			catch(Exception e2)
			{
				// Major problem, return a nack.
				piConnected									= false;
				return false;
			}
		}
		piConnected											= true;
		return true;
	}
	
	
	public Ctrl_Abstract ping()
	{
		System.out.println("ping");
		Ctrl_Abstract.Ping 		piPingSend 				= new Ctrl_Abstract().new Ping();
		return piTransaction((Ctrl_Abstract) piPingSend);
	}
	public Ctrl_Abstract piTransaction(Ctrl_Abstract messageSend)
	{
		Ctrl_Abstract					messageReceive		= null;
		
		if (piConnected)
		{
			try
			{
				piOutputStream.writeObject(messageSend);
				System.out.println("piTransaction : writeObj");
				
				piOutputStream.flush();
				System.out.println("piTransaction : flush");
				
				piInputStream									= new ObjectInputStream(piSocket.getInputStream());
				System.out.println("TCP got input local");
				
		        messageReceive									= (Ctrl_Abstract) piInputStream.readObject();
				System.out.println("piTransaction : receive");
				
				piInputStream.close();
				piInputStream									= null;
		        
				return messageReceive;
			}
			catch (SocketTimeoutException eTimeOut)
			{
				System.out.println("Whoops! It didn't work eTimeOut");
				eTimeOut.printStackTrace();
				return new Ctrl_Abstract().new Nack();
			}
			catch(Exception e)
			{
				System.out.println("Whoops! It didn't work e");
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