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
//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------

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
		super("Ok_Delete");
		this.itemData																		= itemData;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);

    	headingGeneral			 															= new Element_Heading("Parameters");
    	dateStart																			= new Element_Standard("Date Start");
    	timeStart																			= new Element_Standard("Time Start");
    	dateEnd																				= new Element_Standard("Date End");
    	timeEnd																				= new Element_Standard("Time End");

    	panelInsertPoint.addView(headingGeneral);
    	panelInsertPoint.addView(dateStart);
    	panelInsertPoint.addView(timeStart);
    	panelInsertPoint.addView(dateEnd);
    	panelInsertPoint.addView(timeEnd);

    	displayTitles("Calendar", "Away Item");
        displayContents();
        setListens();
        
        return panelView;
    }
	public void displayContents()
	{
		dateStart						.setValue		(Global.displayDate(itemData.dateTimeStart));
		timeStart						.setValue		(Global.displayTimeShort(itemData.dateTimeStart));
		dateEnd							.setValue		(Global.displayDate(itemData.dateTimeEnd));
		timeEnd							.setValue		(Global.displayTimeShort(itemData.dateTimeEnd));
   	}
	public void setListens()
	{
		dateStart						.setListener(this);
		timeStart						.setListener(this);
		dateEnd							.setListener(this);
		timeEnd							.setListener(this);
	}
    @Override
	public void onElementClick(View clickedView) 
	{
    	if ((clickedView == dateStart) || (clickedView == timeStart))
    	{
     		Dialog_DateTime	 								dialog 							= new Dialog_DateTime(itemData.dateTimeStart, itemData, this, 1);
     		dialog.show(getFragmentManager(), "Dialog_Date_Time");
    	}
    	else if ((clickedView == dateEnd) || (clickedView == timeEnd))
    	{
     		Dialog_DateTime	 								dialog 							= new Dialog_DateTime(itemData.dateTimeEnd, itemData, this, 2);
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
    public void onDialogReturnWithId(int id)
//    public void onDialogReturn()
    {
    	if (id == 1)																		// dateTimeStart has just been modified
    	{
    		if (itemData.dateTimeStart > itemData.dateTimeEnd)								// Start is after end, end = start + 24 hours
    		{
    			itemData.dateTimeEnd														= itemData.dateTimeStart + 24L * 60L * 60L * 1000L;
    		}
    	}
    	else																				// dateTimeEnd has just been modified
    	{
    		if (itemData.dateTimeStart > itemData.dateTimeEnd)								// Start is after end, end = start + 24 hours
    		{
    			itemData.dateTimeStart														= itemData.dateTimeEnd - 24L * 60L * 60L * 1000L;
    		}
    	}
    	displayContents();
    }
}