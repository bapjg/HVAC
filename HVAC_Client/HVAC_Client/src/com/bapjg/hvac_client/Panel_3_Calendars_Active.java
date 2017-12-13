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
      }
	public void displayContents()
	{
		Panel_3_Calendars_Active_Adapter								arrayAdapter				= new Panel_3_Calendars_Active_Adapter(Global.actContext, R.id.listView, Global.eRegCalendars.circuitList);
		((AdapterView <Panel_3_Calendars_Active_Adapter>) adapterView).setAdapter(arrayAdapter);
	}
	public void setListens()
	{
		((AdapterView <?>) adapterView).setOnItemClickListener(this);
	}
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
    	Ctrl_Calendars.Circuit									itemData					= Global.eRegCalendars.circuitList.get(position);
    	itemData.active																		= ! itemData.active;
    	displayContents();
    	setListens();
	}
	public void processFinishHTTP(Msg__Abstract result) 
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
}