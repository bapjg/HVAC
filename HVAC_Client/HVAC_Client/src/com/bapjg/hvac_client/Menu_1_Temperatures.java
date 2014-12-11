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
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------

public class Menu_1_Temperatures 								extends 					Menu_0_Fragment 
																implements 					View.OnClickListener
{
	public Menu_1_Temperatures()
	{
		super();
		this.menuLayout																		= R.layout.menu_1_temperatures;
	}
	public void onClick(View myView) 									// This is the onClick event from the Menu
	{
		super.onClick(myView);
		
    	String													caption						= ((Button) myView).getText().toString();
    	FragmentTransaction										fTransaction				= getFragmentManager().beginTransaction();
    	Fragment 												panelFragment				= null;
		
    	if   (caption.equalsIgnoreCase("Temperatures"))			panelFragment 				= new Panel_1_Temperatures();
    	if   (caption.equalsIgnoreCase("Temperatures Large"))	panelFragment 				= new Panel_1_Temperatures();
    	if   (caption.equalsIgnoreCase("Test"))					panelFragment 				= new Panel_1_Test();

    	if (panelFragment != null)
    	{
    		fTransaction.replace(R.id.panel_container, panelFragment);
    	}
		fTransaction.commit();
	}
}
