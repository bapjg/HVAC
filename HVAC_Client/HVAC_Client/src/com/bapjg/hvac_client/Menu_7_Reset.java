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
public class Menu_7_Reset 										extends Menu_0_Fragment 	implements View.OnClickListener
{
	public Menu_7_Reset()
	{
		super();
		this.menuLayout																		= R.layout.menu_7_reset;
	}
	public void onClick(View myView) 
	{
		super.onClick(myView);
		
    	String													caption						= ((Button) myView).getText().toString();
    	FragmentTransaction										fTransaction				= getFragmentManager().beginTransaction();
    	Fragment 												panelFragment				= null;
    	
//    	TODO This must be done
    	
    	if      (caption.equalsIgnoreCase("Reset Configuration"))	panelFragment 			= new Panel_6_Actions_Relays();
    	else if (caption.equalsIgnoreCase("Reset Calendars"))		panelFragment 			= new Panel_6_Actions_Test_Mail();

    	if (panelFragment != null)
    	{
    		fTransaction.replace(R.id.panel_container, panelFragment);
    	}
		fTransaction.commit();
	}
}
