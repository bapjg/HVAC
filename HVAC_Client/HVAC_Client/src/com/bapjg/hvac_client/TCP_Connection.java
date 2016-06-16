package com.bapjg.hvac_client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import android.widget.Toast;
import HVAC_Common.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class TCP_Connection
{
	public Socket												piSocket;
	public ObjectOutputStream 									piOutputStream 				= null;
	public ObjectInputStream 									piInputStream				= null;
	public Boolean												piConnected					= false;
	
	public TCP_Connection()
	{
	}

	public Boolean connect()
	{
		InetSocketAddress										piSocketAddress;
		
		if (Global.isLocalIpAddress())							piSocketAddress 			= new InetSocketAddress("192.168.5.51", 8889);
		else													piSocketAddress 			= new InetSocketAddress("home.bapjg.com", 8889);

		try
		{
			piSocket 																		= new Socket();
			piSocket.connect(piSocketAddress, 20000);
			piSocket.setSoTimeout(20000);
			piSocket.setKeepAlive(true);
			piConnected																		= true;
			return true;									// Exit procedure as we are connected - NormalOperating
		}
		catch (Exception e1)
		{
			piConnected																		= false;
			return false;
		}
	}
	public void disconnect()
	{
		try { piOutputStream.close(); 	}				catch(Exception e) {}
		try { piInputStream.close(); 	}				catch(Exception e) {}
		try { piSocket.close(); 		}				catch(Exception e) {}
	}
	public Ctrl__Abstract piTransaction(Ctrl__Abstract messageSend)
	{
		if(connect())
		{
			try
			{
				piOutputStream																= new ObjectOutputStream(piSocket.getOutputStream());
				piOutputStream.writeObject(messageSend);
				piOutputStream.flush();
				
				piInputStream																= new ObjectInputStream(piSocket.getInputStream());
				
				return (Ctrl__Abstract) piInputStream.readObject();
			}
			catch (SocketTimeoutException eTimeOut)		{	return new Ctrl__Abstract().new TimeOut();		}
			catch (Exception e)							{	return new Ctrl__Abstract().new NoData();			}
		}
		else											{	return new Ctrl__Abstract().new NoConnection();	}
	}
}