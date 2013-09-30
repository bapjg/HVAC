package com.bapjg.hvac_client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_Temperatures extends Fragment 
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	// Inflate the layout for this fragment
		HTTP_Req_Temp							httpRequest			= new HTTP_Req_Temp();
		httpRequest.execute(new Mgmt_Msg_Temperatures_Req());

        return inflater.inflate(R.layout.fragment_temperatures, container, false);
    }
	private class HTTP_Req_Temp extends AsyncTask <Mgmt_Msg_Abstract, Void, Mgmt_Msg_Abstract> 
	{
		public URL						serverURL;
		public URLConnection			servletConnection;

		public HTTP_Req_Temp()
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
	    }
		@Override
	    protected void onPostExecute(Mgmt_Msg_Abstract result) 
		{             
			System.out.println("step 4");
			
			if (result.getClass() == Mgmt_Msg_Temperatures.class)
			{
				Mgmt_Msg_Temperatures msg_received 	= (Mgmt_Msg_Temperatures) result;
				Activity a							= getActivity();

				((TextView) a.findViewById(R.id.Date)).setText(displayDate(msg_received.dateTime));
				((TextView) a.findViewById(R.id.Time)).setText(displayTime(msg_received.dateTime));

				((TextView) a.findViewById(R.id.Boiler)).setText(displayTemperature(msg_received.tempBoiler));
				((TextView) a.findViewById(R.id.HotWater)).setText(displayTemperature(msg_received.tempHotWater));
				((TextView) a.findViewById(R.id.Outside)).setText(displayTemperature(msg_received.tempOutside));
				((TextView) a.findViewById(R.id.BoilerIn)).setText(displayTemperature(msg_received.tempBoilerIn));
				((TextView) a.findViewById(R.id.FloorOut)).setText(displayTemperature(msg_received.tempFloorOut));
				((TextView) a.findViewById(R.id.FloorHot)).setText(displayTemperature(msg_received.tempFloorHot));
				((TextView) a.findViewById(R.id.FloorCold)).setText(displayTemperature(msg_received.tempFloorCold));
				((TextView) a.findViewById(R.id.RadiatorOut)).setText(displayTemperature(msg_received.tempRadiatorOut));
				((TextView) a.findViewById(R.id.RadiatorIn)).setText(displayTemperature(msg_received.tempRadiatorIn));
				((TextView) a.findViewById(R.id.LivingRoom)).setText(displayTemperature(msg_received.tempLivingRoom));
			}
			else
			{
				Context 				context 				= getActivity();
				CharSequence 			text 					= "A Nack has been returned";
				int 					duration 				= Toast.LENGTH_SHORT;

				Toast 					toast 					= Toast.makeText(context, text, duration);
				toast.show();
				
				Toast.makeText(getActivity(), "What a shame", Toast.LENGTH_LONG).show();
				
				
//				AlertDialog alertDialog 						= new AlertDialog.Builder(this).create();
// 
//				alertDialog.setTitle("Alert Dialog");
//				alertDialog.setMessage("We have a problem");
//				alertDialog.setButton("OK", new DialogInterface.OnClickListener() 
//				{
//                public void onClick(DialogInterface dialog, int which) 
//                	{
//
//                	Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
//                	}
//				});
// 
//				alertDialog.show();
			}
	    }
		public Mgmt_Msg_Abstract sendData(Mgmt_Msg_Abstract messageSend)
		{
			serverURL											= null;
			servletConnection									= null;
			Mgmt_Msg_Abstract				messageReceive		= null;
			
			try
			{
				serverURL = new URL("http://192.168.5.20:8080/hvac/Management");
				servletConnection = serverURL.openConnection();
				servletConnection.setDoOutput(true);
				servletConnection.setUseCaches(false);
				servletConnection.setConnectTimeout(1000);
				servletConnection.setReadTimeout(1000);
				servletConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");
				ObjectOutputStream 			outputToServlet;
				outputToServlet 								= new ObjectOutputStream(servletConnection.getOutputStream());
				outputToServlet.writeObject(messageSend);
				outputToServlet.flush();
				outputToServlet.close();
	    		System.out.println(" HTTP_Request Sent ");
				ObjectInputStream 		response 				= new ObjectInputStream(servletConnection.getInputStream());
				messageReceive 									= (Mgmt_Msg_Abstract) response.readObject();
			}
			catch (MalformedURLException eMUE) // thrown by new URL
			{
				eMUE.printStackTrace();
				return new Mgmt_Msg_Nack();	
			}
			catch (SocketTimeoutException eTimeOut) // thrown on connection or read timeout
			{
	    		// Consider retries
				System.out.println(" HTTP_Request TimeOut on read or write : " + eTimeOut);
				return new Mgmt_Msg_Nack();	
			}
	    	catch (ClassNotFoundException eClassNotFound) // thrown if read returns unexpected class
	    	{
	    		System.out.println(" HTTP_Request ClassNotFound : " + eClassNotFound);
	    		return new Mgmt_Msg_Nack();
			}
			catch (IOException eIO) //thrown by various
			{
				eIO.printStackTrace();
				return new Mgmt_Msg_Nack();	
			}
			catch (Exception e) 
			{
	    		System.out.println(" HTTP_Request Send or read : " + e);
				return new Mgmt_Msg_Nack();	
			}
			return messageReceive;			
		}
	}
	private String displayTemperature(Integer temperature)
	{
		int degrees = temperature/10;
		int decimal = temperature - degrees*10;
		return degrees + "." + decimal;
	}
	private String displayDate(String dateTime)
	{
		return dateTime.substring(8,10) + "/" + dateTime.substring(5,7);
	}
	private String displayTime(String dateTime)
	{
		return dateTime.substring(11,13) + ":" + dateTime.substring(14,16) + ":" + dateTime.substring(17,19);
	}    
}

