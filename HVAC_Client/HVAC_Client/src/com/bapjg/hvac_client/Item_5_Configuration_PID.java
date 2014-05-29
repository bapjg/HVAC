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

public class Item_5_Configuration_PID 						extends 					Panel_0_Fragment
{		
	private Ctrl_Configuration.PID_Data	 						itemData;
	private ViewGroup											itemView;
	
	public Item_5_Configuration_PID(Ctrl_Configuration.PID_Data itemData)
	{
		super();
		this.itemData																		= itemData;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        View 													itemView					= inflater.inflate(R.layout.item_5_configuration_pid, container, false);
        this.itemView																		= (ViewGroup) itemView;

        if ((Global.eRegConfiguration != null)
        &&  (Global.eRegConfiguration.pidList != null))
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
    	TextView 												pidName 					= (TextView) itemView.findViewById(R.id.pidName);
    	TextView 												depth						= (TextView) itemView.findViewById(R.id.depth);
		TextView 												sampleIncrement 			= (TextView) itemView.findViewById(R.id.sampleIncrement);
 	
		pidName.setText											(itemData.name);
		depth.setText											(itemData.depth.toString());
		sampleIncrement.setText									(itemData.sampleIncrement.toString());
   	}
	public void setListens()
	{
    	if (itemData != null)
    	{
			itemView.findViewById(R.id.pidName).setOnClickListener(this);
			itemView.findViewById(R.id.depth).setOnClickListener(this);
			itemView.findViewById(R.id.sampleIncrement).setOnClickListener(this);
    	}
	}
    @Override
	public void onClick(View clickedView) 
	{
    	Dialog_Integer dialog ;
    	switch(clickedView.getId())
		{
			case R.id.pidName:
				// TODO Do Text Dialog
//	     		dialogTemperature 																= new Dialog_Temperature(itemData.tempGradient.tempLow,  -20, 50, this);
//	     		dialogTemperature.show(getFragmentManager(), "Dialog_Temperature");
	      		break;
	     	case R.id.depth:
	     		dialog																			= new Dialog_Integer(itemData.depth,  itemData, 1, 100, "Select PID Depth", this);
	     		dialog.show(getFragmentManager(), "Dialog_Depth");
	      		break;
	     	case R.id.sampleIncrement:
	     		dialog																			= new Dialog_Integer(itemData.sampleIncrement,  itemData, 1, 300, "Select Sample (unit = 10s)", this);
	     		dialog.show(getFragmentManager(), "Dialog_SampleIncrement");
	      		break;
		}
 	}
    public void onDialogReturn()
    {
    	displayContents();
    }
}

