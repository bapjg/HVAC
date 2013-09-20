package com.bapjg.hvac_client;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Main extends Activity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		HTTP_Req_Temp							httpRequest			= new HTTP_Req_Temp();
		httpRequest.execute(new Mgmt_Msg_Temperatures_Req());
		
		Global									global				= new Global();
		global.henry												= "Hen3ry";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void temperaturesClick(View v)
	{
		HTTP_Req_Temp							httpRequest			= new HTTP_Req_Temp();
		httpRequest.execute(new Mgmt_Msg_Temperatures_Req());
	}
	public void configurationClick(View v)
	{
	//	HTTP_Req_Temp							httpRequest			= new HTTP_Req_Temp();
	//	httpRequest.execute(new Mgmt_Msg_Temperatures_Req());
		Intent i = new Intent(getBaseContext(), Activity_Configuration.class);                      
	//	i.putExtra("PersonID", personID);
		startActivity(i);
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
				Mgmt_Msg_Temperatures msg_received = (Mgmt_Msg_Temperatures) result;

				TextView field	 						= (TextView) findViewById(R.id.Date);
				field.setText(displayDate(msg_received.dateTime));

				field	 								= (TextView) findViewById(R.id.Time);
				field.setText(displayTime(msg_received.dateTime));
				
				field	 								= (TextView) findViewById(R.id.Boiler);
				field.setText(displayTemperature(msg_received.tempBoiler));

				field	 								= (TextView) findViewById(R.id.HotWater);
				field.setText(displayTemperature(msg_received.tempHotWater));

				field	 								= (TextView) findViewById(R.id.Outside);
				field.setText(displayTemperature(msg_received.tempOutside));

				field	 								= (TextView) findViewById(R.id.BoilerIn);
				field.setText(displayTemperature(msg_received.tempBoilerIn));

				field	 								= (TextView) findViewById(R.id.FloorOut);
				field.setText(displayTemperature(msg_received.tempFloorOut));

				field	 								= (TextView) findViewById(R.id.FloorHot);
				field.setText(displayTemperature(msg_received.tempFloorHot));

				field	 								= (TextView) findViewById(R.id.FloorCold);
				field.setText(displayTemperature(msg_received.tempFloorCold));

				field	 								= (TextView) findViewById(R.id.RadiatorOut);
				field.setText(displayTemperature(msg_received.tempRadiatorOut));

				field	 								= (TextView) findViewById(R.id.RadiatorIn);
				field.setText(displayTemperature(msg_received.tempRadiatorIn));

				field	 								= (TextView) findViewById(R.id.LivingRoom);
				field.setText(displayTemperature(msg_received.tempLivingRoom));
				
			}
			else
			{
				Context 				context 				= getApplicationContext();
				CharSequence 			text 					= "A Nack has been returned";
				int 					duration 				= Toast.LENGTH_SHORT;

				Toast 					toast 					= Toast.makeText(context, text, duration);
				toast.show();
				
				Toast.makeText(getApplicationContext(), "What a shame", Toast.LENGTH_LONG).show();
				
				
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
