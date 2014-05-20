package com.bapjg.hvac_client;

import HVAC_Messages.*;
import HVAC_Messages.Ctrl_Calendars.Word;
import HVAC_Messages.Ctrl_Configuration.Request;
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
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("ValidFragment")
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------

public class Item_5_Configuration_Circuit 						extends 					Panel_0_Fragment
{		
	private Ctrl_Configuration.Circuit	 						itemData;
	private Ctrl_Configuration.Circuit 							itemDataWork;
	private ViewGroup											itemView;
	
	public Item_5_Configuration_Circuit(Ctrl_Configuration.Circuit itemData)
	{
		super();
		this.itemData																		= itemData;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        View 													itemView					= inflater.inflate(R.layout.item_5_configuration_circuit, container, false);
        this.itemView																		= (ViewGroup) itemView;

        if ((Global.eRegConfiguration != null)
        &&  (Global.eRegConfiguration.circuitList != null))
        {
        	displayHeader();
        	displayContents();
            setListens();
        }
        else // we need to reconnect to the server
        {
            Global.toaster("Please wait for data to arrive", false);
        }
        
        return itemView;
    }
	public void displayHeader()
	{
	}
	public void displayContents()
	{
    	TextView 										pumpName 					= (TextView) itemView.findViewById(R.id.pumpName);
    	TextView 										thermometerName				= (TextView) itemView.findViewById(R.id.thermometerName);

    	pumpName.setText								(itemData.pump);
    	thermometerName.setText							(itemData.thermometer);
   	}
	public void setListens()
	{
//		itemView.findViewById(R.id.buttonOk).setOnClickListener(this);
//		itemView.findViewById(R.id.buttonDelete).setOnClickListener(this);
//	    itemView.findViewById(R.id.dateStart).setOnClickListener(this);
//	    itemView.findViewById(R.id.dateEnd).setOnClickListener(this);
//	    itemView.findViewById(R.id.timeStart).setOnClickListener(this);
//	    itemView.findViewById(R.id.timeEnd).setOnClickListener(this);
	}
    @Override
	public void onClick(View clickedView) 
	{
     	if (clickedView.getId() == R.id.buttonOk)
    	{
//     		itemData.name															= ((EditText) itemView.findViewById(R.id.name)).getText().toString();
      		getFragmentManager().popBackStackImmediate();
    	}
     	else if (clickedView.getId() == R.id.buttonDelete)
    	{
     		Global.eRegCalendars.awayList.remove(itemData);
     		getFragmentManager().popBackStackImmediate();
    	}
    	else if (clickedView.getId() == R.id.dateStart)
    	{
     		// TODO
    		getFragmentManager().popBackStackImmediate();
    	}
    	else if (clickedView.getId() == R.id.timeStart)
    	{
     		// TODO
      		getFragmentManager().popBackStackImmediate();
    	}
    	else if (clickedView.getId() == R.id.dateEnd)
    	{
     		// TODO
     		getFragmentManager().popBackStackImmediate();
    	}
    	else if (clickedView.getId() == R.id.timeEnd)
    	{
     		// TODO
     		getFragmentManager().popBackStackImmediate();
    	}
 	}
}

