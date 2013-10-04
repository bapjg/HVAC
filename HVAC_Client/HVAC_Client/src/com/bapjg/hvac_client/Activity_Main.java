package com.bapjg.hvac_client;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;

import android.app.ActionBar;
import android.app.Activity;

import android.content.Context;

import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;


import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;

public class Activity_Main extends Activity 
{
	public static	Global						global;

	private 		Adapter_Thermometers 		adapter;
	private			TabHost 					tabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Contains activity_container which contains choices_container (left) and panel_container(right)
        
        global											= new Global();
        global.appContext 								= getApplicationContext();
        global.actContext								= (Context) this;
        global.activity									= (Activity) this;
        global.serverURL								= "http://192.168.5.20:8080/hvac/Management";
        
        ActionBar 				actionbar 				= getActionBar();
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        ActionBar.Tab 			tabTemperatures 		= actionbar.newTab().setText("Temperatures");
        ActionBar.Tab 			tabConfiguration 		= actionbar.newTab().setText("Configuration");
        ActionBar.Tab 			tabCalendars		 	= actionbar.newTab().setText("Calendars");
        ActionBar.Tab 			tabActions				= actionbar.newTab().setText("Actions");
        
        global.fragmentTemperatures 					= new Fragment_Temperatures();
        global.fragmentConfiguration 					= new Fragment_Configuration();
        global.fragmentCalendars 						= new Fragment_Calendars();
        global.fragmentActions 							= new Fragment_Actions();
        
        Choices_Fragment		choicesTemperatures		= new Choices_Fragment(global.fragmentTemperatures, R.layout.choices_temperatures);
        Choices_Fragment		choicesConfiguration	= new Choices_Fragment(global.fragmentConfiguration, R.layout.choices_configuration);
        Choices_Fragment		choicesActions			= new Choices_Fragment(global.fragmentActions, R.layout.choices_actions);
        Choices_Fragment		choicesCalendars		= new Choices_Fragment(global.fragmentCalendars, R.layout.choices_calendars);

        
        

        // Note that first argument is for the buttons/tabs the second for information page
        //                                                 buttons layout     ,  Information layou
        tabTemperatures.setTabListener	(new Listener_Tabs(choicesTemperatures, global.fragmentTemperatures));
        tabConfiguration.setTabListener	(new Listener_Tabs(choicesConfiguration, global.fragmentConfiguration));
        tabCalendars.setTabListener		(new Listener_Tabs(choicesCalendars, global.fragmentCalendars));
        tabActions.setTabListener		(new Listener_Tabs(choicesActions, global.fragmentActions));
        
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

	//	HTTP_Req_Temp							httpRequest				= new HTTP_Req_Temp();
	//	httpRequest.execute(new Mgmt_Msg_Temperatures_Req());
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
	private class HTTP_Req_Temp extends AsyncTask <Mgmt_Msg_Abstract, Void, Mgmt_Msg_Abstract> 
	{
		public HTTP_Request				http;
		public HTTP_Req_Temp()
		{
			http													= new HTTP_Request();
		}
		@Override
		protected Mgmt_Msg_Abstract doInBackground(Mgmt_Msg_Abstract... messageOut) 
		{
			return http.sendData(messageOut[0]);			
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
				Toast.makeText(Global.appContext, "A Nack has been returned from " + Global.serverURL, Toast.LENGTH_LONG).show();
			}
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
