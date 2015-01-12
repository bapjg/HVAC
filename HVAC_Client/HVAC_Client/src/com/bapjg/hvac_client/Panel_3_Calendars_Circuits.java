package com.bapjg.hvac_client;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("ValidFragment")
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Panel_3_Calendars_Circuits 					extends 					Panel_0_Fragment
																implements					AdapterView.OnItemClickListener	
{
	public String												circuitName;
	
    public Panel_3_Calendars_Circuits(String circuitName)
    {
		super("Add");
		this.circuitName																	= circuitName;
   }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);

    	Element_ListView										listView 					= new Element_ListView("Henry");
    	panelInsertPoint.addView(listView);

    	displayTitles("Calendars", this.circuitName);

        this.adapterView																	= (AdapterView) panelView.findViewById(R.id.listView);
        
        if ((Global.eRegCalendars != null)
        &&  (Global.eRegCalendars.circuitList != null))
        {
        	displayContents();
        	setListens();
        }
        else // we need to reconnect to the server
        {
        	Global.toaster("Please wait or refresh if necessary", true);
        }
        return panelView;
    }
	public void displayContents()
	{
        Ctrl_Calendars.Circuit									circuit						= Global.eRegCalendars.fetchCircuit(this.circuitName);
        Panel_3_Calendars_Circuits_Adapter							arrayAdapter				= new Panel_3_Calendars_Circuits_Adapter(Global.actContext, R.id.listView, circuit.calendarList);
		((AdapterView <Panel_3_Calendars_Circuits_Adapter>) adapterView).setAdapter(arrayAdapter);
	}
	public void setListens()
	{
		((AdapterView <?>) adapterView).setOnItemClickListener(this);
	}
	@Override
    public void onClick(View clickedView)
	{
		super.onClick(clickedView);
	}
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
    	Ctrl_Calendars.Calendar									itemData					= Global.eRegCalendars.fetchCircuit(this.circuitName).calendarList.get(position);

    	Panel_3_Calendars_Circuits_Item					itemFragment				= new Panel_3_Calendars_Circuits_Item(itemData, this.circuitName);
 
    	FragmentTransaction 									fTransaction 				= getActivity().getFragmentManager().beginTransaction();
   		fTransaction.replace(R.id.panel_container, itemFragment);
   		fTransaction.addToBackStack(null);
   		fTransaction.commit();
	}
    @Override 
    public void onPanelButtonAdd()
    {
		Ctrl_Calendars.Calendar								itemNew						= new Ctrl_Calendars().new Calendar();

		itemNew.days																	= "";
		itemNew.timeStart																= new Cmn_Time("09:00");
		itemNew.timeEnd																	= new Cmn_Time("10:00");
		itemNew.tempObjective															= new Cmn_Temperature("30");
		itemNew.stopOnObjective															= true;

		Global.eRegCalendars.fetchCircuit(this.circuitName).calendarList.add(itemNew);
		displayContents();
		setListens();
    }
    public void onPanelButtonDelete()
    {
    	
    }
    public void onPanelButtonOk()
    {
    	
    }

}

