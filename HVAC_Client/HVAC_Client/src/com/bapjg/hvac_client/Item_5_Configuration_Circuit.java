package com.bapjg.hvac_client;

import HVAC_Common.*;
import android.annotation.SuppressLint;
import android.app.Activity;
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

public class Item_5_Configuration_Circuit 						extends 					Panel_0_Fragment
{		
	private Ctrl_Configuration.Circuit	 						itemData;
	private ViewGroup											itemView;
	
	public Item_5_Configuration_Circuit(Ctrl_Configuration.Circuit itemData)
	{
		super();
		this.itemData																		= itemData;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        View 													itemView					= inflater.inflate(R.layout.item_5_configuration_circuit, container, false);
        this.itemView																		= (ViewGroup) itemView;

        if ((Global.eRegConfiguration != null)
        &&  (Global.eRegConfiguration.circuitList != null))
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
		((TextView) itemView.findViewById(R.id.title)).setText("Circuit Configuration");
		((TextView) itemView.findViewById(R.id.subTitle)).setText(itemData.name);
	}
	public void displayContents()
	{
    	TextView 												pumpName 					= (TextView) itemView.findViewById(R.id.pumpName);
    	TextView 												thermometerName				= (TextView) itemView.findViewById(R.id.thermometerName);
		TextView 												outsideLow 					= (TextView) itemView.findViewById(R.id.outsideLow);
    	TextView 												outsideHigh					= (TextView) itemView.findViewById(R.id.outsideHigh);
   		TextView 												tempLow 					= (TextView) itemView.findViewById(R.id.tempLow);
    	TextView 												tempHigh					= (TextView) itemView.findViewById(R.id.tempHigh);
   		TextView 												thermometer					= (TextView) itemView.findViewById(R.id.thermometer);
    	TextView 												swingTime					= (TextView) itemView.findViewById(R.id.swingTime);
   		TextView 												relayUp 					= (TextView) itemView.findViewById(R.id.relayUp);
    	TextView 												relayDown					= (TextView) itemView.findViewById(R.id.relayDown);
    	TextView 												thermometerPID				= (TextView) itemView.findViewById(R.id.thermometer);
    	TextView 												gainP						= (TextView) itemView.findViewById(R.id.gainP);
    	TextView 												timeD						= (TextView) itemView.findViewById(R.id.timeD);
    	TextView 												timeI						= (TextView) itemView.findViewById(R.id.timeI);
    	TextView 												timeDelay					= (TextView) itemView.findViewById(R.id.timeDelay);
    	TextView 												timeProjection				= (TextView) itemView.findViewById(R.id.timeProjection);
    	TextView 												marginProjection			= (TextView) itemView.findViewById(R.id.marginProjection);
	
    	pumpName.setText										(itemData.pump);
    	thermometerName.setText									(itemData.thermometer);
    	
    	if (itemData.tempGradient != null)
    	{
        	Ctrl_Configuration.TempGradient						tempGradient				= itemData.tempGradient;
    	
        	outsideLow.setText									(tempGradient.outsideLow.displayInteger());
        	outsideHigh.setText									(tempGradient.outsideHigh.displayInteger());
        	tempLow.setText										(tempGradient.tempLow.displayInteger());
        	tempHigh.setText									(tempGradient.tempHigh.displayInteger());
     	}
    	else
    	{
    		ViewGroup											gradientView				= (ViewGroup) itemView.findViewById(R.id.gradient);	
    		gradientView.setVisibility(View.GONE);
    	}
    	if (itemData.mixer != null)
    	{
        	Ctrl_Configuration.Mixer							mixer						= itemData.mixer;
     	
        	thermometer.setText									(mixer.name);
        	swingTime.setText									(((Integer) (mixer.swingTime/1000)).toString());
        	relayUp.setText										(mixer.relayUp);
        	relayDown.setText									(mixer.relayDown);

        	Ctrl_Configuration.PID_Params						pidParams					= itemData.mixer.pidParams;
    	
        	thermometerPID.setText								(pidParams.thermometer);
        	gainP.setText										(pidParams.gainP.toString());
        	timeD.setText										(pidParams.timeD.toString() + " ms/°C");
        	timeI.setText										(milliToString(pidParams.timeI) + " °C/ms");
        	timeDelay.setText									(milliToString(pidParams.timeDelay) + " s");
        	timeProjection.setText								(milliToString(pidParams.timeProjection) + " s");
        	marginProjection.setText							(milliToString(pidParams.marginProjection) + " °C");
    	}
    	else
    	{
    		ViewGroup											mixerView				= (ViewGroup) itemView.findViewById(R.id.mixer);	
    		mixerView.setVisibility(View.GONE);
    	}
   	}
	public void setListens()
	{
    	if (itemData.tempGradient != null)
    	{
			itemView.findViewById(R.id.tempLow).setOnClickListener(this);
			itemView.findViewById(R.id.tempHigh).setOnClickListener(this);
			itemView.findViewById(R.id.outsideHigh).setOnClickListener(this);
			itemView.findViewById(R.id.outsideLow).setOnClickListener(this);
			itemView.findViewById(R.id.thermometer).setOnClickListener(this);
			itemView.findViewById(R.id.swingTime).setOnClickListener(this);
			itemView.findViewById(R.id.relayUp).setOnClickListener(this);
			itemView.findViewById(R.id.relayDown).setOnClickListener(this);
    	}
	}
    @Override
	public void onClick(View clickedView) 
	{
    	Dialog_Temperature										dialogTemperature;
    	switch(clickedView.getId())
		{
	     	case R.id.tempLow:
	     		dialogTemperature 																= new Dialog_Temperature(itemData.tempGradient.tempLow,  -20, 50, this);
	     		dialogTemperature.show(getFragmentManager(), "Dialog_Temperature");
	      		break;
	     	case R.id.tempHigh:
	     		dialogTemperature																= new Dialog_Temperature(itemData.tempGradient.tempHigh,  -20, 50, this);
	     		dialogTemperature.show(getFragmentManager(), "Dialog_Temperature");
	      		break;
	     	case R.id.outsideHigh:
	     		dialogTemperature																= new Dialog_Temperature(itemData.tempGradient.outsideHigh,  -20, 50, this);
	     		dialogTemperature.show(getFragmentManager(), "Dialog_Temperature");
	      		break;
	     	case R.id.outsideLow:
	     		dialogTemperature 																= new Dialog_Temperature(itemData.tempGradient.outsideLow,  -20, 50, this);
	     		dialogTemperature.show(getFragmentManager(), "Dialog_Temperature");
	      		break;
	     	case R.id.thermometer:				//ie thermometerName
	    		Dialog_String_List		 						dialogThermometers				= new Dialog_String_List(itemData.mixer.name, (Object) itemData.mixer, null, this);

	    		for (Ctrl_Configuration.Thermometer thermometer : Global.eRegConfiguration.thermometerList)
	    		{
	    			dialogThermometers.items.add(thermometer.name);
	    		}
	    		dialogThermometers.show(getFragmentManager(), "Thermometer_List");
	      		break;
	     	case R.id.swingTime:
	      		Global.toaster("Its Ok swingTime", false);
	      		break;
	     	case R.id.relayUp:
	      		Global.toaster("Its Ok relayUp", false);
	      		break;
	     	case R.id.relayDown:
	      		Global.toaster("Its Ok relayDown", false);
	      		break;
		}
 	}
    public void onDialogReturn()
    {
    	displayContents();
    }
    public String milliToString(Long milli)
    {
    	Long		unitsLong = milli/1000;
    	return ((Integer) unitsLong.intValue()).toString();
    }
    public String milliToString(Integer milli)
    {
    	Integer		unitsInteger = milli/1000;
    	return unitsInteger.toString();
    }
    public String milliToString(Float milli)
    {
    	Float		unitsFloat = milli/1000;
    	return ((Integer) unitsFloat.intValue()).toString();
    }
}

