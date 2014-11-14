package com.bapjg.hvac_client;

import com.google.gson.Gson;

import HVAC_Common.*;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Activity_Main 										extends 					Activity 
																implements					TCP_Response,
																							HTTP_Response
{
	public static	Global										global;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Contains activity_container which contains choices_container (left) and panel_container(right)

        global																				= new Global();
        Global.appContext 																	= getApplicationContext();
        Global.actContext																	= (Context)  this;
        Global.activity																		= (Activity) this;

        HTTP_Send	(new Ctrl_Json().new 		Request(Ctrl_Json.TYPE_Calendar));				// Fire these async actions as soon as possible
     	HTTP_Send	(new Ctrl_Json().new 		Request(Ctrl_Json.TYPE_Configuration));
        TCP_Send	(new Ctrl_Weather().new 	Request());
        
        ActionBar 												actionbar 					= getActionBar();
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        ActionBar.Tab 											tabTemperatures 			= actionbar.newTab().setText("Temperatures");
        ActionBar.Tab 											tabImmediate	 			= actionbar.newTab().setText("Immediate");
        ActionBar.Tab 											tabCalendars		 		= actionbar.newTab().setText("Calendars");
        ActionBar.Tab 											tabWeather			 		= actionbar.newTab().setText("Weather");
        ActionBar.Tab 											tabConfiguration 			= actionbar.newTab().setText("Configuration");
        ActionBar.Tab 											tabActions					= actionbar.newTab().setText("Actions");
        ActionBar.Tab 											tabReset					= actionbar.newTab().setText("Reset");
        
        // Setup the Menu Fragments
        // Menu_Fragment constructor takes 2 arguments : PanelFragment, Layout.id
        // The onCreate method, calls the onClick argument of the first item in the list	
        //	
        Global.menuTemperatures																= new Menu_1_Temperatures	();
        Global.menuImmediate																= new Menu_2_Immediate		();
        Global.menuCalendars																= new Menu_3_Calendars		();
        Global.menuWeather																	= new Menu_4_Weather		();
        Global.menuConfiguration															= new Menu_5_Configuration	();
        Global.menuActions																	= new Menu_6_Actions		();
        Global.menuReset																	= new Menu_7_Reset			();
 
        // Setup the listener to change the 2 pages to be displayed on each "tab" click
        //                                                 menu fragment   ,  	panel object
        tabTemperatures.setTabListener	(new Listener_Tabs(Global.menuTemperatures));
        tabImmediate.setTabListener		(new Listener_Tabs(Global.menuImmediate));
        tabCalendars.setTabListener		(new Listener_Tabs(Global.menuCalendars));
        tabWeather.setTabListener		(new Listener_Tabs(Global.menuWeather));
        tabConfiguration.setTabListener	(new Listener_Tabs(Global.menuConfiguration));
        tabActions.setTabListener		(new Listener_Tabs(Global.menuActions));
        tabReset.setTabListener			(new Listener_Tabs(Global.menuReset));
        
        actionbar.addTab(tabTemperatures);
        actionbar.addTab(tabImmediate);
        actionbar.addTab(tabCalendars);
        actionbar.addTab(tabWeather);
        actionbar.addTab(tabConfiguration);
        actionbar.addTab(tabActions);
        actionbar.addTab(tabReset);
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
			case R.id.menuitem_quit:
				Toast.makeText(Global.appContext, "quit", Toast.LENGTH_SHORT).show();
				finish();
				return true;
		}
		return false;
	}
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
	public void TCP_Send(Ctrl__Abstract message)		
	{		
		Global.setStatusTCP("Waiting");
		TCP_Task												task						= new TCP_Task();
	   	task.callBack																		= this;					// processFinish
	   	task.execute(message);
	}
	public void HTTP_Send(Ctrl__Abstract message)
	{
		Global.setStatusHTTP("Waiting");
		HTTP_Task												task						= new HTTP_Task();
	   	task.callBack																		= this;					// processFinish
	   	task.execute(message);		
	}		
	public void processFinishHTTP(Ctrl__Abstract result) 
	{  
		((TextView) this.findViewById(R.id.address_space)).setText((Global.isLocalIpAddress() ? "Local" : "Remote"));
		
		if 		(result instanceof Ctrl_Calendars.Data) 		
		{	
	        Global.setStatusHTTP("Ok");
			Global.eRegCalendars 		= (Ctrl_Calendars.Data) result;			
		}
		else if (result instanceof Ctrl_Configuration.Data)		
		{	
	        Global.setStatusHTTP("Ok");
			Global.eRegConfiguration	= (Ctrl_Configuration.Data) result;		
		}
		else if (result instanceof Ctrl_Json.Data)				
		{	
			Ctrl_Json.Data											messageReceived			= (Ctrl_Json.Data) result;
			if (messageReceived.type == Ctrl_Json.TYPE_Calendar)
			{
				String												JsonString				= ((Ctrl_Json.Data) result).json;
				Global.eRegCalendars														= new Gson().fromJson(JsonString, Ctrl_Calendars.Data.class);
				Global.setStatusHTTP("Ok");
			}
			else if (messageReceived.type == Ctrl_Json.TYPE_Configuration)
			{
				String												JsonString				= ((Ctrl_Json.Data) result).json;
				Global.eRegConfiguration													= new Gson().fromJson(JsonString, Ctrl_Configuration.Data.class);
				Global.setStatusHTTP("Ok");
			}
			else
			{
				Global.setStatusHTTP("Bad JSon Data");
			}
		}
		else if (result instanceof Ctrl_Configuration.NoConnection)			Global.setStatusHTTP("No Connection");
		else if (result instanceof Ctrl_Configuration.NoData)				Global.setStatusHTTP("No Data");			
		else if (result instanceof Ctrl_Configuration.TimeOut)				Global.setStatusHTTP("Time Out");
		else if (result instanceof Ctrl_Configuration.Nack)					Global.setStatusHTTP("Nack");
		else																Global.setStatusHTTP("Other Pb");
	}
	public void processFinishTCP(Ctrl__Abstract result) 
	{  
		((TextView) this.findViewById(R.id.address_space)).setText((Global.isLocalIpAddress() ? "Local" : "Remote"));
		
		if (result instanceof Ctrl_Weather.Data)
		{
			Ctrl_Weather.Data									resultWeather				= (Ctrl_Weather.Data) result;
			Global.weatherForecast				 											= (Ctrl_WeatherData) resultWeather.weatherData;
			((TextView) this.findViewById(R.id.connection_tcp)).setText("Connected");
		}
		else
		{
			((TextView) this.findViewById(R.id.connection_tcp)).setText("Bad Data");
		}
	}
}
