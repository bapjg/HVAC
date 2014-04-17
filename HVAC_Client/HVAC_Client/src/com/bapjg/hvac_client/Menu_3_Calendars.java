package com.bapjg.hvac_client;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

@SuppressLint("ValidFragment")
//Template										variable			= something
//Template										ext/imp				class
public class Menu_3_Calendars 					extends 			Menu_0_Fragment 
												implements 			View.OnClickListener
{
	public Menu_3_Calendars(int menuLayout)
	{
		super( menuLayout);
	}
	public void onClick(View myView) // This is the onClick event from the Menu
	{
		super.onClick(myView);
		
    	Button 									myButton 			= (Button) myView;
    	String									myCaption			= myButton.getText().toString();
    	FragmentManager 						fManager			= getFragmentManager();
    	FragmentTransaction						fTransaction		= fManager.beginTransaction();
    	Fragment 								panelFragment		= null;
    	
    	if (myCaption.equalsIgnoreCase("Relays"))
    	{
//    		panelFragment 											= new Panel_1_Temperatures(R.layout.panel_1_temperatures);
    	}
    	else if (myCaption.equalsIgnoreCase("Widgets"))
    	{
//    		panelFragment 											= new Panel_1_Temperatures(R.layout.panel_1_temperatures);
    	}
    	if (panelFragment != null)
    	{
    		fTransaction.replace(R.id.panel_container, panelFragment);
    	}
		fTransaction.commit();
	}
}
