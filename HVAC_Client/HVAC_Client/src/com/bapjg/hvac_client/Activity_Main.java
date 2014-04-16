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
        global.appContext 								= getApplicationContext();
        global.actContext								= (Context)  this;
        global.activity									= (Activity) this;
        global.piSocketAddress							= null;
        
        ActionBar 				actionbar 				= getActionBar();
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        ActionBar.Tab 			tabTemperatures 		= actionbar.newTab().setText("Temperatures");
        ActionBar.Tab 			tabImmediate	 		= actionbar.newTab().setText("Immediate");
        ActionBar.Tab 			tabConfiguration 		= actionbar.newTab().setText("Configuration");
        ActionBar.Tab 			tabCalendars		 	= actionbar.newTab().setText("Calendars");
        ActionBar.Tab 			tabActions				= actionbar.newTab().setText("Actions");
        
        // Setup the Menu Fragments
        // Menu_Fragment constructor takes 2 arguments : PanelFragment, Layout.id
        // The onCreate method, calls the onClick argument of the first item in the list	
        //	
        global.menuTemperatures							= new Menu_1_Temperatures	(R.layout.menu_1_temperatures);
        global.menuImmediate							= new Menu_2_Immediate		(R.layout.menu_2_immediate);
        global.menuCalendars							= new Menu_3_Calendars		(R.layout.menu_3_calendars);
        global.menuConfiguration						= new Menu_4_Config			(R.layout.menu_4_configuration);
        global.menuActions								= new Menu_5_Actions		(R.layout.menu_5_actions);
 
        // Setup the listener to change the 2 pages to be displayed on each "tab" click
        //                                                 menu fragment   ,  	panel object
        tabTemperatures.setTabListener	(new Listener_Tabs(global.menuTemperatures));
        tabImmediate.setTabListener		(new Listener_Tabs(global.menuImmediate));
        tabCalendars.setTabListener		(new Listener_Tabs(global.menuCalendars));
        tabConfiguration.setTabListener	(new Listener_Tabs(global.menuConfiguration));
        tabActions.setTabListener		(new Listener_Tabs(global.menuActions));
        
        actionbar.addTab(tabTemperatures);
        actionbar.addTab(tabImmediate);
        actionbar.addTab(tabConfiguration);
        actionbar.addTab(tabCalendars);
        actionbar.addTab(tabActions);

        //HTTP_Req_Ping		httpRequest					= new HTTP_Req_Ping();
		//httpRequest.execute();
		Global.initialisationCompleted					= true;
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

	//===========================================================================================================================
	//
	//
	private class HTTP_Req_Ping extends AsyncTask <Void, Void, Mgmt_Msg_Abstract> 
	{
		public HTTP_Request				http;
		
		public HTTP_Req_Ping()
		{
			http													= new HTTP_Request();
		}
		@Override
		protected Mgmt_Msg_Abstract doInBackground(Void... params) 
		{
			return http.ping();
		}
		@Override
		protected void onProgressUpdate(Void... progress) {}
		@Override
	    protected void onPostExecute(Mgmt_Msg_Abstract result) 
		{             
			if (result instanceof Mgmt_Msg_Abstract.Ack)
			{
		        HTTP_Req_Configuration			httpRequest			= new HTTP_Req_Configuration();
				httpRequest.execute();
			}
	    }
	}
	//
	//
	//===========================================================================================================================

	//===========================================================================================================================
	//
	//
	private class HTTP_Req_Configuration extends AsyncTask <Void, Void, Mgmt_Msg_Abstract> 
	{
		public HTTP_Request				http;
		public HTTP_Req_Configuration()
		{
			http													= new HTTP_Request();
		}
		@Override
		protected Mgmt_Msg_Abstract doInBackground(Void... messageOut) 
		{
			return http.sendData(new Mgmt_Msg_Configuration().new Request());			
		}
		@Override
		protected void onProgressUpdate(Void... progress) {}
		@Override
	    protected void onPostExecute(Mgmt_Msg_Abstract result)
		{             
			if (result instanceof Mgmt_Msg_Configuration.Data)
			{
				Mgmt_Msg_Configuration.Data msg_received 			= (Mgmt_Msg_Configuration.Data) result;
//				Global.configuration					 			= msg_received;
			}
	    }
	}
	//
	//
	//===========================================================================================================================
}
