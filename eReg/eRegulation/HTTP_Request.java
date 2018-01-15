package eRegulation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.*;
import HVAC_Common.*;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class HTTP_Request <SendType>
{
	public URL						serverURL;
	public URLConnection			servletConnection;
	
	public HTTP_Request(String servlet)
	{
		serverURL																			= null;
		servletConnection																	= null;
		
		try
		{
			serverURL = new URL("http://192.168.5.10:8888/hvac/" + servlet);
		}
		catch (MalformedURLException e)
		{
           	LogIt.error("HTTP_Request", "constructor", "Exception (new URL) : " + e);
		}

		try
		{
			servletConnection = serverURL.openConnection();
		}
		catch (IOException e)
		{
           	LogIt.error("HTTP_Request", "constructor", "Exception (openConnection) : " + e);
		}
		
		servletConnection.setDoOutput(true);
		servletConnection.setUseCaches(false);
		servletConnection.setConnectTimeout(2000);			// Changed from 1 sec to 2 secs 14/01/2018
		servletConnection.setReadTimeout(2000);
		servletConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");
	}
	//
	// New version based on Msg__Abstract (old version at end of this file (commented out)
	//
	public Msg__Abstract sendData(Msg__Abstract messageSend)
	{
		Msg__Abstract											messageReceive				= null;

		try
		{
			ObjectOutputStream 									outputToServlet;
			outputToServlet 																= new ObjectOutputStream(servletConnection.getOutputStream());
			outputToServlet.writeObject(messageSend);
			outputToServlet.flush();
			outputToServlet.close();
		}
		catch (SocketTimeoutException eTimeOut)			{	LogIt.info("HTTP_Request", "sendData", "writeObject/TimeOut : " 			+ eTimeOut		);	}
		catch (ConnectException eConnect) 				{   LogIt.info("HTTP_Request", "sendData", "writeObject/Connect Exception : " 	+ eConnect		);	}
		catch (Exception eSend) 						{   LogIt.info("HTTP_Request", "sendData", "writeObject/Exception : " 			+ eSend			);	}

		try
		{
			ObjectInputStream 									response 					= new ObjectInputStream(servletConnection.getInputStream());
			messageReceive 																	= (Msg__Abstract) response.readObject();
		}
    	catch (ClassNotFoundException eClassNotFound)	{	LogIt.info("HTTP_Request", "sendData", "readObject/ClassNotFound : " 		+ eClassNotFound);	}
		catch (SocketTimeoutException eTimeOut)			{	LogIt.info("HTTP_Request", "sendData", "readObject/TimeOut  : " 			+ eTimeOut		);	}
		catch (ConnectException eConnect) 				{	LogIt.info("HTTP_Request", "sendData", "writeObject/Connect Exception : " 	+ eConnect		);	}
		catch (ClassCastException eClassCast) 			{	LogIt.info("HTTP_Request", "sendData", "readObject/ClassCast : " 			+ eClassCast	);	}
		catch (Exception eReceive) 						{	LogIt.info("HTTP_Request", "sendData", "readObject/Exception : " 			+ eReceive		);	}
		
		return messageReceive;			
	}
//	public Msg__Abstract sendData(Msg__Abstract messageSend)
//	{
//		Msg__Abstract											messageReceive				= null;
//
//		try
//		{
//			ObjectOutputStream 									outputToServlet;
//			outputToServlet 																= new ObjectOutputStream(servletConnection.getOutputStream());
//			outputToServlet.writeObject(messageSend);
//			outputToServlet.flush();
//			outputToServlet.close();
//		}
//		catch (SocketTimeoutException eTimeOut)
//		{
//    		LogIt.info("HTTP_Request", "sendData", "Rpt_Abstract messageSend TimeOut on write : " + eTimeOut);
//		}
//		catch (Exception eSend) 
//		{
//    		LogIt.info("HTTP_Request", "sendData", "Rpt_Abstract messageSend : " + eSend);
//		}
//
//		try
//		{
//			ObjectInputStream 									response 					= new ObjectInputStream(servletConnection.getInputStream());
//			messageReceive 																	= (Msg__Abstract) response.readObject();
//		}
//    	catch (ClassNotFoundException eClassNotFound) 
//    	{
//    		LogIt.info("HTTP_Request", "sendData", "Rpt_Abstract messageSend ClassNotFound : " + eClassNotFound);
//		}
//		catch (SocketTimeoutException eTimeOut)
//		{
//    		LogIt.info("HTTP_Request", "sendData", "Rpt_Abstract messageSend TimeOut on read  : " + eTimeOut);
//		}
//		catch (ClassCastException eClassCast) 
//		{
//    		LogIt.info("HTTP_Request", "sendData", "Rpt_Abstract messageSend ClassCast : " + eClassCast);
//    		LogIt.info("HTTP_Request", "sendData", "Rpt_Abstract messageSend ClassCast : " + eClassCast.getMessage());
//		}
//		catch (Exception eReceive) 
//		{
//    		LogIt.info("HTTP_Request", "sendData", "Rpt_Abstract messageSend Other : " + eReceive);
//    		LogIt.info("HTTP_Request", "sendData", "Rpt_Abstract messageSend Other : " + eReceive.getMessage());
//		}
//		return messageReceive;			
//	}
//	public Msg__Abstract sendData(Msg__Abstract messageSend)
//	{
//// Changed 08/12/2017 after casting error  STOPPRESS changes undone
//		Msg__Abstract											messageReceive				= null;
////		Rpt_Abstract											messageReceive				= null;
//
//		try
//		{
//			ObjectOutputStream 									outputToServlet;
//			outputToServlet 																= new ObjectOutputStream(servletConnection.getOutputStream());
//			outputToServlet.writeObject(messageSend);
//			outputToServlet.flush();
//			outputToServlet.close();
//		}
//		catch (SocketTimeoutException eTimeOut)
//		{
//    		LogIt.info("HTTP_Request", "sendData", "Ctrl__Abstract messageSend TimeOut on write : " + eTimeOut);
//		}
//		catch (Exception eSend) 
//		{
//    		LogIt.info("HTTP_Request", "sendData", "Ctrl__Abstract messageSend : " + eSend);
//		}
//
//		try
//		{
//			ObjectInputStream 									response 					= new ObjectInputStream(servletConnection.getInputStream());
//			messageReceive 																	= (Msg__Abstract) response.readObject();
////			messageReceive 																	= (Rpt_Abstract)   response.readObject();
//		}
//    	catch (ClassNotFoundException eClassNotFound) 
//    	{
//    		LogIt.info("HTTP_Request", "sendData", "Ctrl__Abstract messageSend ClassNotFound : " + eClassNotFound);
//		}
//		catch (SocketTimeoutException eTimeOut)
//		{
//    		LogIt.info("HTTP_Request", "sendData", "Ctrl__Abstract messageSend TimeOut on read  : " + eTimeOut);
//		}
//		catch (ClassCastException eClassCast) 
//		{
//    		LogIt.info("HTTP_Request", "sendData", "Ctrl__Abstract messageSend ClassCast : " + eClassCast);
//    		LogIt.info("HTTP_Request", "sendData", "Ctrl__Abstract messageSend ClassCast : " + eClassCast.getMessage());
//		}
//		catch (Exception eReceive) 
//		{
//    		LogIt.info("HTTP_Request", "sendData", "Ctrl__Abstract messageSend Other : " + eReceive);
//    		LogIt.info("HTTP_Request", "sendData", "Ctrl__Abstract messageSend Other : " + eReceive.getMessage());
//		}
//		return messageReceive;			
//	}
}
