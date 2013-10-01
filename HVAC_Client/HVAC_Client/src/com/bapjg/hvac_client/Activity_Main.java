package com.bapjg.hvac_client;

import com.bapjg.hvac_client.Mgmt_Msg_Configuration.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;
import android.os.Bundle;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.MenuInflater;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Main extends Activity 
{
	public static	Global						global;

	private 		Adapter_Thermometers 		adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        global											= new Global();
        global.appContext 								= getApplicationContext();
        global.actContext								= (Context) this;
        global.activity									= (Activity) this;

        ActionBar 				actionbar 				= getActionBar();
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        ActionBar.Tab 			tabTemperatures 		= actionbar.newTab().setText("Temperatures");
        ActionBar.Tab 			tabConfiguration 		= actionbar.newTab().setText("Configuration");
        ActionBar.Tab 			tabCalendars		 	= actionbar.newTab().setText("Calendars");
        ActionBar.Tab 			tabActions				= actionbar.newTab().setText("Actions");
        
        Fragment_Temperatures	fragmentTemperatures 	= new Fragment_Temperatures();
        Fragment_Configuration	fragmentConfiguration 	= new Fragment_Configuration();
        Fragment_Calendars		fragmentCalendars 		= new Fragment_Calendars();
        Fragment_Actions		fragmentActions 		= new Fragment_Actions();

        tabTemperatures.setTabListener(new Listener_Tabs(fragmentTemperatures));
        tabConfiguration.setTabListener(new Listener_Tabs(fragmentConfiguration));
        tabCalendars.setTabListener(new Listener_Tabs(fragmentCalendars));
        tabActions.setTabListener(new Listener_Tabs(fragmentActions));
        
        actionbar.addTab(tabTemperatures);
        actionbar.addTab(tabConfiguration);
        actionbar.addTab(tabCalendars);
        actionbar.addTab(tabActions);

        // Simulate Configuration message from server
        
        
        Global.configuration											= new Mgmt_Msg_Configuration();
 		
 		Mgmt_Msg_Configuration						config				= Global.configuration;
 
    	Mgmt_Msg_Configuration.Thermometer 			thermometer 		= config.new Thermometer();
    	thermometer.name = "tempBoiler";
    	thermometer.friendlyName ="Chaudiere";
    	thermometer.thermoID = "028-0000xxxx";
    	config.thermometerList.add(thermometer);
 
    	thermometer 													= config.new Thermometer();
        thermometer.name = "tempHotWater";
        thermometer.friendlyName ="Eau Chaude Sanitaire";
        thermometer.thermoID = "028-0000yyyy";
        config.thermometerList.add(thermometer);
 
    	thermometer 													= config.new Thermometer();
    	thermometer.name = "tempRadiator";
        thermometer.friendlyName ="Radiateur";
        thermometer.thermoID = "028-0000zzzz";
        config.thermometerList.add(thermometer);

		HTTP_Req_Temp							httpRequest			= new HTTP_Req_Temp();
		httpRequest.execute(new Mgmt_Msg_Temperatures_Req());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch(item.getItemId()) 
		{
			case R.id.menuitem_search:
				Toast.makeText(Global.appContext, "search", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.menuitem_add:
				Toast.makeText(Global.appContext, "add", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.menuitem_share:
				Toast.makeText(Global.appContext, "share", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.menuitem_feedback:
				Toast.makeText(Global.appContext, "feedback", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.menuitem_about:
				Toast.makeText(Global.appContext, "about", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.menuitem_quit:
				Toast.makeText(Global.appContext, "quit", Toast.LENGTH_SHORT).show();
				return true;
		}
		return false;
	}
	
	public void temperaturesClick(View v)
	{
		// This is to update the temperature readings - but it is all wrong
		HTTP_Req_Temp							httpRequest			= new HTTP_Req_Temp();
		httpRequest.execute(new Mgmt_Msg_Temperatures_Req());
	}
	public void configurationClick(View v)
	{
//	Intent i 														= new Intent(getBaseContext(), Activity_Configuration.class);                      
	//	i.putExtra("PersonID", personID);
//		startActivity(i);
	//	System.out.println("before sCV");
	//	setContentView(R.layout.activity_configuration);
	//	System.out.println("after sCV");
	}
	
	private class HTTP_Req_Temp extends AsyncTask <Mgmt_Msg_Abstract, Void, Mgmt_Msg_Abstract> 
	{
		public URL						serverURL;
		public URLConnection			servletConnection;
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


				((TextView) findViewById(R.id.Date)).setText(displayDate(msg_received.dateTime));
				((TextView) findViewById(R.id.Time)).setText(displayTime(msg_received.dateTime));

				((TextView) findViewById(R.id.Boiler)).setText(displayTemperature(msg_received.tempBoiler));
				((TextView) findViewById(R.id.HotWater)).setText(displayTemperature(msg_received.tempHotWater));
				((TextView) findViewById(R.id.Outside)).setText(displayTemperature(msg_received.tempOutside));
				((TextView) findViewById(R.id.BoilerIn)).setText(displayTemperature(msg_received.tempBoilerIn));
				((TextView) findViewById(R.id.FloorOut)).setText(displayTemperature(msg_received.tempFloorOut));
				((TextView) findViewById(R.id.FloorHot)).setText(displayTemperature(msg_received.tempFloorHot));
				((TextView) findViewById(R.id.FloorCold)).setText(displayTemperature(msg_received.tempFloorCold));
				((TextView) findViewById(R.id.RadiatorOut)).setText(displayTemperature(msg_received.tempRadiatorOut));
				((TextView) findViewById(R.id.RadiatorIn)).setText(displayTemperature(msg_received.tempRadiatorIn));
				((TextView) findViewById(R.id.LivingRoom)).setText(displayTemperature(msg_received.tempLivingRoom));
			}
			else
			{
				Context 				context 				= getApplicationContext();
				CharSequence 			text 					= "A Nack has been returned";
				int 					duration 				= Toast.LENGTH_SHORT;

				Toast 					toast 					= Toast.makeText(context, text, duration);
				toast.show();
				
				Toast.makeText(getApplicationContext(), "What a shame", Toast.LENGTH_LONG).show();
			}
	    }
		public Mgmt_Msg_Abstract sendData(Mgmt_Msg_Abstract messageSend)
		{
			serverURL											= null;
			servletConnection									= null;
			Mgmt_Msg_Abstract				messageReceive		= null;
			
			try
			{
//				serverURL = new URL("http://192.168.5.20:8080/hvac/Management");
				serverURL = new URL("http://home.bapjg.com:8080/hvac/Management");
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
