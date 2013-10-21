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
        global.actContext								= (Context)  this;
        global.activity									= (Activity) this;
        global.serverURL								= "";
        ActionBar 				actionbar 				= getActionBar();
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        ActionBar.Tab 			tabTemperatures 		= actionbar.newTab().setText("Temperatures");
        ActionBar.Tab 			tabConfiguration 		= actionbar.newTab().setText("Configuration");
        ActionBar.Tab 			tabCalendars		 	= actionbar.newTab().setText("Calendars");
        ActionBar.Tab 			tabActions				= actionbar.newTab().setText("Actions");
        
        global.panelTemperatures 						= new Panel_1_Temperatures();
        global.panelConfiguration 						= new Panel_2_Configuration();
        global.panelCalendars 							= new Panel_3_Calendars();
        global.panelActions 							= new Panel_4_Actions();
        
        // Setup the Menu Fragments
        // Menu_0_Fragment constructor takes 2 arguments : PanelFragment, Layout.id
        // The onCreate method, calls the onClick argument of the first item in the list	
        //	
        Menu_0_Fragment		menuTemperatures			= new Menu_0_Fragment(global.panelTemperatures, 	R.layout.menu_1_temperatures);
        Menu_0_Fragment		menuConfiguration			= new Menu_0_Fragment(global.panelConfiguration, 	R.layout.menu_2_configuration);
        Menu_0_Fragment		menuActions					= new Menu_0_Fragment(global.panelActions, 			R.layout.menu_4_actions);
        Menu_0_Fragment		menuCalendars				= new Menu_0_Fragment(global.panelCalendars, 		R.layout.menu_3_calendars);
 
        // Setup the listener to change the 2 pages to be displayed on each "tab" click
        //                                                 menu fragment   ,  	panel object
        tabTemperatures.setTabListener	(new Listener_Tabs(menuTemperatures, 	global.panelTemperatures));
        tabConfiguration.setTabListener	(new Listener_Tabs(menuConfiguration, 	global.panelConfiguration));
        tabCalendars.setTabListener		(new Listener_Tabs(menuCalendars, 		global.panelCalendars));
        tabActions.setTabListener		(new Listener_Tabs(menuActions, 		global.panelActions));
        
        actionbar.addTab(tabTemperatures);
        actionbar.addTab(tabConfiguration);
        actionbar.addTab(tabCalendars);
        actionbar.addTab(tabActions);

        HTTP_Req_Ping								httpRequest			= new HTTP_Req_Ping();
		httpRequest.execute();
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
				System.out.println("Activity_Main : Reply to Ping with Ack");
				Toast.makeText(Global.appContext, "Activity_Main : Connected to server : " + Global.serverURL, Toast.LENGTH_SHORT).show();
		        HTTP_Req_Configuration			httpRequest			= new HTTP_Req_Configuration();
				httpRequest.execute();
			}
			else
			{
				Toast.makeText(Global.appContext, "Activity_Main : Not Connected, no server replied", Toast.LENGTH_SHORT).show();
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
			System.out.println("Activity_Main : hello here");
			if (result instanceof Mgmt_Msg_Configuration.Data)
			{
				Mgmt_Msg_Configuration.Data msg_received 			= (Mgmt_Msg_Configuration.Data) result;
				Global.configuration					 			= msg_received;
				System.out.println("Activity_Main : hello here");
			}
			else
			{
				Toast.makeText(Global.appContext, "Activity_Main : A Nack has been returned from " + Global.serverURL, Toast.LENGTH_LONG).show();
			}
	    }
	}
	//
	//
	//===========================================================================================================================
}
