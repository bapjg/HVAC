package eRegulation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class LogIt_HTTP <SendType, RespType>
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
	public RespType sendData(SendType messageSend)
	{
		RespType		messageReceive		= null;
		return messageReceive;
	}
}
