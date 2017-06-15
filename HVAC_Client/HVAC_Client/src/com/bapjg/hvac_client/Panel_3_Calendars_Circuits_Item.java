package com.bapjg.hvac_client;

import java.util.ArrayList;

import HVAC_Common.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Panel_3_Calendars_Circuits_Item 				extends 					Panel_0_Fragment
{		
	private Ctrl_Calendars.Calendar 							itemData;
	private	String												daysWord;
	private	String												daysNumbers;
	private String												circuitName;
	
	private	Element_Slots_WeekDays								weekDays;
	private	Element_Standard									timeStart;
	private	Element_Standard									timeEnd;
	private	Element_Standard									tempObjective;
	private	Element_CheckBox									stopOnObjective;
	
	public Panel_3_Calendars_Circuits_Item(Ctrl_Calendars.Calendar itemData, String circuitName)
	{
		super("Ok_Delete");
		this.itemData																		= itemData;
		this.circuitName																	= circuitName;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);

    	displayTitles("Calendar", circuitName);
    	
    	weekDays																			= new Element_Slots_WeekDays();
    	timeStart																			= new Element_Standard("Time Start");
    	timeEnd																				= new Element_Standard("Time End");
    	tempObjective																		= new Element_Standard("Temperature Objective");
    	stopOnObjective																		= new Element_CheckBox("Stop on Objective");
    	
    	panelInsertPoint.addView(weekDays);
    	panelInsertPoint.addView(timeStart);
    	panelInsertPoint.addView(timeEnd);
    	panelInsertPoint.addView(tempObjective);
    	panelInsertPoint.addView(stopOnObjective);
    	

        displayContents();
        setListens();
        
        return panelView;
    }
	public void displayContents()
	{
		String 													days						= Global.eRegCalendars.fetchDays(itemData.days);
		
		if (days == null)																	// itemData.days contains eg "123"
		{
			weekDays					.setData		("Selected Days", itemData.days);			
		}
		else
		{
			weekDays					.setData		(itemData.days, Global.eRegCalendars.fetchDays(itemData.days));			
		}

		timeStart						.setValue		(itemData.timeStart);
		timeEnd							.setValue		(itemData.timeEnd);
		tempObjective					.setValue		(itemData.tempObjective);
		stopOnObjective					.setChecked		(itemData.stopOnObjective);
		
 	}
	public void setListens()
	{
		weekDays						.setListener(this);
		timeStart						.setListener(this);
		timeEnd							.setListener(this);
		tempObjective					.setListener(this);
		stopOnObjective					.setListener(this);
	}
    @Override
	public void onElementClick(View clickedView) 
	{
     	super.onClick(clickedView);
     	
     	if (clickedView == weekDays.textName)
    	{
     		Dialog_String_List		 							df 							= new Dialog_String_List(itemData.days, (Object) itemData, null, this);
    		df.items.add("Selected days");
    		df.itemSelected																	= "Selected days";

    		for (Ctrl_Calendars.Word word : Global.eRegCalendars.wordList)
    		{
    			if(word.name.equalsIgnoreCase(daysWord))
    			{
    				df.itemSelected															= daysWord;
    			}
    			df.items.add(word.name);
    		}
    		df.show(getFragmentManager(), "Dialog_List");
    	}
    	else if ((clickedView == weekDays.day_1)
    	||		 (clickedView == weekDays.day_2)
    	||		 (clickedView == weekDays.day_3)
    	||		 (clickedView == weekDays.day_4)
    	||		 (clickedView == weekDays.day_5)
    	||		 (clickedView == weekDays.day_6)
    	||		 (clickedView == weekDays.day_7)   )
    	{
    		if (weekDays.textName.getText().toString().equalsIgnoreCase("Selected days"))
    		{
    			String dayCellText = ((TextView) clickedView).getText().toString();
    		
	    		if ((itemData.days).indexOf(dayCellText) > -1)
	    		{
	    			itemData.days = itemData.days.replace(dayCellText,"");
	    		}
	    		else
	    		{
	    			itemData.days = itemData.days + dayCellText;
	    		}
    		}
    		displayContents();
    	}
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
     	else if (clickedView == timeStart)
    	{
     		Dialog_Time	 										df 							= new Dialog_Time(itemData.timeStart, this);
    		df.show(getFragmentManager(), "Dialog_Time");
    	}
     	else if (clickedView == timeEnd)
    	{
     		Dialog_Time											df 							= new Dialog_Time(itemData.timeEnd, this);
    		df.show(getFragmentManager(), "Dialog_Time");
    	}
     	else if (clickedView == tempObjective)
    	{
     		Dialog_Temperature 									df;
     		if (this.circuitName.equalsIgnoreCase("Floor"))
    		{
     			df 																			= new Dialog_Temperature(itemData.tempObjective, 15, 25, this);
    		}
     		else if (this.circuitName.equalsIgnoreCase("Hot_Water"))
    		{
     			df 																			= new Dialog_Temperature(itemData.tempObjective, 35, 70, this);
    		}
     		else
     		{
     			df 																			= new Dialog_Temperature(itemData.tempObjective, 20, 20, this);
     		}
    		df.show(getFragmentManager(), "Dialog_Temperature");
    	}
     	else if (clickedView == stopOnObjective)
    	{
     		itemData.stopOnObjective														= (! itemData.stopOnObjective);
    		displayContents();
    	}
	}
    public void onDialogReturn()
    {
        displayContents();
    }
 	public void onPanelButtonOk()
    {
    	getFragmentManager().popBackStackImmediate();
    }
    @Override
 	public void onPanelButtonAdd()
    {
     	Global.toaster("Invalid button in this situation", true);
     	getFragmentManager().popBackStackImmediate();
    }
    @Override
 	public void onPanelButtonDelete()
    {
    	Global.eRegCalendars.fetchCircuit(circuitName).calendarList.remove(itemData);
    	getFragmentManager().popBackStackImmediate();
    }
}

