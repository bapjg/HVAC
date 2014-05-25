package eRegulation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import HVAC_Common.*;

public class HTTP_Request <SendType>
{
	public URL						serverURL;
	public URLConnection			servletConnection;
	
	public HTTP_Request(String servlet)
	{
		serverURL							= null;
		servletConnection					= null;
		
		try
		{
			serverURL = new URL("http://192.168.5.10:8888/hvac/" + servlet);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}

		try
		{
			servletConnection = serverURL.openConnection();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		servletConnection.setDoOutput(true);
		servletConnection.setUseCaches(false);
		servletConnection.setConnectTimeout(1000);
		servletConnection.setReadTimeout(1000);
		servletConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");
	}
	public Rpt_Abstract sendData(Rpt_Abstract messageSend)
	{
		Rpt_Abstract				messageReceive		= null;

		try
		{
			ObjectOutputStream 			outputToServlet;
			outputToServlet 								= new ObjectOutputStream(servletConnection.getOutputStream());
			outputToServlet.writeObject(messageSend);
			outputToServlet.flush();
			outputToServlet.close();
		}
		catch (SocketTimeoutException eTimeOut)
		{
    		// System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP TimeOut on write : " + eTimeOut);
		}
		catch (Exception eSend) 
		{
    		// System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP Send : " + eSend);
		}

		try
		{
			ObjectInputStream 		response 				= new ObjectInputStream(servletConnection.getInputStream());
			messageReceive 									= (Rpt_Abstract) response.readObject();
		}
    	catch (ClassNotFoundException eClassNotFound) 
    	{
    		// System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP ClassNotFound : " + eClassNotFound);
		}
		catch (SocketTimeoutException eTimeOut)
		{
    		// System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP TimeOut on read  : " + eTimeOut);
		}
		catch (Exception eReceive) 
		{
    		// System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP Other : " + eReceive);
    		// System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP Other : " + eReceive.getMessage());
		}
			
		return messageReceive;			
	}
	public Ctrl__Abstract sendData(Ctrl__Abstract messageSend)
	{
		Ctrl__Abstract					messageReceive		= null;

		try
		{
			ObjectOutputStream 			outputToServlet;
			outputToServlet 								= new ObjectOutputStream(servletConnection.getOutputStream());
			outputToServlet.writeObject(messageSend);
			outputToServlet.flush();
			outputToServlet.close();
		}
		catch (SocketTimeoutException eTimeOut)
		{
    		// System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP TimeOut on write : " + eTimeOut);
		}
		catch (Exception eSend) 
		{
    		// System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP Send : " + eSend);
		}

		try
		{
			ObjectInputStream 		response 				= new ObjectInputStream(servletConnection.getInputStream());
			messageReceive 									= (Ctrl__Abstract) response.readObject();
		}
    	catch (ClassNotFoundException eClassNotFound) 
    	{
    		// System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP ClassNotFound : " + eClassNotFound);
		}
		catch (SocketTimeoutException eTimeOut)
		{
    		// System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP TimeOut on read  : " + eTimeOut);
		}
		catch (Exception eReceive) 
		{
    		// System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP Other : " + eReceive);
    		// System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP Other : " + eReceive.getMessage());
		}
			
		return messageReceive;			
	}
}
