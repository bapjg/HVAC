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

public class Item_5_Configuration_Pump 							extends 					Panel_0_Fragment
{		
	private Ctrl_Configuration.Pump	 							itemData;
	private ViewGroup											itemView;
	
	public Item_5_Configuration_Pump(Ctrl_Configuration.Pump itemData)
	{
		super();
		this.itemData																		= itemData;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        View 													itemView					= inflater.inflate(R.layout.item_5_configuration_pump, container, false);
        this.itemView																		= (ViewGroup) itemView;

        if ((Global.eRegConfiguration != null)
        &&  (Global.eRegConfiguration.pumpList != null))
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
		// TODO		((TextView) itemView.findViewById(R.id.pidName)).setText(itemData.name);
	}
	public void displayContents()
	{
    	TextView 												pumpName 					= (TextView) itemView.findViewById(R.id.pumpName);
    	TextView 												relayName					= (TextView) itemView.findViewById(R.id.relayName);
 	
    	pumpName.setText										(itemData.name);
    	relayName.setText										(itemData.relay);
   	}
	public void setListens()
	{
    	if (itemData != null)
    	{
			itemView.findViewById(R.id.pumpName).setOnClickListener(this);
			itemView.findViewById(R.id.relayName).setOnClickListener(this);
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
			case R.id.pumpName:
				// TODO Do Text Dialog
				dialogText 																		= new Dialog_Text(itemData.name, itemData, "Enter Pump Name", this);
				dialogText.show(getFragmentManager(), "Dialog_Text");
	      		break;
	     	case R.id.relayName:
	     		dialogList 																		= new Dialog_String_List(itemData.relay, itemData, null, this);
	     		dialogList.itemSelected															= "";
	
	    		for (Ctrl_Configuration.Relay relay : Global.eRegConfiguration.relayList)
	    		{
	    			dialogList.items.add(relay.name);
	    		}
	    		dialogList.show(getFragmentManager(), "Dialog_List");
	      		break;
	     	case R.id.buttonDelete:
	     		Global.eRegConfiguration.pumpList.remove(itemData);
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

