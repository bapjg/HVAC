package com.bapjg.hvac_client;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.widget.Toast;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.view.Menu;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Listener_Tabs 										implements 					ActionBar.TabListener
{
	public Fragment 											menu;
	
	public Listener_Tabs(Fragment menu) 
	{
		this.menu 																			= menu;		//menu object
	}
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) 
	{
	}
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) 
	{
		ft.replace(R.id.menu_container, menu);
	}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) 
	{
		ft.remove(menu);
	}
}
