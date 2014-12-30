package com.bapjg.hvac_client;

import HVAC_Common.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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

public class Item_5_Configuration_Thermometer 					extends 					Panel_0_Fragment
{		
	private Ctrl_Configuration.Thermometer 						itemData;
	private ViewGroup											itemView;
	
	public Item_5_Configuration_Thermometer(Ctrl_Configuration.Thermometer itemData)
	{
		super();
		this.itemData																		= itemData;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        View 													itemView					= inflater.inflate(R.layout.item_5_configuration_thermometer, container, false);
        this.itemView																		= (ViewGroup) itemView;
 
        if ((Global.eRegConfiguration != null)
        &&  (Global.eRegConfiguration.thermometerList != null))
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
		((TextView) itemView.findViewById(R.id.title)).setText		("Configuration");
		((TextView) itemView.findViewById(R.id.subTitle)).setText	("Thermometre");
	}
	public void displayContents()
	{
    	TextView 												thermoName 					= (TextView) itemView.findViewById(R.id.thermoName);
    	TextView 												address						= (TextView) itemView.findViewById(R.id.address);
		TextView 												pidName				 		= (TextView) itemView.findViewById(R.id.pidName);
 	
		thermoName.setText										(itemData.name);
		address.setText											(itemData.address);
		pidName.setText											(itemData.pidName);
   	}
	public void setListens()
	{
    	if (itemData != null)
    	{
			itemView.findViewById(R.id.thermoName).setOnClickListener(this);
			itemView.findViewById(R.id.address).setOnClickListener(this);
			itemView.findViewById(R.id.pidName).setOnClickListener(this);
			itemView.findViewById(R.id.buttonOk).setOnClickListener(this);
			itemView.findViewById(R.id.buttonDelete).setOnClickListener(this);
    	}
	}
    @Override
	public void onClick(View clickedView) 
	{
       	Dialog_Text												dialogText;
    	Dialog_Integer											dialogInteger;
    	Dialog_String_List										dialogList;
     	switch(clickedView.getId())
		{
			case R.id.thermoName:
				// TODO Do Text Dialog
				dialogText 																		= new Dialog_Text(itemData.name, itemData, "Enter Thermometer Name", this);
				dialogText.show(getFragmentManager(), "Dialog_Text");
	      		break;
	     	case R.id.address:
	     		dialogText 																		= new Dialog_Text(itemData.address, itemData, "Enter Thermometer Address", this);
	     		dialogText.show(getFragmentManager(), "Dialog_Text");
	      		break;
	     	case R.id.pidName:
	     		dialogList 																		= new Dialog_String_List(itemData.pidName, itemData, null, this);
	     		dialogList.itemSelected															= "";
	
	    		for (Ctrl_Configuration.PID_Data pid : Global.eRegConfiguration.pidList)
	    		{
	    			dialogList.items.add(pid.name);
	    		}
	    		dialogList.show(getFragmentManager(), "Dialog_List");
	      		break;
	     	case R.id.buttonDelete:
	     		Global.eRegConfiguration.thermometerList.remove(itemData);
	      		// Just fall through
	     	case R.id.buttonOk:
	      		getFragmentManager().popBackStackImmediate();
	      		break;
		}
 	}
    public void onDialogReturn()
    {
    	displayContents();
    }
}

