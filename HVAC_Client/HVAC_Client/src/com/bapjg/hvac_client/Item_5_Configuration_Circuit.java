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
        	displayTitles("Circuit Configuration", itemData.name);
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
    	// General :
		TextView 												pumpName 					= (TextView) itemView.findViewById(R.id.pumpName);
    	TextView 												thermometerName				= (TextView) itemView.findViewById(R.id.thermometerName);
    	// Temperature Gradient :
		TextView 												outsideLow 					= (TextView) itemView.findViewById(R.id.outsideLow);
    	TextView 												outsideHigh					= (TextView) itemView.findViewById(R.id.outsideHigh);
   		TextView 												tempLow 					= (TextView) itemView.findViewById(R.id.tempLow);
    	TextView 												tempHigh					= (TextView) itemView.findViewById(R.id.tempHigh);
    	// Mixer :
  		TextView 												thermometer					= (TextView) itemView.findViewById(R.id.thermometer);
    	TextView 												swingTime					= (TextView) itemView.findViewById(R.id.swingTime);
   		TextView 												relayUp 					= (TextView) itemView.findViewById(R.id.relayUp);
    	TextView 												relayDown					= (TextView) itemView.findViewById(R.id.relayDown);
    	// Low Level PID (Mixer) :
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

			itemView.findViewById(R.id.thermometer).setOnClickListener(this);
			itemView.findViewById(R.id.gainP).setOnClickListener(this);
			itemView.findViewById(R.id.timeD).setOnClickListener(this);
			itemView.findViewById(R.id.timeI).setOnClickListener(this);
			itemView.findViewById(R.id.timeDelay).setOnClickListener(this);
			itemView.findViewById(R.id.timeProjection).setOnClickListener(this);
			itemView.findViewById(R.id.marginProjection).setOnClickListener(this);
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
	     		Dialog_Integer									dialogSwingTime					= new Dialog_Integer(itemData.mixer.swingTime, (Object) itemData.mixer, 10, 200, "Define Proprtional Gain",	this);
	     		dialogSwingTime.show(getFragmentManager(), "Mixer Swing Time (ms)");
	     		break;
	     	case R.id.relayUp:
	      		Global.toaster("Its Ok relayUp", false);
	      		break;
	     	case R.id.relayDown:
	      		Global.toaster("Its Ok relayDown", false);
	      		break;
//	     	case R.id.thermometer:
//	      		break;
	     	case R.id.gainP:
	     		Dialog_Float									dialogGain 						= new Dialog_Float(		itemData.mixer.pidParams.gainP,
	     																												(Object) itemData.mixer.pidParams, 
	     																												"Define Proportional Gain (ms/°C)",
	     																												this);
	     		dialogGain.show(getFragmentManager(), "gainP");
	     		break;
	     	case R.id.timeD:
	     		Dialog_Float									dialogTimeD						= new Dialog_Float(		itemData.mixer.pidParams.timeD,
	     																												(Object) itemData.mixer.pidParams, 
	     																												"Differential Time Constant (ms/C)",
	     																												this);
	     		dialogTimeD.show(getFragmentManager(), "timeD");
	     		break;
	     	case R.id.timeI:
	     		Dialog_Float									dialogTimeIntegration			= new Dialog_Float(		itemData.mixer.pidParams.timeI, 
	     																												(Object) itemData.mixer.pidParams, 
	     																												"Time Integration Factor (°C/ms)",	
	     																												this);
	     		dialogTimeIntegration.show(getFragmentManager(), "timeI");
	     		break;
	     	case R.id.timeDelay:
	     		Dialog_Integer									dialogTimeDelay					= new Dialog_Integer(itemData.mixer.pidParams.timeDelay, (Object) itemData.mixer.pidParams, 10, 200, "Time Delay (s)",	this);
	     		dialogTimeDelay.show(getFragmentManager(), "timeDelay");
	     		break;
	     	case R.id.timeProjection:
	     		Dialog_Integer									dialogTimeProjection			= new Dialog_Integer(itemData.mixer.pidParams.timeProjection, (Object) itemData.mixer.pidParams, 0, 100, "Time Extrapolation (s)",	this);
	     		dialogTimeProjection.show(getFragmentManager(), "timeProjection");
	     		break;
	     	case R.id.marginProjection:
	     		Dialog_Integer									dialogMarginProjection			= new Dialog_Integer(itemData.mixer.pidParams.marginProjection, (Object) itemData.mixer.pidParams, 0, 10, "Define Temperature Margin on Extrapolation (°C)",	this);
	     		dialogMarginProjection.show(getFragmentManager(), "marginProjection");
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

