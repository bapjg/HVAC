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
//Template												NEWNEWNEW					= NEWNEWNEW
//Template												variable					= something
//Template												ext/imp						class
public class Menu_5_Config 								extends 					Menu_0_Fragment 
														implements 					View.OnClickListener
{
	public Menu_5_Config()
	{
		super();
		this.menuLayout																= R.layout.menu_5_configuration;
	}
	public void onClick(View myView) // This is the onClick event from the Menu
	{
		super.onClick(myView);
		
    	String											caption						= ((Button) myView).getText().toString();
    	FragmentTransaction								fTransaction				= getFragmentManager().beginTransaction();
    	Fragment 										panelFragment				= null;
    	
    	if      (caption.equalsIgnoreCase("Thermometers"))	panelFragment 			= new Panel_5_Config_Thermometers();
    	else if (caption.equalsIgnoreCase("Relays"))	panelFragment 				= new Panel_5_Config_Relays();
    	else if (caption.equalsIgnoreCase("Pumps"))		panelFragment 				= new Panel_5_Config_Pumps();
    	else if (caption.equalsIgnoreCase("Circuits"))	panelFragment 				= new Panel_5_Config_Circuits();

    	if (panelFragment != null)
    	{
    		fTransaction.replace(R.id.panel_container, panelFragment);
    	}
		fTransaction.commit();
	}
}
