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
//	public Fragment 	panel;
	
	public Listener_Tabs(Fragment menu) 
	{
		this.menu 		= menu;		//menu object
	}
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) 
	{
	}
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) 
	{
		ft.replace(R.id.menu_container, menu);
//		ft.addToBackStack(null);
	}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) 
	{
		// params for ft.replace : Fragment_Object to be removed
		ft.remove(menu);
	}
}
