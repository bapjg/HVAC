package com.bapjg.hvac_client;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;

public class HTTP_Request extends AsyncTask <Message_Abstract, Void, Message_Abstract> 
{
	public URL						serverURL;
	public URLConnection			servletConnection;

	public HTTP_Request()
	{
	}
	
	@Override
	protected Message_Abstract doInBackground(Message_Abstract... messageOut) 
	{
		return sendData(messageOut[0]);
	}	
	@Override
	protected void onProgressUpdate(Void... progress) 
	{
//         setProgressPercent(progress[0]);
    }
	@Override
    protected void onPostExecute(Message_Abstract result) 
	{             
		System.out.println("step 4");
		System.out.println("This step is " + result.getClass().getSimpleName());
    }

	public Message_Abstract sendData(Message_Abstract messageSend)
	{
		serverURL							= null;
		servletConnection					= null;
		
		try
		{
			serverURL = new URL("http://192.168.5.20:8080/hvac/Management");
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
		servletConnection.setConnectTimeout(1000);
		servletConnection.setReadTimeout(1000);
		servletConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");
		
		Message_Abstract				messageReceive		= null;

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
			messageReceive 									= (Message_Abstract) response.readObject();
		}
    	catch (ClassNotFoundException eClassNotFound) 
    	{
    		System.out.println(" HTTP_Request ClassNotFound : " + eClassNotFound);
		}
		catch (SocketTimeoutException eTimeOut)
		{
    		System.out.println(" HTTP_Request TimeOut on read  : " + eTimeOut);
		}
		catch (Exception e) 
		{
    		System.out.println(" HTTP_Request Other 1 : " + e);
    		System.out.println(" HTTP_Request Other 2 : " + e.getMessage());
		}
			
		return messageReceive;			
	}













