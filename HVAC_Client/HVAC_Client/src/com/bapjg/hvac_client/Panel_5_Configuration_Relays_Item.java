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
//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Panel_5_Configuration_Relays_Item 					extends 					Panel_0_Fragment
{		
	private Ctrl_Configuration.Relay	 						itemData;
	
	private Element_Heading										headingGeneral;
	private Element_Standard									relayName;
	private Element_Standard									relayBank;
	private Element_Standard									relayNumber;
	private Element_Standard									relayChannelGPIO;

	private View												buttonOk;
	private View												buttonDelete;
	
	public Panel_5_Configuration_Relays_Item(Ctrl_Configuration.Relay itemData)
	{
		super("Ok_Delete");
		this.itemData																		= itemData;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);

    	headingGeneral			 															= new Element_Heading("Parameters");
    	relayName																			= new Element_Standard("Relay Name");
    	relayBank																			= new Element_Standard("Bank number");
    	relayNumber																			= new Element_Standard("Address");
    	relayChannelGPIO																	= new Element_Standard("Channel GPIO");
    	
    	panelInsertPoint.addView(headingGeneral);
    	panelInsertPoint.addView(relayName);
    	panelInsertPoint.addView(relayBank);
    	panelInsertPoint.addView(relayNumber);
    	panelInsertPoint.addView(relayChannelGPIO);
        
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
        return panelView;
    }
	public void displayContents()
	{
		relayName							.setValue	(itemData.name);
		relayBank							.setValue	(itemData.relayBank);
		relayNumber							.setValue	(itemData.relayNumber);
		relayChannelGPIO					.setValue	(itemData.gpioChannel);
   	}
	public void setListens()
	{
    	if (itemData != null)
    	{
    		relayName						.setListener(this);
			relayBank						.setListener(this);
			relayNumber						.setListener(this);
			relayChannelGPIO				.setListener(this);
    	}
	}
    @Override
	public void onElementClick(View clickedView) 
	{
    	Dialog_Text												dialogText;
    	Dialog_Integer_Spinner									dialogInteger;
    	Dialog_String_List										dialogList;

    	if (clickedView == relayName)
    	{
			dialogText 																		= new Dialog_Text(itemData.name, itemData, "Enter Relay Name", this);
			dialogText.show(getFragmentManager(), "Dialog_Text");
    	}
    	else if (clickedView == relayBank)
    	{
     		dialogInteger 																	= new Dialog_Integer_Spinner(itemData.relayBank, itemData, 0, 4, "Enter Relay Bank", this);
     		dialogInteger.show(getFragmentManager(), "Dialog_Integer");
    	}
    	else if (clickedView == relayNumber)
    	{
     		dialogInteger 																	= new Dialog_Integer_Spinner(itemData.relayNumber, itemData, 0, 8, "Enter Relay Number", this);
     		dialogInteger.show(getFragmentManager(), "Dialog_Integer");
    	}
    	else if (clickedView == relayChannelGPIO)
    	{
     		if (itemData.gpioChannel == null)					itemData.gpioChannel		= 0;
    		dialogInteger 																	= new Dialog_Integer_Spinner(itemData.gpioChannel, itemData, 0, 99, "Enter GPIO Channel", this);
     		dialogInteger.show(getFragmentManager(), "Dialog_Integer");
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

