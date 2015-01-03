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
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ValidFragment")
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------

public class Panel_5_Configuration_Relay_Item 					extends 					Panel_0_Fragment
{		
	private Ctrl_Configuration.Relay	 						itemData;
	private ViewGroup											itemView;
	
	
	
	private Element_Heading										headingGeneral;
	private Element_Standard									relayName;
	private Element_Standard									relayBank;
	private Element_Standard									relayNumber;

	private View												buttonOk;
	private View												buttonDelete;
	
	public Panel_5_Configuration_Relay_Item(Ctrl_Configuration.Relay itemData)
	{
		super();
		this.itemData																		= itemData;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
//        View 													itemView					= inflater.inflate(R.layout.item_5_configuration_relay, container, false);
//        this.itemView																		= (ViewGroup) itemView;

    	this.container																		= container;
		this.panelView																		= inflater.inflate(R.layout.panal_0_standard_with_buttons_ok_delete, container, false);
		this.buttonOk																		= this.panelView.findViewById(R.id.buttonOk);
		this.buttonDelete																	= this.panelView.findViewById(R.id.buttonDelete);

    	LinearLayout insertPoint 															= (LinearLayout) panelView.findViewById(R.id.base_insert_point);

    	headingGeneral			 															= new Element_Heading(getActivity(), "Parameters");
    	relayName																			= new Element_Standard(getActivity(), "Relay Name");
    	relayBank																			= new Element_Standard(getActivity(), "Bank number");
    	relayNumber																			= new Element_Standard(getActivity(), "Address");
    	
    	insertPoint.addView(headingGeneral);
    	insertPoint.addView(relayName);
    	insertPoint.addView(relayBank);
    	insertPoint.addView(relayNumber);
        
    	displayTitles("Configuration", "Relay");      
        
        if ((Global.eRegConfiguration != null)
        &&  (Global.eRegConfiguration.relayList != null))
        {
        	displayContents();
            setListens();
        }
        else // we need to reconnect to the server
        {
            Global.toaster("Please wait for data to arrive", false);
        }
        return itemView;
    }
	public void displayContents()
	{
		relayName							.setTextRight	(itemData.name);
		relayBank							.setTextRight	(itemData.relayBank);
		relayNumber							.setTextRight	(itemData.relayNumber);
   	}
	public void setListens()
	{
    	if (itemData != null)
    	{
    		relayName						.setListener(this);
			relayBank						.setListener(this);
			relayNumber						.setListener(this);
			buttonOk						.setOnClickListener(this);
			buttonDelete					.setOnClickListener(this);
    	}
	}
    @Override
	public void onClick(View clickedView) 
	{
    	Dialog_Text												dialogText;
    	Dialog_Integer											dialogInteger;
    	Dialog_String_List										dialogList;

    	if (clickedView == relayName)
    	{
			dialogText 																		= new Dialog_Text(itemData.name, itemData, "Enter Relay Name", this);
			dialogText.show(getFragmentManager(), "Dialog_Text");
    	}
    	else if (clickedView == relayBank)
    	{
     		dialogInteger 																	= new Dialog_Integer(itemData.relayBank, itemData, 0, 4, "Enter Relay Bank", this);
     		dialogInteger.show(getFragmentManager(), "Dialog_Integer");
    	}
    	else if (clickedView == relayNumber)
    	{
     		dialogInteger 																	= new Dialog_Integer(itemData.relayNumber, itemData, 0, 8, "Enter Relay Number", this);
     		dialogInteger.show(getFragmentManager(), "Dialog_Integer");
    	}
    	
    	
    	
    	
    	
    	switch(clickedView.getId())
		{
//			case R.id.relayName:
//				// TODO Do Text Dialog
//				dialogText 																		= new Dialog_Text(itemData.name, itemData, "Enter Relay Name", this);
//				dialogText.show(getFragmentManager(), "Dialog_Text");
//	      		break;
//	     	case R.id.relayBank:
//	     		dialogInteger 																	= new Dialog_Integer(itemData.relayBank, itemData, 0, 4, "Enter Relay Bank", this);
//	     		dialogInteger.show(getFragmentManager(), "Dialog_Integer");
//	      		break;
//	     	case R.id.relayNumber:
//	     		dialogInteger 																	= new Dialog_Integer(itemData.relayNumber, itemData, 0, 8, "Enter Relay Number", this);
//	     		dialogInteger.show(getFragmentManager(), "Dialog_Integer");
//	      		break;
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

