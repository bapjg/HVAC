package com.bapjg.hvac_client;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.widget.Toast;

public class Listener_Tabs implements ActionBar.TabListener
{
	public Fragment fragment;
	
	public Listener_Tabs(Fragment fragment) 
	{
		this.fragment = fragment;
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) 
	{
		Toast.makeText(Activity_Main.appContext, "Reselected!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) 
	{
//		ft.replace(R.id.fragment_container, fragment);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) 
	{
		ft.remove(fragment);
	}
}
