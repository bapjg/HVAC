package com.bapjg.hvac_client;

import HVAC_Common.*;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("ValidFragment")
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------

public class Panel_3_Calendars_Away_Item 						extends 					Panel_0_Fragment
																implements					Dialog_Response
{		
	private Ctrl_Calendars.Away 								itemData;
	private Ctrl_Calendars.Away 								itemDataWork;

	private Element_Heading										headingGeneral;
	private Element_Standard									dateStart;
	private Element_Standard									dateEnd;
	private Element_Standard									timeStart;
	private Element_Standard									timeEnd;

	public Panel_3_Calendars_Away_Item(Ctrl_Calendars.Away itemData)
	{
		super("Ok-Delete");
		this.itemData																		= itemData;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);

    	headingGeneral			 															= new Element_Heading("Parameters");
    	dateStart																			= new Element_Standard("Thermometer Name");
    	dateEnd																				= new Element_Standard("Thermometer Name");
    	timeStart																			= new Element_Standard("Thermometer Name");
    	timeEnd																				= new Element_Standard("Thermometer Name");

    	panelInsertPoint.addView(headingGeneral);
    	panelInsertPoint.addView(dateStart);
    	panelInsertPoint.addView(dateEnd);
    	panelInsertPoint.addView(timeStart);
    	panelInsertPoint.addView(timeEnd);

    	displayTitles("Calendar", "Away Item");
        displayContents();
        setListens();
        
        return panelView;
    }
	public void displayContents()
	{
		dateStart						.setTextRight		(Global.displayDate(itemData.dateTimeStart));
		dateEnd							.setTextRight		(Global.displayTimeShort(itemData.dateTimeStart));
		timeStart						.setTextRight		(Global.displayDate(itemData.dateTimeEnd));
		timeEnd							.setTextRight		(Global.displayTimeShort(itemData.dateTimeEnd));
   	}
	public void setListens()
	{
		dateStart						.setListener(this);
		dateEnd							.setListener(this);
		timeStart						.setListener(this);
		timeEnd							.setListener(this);
	}
    @Override
	public void onElementClick(View clickedView) 
	{
    	if ((clickedView == dateStart) || (clickedView == timeStart))
    	{
     		Dialog_DateTime	 								dialog 							= new Dialog_DateTime(itemData.dateTimeStart, itemData, this);
     		dialog.show(getFragmentManager(), "Dialog_Date_Time");
    	}
    	else if ((clickedView == dateEnd) || (clickedView == timeEnd))
    	{
     		Dialog_DateTime	 								dialog 							= new Dialog_DateTime(itemData.dateTimeEnd, itemData, this);
     		dialog.show(getFragmentManager(), "Dialog_Date_Time");
    	}
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
    	Global.eRegCalendars.awayList.remove(itemData);
    	getFragmentManager().popBackStackImmediate();
    }
    @Override
    public void onDialogReturn()
    {
    	displayContents();
    }
}