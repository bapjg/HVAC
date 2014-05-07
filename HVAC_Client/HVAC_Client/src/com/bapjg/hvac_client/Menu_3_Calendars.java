package com.bapjg.hvac_client;

import HVAC_Messages.Ctrl_Calendars;
import HVAC_Messages.Ctrl_Configuration;
import HVAC_Messages.Ctrl_Weather;
import HVAC_Messages.Ctrl_Configuration.Request;
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
public class Menu_3_Calendars 							extends 					Menu_0_Fragment 
														implements 					View.OnClickListener
{
	public Menu_3_Calendars()
	{
		super();
		this.menuLayout																= R.layout.menu_3_calendars;
	}
	public void onClick(View myView) // This is the onClick event from the Menu
	{
		super.onClick(myView);
		
    	String											caption						= ((Button) myView).getText().toString();
    	FragmentTransaction								fTransaction				= getFragmentManager().beginTransaction();
    	Fragment 										panelFragment				= null;
   	
    	if      (caption.equalsIgnoreCase("Vocabulary"))panelFragment 				= new Panel_3_Calendars_Vocabulary();
    	else if (caption.equalsIgnoreCase("Hot Water")) panelFragment 				= new Panel_3_Calendars_Circuits("Hot_Water");
     	else if (caption.equalsIgnoreCase("Radiator"))	panelFragment 				= new Panel_3_Calendars_Circuits("Radiator");
    	else if (caption.equalsIgnoreCase("Floor"))		panelFragment 				= new Panel_3_Calendars_Circuits("Floor");
       	else if (caption.equalsIgnoreCase("Refresh"))	doRefresh();
    	else if (caption.equalsIgnoreCase("Update"))	doUpdate();

    	if 		(panelFragment != null)
    	{
    		fTransaction.replace(R.id.panel_container, panelFragment);
    	}
    	fTransaction.commit();  
	}
	public void doRefresh()
	{
        HTTP_Send	(new Ctrl_Calendars().new Request());				// Fire these async actions as soon as possible
        TCP_Send	(new Ctrl_Configuration().new Request());
        TCP_Send	(new Ctrl_Weather().new Request());
		Global.toaster("doRefresh", false);
	}
	public void doUpdate()
	{
		Global.toaster("doUpdate", false);
	}
}
