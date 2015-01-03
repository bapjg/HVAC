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

public class Panel_5_Configuration_PID_Item 						extends 					Panel_0_Fragment
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
		super();
		this.itemData																		= itemData;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	this.container																		= container;
		this.panelView																		= inflater.inflate(R.layout.panal_0_standard_with_buttons_ok_delete, container, false);
		this.buttonOk																		= this.panelView.findViewById(R.id.buttonOk);
		this.buttonDelete																	= this.panelView.findViewById(R.id.buttonDelete);

    	LinearLayout insertPoint 															= (LinearLayout) panelView.findViewById(R.id.base_insert_point);
    	
    	headingGeneral			 															= new Element_Heading(getActivity(), "Parameters");
    	pidName																				= new Element_Standard(getActivity(), "PID Name");
    	depth																				= new Element_Standard(getActivity(), "Depth");
    	sampleIncrement																		= new Element_Standard(getActivity(), "Sample Increment", "s");
    	
    	insertPoint.addView(headingGeneral);
    	insertPoint.addView(pidName);
    	insertPoint.addView(depth);
    	insertPoint.addView(sampleIncrement);
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
    		pidName				.setOnClickListener(this);
    		depth				.setOnClickListener(this);
    		sampleIncrement		.setOnClickListener(this);
    		buttonOk			.setOnClickListener(this);
    		buttonDelete		.setOnClickListener(this);
    	}
	}
    @Override
	public void onClick(View clickedView) 
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
    	else if (clickedView == buttonDelete)
    	{
	     	Global.eRegConfiguration.pidList.remove(itemData);
	     	getFragmentManager().popBackStackImmediate();
		}
    	else if (clickedView == buttonOk)
    	{
	      	getFragmentManager().popBackStackImmediate();
		}
 	}
    public void onDialogReturn()
    {
    	displayContents();
    }
}

