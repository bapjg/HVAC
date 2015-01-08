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
	private Element_Standard									pumpCleanTime;
	private Element_Standard									pumpCleanDuration;
	private Element_Standard									antiFreeze;
	private Element_Standard									summerTemp;
	private Element_Standard									winterTemp;
	private Element_Standard									sunshineInfluence;

	
	public Panel_3_Calendars_Background_Tasks()
	{
		super("Standard");
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);
    	displayTitles("Calendars", "Background Tasks");

    	pumpCleanTime 																		= new Element_Standard("Pump Clean Time");
    	pumpCleanDuration 																	= new Element_Standard("Pump Clean Duration");
    	antiFreeze 																			= new Element_Standard("Anti Freeze");
    	summerTemp 																			= new Element_Standard("Summer Temperature");
    	winterTemp 																			= new Element_Standard("Winter Temperature");
    	sunshineInfluence 																	= new Element_Standard("Sunshine Influence");

    	panelInsertPoint.addView(new Element_Heading( "Parameters"));
    	panelInsertPoint.addView(pumpCleanTime);
    	panelInsertPoint.addView(pumpCleanDuration);
    	panelInsertPoint.addView(antiFreeze);
    	panelInsertPoint.addView(summerTemp);
    	panelInsertPoint.addView(winterTemp);
    	panelInsertPoint.addView(sunshineInfluence);

        if ((Global.eRegCalendars != null)
        &&  (Global.eRegCalendars.tasksBackGround != null))
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
			Ctrl_Calendars.TasksBackGround						backgroundTasks				= Global.eRegCalendars.tasksBackGround;
			pumpCleanTime		.setValue	(backgroundTasks.pumpCleanTime.displayShort());
			pumpCleanDuration	.setValue	(backgroundTasks.pumpCleanDurationMinutes.toString() + " mn");
			antiFreeze			.setValue	(backgroundTasks.antiFreeze.displayInteger());
			summerTemp			.setValue	(backgroundTasks.summerTemp.displayInteger());
			winterTemp			.setValue	(backgroundTasks.winterTemp.displayInteger());
			sunshineInfluence	.setValue	(backgroundTasks.sunshineInfluence.displayInteger());
		}
	}
	public void setListens()
	{
		pumpCleanTime			.setOnClickListener(this);
		pumpCleanDuration		.setOnClickListener(this);
		antiFreeze				.setOnClickListener(this);
		summerTemp				.setOnClickListener(this);
		winterTemp				.setOnClickListener(this);
		sunshineInfluence		.setOnClickListener(this);
	}
	public void processFinishTCP(Ctrl__Abstract result) 
	{  
		super.processFinishTCP(result);
		if (result instanceof Ctrl_Calendars.Data)
		{
//			Global.eRegConfiguration			 											= (Ctrl_Configuration.Data) result;
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
	   	if (clickedView == pumpCleanTime)
	   	{
    		dialogTime 																		= new Dialog_Time(backgroundTasks.pumpCleanTime, this);
    		dialogTime.show(getFragmentManager(), "Dialog_Time");
	   	}
	   	else if (clickedView == pumpCleanDuration)
	   	{
     		dialogInteger	 																= new Dialog_Integer(backgroundTasks.pumpCleanDurationMinutes,  (Object) backgroundTasks, 1, 10, "Select Duration (minutes)", this);
     		dialogInteger.show(getFragmentManager(), "Dialog_Integer");
	   	}
      	else if (clickedView == antiFreeze)
	   	{
      		dialogTemperature 																= new Dialog_Temperature(backgroundTasks.antiFreeze,  0, 15, this);
      		dialogTemperature.show(getFragmentManager(), "Dialog_Temperature");
	   	}
      	else if (clickedView == summerTemp)
	   	{
      		dialogTemperature 																= new Dialog_Temperature(backgroundTasks.summerTemp,  5, 25, this);
      		dialogTemperature.show(getFragmentManager(), "Dialog_Temperature");
	   	}
  		else if (clickedView == winterTemp)
	   	{
  			dialogTemperature 																= new Dialog_Temperature(backgroundTasks.winterTemp,  5, 25, this);
  			dialogTemperature.show(getFragmentManager(), "Dialog_Temperature");
	   	}
  		else if (clickedView == sunshineInfluence)
	   	{
  			dialogTemperature 																= new Dialog_Temperature(backgroundTasks.sunshineInfluence,  0, 10, this);
  			dialogTemperature.show(getFragmentManager(), "Dialog_Temperature");
		}
	}
    public void onDialogReturn()
    {
    	displayContents();
    }

}