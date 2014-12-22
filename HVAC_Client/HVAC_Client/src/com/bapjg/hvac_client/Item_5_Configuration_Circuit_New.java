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
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("ValidFragment")
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------

public class Item_5_Configuration_Circuit_New 					extends 					Panel_0_Fragment
																implements					Panel_0_Interface
{		
	private Ctrl_Configuration.Circuit	 						itemData;
	
	private Element_Heading										headingGeneral;
	private Element_Standard									pump;
	private Element_Standard									targetThermometer;
	
	private Element_Heading										headingGradient;
	
	private Element_Heading										headingMixer;
	private Element_Standard									mixerThermometer;
	private Element_Standard									swingTime;
	private Element_Standard									swingProportionMin;
	private Element_Standard									swingProportionMax;
	private Element_Standard									relayUp;
	private Element_Standard									relayDown;
	
	private Element_Heading										headingPID;
	private Element_Standard									pidThermometer;
	private Element_Standard 									gainP;	
	private Element_Standard 									timeD;
	private Element_Standard 									timeI;
	private Element_Standard 									timeDelay;	
	private Element_Standard 									timeProjection;	
	private Element_Standard 									marginProjection;	

	
	public Item_5_Configuration_Circuit_New(Ctrl_Configuration.Circuit itemData)
	{
		super();
		this.itemData																		= itemData;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
//        View 													panelView					= inflater.inflate(R.layout.panal_0_standard, container, false);
    	this.container																		= container;
		this.panelView																		= inflater.inflate(R.layout.panal_0_standard, container, false);
//        this.panelView																		= (ViewGroup) panelView;

    	LinearLayout insertPoint 															= (LinearLayout) panelView.findViewById(R.id.base_insert_point);

    	
    	headingGeneral			 															= new Element_Heading(getActivity(), "General");
    	pump			 																	= new Element_Standard(getActivity(), "Pump");
    	targetThermometer																	= new Element_Standard(getActivity(), "Target Thermometer");
        
    	insertPoint.addView(headingGeneral);
    	insertPoint.addView(pump);
    	insertPoint.addView(targetThermometer);

    	headingGradient																		= new Element_Heading(getActivity(), "Gradient");
    	
    	insertPoint.addView(headingGradient);
    	
    	headingMixer																		= new Element_Heading(getActivity(), "Mixer");
    	mixerThermometer																	= new Element_Standard(getActivity(),"Mixer Target Thermometer");
    	swingTime																			= new Element_Standard(getActivity(),"Swing Time", "s");
    	swingProportionMin																	= new Element_Standard(getActivity(),"Swing Min", "%");
    	swingProportionMax																	= new Element_Standard(getActivity(),"Swing Max", "%");
    	relayUp																				= new Element_Standard(getActivity(),"Relay Up");
    	relayDown																			= new Element_Standard(getActivity(),"Relay Down");
    	
    	insertPoint.addView(headingMixer);
    	insertPoint.addView(mixerThermometer);
    	insertPoint.addView(swingTime);
    	insertPoint.addView(swingProportionMin);
    	insertPoint.addView(swingProportionMax);
    	insertPoint.addView(relayUp);
    	insertPoint.addView(relayDown);
    	
    	headingPID																			= new Element_Heading(getActivity(), "PID Data");
    	pidThermometer																		= new Element_Standard(getActivity(),"PID Thermometer");
    	gainP																				= new Element_Standard(getActivity(),"Proportional Gain", "ms/m°C");
    	timeD																				= new Element_Standard(getActivity(),"Differential Time Constant", "ms/°C");
    	timeI																				= new Element_Standard(getActivity(),"Integration Time Constant", "°C/ms");
    	timeDelay																			= new Element_Standard(getActivity(),"Time Delay after Decision", "s");
    	timeProjection																		= new Element_Standard(getActivity(),"Decision Time Projection", "s");
    	marginProjection																	= new Element_Standard(getActivity(),"Temperature Error Margin", "°C");
    	
    	insertPoint.addView(headingPID);
    	insertPoint.addView(pidThermometer);
    	insertPoint.addView(gainP);
    	insertPoint.addView(timeD);
    	insertPoint.addView(timeI);
    	insertPoint.addView(timeDelay);
    	insertPoint.addView(timeProjection);
    	insertPoint.addView(marginProjection);
        
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
        return panelView;
    }
	public void displayContents()
	{
    	// General :
		pump				.setTextRight	(itemData.pump);
		targetThermometer	.setTextRight	(itemData.thermometer);
		
    	// Mixer :
    	if (itemData.tempGradient != null)
    	{
        	Ctrl_Configuration.TempGradient						tempGradient				= itemData.tempGradient;

//        	// Temperature Gradient :
//    		TextView 												outsideLow 					= (TextView) itemView.findViewById(R.id.outsideLow);
//        	TextView 												outsideHigh					= (TextView) itemView.findViewById(R.id.outsideHigh);
//       		TextView 												tempLow 					= (TextView) itemView.findViewById(R.id.tempLow);
//        	TextView 												tempHigh					= (TextView) itemView.findViewById(R.id.tempHigh);

//    	
//        	outsideLow.setText									(tempGradient.outsideLow.displayInteger());
//        	outsideHigh.setText									(tempGradient.outsideHigh.displayInteger());
//        	tempLow.setText										(tempGradient.tempLow.displayInteger());
//        	tempHigh.setText									(tempGradient.tempHigh.displayInteger());
    	}
    	else
    	{
//    		ViewGroup											gradientView				= (ViewGroup) itemView.findViewById(R.id.gradient);	
//    		gradientView.setVisibility(View.GONE);
    	}

    	if (itemData.mixer != null)
    	{
        	Ctrl_Configuration.Mixer							mixer						= itemData.mixer;
//     	
    		//mixerThermometer	.setTextRight	(mixer.name);
//        	thermometer.setText									(mixer.name);
//        	swingTime.setText									(((Integer) (mixer.swingTime/1000)).toString());
//        	relayUp.setText										(mixer.relayUp);
//        	relayDown.setText									(mixer.relayDown);
    		mixerThermometer	.setTextRight	(itemData.pump);																	
        	swingTime			.setTextRight	((Integer) (mixer.swingTime/1000));																			
        	swingProportionMin	.setTextRight	(mixer.swingProportionMin);															
        	swingProportionMax	.setTextRight	(mixer.swingProportionMax);															
        	relayUp				.setTextRight	(mixer.relayUp);																
        	relayDown			.setTextRight	(mixer.relayDown);														

           	Ctrl_Configuration.PID_Params						pidParams					= itemData.mixer.pidParams;

//        	timeI.setText										(milliToString(pidParams.timeI) + " °C/ms");
//        	timeDelay.setText									(milliToString(pidParams.timeDelay) + " s");
//        	timeProjection.setText								(milliToString(pidParams.timeProjection) + " s");
//        	marginProjection.setText							(milliToString(pidParams.marginProjection) + " °C");
    	
        	pidThermometer		.setTextRight	(pidParams.thermometer);
        	gainP				.setTextRight	(pidParams.gainP);
        	timeD				.setTextRight	(pidParams.timeD);																
        	timeI				.setTextRight	(pidParams.timeI);																
        	timeDelay			.setTextRight	(pidParams.timeDelay);																
        	timeProjection		.setTextRight	(pidParams.timeProjection);																
        	marginProjection	.setTextRight	(pidParams.marginProjection);																
    	}
    	else
    	{
//    		ViewGroup											mixerView				= (ViewGroup) itemView.findViewById(R.id.mixer);	
//    		mixerView.setVisibility(View.GONE);
    	}
   	}
	public void setListens()
	{
		pump.setListener(this);
		if (itemData.tempGradient != null)
    	{
//			itemView.findViewById(R.id.tempLow).setOnClickListener(this);
//			itemView.findViewById(R.id.tempHigh).setOnClickListener(this);
//			itemView.findViewById(R.id.outsideHigh).setOnClickListener(this);
//			itemView.findViewById(R.id.outsideLow).setOnClickListener(this);
//			itemView.findViewById(R.id.thermometer).setOnClickListener(this);
//			itemView.findViewById(R.id.swingTime).setOnClickListener(this);
//			itemView.findViewById(R.id.relayUp).setOnClickListener(this);
//			itemView.findViewById(R.id.relayDown).setOnClickListener(this);
//
//			itemView.findViewById(R.id.thermometer).setOnClickListener(this);
//			itemView.findViewById(R.id.gainP).setOnClickListener(this);
//			itemView.findViewById(R.id.timeD).setOnClickListener(this);
//			itemView.findViewById(R.id.timeI).setOnClickListener(this);
//			itemView.findViewById(R.id.timeDelay).setOnClickListener(this);
//			itemView.findViewById(R.id.timeProjection).setOnClickListener(this);
//			itemView.findViewById(R.id.marginProjection).setOnClickListener(this);
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
    @Override
	public void onPanelItemClick(Element_Switch clickedView)
	{
		Global.toaster("A switched has been clicked", true);
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

