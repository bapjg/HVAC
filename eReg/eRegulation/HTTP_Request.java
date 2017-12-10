package eRegulation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import HVAC_Common.*;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
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
           	LogIt.error("HTTP_Request", "constructor", "Exception : " + e);
//			e.printStackTrace();
		}

		try
		{
			servletConnection = serverURL.openConnection();
		}
		catch (IOException e)
		{
           	LogIt.error("HTTP_Request", "constructor", "Exception : " + e);
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
		Rpt_Abstract											messageReceive				= null;

		try
		{
			ObjectOutputStream 									outputToServlet;
			outputToServlet 																= new ObjectOutputStream(servletConnection.getOutputStream());
			outputToServlet.writeObject(messageSend);
			outputToServlet.flush();
			outputToServlet.close();
		}
		catch (SocketTimeoutException eTimeOut)
		{
    		LogIt.info("HTTP_Request", "sendData", "Rpt_Abstract messageSend TimeOut on write : " + eTimeOut);
		}
		catch (Exception eSend) 
		{
    		LogIt.info("HTTP_Request", "sendData", "Rpt_Abstract messageSend : " + eSend);
		}

		try
		{
			ObjectInputStream 									response 					= new ObjectInputStream(servletConnection.getInputStream());
			messageReceive 																	= (Rpt_Abstract) response.readObject();
		}
    	catch (ClassNotFoundException eClassNotFound) 
    	{
    		LogIt.info("HTTP_Request", "sendData", "Rpt_Abstract messageSend ClassNotFound : " + eClassNotFound);
		}
		catch (SocketTimeoutException eTimeOut)
		{
    		LogIt.info("HTTP_Request", "sendData", "Rpt_Abstract messageSend TimeOut on read  : " + eTimeOut);
		}
		catch (ClassCastException eClassCast) 
		{
    		LogIt.info("HTTP_Request", "sendData", "Rpt_Abstract messageSend ClassCast : " + eClassCast);
    		LogIt.info("HTTP_Request", "sendData", "Rpt_Abstract messageSend ClassCast : " + eClassCast.getMessage());
		}
		catch (Exception eReceive) 
		{
    		LogIt.info("HTTP_Request", "sendData", "Rpt_Abstract messageSend Other : " + eReceive);
    		LogIt.info("HTTP_Request", "sendData", "Rpt_Abstract messageSend Other : " + eReceive.getMessage());
		}
		return messageReceive;			
	}
	public Ctrl__Abstract sendData(Ctrl__Abstract messageSend)
	{
// Changed 08/12/2017 after casting error  STOPPRESS changes undone
		Ctrl__Abstract											messageReceive				= null;
//		Rpt_Abstract											messageReceive				= null;

		try
		{
			ObjectOutputStream 									outputToServlet;
			outputToServlet 																= new ObjectOutputStream(servletConnection.getOutputStream());
			outputToServlet.writeObject(messageSend);
			outputToServlet.flush();
			outputToServlet.close();
		}
		catch (SocketTimeoutException eTimeOut)
		{
    		LogIt.info("HTTP_Request", "sendData", "Ctrl__Abstract messageSend TimeOut on write : " + eTimeOut);
		}
		catch (Exception eSend) 
		{
    		LogIt.info("HTTP_Request", "sendData", "Ctrl__Abstract messageSend : " + eSend);
		}

		try
		{
			ObjectInputStream 									response 					= new ObjectInputStream(servletConnection.getInputStream());
			messageReceive 																	= (Ctrl__Abstract) response.readObject();
//			messageReceive 																	= (Rpt_Abstract)   response.readObject();
		}
    	catch (ClassNotFoundException eClassNotFound) 
    	{
    		LogIt.info("HTTP_Request", "sendData", "Ctrl__Abstract messageSend ClassNotFound : " + eClassNotFound);
		}
		catch (SocketTimeoutException eTimeOut)
		{
    		LogIt.info("HTTP_Request", "sendData", "Ctrl__Abstract messageSend TimeOut on read  : " + eTimeOut);
		}
		catch (ClassCastException eClassCast) 
		{
    		LogIt.info("HTTP_Request", "sendData", "Ctrl__Abstract messageSend ClassCast : " + eClassCast);
    		LogIt.info("HTTP_Request", "sendData", "Ctrl__Abstract messageSend ClassCast : " + eClassCast.getMessage());
		}
		catch (Exception eReceive) 
		{
    		LogIt.info("HTTP_Request", "sendData", "Ctrl__Abstract messageSend Other : " + eReceive);
    		LogIt.info("HTTP_Request", "sendData", "Ctrl__Abstract messageSend Other : " + eReceive.getMessage());
		}
		return messageReceive;			
	}
}
