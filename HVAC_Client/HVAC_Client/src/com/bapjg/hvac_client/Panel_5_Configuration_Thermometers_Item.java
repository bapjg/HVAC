package com.bapjg.hvac_client;

import HVAC_Common.*;
import HVAC_Common.Ctrl_Configuration.Thermometer;
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
public class Panel_5_Configuration_Thermometers_Item 			extends 					Panel_0_Fragment
{		
	private Ctrl_Configuration.Thermometer 						itemData;

	private Element_Heading										headingGeneral;
	private Element_Standard									thermoName;
	private Element_Standard									address;
	private Element_Standard									pidName;

	private View												buttonOk;
	private View												buttonDelete;

	public Panel_5_Configuration_Thermometers_Item(Ctrl_Configuration.Thermometer itemData)
	{
		super("Ok_Delete");
		this.itemData																		= itemData;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);

    	headingGeneral			 															= new Element_Heading("Parameters");
    	thermoName																			= new Element_Standard("Thermometer Name");
    	address																				= new Element_Standard("Address");
    	pidName																				= new Element_Standard("PID Name");
    	
    	panelInsertPoint.addView(headingGeneral);
    	panelInsertPoint.addView(thermoName);
    	panelInsertPoint.addView(address);
    	panelInsertPoint.addView(pidName);
        
    	displayTitles("Configuration", "Thermometer");      
    	
        if ((Global.eRegConfiguration != null)
        &&  (Global.eRegConfiguration.thermometerList != null))
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
		thermoName						.setValue		(itemData.name);
		address							.setValue		(itemData.address);
		pidName							.setValue		(itemData.pidName);
   	}
	public void setListens()
	{
    	if (itemData != null)
    	{
    		thermoName					.setListener(this);
    		address						.setListener(this);
    		pidName						.setListener(this);
    	}
	}
    @Override
	public void onElementClick(View clickedView) 
	{
       	Dialog_Text												dialogText;
    	Dialog_Integer_Spinner									dialogInteger;
    	Dialog_String_List										dialogList;
     	if (clickedView == thermoName)
     	{
			// TODO Do Text Dialog
			dialogText 																		= new Dialog_Text(itemData.name, itemData, "Enter Thermometer Name", this);
			dialogText.show(getFragmentManager(), "Dialog_Text");
     	}
     	else if (clickedView == address)
     	{
     		dialogText 																		= new Dialog_Text(itemData.address, itemData, "Enter Thermometer Address", this);
     		dialogText.show(getFragmentManager(), "Dialog_Text");
     	}
     	else if (clickedView == pidName)
     	{
     		dialogList 																		= new Dialog_String_List(itemData.pidName, itemData, null, this);
     		dialogList.itemSelected															= "";

    		for (Ctrl_Configuration.PID_Data pid : Global.eRegConfiguration.pidList)
    		{
    			dialogList.items.add(pid.name);
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
    	Global.eRegConfiguration.thermometerList.remove(itemData);
    	getFragmentManager().popBackStackImmediate();
    }
    @Override
    public void onDialogReturn()
    {
    	displayContents();
    }
}

