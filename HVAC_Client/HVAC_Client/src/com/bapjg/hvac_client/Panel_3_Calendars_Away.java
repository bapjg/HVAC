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
public class Panel_3_Calendars_Away 							extends 					Panel_0_Fragment 
{
	public Panel_3_Calendars_Away()
	{
		super("Add");
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);

    	Element_Heading											listHeading					= new Element_Heading("Start", "End");
    	listHeading.centerColumns();
    	
    	Element_ListView										listView 					= new Element_ListView("Henry");
    	panelInsertPoint.addView(listHeading);
    	panelInsertPoint.addView(listView);

        this.adapterView																	= (AdapterView) panelView.findViewById(R.id.List_View);

    	displayTitles("Calendars", "Away List");
    	
        if ((Global.eRegCalendars != null)
        &&  (Global.eRegCalendars.awayList != null))
        {
        	displayContents();
        	setListens();
        }
        else // we need to reconnect to the server
        {
            Global.toaster("please refresh", false);;
        }
        return panelView;
    }
	public void displayContents()
	{
//	    AdapterView <Panel_3_Calendars_Away_Adapter_Work>				adapterViewList				= (AdapterView <Panel_3_Calendars_Away_Adapter_Work>) adapterView;
	    Panel_3_Calendars_Away_Adapter_Work								arrayAdapter				= new Panel_3_Calendars_Away_Adapter_Work(Global.actContext, R.id.List_View, Global.eRegCalendars.awayList);
//		adapterViewList.setAdapter(arrayAdapter);
	    ((AdapterView <Panel_3_Calendars_Away_Adapter_Work>) adapterView).setAdapter(arrayAdapter);
	}
	public void setListens()
	{
		((AdapterView <Panel_3_Calendars_Away_Adapter_Work>) adapterView).setOnItemClickListener(this);
	}
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
    	Ctrl_Calendars.Away										itemData					= Global.eRegCalendars.awayList.get(position);
		
    	Panel_3_Calendars_Away_Item								itemFragment				= new Panel_3_Calendars_Away_Item(itemData);
		
    	FragmentTransaction 									fTransaction 				= getActivity().getFragmentManager().beginTransaction();
   		fTransaction.replace(R.id.panel_container, itemFragment);
   		fTransaction.addToBackStack(null);
   		fTransaction.commit();
	}
	public void onPanelButtonAdd()
    {
		Ctrl_Calendars.Away									itemNew						= new Ctrl_Calendars().new Away();
		
		itemNew.dateTimeStart															= Global.now();
		itemNew.dateTimeEnd																= Global.now() + 24 * 60 * 60 * 1000L;
		
		Global.eRegCalendars.awayList.add(itemNew);
		displayContents();
		setListens();
	}
}