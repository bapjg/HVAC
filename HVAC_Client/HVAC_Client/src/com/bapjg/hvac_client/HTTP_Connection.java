package com.bapjg.hvac_client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

import android.util.Log;
import android.widget.Toast;
import HVAC_Common.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class HTTP_Connection
{
	public 	URL													server;
	public 	URLConnection										serverConnection;
	
	public HTTP_Connection()
	{
	}

	public Boolean 												connect()
	{
		try
		{
			if (Global.isLocalIpAddress())												server = new URL("http://192.168.5.10:8888/hvac/Management");
			else																		server = new URL("http://home.bapjg.com:8888/hvac/Management");
				
			serverConnection 															= server.openConnection();
			serverConnection.setDoOutput(true);
			serverConnection.setUseCaches(false);
			serverConnection.setConnectTimeout(10000);
			serverConnection.setReadTimeout(10000);
			serverConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");
			Log.v("App", "Ariori Ok");
			return true;
		}
		catch(Exception e1)
		{
			server																		= null;
			return false;
		}
	}
	public Ctrl__Abstract 										serverTransaction(Ctrl__Abstract messageSend)
	{
		if (connect())
		{
			try
			{
				ObjectOutputStream 								serverSend;
				serverSend 																	= new ObjectOutputStream(serverConnection.getOutputStream());
				serverSend.writeObject(messageSend);
				serverSend.flush();
				serverSend.close();
				ObjectInputStream 								serverReceive				= new ObjectInputStream(serverConnection.getInputStream());
				Ctrl__Abstract									returnMessage  				= (Ctrl__Abstract) serverReceive.readObject();
				serverReceive.close();
				return returnMessage;
			}
			catch (SocketTimeoutException eTimeOut)			{	return 						new Ctrl__Abstract().new TimeOut();			}
			catch (Exception e)								{	return 						new Ctrl__Abstract().new NoData();			}
		}
		else												{ 	return 						new Ctrl__Abstract().new NoConnection();	}
	}
}