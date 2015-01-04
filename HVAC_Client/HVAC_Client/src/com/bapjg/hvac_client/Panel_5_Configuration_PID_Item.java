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
public class Panel_5_Configuration_PID_Item 					extends 					Panel_0_Fragment
{		
	private Ctrl_Configuration.PID_Data	 						itemData;
	
	private Element_Heading										headingGeneral;
	private Element_Standard									pidName;
	private Element_Standard									depth;
	private Element_Standard									sampleIncrement;
	private View												buttonOk;
	private View												buttonDelete;
	
	public Panel_5_Configuration_PID_Item(Ctrl_Configuration.PID_Data itemData)
	{
		super("Ok_Delete");
		this.itemData																		= itemData;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);
    	
    	headingGeneral			 															= new Element_Heading("Parameters");
    	pidName																				= new Element_Standard("PID Name");
    	depth																				= new Element_Standard("Depth");
    	sampleIncrement																		= new Element_Standard("Sample Increment", "s");
    	
    	panelInsertPoint.addView(headingGeneral);
    	panelInsertPoint.addView(pidName);
    	panelInsertPoint.addView(depth);
    	panelInsertPoint.addView(sampleIncrement);
    	
    	displayTitles("Configuration", "PID");
    	
    	if ((Global.eRegConfiguration != null)
        &&  (Global.eRegConfiguration.pidList != null))
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
		pidName					.setTextRight	(itemData.name);
		depth					.setTextRight	(itemData.depth);
		sampleIncrement			.setTextRight	(itemData.sampleIncrement);
   	}
	public void setListens()
	{
    	if (itemData != null)
    	{
    		pidName				.setListener(this);
    		depth				.setListener(this);
    		sampleIncrement		.setListener(this);
    	}
	}
    @Override
	public void onElementClick(View clickedView) 
	{
    	Dialog_Text												dialogText;
    	Dialog_Integer											dialogInteger;
    	Dialog_String_List										dialogList;
    	
    	if (clickedView == pidName)
		{
			dialogText 																	= new Dialog_Text(itemData.name, itemData,  "Choose PID Name", this);
	     	dialogText.show(getFragmentManager(), "Dialog_Temperature");
		}
    	else if (clickedView == depth)
    	{
	     	dialogInteger																= new Dialog_Integer(itemData.depth,  itemData, 1, 100, "Select PID Depth", this);
	     	dialogInteger.show(getFragmentManager(), "Dialog_Depth");
		}
    	else if (clickedView == sampleIncrement)
    	{
	     	dialogInteger																= new Dialog_Integer(itemData.sampleIncrement,  itemData, 1, 300, "Select Sample (unit = 10s)", this);
	     	dialogInteger.show(getFragmentManager(), "Dialog_SampleIncrement");
		}
 	}
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
    	Global.eRegConfiguration.pidList.remove(itemData);
    	getFragmentManager().popBackStackImmediate();
    }
    public void onDialogReturn()
    {
    	displayContents();
    }
}

