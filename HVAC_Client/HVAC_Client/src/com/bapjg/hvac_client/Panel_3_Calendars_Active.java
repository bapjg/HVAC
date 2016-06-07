package com.bapjg.hvac_client;

import java.util.ArrayList;

import HVAC_Common.*;
import HVAC_Common.Ctrl_Calendars.Calendar;
import HVAC_Common.Ctrl_Configuration.Request;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.EditText;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Panel_3_Calendars_Active 							extends 					Panel_0_Fragment 
{
	private Element_Switch										activeHotWater;
	private Element_Switch										activeFloor;
	private Element_Switch										activeRadiator;

	
	public Panel_3_Calendars_Active()
	{
		super("Standard");
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);
    	displayTitles("Calendars", "Active");

    	activeHotWater 																		= new Element_Switch("Hot Water");
    	activeFloor 																		= new Element_Switch("Floor");
    	activeRadiator 																		= new Element_Switch("Radiator");

    	panelInsertPoint.addView(new Element_Heading( "Active Circuits"));
    	panelInsertPoint.addView(activeHotWater);
    	panelInsertPoint.addView(activeFloor);
    	panelInsertPoint.addView(activeRadiator);

        if ((Global.eRegCalendars != null)
        &&  (Global.eRegCalendars.tasksActive != null))
        {
        	displayContents();
        	setListens();
        }
        else // we need to reconnect to the server
        {
            Global.toaster("Refresh", false);
        }
 
        return panelView;
    }
	public void displayContents()
	{
		if (getActivity() != null)			// The user has not changed the screen
		{
			Ctrl_Calendars.TasksActive							active						= Global.eRegCalendars.tasksActive;
			activeHotWater			.setChecked(active.hotWater);
			activeFloor				.setChecked(active.floor);
			activeRadiator			.setChecked(active.radiator);
		}
	}
	public void setListens()
	{
		activeHotWater			.setOnClickListener(this);
		activeFloor				.setOnClickListener(this);
		activeRadiator			.setOnClickListener(this);
	}
	public void processFinishHTTP(Ctrl__Abstract result) 
	{  
		super.processFinishHTTP(result);
		if (result instanceof Ctrl_Calendars.Data)
		{
			Global.eRegCalendars			 												= (Ctrl_Calendars.Data) result;
			displayContents();
	        setListens();
 		}
		else
		{
			Global.toaster("P5_Active : Data NOTNOTNOT received", true);
		}
	}
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
	public void onClick(View clickedView)
	{
		Ctrl_Calendars.TasksActive								tasksActive					= Global.eRegCalendars.tasksActive;

	   	if (clickedView == activeHotWater)
	   	{
	   		tasksActive.hotWater															= ! tasksActive.hotWater;								
	   		activeHotWater.setChecked(tasksActive.hotWater);
	   	}
	   	else if (clickedView == activeFloor)
	   	{
	   		tasksActive.floor																= ! tasksActive.floor;								
	   		activeFloor.setChecked(tasksActive.floor);
	   	}
      	else if (clickedView == activeRadiator)
	   	{
	   		tasksActive.radiator															= ! tasksActive.radiator;								
      		activeRadiator.setChecked(tasksActive.radiator);
	   	}
	}
    public void onDialogReturn()
    {
    	displayContents();
    }

}