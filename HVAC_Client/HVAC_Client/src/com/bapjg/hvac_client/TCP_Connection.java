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

import HVAC_Messages.*;

public class TCP_Connection
{
	public Socket					piSocket;
	public ObjectOutputStream 		piOutputStream 			= null;
	public ObjectInputStream 		piInputStream			= null;
	public Boolean					piConnected				= false;
	
	public TCP_Connection()
	{
	}

	public Boolean connect()
	{
		if (Global.piSocketAddress != null)
		{
			try													// Try last known address
			{
				piSocket 										= new Socket();
				piSocket.connect(Global.piSocketAddress,3000);
				piSocket.setSoTimeout(3000);
				return true;
			}
			catch(Exception e1)
			{
			}
		}

		try														// Try local
		{
			Global.piSocketAddress								= new InetSocketAddress("192.168.5.51", 8889);
			piSocket 											= new Socket();
			piSocket.connect(Global.piSocketAddress, 1000);
			piSocket.setSoTimeout(3000);
			
			Global.toaster("TCP_Connection : Connected to Local", true);
		}
		catch(Exception e2)
		{
			try													// It Failed so now try over the internet
			{
				Global.piSocketAddress 							= new InetSocketAddress("home.bapjg.com", 8889);
				piSocket 										= new Socket();
				piSocket.connect(Global.piSocketAddress, 3000);
				piSocket.setSoTimeout(3000);

				Global.toaster("TCP_Connection : Connected to Remote", true);
			}
			catch(Exception e3)
			{
				Global.toaster("TCP_Connection : Failed completely", true);
				return false;
			}
		}
		piConnected												= true;
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