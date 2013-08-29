package eRegulation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

public class LogIt_HTTP <SendType>
{
	public URL						serverURL;
	public URLConnection			servletConnection;
	
	
	public LogIt_HTTP(String servlet)
	{
		URL 			serverURL			= null;
		URLConnection 	servletConnection	= null;
		
		try
		{
			serverURL = new URL("http://192.168.5.20:8080/hvac/" + servlet);
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
	public Message_Abstract sendData(SendType messageSend)
	{
		Message_Temperatures			xx						= (Message_Temperatures) messageSend;
		
		System.out.println(xx);
		
		Message_Abstract				messageReceive			= null;
		
		try 
		{
			try
			{
				ObjectOutputStream 			outputToServlet;
				outputToServlet 								= new ObjectOutputStream(servletConnection.getOutputStream());
outputToServlet.writeObject(xx);
//				outputToServlet.writeObject(messageSend);
				outputToServlet.flush();
				outputToServlet.close();
			}
			catch (SocketTimeoutException eTimeOut)
			{
	    		System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP TimeOut on write : " + eTimeOut);
			}
			catch (Exception eSend) 
			{
	    		System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP Send :" + eSend);
			}
			try
			{
				ObjectInputStream 		response 				= new ObjectInputStream(servletConnection.getInputStream());
				messageReceive 									= (Message_Abstract) response.readObject();
			}
	    	catch (ClassNotFoundException eClassNotFound) 
	    	{
	    		System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP ClassNotFound : " + eClassNotFound);
			}
			catch (SocketTimeoutException eTimeOut)
			{
	    		System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP TimeOut on read  : " + eTimeOut);
			}
		} 
		catch (Exception eOther) 
		{
    		System.out.println(LogIt.dateTimeStamp() + " LogIt_HTTP Other :" + eOther);
		}
			
		return messageReceive;			
	}
}
