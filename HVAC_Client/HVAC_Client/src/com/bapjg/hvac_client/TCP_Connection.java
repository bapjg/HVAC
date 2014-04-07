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
	public ObjectOutputStream 		piOutputStream 			= null;
	public ObjectInputStream 		piInputStream			= null;
	public Boolean					piConnected				= false;
	
	public TCP_Connection(String fromWhom)
	{
		System.out.println("TCP_Connection Contrcutor : " + fromWhom);
	}

	public Boolean connect()
	{
//		if (piConnected)
//		{
//			return true;
//		}
		System.out.println("TCP_Connection : Before socket");
		
		try													// Try local
		{
			piAddressV4										= InetAddress.getByName("192.168.5.51");
			piSocket 										= new Socket(piAddressV4, 8889);
			System.out.println("TCP_Connection : got socket local");
		}
		catch(Exception e)
		{
			try												// It Failed so now try over the internet
			{
				piAddressV4 								= InetAddress.getByName("home.bapjg.com");
				piSocket 									= new Socket(piAddressV4, 8889);
				System.out.println("TCP_Connection : got socket remote");
			}
			catch(Exception e2)
			{
				return false;
			}
		}
		piConnected											= true;
		System.out.println("TCP_Connection : Ended");
		return true;
	}
	public void disconnect()
	{
		try { piOutputStream.close(); }
		catch(Exception e) {}
		try { piInputStream.close(); }
		catch(Exception e) {}
		try { piSocket.close(); }
		catch(Exception e) {}
	}
	public Ctrl_Abstract ping()
	{
		Ctrl_Abstract.Ping 		piPingSend 				= new Ctrl_Abstract().new Ping();
		return piTransaction((Ctrl_Abstract) piPingSend);
	}
	public Ctrl_Abstract piTransaction(Ctrl_Abstract messageSend)
	{
		if(connect())
		{
			try
			{
				piOutputStream									= new ObjectOutputStream(piSocket.getOutputStream());
				piOutputStream.writeObject(messageSend);
				piOutputStream.flush();
				
				piInputStream									= new ObjectInputStream(piSocket.getInputStream());
				
				return (Ctrl_Abstract) piInputStream.readObject();
			}
			catch (SocketTimeoutException eTimeOut)
			{
				eTimeOut.printStackTrace();
				return new Ctrl_Abstract().new Nack();
			}
			catch(Exception e)
			{
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