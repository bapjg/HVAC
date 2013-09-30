package com.bapjg.hvac_client;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;

import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class HTTP_Request extends AsyncTask <Mgmt_Msg_Abstract, Void, Mgmt_Msg_Abstract> 
{
	public URL						serverURL;
	public URLConnection			servletConnection;

	public HTTP_Request()
	{
	}
	
	@Override
	protected Mgmt_Msg_Abstract doInBackground(Mgmt_Msg_Abstract... messageOut) 
	{
		return sendData(messageOut[0]);
	}	
	@Override
	protected void onProgressUpdate(Void... progress) 
	{
//         setProgressPercent(progress[0]);
    }
	@Override
    protected void onPostExecute(Mgmt_Msg_Abstract result) 
	{             
		System.out.println("step 4");
		if (result.getClass() == Mgmt_Msg_Calendar.Data.class)
		{
			Mgmt_Msg_Calendar.Data 		msg_received = (Mgmt_Msg_Calendar.Data) result;
			System.out.println("step dateTime : " + msg_received.dateTime);
		}
		else if (result.getClass() == Mgmt_Msg_Temperatures.class)
		{
			Mgmt_Msg_Temperatures msg_received = (Mgmt_Msg_Temperatures) result;

//			TextView right1 						= (TextView) findViewByID(R.id.right1);
//			right1.setText(msg_received.dateTime);
			
			
			System.out.println("step dateTime     : " + msg_received.dateTime);
			System.out.println("step tempBoiler   : " + msg_received.tempBoiler);
			System.out.println("step tempHotWater : " + msg_received.tempHotWater);
			
			
		}
    }

	public Mgmt_Msg_Abstract sendData(Mgmt_Msg_Abstract messageSend)
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
		
		Mgmt_Msg_Abstract				messageReceive		= null;

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
}












