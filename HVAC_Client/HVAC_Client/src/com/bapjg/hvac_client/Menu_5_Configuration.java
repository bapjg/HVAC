package com.bapjg.hvac_client;

import HVAC_Common.Ctrl_Calendars;
import HVAC_Common.Ctrl_Configuration;
import HVAC_Common.Ctrl_Weather;
import HVAC_Common.Ctrl_Configuration.Request;
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

public class Menu_5_Configuration 								extends 					Menu_0_Fragment 
																implements 					View.OnClickListener,
																							Dialog_Response
{
	public Menu_5_Configuration()
	{
		super();
		this.menuLayout																		= R.layout.menu_5_configuration;
	}
	public void onClick(View myView) // This is the onClick event from the Menu
	{
		super.onClick(myView);
		
    	String													caption						= ((Button) myView).getText().toString();
    	FragmentTransaction										fTransaction				= getFragmentManager().beginTransaction();
    	Fragment 												panelFragment				= null;
    	
    	if      (caption.equalsIgnoreCase("Thermometers"))		panelFragment 				= new Panel_5_Configuration_Thermometers();
    	else if (caption.equalsIgnoreCase("Relays"))			panelFragment 				= new Panel_5_Configuration_Relays();
    	else if (caption.equalsIgnoreCase("Pumps"))				panelFragment 				= new Panel_5_Configuration_Pumps();
    	else if (caption.equalsIgnoreCase("Circuits"))			panelFragment 				= new Panel_5_Configuration_Circuits();
    	else if (caption.equalsIgnoreCase("Burner"))			panelFragment 				= new Panel_5_Configuration_Burner();
    	else if (caption.equalsIgnoreCase("Boiler"))			panelFragment 				= new Panel_5_Configuration_Boiler();
    	else if (caption.equalsIgnoreCase("Refresh"))			doRefresh();
    	else if (caption.equalsIgnoreCase("Update"))			doUpdate();

    	if (panelFragment != null)
    	{
    		fTransaction.replace(R.id.panel_container, panelFragment);
    	}
		fTransaction.commit();
	}
	public void doRefresh()
	{
		Global.toaster("doRefresh", false);
        HTTP_Send	(new Ctrl_Configuration().new Request());
	}
	public void doUpdate()
	{
		Dialog_Yes_No												messageYesNo			= new Dialog_Yes_No("Are you certain ?", this);
		messageYesNo.show(getFragmentManager(), "Dialog_Yes_No");
	}
	public void onDialogReturn(boolean yes)
	{
		if (yes)
		{
			HTTP_Send	(new Ctrl_Configuration().new Update());
			Global.toaster("doUpdate", false);
		}
	}
}
