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

public class HTTP_Connection
{
	public static							URL				server;
	public 									URLConnection	serverConnection;
	
	public HTTP_Connection()
	{
	}

	public Boolean connect()
	{
		if (server == null)
		{
			try												// Try last known address - DNS always resolves home.bapjg.com, either to local or remote
			{
				server 										= new URL("http://home.bapjg.com:8888/hvac/Management");
				serverConnection 							= server.openConnection();
				serverConnection.setDoOutput(true);
				serverConnection.setUseCaches(false);
				serverConnection.setConnectTimeout(5000);
				serverConnection.setReadTimeout(5000);
				serverConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");
				return true;
			}
			catch(Exception e1)
			{
				return false;
			}
		}
		return true;
	}
	public Ctrl__Abstract serverTransaction(Ctrl__Abstract messageSend)
	{
		if (connect())
		{
			try
			{
				ObjectOutputStream 			serverSend;
				serverSend 										= new ObjectOutputStream(serverConnection.getOutputStream());
				serverSend.writeObject(messageSend);
				serverSend.flush();
				serverSend.close();
				ObjectInputStream 			serverReceive		= new ObjectInputStream(serverConnection.getInputStream());
				Ctrl__Abstract				returnMessage  		= (Ctrl__Abstract) serverReceive.readObject();
				serverReceive.close();
				return returnMessage;
			}
			catch (SocketTimeoutException eTimeOut)
			{
	    		// Failure occurred perhaps due to old connection, so set it to null
				// to force a reconnection
				server 											= null;
				return new Ctrl__Abstract().new Nack();
			}
			catch(Exception e)
			{
				server 											= null;
				return new Ctrl__Abstract().new Nack();
			}
		}
		else
		{
			return new Ctrl__Abstract().new NoConnection();
		}
	}
}