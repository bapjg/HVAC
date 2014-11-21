package com.bapjg.hvac_client;

import com.google.gson.Gson;

import HVAC_Common.*;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
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
        

        
//        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) 
//        {     
//            Toast.makeText(this, "Large screen",Toast.LENGTH_LONG).show();
//        }
//        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) 
//        {     
//            Toast.makeText(this, "Normal sized screen" , Toast.LENGTH_LONG).show();
//        } 
//        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) 
//        {     
//            Toast.makeText(this, "Small sized screen" , Toast.LENGTH_LONG).show();
//        }
//        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) 
//        {     
//            Toast.makeText(this, "Extra Large sized screen" , Toast.LENGTH_LONG).show();
//        }
//        else 
//        {
//            Toast.makeText(this, "Screen size is neither large, normal or small" , Toast.LENGTH_LONG).show();
//        }
        

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
		Global.setAddressSpace();
		Global.setStatusHTTP(result);
		
		if 		(result instanceof Ctrl_Calendars.Data) 		
		{	
			Global.eRegCalendars 		= (Ctrl_Calendars.Data) result;			
		}
		else if (result instanceof Ctrl_Configuration.Data)		
		{	
			Global.eRegConfiguration	= (Ctrl_Configuration.Data) result;		
		}
		else if (result instanceof Ctrl_Json.Data)				
		{	
			Ctrl_Json.Data											messageReceived			= (Ctrl_Json.Data) result;
			if (messageReceived.type == Ctrl_Json.TYPE_Calendar)
			{
				String												JsonString				= ((Ctrl_Json.Data) result).json;
				Global.eRegCalendars														= new Gson().fromJson(JsonString, Ctrl_Calendars.Data.class);
			}
			else if (messageReceived.type == Ctrl_Json.TYPE_Configuration)
			{
				String												JsonString				= ((Ctrl_Json.Data) result).json;
				Global.eRegConfiguration													= new Gson().fromJson(JsonString, Ctrl_Configuration.Data.class);
			}
			else
			{
			}
		}
	}
	public void processFinishTCP(Ctrl__Abstract result) 
	{  
		Global.setAddressSpace();
		Global.setStatusTCP(result);
		
		if (result instanceof Ctrl_Weather.Data)
		{
			Ctrl_Weather.Data									resultWeather				= (Ctrl_Weather.Data) result;
			Global.weatherForecast				 											= (Ctrl_WeatherData) resultWeather.weatherData;
		}
		else
		{
		}
	}
}
