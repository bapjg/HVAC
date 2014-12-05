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

public class Item_5_Configuration_Relay 						extends 					Panel_0_Fragment
{		
	private Ctrl_Configuration.Relay	 						itemData;
	private ViewGroup											itemView;
	
	public Item_5_Configuration_Relay(Ctrl_Configuration.Relay itemData)
	{
		super();
		this.itemData																		= itemData;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        View 													itemView					= inflater.inflate(R.layout.item_5_configuration_relay, container, false);
        this.itemView																		= (ViewGroup) itemView;

        if ((Global.eRegConfiguration != null)
        &&  (Global.eRegConfiguration.relayList != null))
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
		((TextView) itemView.findViewById(R.id.subTitle)).setText	("Relay");
	}
	public void displayContents()
	{
    	TextView 												relayName 					= (TextView) itemView.findViewById(R.id.relayName);
    	TextView 												relayBank					= (TextView) itemView.findViewById(R.id.relayBank);
		TextView 												relayNumber					= (TextView) itemView.findViewById(R.id.relayNumber);
 	
		relayName.setText										(itemData.name);
		relayBank.setText										(itemData.relayBank.toString());
		relayNumber.setText										(itemData.relayNumber.toString());
   	}
	public void setListens()
	{
    	if (itemData != null)
    	{
			itemView.findViewById(R.id.relayName).setOnClickListener(this);
			itemView.findViewById(R.id.relayBank).setOnClickListener(this);
			itemView.findViewById(R.id.relayNumber).setOnClickListener(this);
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
			case R.id.relayName:
				// TODO Do Text Dialog
				dialogText 																		= new Dialog_Text(itemData.name, itemData, "Enter Relay Name", this);
				dialogText.show(getFragmentManager(), "Dialog_Text");
	      		break;
	     	case R.id.relayBank:
	     		dialogInteger 																	= new Dialog_Integer(itemData.relayBank, itemData, 0, 4, "Enter Relay Bank", this);
	     		dialogInteger.show(getFragmentManager(), "Dialog_Integer");
	      		break;
	     	case R.id.relayNumber:
	     		dialogInteger 																	= new Dialog_Integer(itemData.relayNumber, itemData, 0, 8, "Enter Relay Number", this);
	     		dialogInteger.show(getFragmentManager(), "Dialog_Integer");
	      		break;
	     	case R.id.buttonDelete:
	     		Global.eRegConfiguration.relayList.remove(itemData);
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

