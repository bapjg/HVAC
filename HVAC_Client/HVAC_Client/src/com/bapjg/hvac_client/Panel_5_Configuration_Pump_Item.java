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

public class Panel_5_Configuration_Pump_Item 					extends 					Panel_0_Fragment
{		
	private Ctrl_Configuration.Pump	 							itemData;

	private Element_Heading										headingGeneral;
	private Element_Standard									pumpName;
	private Element_Standard									relayName;

	public Panel_5_Configuration_Pump_Item(Ctrl_Configuration.Pump itemData)
	{
		super("Ok_Delete");
		this.itemData																		= itemData;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);
    	
    	headingGeneral			 															= new Element_Heading("Parameters");
    	pumpName																			= new Element_Standard("Pump Name");
    	relayName																			= new Element_Standard("Relay number");
    	
    	panelInsertPoint.addView(headingGeneral);
    	panelInsertPoint.addView(pumpName);
    	panelInsertPoint.addView(relayName);

    	displayTitles("Configuration", "Pump");
    	
        if ((Global.eRegConfiguration != null)
        &&  (Global.eRegConfiguration.pumpList != null))
        {
        	displayContents();
            setListens();
        }
        else // we need to reconnect to the server
        {
            Global.toaster("Please wait for data to arrive", false);
        }
        return panelView;
    }
	public void displayContents()
	{
    	pumpName							.setTextRight	(itemData.name);
    	relayName							.setTextRight	(itemData.relay);
   	}
	public void setListens()
	{
    	if (itemData != null)
    	{
    		pumpName						.setListener(this);
    		relayName						.setListener(this);
    	}
	}
    @Override
	public void onElementClick(View clickedView) 
	{
    	Dialog_Text												dialogText;
    	Dialog_Integer											dialogInteger;
    	Dialog_String_List										dialogList;
    	if (clickedView == pumpName)
    	{
			// TODO Do Text Dialog
			dialogText 																		= new Dialog_Text(itemData.name, itemData, "Enter Pump Name", this);
			dialogText.show(getFragmentManager(), "Dialog_Text");
    	}
    	else if (clickedView == relayName)
    	{
     		dialogList 																		= new Dialog_String_List(itemData.relay, itemData, null, this);
     		dialogList.itemSelected															= "";

    		for (Ctrl_Configuration.Relay relay : Global.eRegConfiguration.relayList)
    		{
    			dialogList.items.add(relay.name);
    		}
    		dialogList.show(getFragmentManager(), "Dialog_List");
		}
 	}
    @Override
 	public void onPanelButtonOk()
    {
    	getFragmentManager().popBackStackImmediate();
    }
    @Override
 	public void onPanelButtonAdd()
    {
     	Global.toaster("Invalid button in this situation", true);
     	getFragmentManager().popBackStackImmediate();
    }
    @Override
 	public void onPanelButtonDelete()
    {
    	Global.eRegConfiguration.relayList.remove(itemData);
    	getFragmentManager().popBackStackImmediate();
    }
    public void onDialogReturn()
    {
    	displayContents();
    }
}

