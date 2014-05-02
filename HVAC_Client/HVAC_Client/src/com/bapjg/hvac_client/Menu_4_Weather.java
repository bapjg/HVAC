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
public class Menu_4_Weather 					extends 			Menu_0_Fragment 
												implements 			View.OnClickListener
{
	public Menu_4_Weather(int menuLayout)
	{
		super(menuLayout);
	}
	public void onClick(View myView) // This is the onClick event from the Menu
	{
		super.onClick(myView);
		
    	Button 									myButton 			= (Button) myView;
    	String									myCaption			= myButton.getText().toString();
    	FragmentManager 						fManager			= getFragmentManager();
    	FragmentTransaction						fTransaction		= fManager.beginTransaction();
    	Fragment 								panelFragment		= null;
    	
    	if      (myCaption.equalsIgnoreCase("Today")) 		panelFragment 	= new Panel_4_Weather("Today");
    	else if (myCaption.equalsIgnoreCase("Tomorrow"))   	panelFragment 	= new Panel_4_Weather("Tomorrow");
     	else if (myCaption.equalsIgnoreCase("Beyond"))		panelFragment 	= new Panel_4_Weather("Beyond");

    	if 		(panelFragment != null)
    	{
    		fTransaction.replace(R.id.panel_container, panelFragment);
    	}
    	fTransaction.commit();  
	}
}