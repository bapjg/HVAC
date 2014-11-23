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
public class Panel_3_Calendars_Background_Tasks 				extends 					Panel_0_Fragment 
{
	public Panel_3_Calendars_Background_Tasks()
	{
		super();
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        // Inflate the layout for this fragment
    	this.panelLayout																	= R.layout.panel_3_calendars_background_tasks;
    	this.container																		= container;
    	this.panelView																		= inflater.inflate(R.layout.panel_3_calendars_background_tasks, container, false);

        if ((Global.eRegCalendars != null)
        &&  (Global.eRegCalendars.tasksBackGround != null))
        {
        	displayHeader();
        	displayContents();
        	setListens();
        }
        else // we need to reconnect to the server
        {
            Global.toaster("Refresh", false);
        }
 
        return panelView;
    }
	public void displayHeader()
	{
		((TextView) panelView.findViewById(R.id.title)).setText		("Calendars");
		((TextView) panelView.findViewById(R.id.subTitle)).setText	("Background Tasks");
//		TextView												title						= (TextView) panelView.findViewById(R.id.name);
	}
	public void displayContents()
	{
		if (getActivity() != null)			// The user has not changed the screen
		{
			Ctrl_Calendars.TasksBackGround						backgroundTasks				= Global.eRegCalendars.tasksBackGround;
			((TextView) panelView.findViewById(R.id.pumpCleanTime)).setText		(backgroundTasks.pumpCleanTime.displayShort());
			((TextView) panelView.findViewById(R.id.pumpCleanDuration)).setText	(backgroundTasks.pumpCleanDurationMinutes.toString() + " mn");
			((TextView) panelView.findViewById(R.id.antiFreeze)).setText		(backgroundTasks.antiFreeze.displayInteger());
			((TextView) panelView.findViewById(R.id.summerTemp)).setText		(backgroundTasks.summerTemp.displayInteger());
			((TextView) panelView.findViewById(R.id.winterTemp)).setText		(backgroundTasks.winterTemp.displayInteger());
			((TextView) panelView.findViewById(R.id.sunshineInfluence)).setText	(backgroundTasks.sunshineInfluence.displayInteger());
		}
	}
	public void setListens()
	{
		panelView.findViewById(R.id.pumpCleanTime).setOnClickListener(this);
		panelView.findViewById(R.id.pumpCleanDuration).setOnClickListener(this);
		panelView.findViewById(R.id.antiFreeze).setOnClickListener(this);
		panelView.findViewById(R.id.summerTemp).setOnClickListener(this);
		
		TextView x = (TextView) panelView.findViewById(R.id.winterTemp);
		
		panelView.findViewById(R.id.winterTemp).setOnClickListener(this);
		panelView.findViewById(R.id.sunshineInfluence).setOnClickListener(this);
	}
	public void processFinishTCP(Ctrl__Abstract result) 
	{  
		super.processFinishTCP(result);
		if (result instanceof Ctrl_Calendars.Data)
		{
//			Global.eRegConfiguration			 											= (Ctrl_Configuration.Data) result;
			displayHeader();
			displayContents();
	        setListens();
 		}
		else
		{
			Global.toaster("P5_Cals_Away : Data NOTNOTNOT received", true);
		}
	}
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
	public void onClick(View clickedView)
	{
		Ctrl_Calendars.TasksBackGround							backgroundTasks				= Global.eRegCalendars.tasksBackGround;
 		Dialog_Temperature										dialogTemperature;
 		Dialog_Time												dialogTime;
 		Dialog_Integer											dialogInteger;
	   	switch(clickedView.getId())
		{
     	case R.id.pumpCleanTime:
    		dialogTime 																		= new Dialog_Time(backgroundTasks.pumpCleanTime, this);
    		dialogTime.show(getFragmentManager(), "Dialog_Time");
      		break;
     	case R.id.pumpCleanDuration:
     		dialogInteger	 																= new Dialog_Integer(backgroundTasks.pumpCleanDurationMinutes,  (Object) backgroundTasks, 1, 10, "Select Duration (minutes)", this);
     		dialogInteger.show(getFragmentManager(), "Dialog_Integer");
      		break;
     	case R.id.antiFreeze:
     		dialogTemperature 																= new Dialog_Temperature(backgroundTasks.antiFreeze,  0, 15, this);
     		dialogTemperature.show(getFragmentManager(), "Dialog_Temperature");
      		break;
     	case R.id.summerTemp:
     		dialogTemperature 																= new Dialog_Temperature(backgroundTasks.summerTemp,  5, 25, this);
     		dialogTemperature.show(getFragmentManager(), "Dialog_Temperature");
      		break;
    	case R.id.winterTemp:
     		dialogTemperature 																= new Dialog_Temperature(backgroundTasks.winterTemp,  5, 25, this);
     		dialogTemperature.show(getFragmentManager(), "Dialog_Temperature");
      		break;
     	case R.id.sunshineInfluence:
     		dialogTemperature 																= new Dialog_Temperature(backgroundTasks.sunshineInfluence,  0, 10, this);
     		dialogTemperature.show(getFragmentManager(), "Dialog_Temperature");
      		break;
		}
	}
    public void onDialogReturn()
    {
    	displayContents();
    }

}