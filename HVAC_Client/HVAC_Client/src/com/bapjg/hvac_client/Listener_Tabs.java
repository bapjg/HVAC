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
	public Fragment choices;
	public Fragment information;
	
	public Listener_Tabs(Fragment choices, Fragment information) 
	{
		this.choices 		= choices;
		this.information 	= information;
	}
	public Listener_Tabs(Fragment information) 
	{
		this.choices 		= null;
		this.information 	= information;
	}
	public Listener_Tabs(String tabName) 
	{
		this.choices 		= null;
		this.information 	= null;
	}
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) 
	{
		Toast.makeText(Global.appContext, "Reselected!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) 
	{
		if (this.choices != null)
		{
			ft.replace(R.id.choices_container, choices);
		}
		ft.replace(R.id.panel_container, information);
	}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) 
	{
		if (choices != null)
		{
			ft.remove(choices);
		}
		ft.remove(information);
	}
}
