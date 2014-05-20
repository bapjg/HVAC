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

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Menu_6_Actions 									extends Menu_0_Fragment 	implements View.OnClickListener
{
	public Menu_6_Actions()
	{
		super();
		this.menuLayout																		= R.layout.menu_6_actions;
	}
	public void onClick(View myView) 
	{
		super.onClick(myView);
		
    	String													caption						= ((Button) myView).getText().toString();
    	FragmentTransaction										fTransaction				= getFragmentManager().beginTransaction();
    	Fragment 												panelFragment				= null;
    	
    	if      (caption.equalsIgnoreCase("Relays"))			panelFragment 				= new Panel_6_Actions_Relays();
    	else if (caption.equalsIgnoreCase("Test Mail"))			panelFragment 				= new Panel_6_Actions_Test_Mail();
    	else if (caption.equalsIgnoreCase("Stop"))				panelFragment 				= new Panel_6_Actions_Stop();

    	if (panelFragment != null)
    	{
    		fTransaction.replace(R.id.panel_container, panelFragment);
    	}
		fTransaction.commit();
	}
}
