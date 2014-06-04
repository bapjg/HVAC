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

public class Item_3_Calendars_Away 								extends 					Panel_0_Fragment
																implements					Dialog_Response
{		
	private Ctrl_Calendars.Away 								itemData;
	private Ctrl_Calendars.Away 								itemDataWork;
	private ViewGroup											itemView;
	
	public Item_3_Calendars_Away(Ctrl_Calendars.Away itemData)
	{
		super();
		this.itemData																		= itemData;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        View 													itemView					= inflater.inflate(R.layout.item_3_calendars_away, container, false);
        this.itemView																		= (ViewGroup) itemView;

        displayHeader();
        displayContents();
        setListens();
        
        return itemView;
    }
	public void displayHeader()
	{
	}
	public void displayContents()
	{
    	TextView 												dateStart 					= (TextView) itemView.findViewById(R.id.dateStart);
    	TextView 												dateEnd						= (TextView) itemView.findViewById(R.id.dateEnd);
    	TextView 												timeStart					= (TextView) itemView.findViewById(R.id.timeStart);
    	TextView 												timeEnd						= (TextView) itemView.findViewById(R.id.timeEnd);
 
    	dateStart.setText(Global.displayDate(itemData.dateTimeStart));
    	timeStart.setText(Global.displayTimeShort(itemData.dateTimeStart));
    	
    	dateEnd.setText(Global.displayDate(itemData.dateTimeEnd));
    	timeEnd.setText(Global.displayTimeShort(itemData.dateTimeEnd));
   	}
	public void setListens()
	{
		itemView.findViewById(R.id.buttonOk).setOnClickListener(this);
		itemView.findViewById(R.id.buttonDelete).setOnClickListener(this);
	    itemView.findViewById(R.id.dateStart).setOnClickListener(this);
	    itemView.findViewById(R.id.dateEnd).setOnClickListener(this);
	    itemView.findViewById(R.id.timeStart).setOnClickListener(this);
	    itemView.findViewById(R.id.timeEnd).setOnClickListener(this);
	}
    @Override
	public void onClick(View clickedView) 
	{
     	if (clickedView.getId() == R.id.buttonOk)
    	{
//     		itemData.name															= ((EditText) itemView.findViewById(R.id.name)).getText().toString();
      		if (itemData.dateTimeEnd > itemData.dateTimeStart)
      		{
      			getFragmentManager().popBackStackImmediate();
      		}
      		else
      		{
      			Global.toaster("End must be after start", true);
      		}
    	}
     	else if (clickedView.getId() == R.id.buttonDelete)
    	{
     		Global.eRegCalendars.awayList.remove(itemData);
     		getFragmentManager().popBackStackImmediate();
    	}
    	else if ((clickedView.getId() == R.id.dateStart) || (clickedView.getId() == R.id.timeStart))
    	{
     		Dialog_Date_Time	 								dialog 							= new Dialog_Date_Time(itemData.dateTimeStart, itemData, this);
     		dialog.show(getFragmentManager(), "Dialog_Date_Time");
    	}
    	else if ((clickedView.getId() == R.id.dateEnd) || (clickedView.getId() == R.id.timeEnd))
    	{
     		Dialog_Date_Time	 								dialog 							= new Dialog_Date_Time(itemData.dateTimeEnd, itemData, this);
     		dialog.show(getFragmentManager(), "Dialog_Date_Time");
    	}
 	}
    @Override
	public void onDialogReturn() 
    {
    	displayContents();
    }

}

