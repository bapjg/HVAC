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
	public Panel_3_Calendars_Active()
	{
		super("None");
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);

    	Element_ListView										listView 					= new Element_ListView("Henry");
    	panelInsertPoint.addView(listView);
    	
    	this.adapterView																	= (AdapterView) panelView.findViewById(R.id.listView);
     	displayTitles("Calendars", "Active Circuits");
 
        if ((Global.eRegCalendars != null)
        &&  (Global.eRegCalendars.circuitList != null))
        {
        	displayContents();
            setListens();
        }
        else // we need to reconnect to the server
        {
        	Global.toaster("Please refresh", true);
        }
        return panelView;
    	
//    	activeHotWater 																		= new Element_Switch("Hot Water");
//    	activeFloor 																		= new Element_Switch("Floor");
//    	activeRadiator 																		= new Element_Switch("Radiator");
//
//    	panelInsertPoint.addView(new Element_Heading( "Active Circuits"));
//    	panelInsertPoint.addView(activeHotWater);
//    	panelInsertPoint.addView(activeFloor);
//    	panelInsertPoint.addView(activeRadiator);
//
//        if (Global.eRegCalendars == null)
//        {
//        	Global.toaster("Refresh", false);
//        }
//        else 
//        {
// 			Required when first introduced in the Json string
//        	if  (Global.eRegCalendars.tasksActive == null)
//        	{
//        		Global.eRegCalendars.tasksActive											= new Ctrl_Calendars ().new TasksActive();
//        		Global.eRegCalendars.tasksActive.hotWater									= true;
//        		Global.eRegCalendars.tasksActive.floor										= true;
//        		Global.eRegCalendars.tasksActive.radiator									= true;
//        	}
//        	displayContents();
//        	setListens();
 //       }
 //       return panelView;
    }
	public void displayContents()
	{
		Panel_3_Calendars_Active_Adapter								arrayAdapter				= new Panel_3_Calendars_Active_Adapter(Global.actContext, R.id.listView, Global.eRegCalendars.circuitList);
		((AdapterView <Panel_3_Calendars_Active_Adapter>) adapterView).setAdapter(arrayAdapter);
	}
	public void setListens()
	{
		((AdapterView <?>) adapterView).setOnItemClickListener(this);
	   	((ViewGroup) this.adapterView.getParent()).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
	   	((ViewGroup) this.adapterView).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

//		activeHotWater			.setListener(this);
//		activeFloor				.setListener(this);
//		activeRadiator			.setListener(this);
	}
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
    	Ctrl_Calendars.Circuit									itemData					= Global.eRegCalendars.circuitList.get(position);
    	if (itemData.active == null)							itemData.active				= true;
    	else 													itemData.active				= ! itemData.active;
    	displayContents();
    	setListens();
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
	public void onElementClick(View clickedView)
	{
    	Ctrl_Calendars.Circuit									itemData					= Global.eRegCalendars.circuitList.get(0);
    	itemData.active																		= ! itemData.active;
		
////		Ctrl_Calendars.TasksActive								tasksActive					= Global.eRegCalendars.tasksActive;
//
//	   	if (clickedView == activeHotWater)
//	   	{
////	   		tasksActive.hotWater															= ! tasksActive.hotWater;								
////	   		activeHotWater.setChecked(tasksActive.hotWater);
//	   	}
//	   	else if (clickedView == activeFloor)
//	   	{
////	   		tasksActive.floor																= ! tasksActive.floor;								
////	   		activeFloor.setChecked(tasksActive.floor);
//	   	}
//      	else if (clickedView == activeRadiator)
//	   	{
////	   		tasksActive.radiator															= ! tasksActive.radiator;								
////      		activeRadiator.setChecked(tasksActive.radiator);
//	   	}
	}
    public void onDialogReturn()
    {
    	displayContents();
    }

}