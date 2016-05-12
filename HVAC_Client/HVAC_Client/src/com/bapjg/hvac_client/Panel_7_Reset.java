package com.bapjg.hvac_client;

import HVAC_Common.*;
import HVAC_Common.Ctrl_Actions_Test_Mail.Execute;
import HVAC_Common.Ctrl_Temperatures.Request;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Panel_7_Reset 										extends 					Panel_0_Fragment  
{
	private	Element_Button										buttonConfiguration;
	private	Element_Button										buttonCalendars;

	public Panel_7_Reset()
	{
		super("Centered");
	}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);
    	
    	buttonConfiguration																= new Element_Button("Reset Configuration");
    	buttonCalendars																	= new Element_Button("Reset Calendars");
    	
    	panelInsertPoint.addView(buttonConfiguration);
    	panelInsertPoint.addView(buttonCalendars);
    	
    	displayTitles("Reset", " ");
    	displayContents();
        setListens();
   	
        return panelView;
    }
	public void displayContents()
	{
	}
	public void setListens()
	{
		buttonCalendars.setListener(this);
	}
	public void onElementClick(View clickedView)
    {
    	Ctrl_Actions_Stop.Execute 								resetMessage				= new Ctrl_Actions_Stop().new Execute();
		if      (clickedView == buttonConfiguration)			resetMessage.actionRequest	= Ctrl_Actions_Stop.ACTION_Stop;	// Reset_Config
		else if (clickedView == buttonCalendars)				resetMessage.actionRequest	= Ctrl_Actions_Stop.ACTION_Stop;	// Reset Calendars
    	TCP_Send(resetMessage);
    }
    @Override
	public void processFinishTCP(Ctrl__Abstract result) 
	{  // Should be Http message
		super.processFinishTCP(result);
		if (result instanceof Ctrl_Actions_Stop.Ack)			Global.toaster("Reset Request accepted", true);
		else   													Global.toaster("Reset generated error " + result.getClass().toString(), true);
	}
}

