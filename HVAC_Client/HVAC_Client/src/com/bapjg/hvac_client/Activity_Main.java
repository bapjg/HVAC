package com.bapjg.hvac_client;

import java.util.ArrayList;

import com.bapjg.hvac_client.Mgmt_Msg_Temperatures.Request;

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

public class Activity_Main 			extends Activity 
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
        Global.appContext 								= getApplicationContext();
        Global.actContext								= (Context)  this;
        Global.activity									= (Activity) this;
        Global.piSocketAddress							= null;
        
        ActionBar 				actionbar 				= getActionBar();
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        ActionBar.Tab 			tabTemperatures 		= actionbar.newTab().setText("Temperatures");
        ActionBar.Tab 			tabImmediate	 		= actionbar.newTab().setText("Immediate");
        ActionBar.Tab 			tabCalendars		 	= actionbar.newTab().setText("Calendars");
        ActionBar.Tab 			tabConfiguration 		= actionbar.newTab().setText("Configuration");
        ActionBar.Tab 			tabActions				= actionbar.newTab().setText("Actions");
        
        // Setup the Menu Fragments
        // Menu_Fragment constructor takes 2 arguments : PanelFragment, Layout.id
        // The onCreate method, calls the onClick argument of the first item in the list	
        //	
        Global.menuTemperatures							= new Menu_1_Temperatures	(R.layout.menu_1_temperatures);
        Global.menuImmediate							= new Menu_2_Immediate		(R.layout.menu_2_immediate);
        Global.menuCalendars							= new Menu_3_Calendars		(R.layout.menu_3_calendars);
        Global.menuConfiguration						= new Menu_4_Config			(R.layout.menu_4_configuration);
        Global.menuActions								= new Menu_5_Actions		(R.layout.menu_5_actions);
 
        // Setup the listener to change the 2 pages to be displayed on each "tab" click
        //                                                 menu fragment   ,  	panel object
        tabTemperatures.setTabListener	(new Listener_Tabs(Global.menuTemperatures));
        tabImmediate.setTabListener		(new Listener_Tabs(Global.menuImmediate));
        tabCalendars.setTabListener		(new Listener_Tabs(Global.menuCalendars));
        tabConfiguration.setTabListener	(new Listener_Tabs(Global.menuConfiguration));
        tabActions.setTabListener		(new Listener_Tabs(Global.menuActions));
        
        actionbar.addTab(tabTemperatures);
        actionbar.addTab(tabImmediate);
        actionbar.addTab(tabCalendars);
        actionbar.addTab(tabConfiguration);
        actionbar.addTab(tabActions);

        //HTTP_Req_Ping		httpRequest					= new HTTP_Req_Ping();
		//httpRequest.execute();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	
	//===========================================================================================================================
	//
	//
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
				finish();
				return true;
		}
		return false;
	}
	//
	//
	//===========================================================================================================================
}
