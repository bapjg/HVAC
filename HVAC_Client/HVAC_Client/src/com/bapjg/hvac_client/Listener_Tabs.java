package com.bapjg.hvac_client;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.widget.Toast;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.view.Menu;

public class Listener_Tabs implements ActionBar.TabListener
{
	public Fragment 	menu;
	public Fragment 	panel;
	
	public Listener_Tabs(Fragment choices, Fragment panel) 
	{
		this.menu 		= choices;
		this.panel 		= panel;
	}
//	public Listener_Tabs(Fragment information) 
//	{
//		this.menu 		= null;
//		this.panel 		= panel;
//	}
//	public Listener_Tabs(String tabName) 
//	{
//		this.menu 		= null;
//		this.panel 		= null;
//	}
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) 
	{
	//	Toast.makeText(Global.appContext, "Reselected!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) 
	{
		// params for ft.replace : Destination in layout, Fragment_Object to be placed within
		ft.replace(R.id.menu_container, menu);					
		ft.replace(R.id.panel_container, panel);
	}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) 
	{
		// params for ft.replace : Fragment_Object to be removed
		ft.remove(menu);
		ft.remove(panel);
	}
}
