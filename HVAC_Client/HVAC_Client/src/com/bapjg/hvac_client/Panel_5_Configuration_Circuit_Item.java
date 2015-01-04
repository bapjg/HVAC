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
public class Panel_5_Configuration_Circuit_Item 				extends 					Panel_0_Fragment
																implements					Panel_0_Interface
{		
	private Ctrl_Configuration.Circuit	 						itemData;
	
	private Element_Heading										headingGeneral;
	private Element_Standard									pump;
	private Element_Standard									targetThermometer;
	private Element_Standard									circuitType;
	
	private Element_Heading										headingGradient;
	private Element_Gradient									gradient;
	
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

	
	public Panel_5_Configuration_Circuit_Item(Ctrl_Configuration.Circuit itemData)
	{
		super("Standard");
		this.itemData																		= itemData;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);
    	
    	headingGeneral			 															= new Element_Heading("General");
    	pump			 																	= new Element_Standard("Pump");
    	targetThermometer																	= new Element_Standard("Target Thermometer");
    	circuitType																			= new Element_Standard("Circuit Type");
    	panelInsertPoint.addView(headingGeneral);
    	panelInsertPoint.addView(pump);
    	panelInsertPoint.addView(targetThermometer);
    	panelInsertPoint.addView(circuitType);

    	headingGradient																		= new Element_Heading("Gradient");
    	gradient				 															= new Element_Gradient();
    	
    	panelInsertPoint.addView(headingGradient);
    	panelInsertPoint.addView(gradient);
    	
    	headingMixer																		= new Element_Heading("Mixer");
    	mixerThermometer																	= new Element_Standard("Mixer Target Thermometer");
    	swingTime																			= new Element_Standard("Swing Time", "s");
    	swingProportionMin																	= new Element_Standard("Swing Min", "%");
    	swingProportionMax																	= new Element_Standard("Swing Max", "%");
    	relayUp																				= new Element_Standard("Relay Up");
    	relayDown																			= new Element_Standard("Relay Down");
    	
    	panelInsertPoint.addView(headingMixer);
    	panelInsertPoint.addView(mixerThermometer);
    	panelInsertPoint.addView(swingTime);
    	panelInsertPoint.addView(swingProportionMin);
    	panelInsertPoint.addView(swingProportionMax);
    	panelInsertPoint.addView(relayUp);
    	panelInsertPoint.addView(relayDown);
    	
    	headingPID																			= new Element_Heading("PID Data");
    	pidThermometer																		= new Element_Standard("PID Thermometer");
    	gainP																				= new Element_Standard("Proportional Gain", "s/°C");
    	timeD																				= new Element_Standard("Differential Time Constant", "s");
    	timeI																				= new Element_Standard("Integration Time Constant", "s");
    	timeDelay																			= new Element_Standard("Time Delay after Decision", "s");
    	timeProjection																		= new Element_Standard("Decision Time Projection", "s");
    	marginProjection																	= new Element_Standard("Temperature Error Margin", "°C");
    	
    	panelInsertPoint.addView(headingPID);
    	panelInsertPoint.addView(pidThermometer);
    	panelInsertPoint.addView(gainP);
    	panelInsertPoint.addView(timeD);
    	panelInsertPoint.addView(timeI);
    	panelInsertPoint.addView(timeDelay);
    	panelInsertPoint.addView(timeProjection);
    	panelInsertPoint.addView(marginProjection);
        
    	displayTitles("Circuit Configuration", itemData.name);

    	if ((Global.eRegConfiguration != null)
        &&  (Global.eRegConfiguration.circuitList != null))
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
		// CIRCUIT_TYPE_HotWater = 0
		// CIRCUIT_TYPE_Gradient = 1
		// CIRCUIT_TYPE_Mixer = 2
		
		pump						.setTextRight	(itemData.pump);
		targetThermometer			.setTextRight	(itemData.thermometer);
		circuitType					.setTextRight	(itemData.thermometer);
		
    	if (itemData.type >= 1)		// Gradient or Mixer
    	{
        	Ctrl_Configuration.TempGradient						tempGradient				= itemData.tempGradient;
        	
        	gradient				.setTempLow		(tempGradient.tempLow);
        	gradient				.setTempHigh	(tempGradient.tempHigh);
        	gradient				.setOutsideLow	(tempGradient.outsideLow);
        	gradient				.setOutsideHigh	(tempGradient.outsideHigh);
    	}
    	else
    	{
    		headingGradient			.setVisibility	(View.GONE);
    		gradient				.setVisibility	(View.GONE);
    	}

    	if (itemData.type == 2)		// Mixer
    	{
        	Ctrl_Configuration.Mixer							mixer						= itemData.mixer;

    		mixerThermometer		.setTextRight	(mixer.name);																	
        	swingTime				.setTextRight	((Integer) (mixer.swingTime/1000));																			
        	swingProportionMin		.setTextRight	(mixer.swingProportionMin);															
        	swingProportionMax		.setTextRight	(mixer.swingProportionMax);															
        	relayUp					.setTextRight	(mixer.relayUp);																
        	relayDown				.setTextRight	(mixer.relayDown);														

           	Ctrl_Configuration.PID_Params						pidParams					= itemData.mixer.pidParams;

        	pidThermometer			.setTextRight	(pidParams.thermometer);
        	gainP					.setTextRight	(pidParams.gainP);
        	timeD					.setTextRight	(pidParams.timeD);																
        	timeI					.setTextRight	(pidParams.timeI);																
        	timeDelay				.setTextRight	(pidParams.timeDelay);																
        	timeProjection			.setTextRight	(pidParams.timeProjection);																
        	marginProjection		.setTextRight	(pidParams.marginProjection);																
    	}
    	else
    	{
    		headingMixer			.setVisibility	(View.GONE);
    		mixerThermometer		.setVisibility	(View.GONE);																	
        	swingTime				.setVisibility	(View.GONE);																		
        	swingProportionMin		.setVisibility	(View.GONE);															
        	swingProportionMax		.setVisibility	(View.GONE);														
        	relayUp					.setVisibility	(View.GONE);															
        	relayDown				.setVisibility	(View.GONE);													

    		headingPID				.setVisibility	(View.GONE);
        	pidThermometer			.setVisibility	(View.GONE);
        	gainP					.setVisibility	(View.GONE);
        	timeD					.setVisibility	(View.GONE);																
        	timeI					.setVisibility	(View.GONE);														
        	timeDelay				.setVisibility	(View.GONE);																
        	timeProjection			.setVisibility	(View.GONE);															
        	marginProjection		.setVisibility	(View.GONE);														
    	}
   	}
	public void setListens()
	{
		pump						.setListener(this);
		targetThermometer			.setListener(this);
		
		if (itemData.type >= 1)
    	{
			gradient				.setListener(this);
    	}
		if (itemData.type == 2)
    	{
    		mixerThermometer		.setListener(this);																	
        	swingTime				.setListener(this);																	
        	swingProportionMin		.setListener(this);															
        	swingProportionMax		.setListener(this);														
        	relayUp					.setListener(this);															
        	relayDown				.setListener(this);

			pidThermometer			.setListener(this);																	
			gainP					.setListener(this);																	
			timeD					.setListener(this);															
			timeI					.setListener(this);														
			timeDelay				.setListener(this);															
			timeProjection			.setListener(this);													
			marginProjection		.setListener(this);													
    	}
	}
    @Override
	public void onElementClick(View clickedView) 
	{
    	Dialog_Temperature										dialogTemperature;
    	if (clickedView == pump)
		{
    		// TODO Need to dialog pump list
		}
    	else if (clickedView == targetThermometer)
		{
    		Dialog_String_List		 						dialogThermometers				= new Dialog_String_List(itemData.mixer.name, (Object) itemData.mixer, null, this);

    		for (Ctrl_Configuration.Thermometer thermometer : Global.eRegConfiguration.thermometerList)
    		{
    			dialogThermometers.items.add(thermometer.name);
    		}
    		dialogThermometers.show(getFragmentManager(), "Thermometer_List");
		}
    	else if (clickedView == gradient.tempLow)
		{
     		dialogTemperature 																= new Dialog_Temperature(itemData.tempGradient.tempLow,  -20, 50, this);
     		dialogTemperature.show(getFragmentManager(), "Dialog_Temperature");
		}
    	else if (clickedView == gradient.tempHigh)
		{
     		dialogTemperature																= new Dialog_Temperature(itemData.tempGradient.tempHigh,  -20, 50, this);
     		dialogTemperature.show(getFragmentManager(), "Dialog_Temperature");
		}
    	else if (clickedView == gradient.outsideLow)
		{
     		dialogTemperature 																= new Dialog_Temperature(itemData.tempGradient.outsideLow,  -20, 50, this);
     		dialogTemperature.show(getFragmentManager(), "Dialog_Temperature");
		}
    	else if (clickedView == gradient.outsideHigh)
		{
     		dialogTemperature																= new Dialog_Temperature(itemData.tempGradient.outsideHigh,  -20, 50, this);
     		dialogTemperature.show(getFragmentManager(), "Dialog_Temperature");
		}
    	else if (clickedView == swingTime)
		{
     		Dialog_Integer									dialogSwingTime					= new Dialog_Integer(itemData.mixer.swingTime, (Object) itemData.mixer, 10, 200, "Define Proprtional Gain",	this);
     		dialogSwingTime.show(getFragmentManager(), "Mixer Swing Time (ms)");
		}
    	else if (clickedView == swingProportionMin)
		{
      		Global.toaster("Its Swing Prop", false);
		}
    	else if (clickedView == swingProportionMax)
		{
      		Global.toaster("Its Swing Prop", false);
		}
    	else if (clickedView == relayUp)
		{
      		Global.toaster("Its Ok relayUp", false);
		}
    	else if (clickedView == relayDown)
		{
      		Global.toaster("Its Ok relayDown", false);
		}
    	else if (clickedView == gainP)
		{
     		Dialog_Float									dialogGain 						= new Dialog_Float(		itemData.mixer.pidParams.gainP,
																												(Object) itemData.mixer.pidParams, 
																												"Define Proportional Gain (ms/°C)",
																												this);
     		dialogGain.show(getFragmentManager(), "gainP");
		}
    	else if (clickedView == timeD)
		{
     		Dialog_Float									dialogTimeD						= new Dialog_Float(		itemData.mixer.pidParams.timeD,
						(Object) itemData.mixer.pidParams, 
						"Differential Time Constant (ms/C)",
						this);
     		dialogTimeD.show(getFragmentManager(), "timeD");
		}
    	else if (clickedView == timeI)
		{
     		Dialog_Float									dialogTimeIntegration			= new Dialog_Float(		itemData.mixer.pidParams.timeI, 
						(Object) itemData.mixer.pidParams, 
						"Time Integration Factor (°C/ms)",	
						this);
     					dialogTimeIntegration.show(getFragmentManager(), "timeI");
		}
    	else if (clickedView == timeDelay)
		{
     		Dialog_Integer									dialogTimeDelay					= new Dialog_Integer(itemData.mixer.pidParams.timeDelay, (Object) itemData.mixer.pidParams, 10, 200, "Time Delay (s)",	this);
     		dialogTimeDelay.show(getFragmentManager(), "timeDelay");
		}
    	else if (clickedView == timeProjection)
		{
     		Dialog_Integer									dialogTimeProjection			= new Dialog_Integer(itemData.mixer.pidParams.timeProjection, (Object) itemData.mixer.pidParams, 0, 100, "Time Extrapolation (s)",	this);
     		dialogTimeProjection.show(getFragmentManager(), "timeProjection");
		}
    	else if (clickedView == marginProjection)
		{
     		Dialog_Integer									dialogMarginProjection			= new Dialog_Integer(itemData.mixer.pidParams.marginProjection, (Object) itemData.mixer.pidParams, 0, 10, "Define Temperature Margin on Extrapolation (°C)",	this);
     		dialogMarginProjection.show(getFragmentManager(), "marginProjection");
		}

//    	switch(clickedView.getId())
//		{
//	     	case R.id.tempLow:
//	     		dialogTemperature 																= new Dialog_Temperature(itemData.tempGradient.tempLow,  -20, 50, this);
//	     		dialogTemperature.show(getFragmentManager(), "Dialog_Temperature");
//	      		break;
//	     	case R.id.tempHigh:
//	     		dialogTemperature																= new Dialog_Temperature(itemData.tempGradient.tempHigh,  -20, 50, this);
//	     		dialogTemperature.show(getFragmentManager(), "Dialog_Temperature");
//	      		break;
//	     	case R.id.outsideHigh:
//	     		dialogTemperature																= new Dialog_Temperature(itemData.tempGradient.outsideHigh,  -20, 50, this);
//	     		dialogTemperature.show(getFragmentManager(), "Dialog_Temperature");
//	      		break;
//	     	case R.id.outsideLow:
//	     		dialogTemperature 																= new Dialog_Temperature(itemData.tempGradient.outsideLow,  -20, 50, this);
//	     		dialogTemperature.show(getFragmentManager(), "Dialog_Temperature");
//	      		break;
//	     	case R.id.thermometer:				//ie thermometerName
//	    		Dialog_String_List		 						dialogThermometers				= new Dialog_String_List(itemData.mixer.name, (Object) itemData.mixer, null, this);
//
//	    		for (Ctrl_Configuration.Thermometer thermometer : Global.eRegConfiguration.thermometerList)
//	    		{
//	    			dialogThermometers.items.add(thermometer.name);
//	    		}
//	    		dialogThermometers.show(getFragmentManager(), "Thermometer_List");
//	      		break;
//	     	case R.id.swingTime:
//	     		Dialog_Integer									dialogSwingTime					= new Dialog_Integer(itemData.mixer.swingTime, (Object) itemData.mixer, 10, 200, "Define Proprtional Gain",	this);
//	     		dialogSwingTime.show(getFragmentManager(), "Mixer Swing Time (ms)");
//	     		break;
//	     	case R.id.relayUp:
//	      		Global.toaster("Its Ok relayUp", false);
//	      		break;
//	     	case R.id.relayDown:
//	      		Global.toaster("Its Ok relayDown", false);
//	      		break;
//	     	case R.id.thermometer:
//	      		break;
//	     	case R.id.gainP:
//	     		Dialog_Float									dialogGain 						= new Dialog_Float(		itemData.mixer.pidParams.gainP,
//	     																												(Object) itemData.mixer.pidParams, 
//	     																												"Define Proportional Gain (ms/°C)",
//	     																												this);
//	     		dialogGain.show(getFragmentManager(), "gainP");
//	     		break;
//	     	case R.id.timeD:
//	     		Dialog_Float									dialogTimeD						= new Dialog_Float(		itemData.mixer.pidParams.timeD,
//	     																												(Object) itemData.mixer.pidParams, 
//	     																												"Differential Time Constant (ms/C)",
//	     																												this);
//	     		dialogTimeD.show(getFragmentManager(), "timeD");
//	     		break;
//	     	case R.id.timeI:
//	     		Dialog_Float									dialogTimeIntegration			= new Dialog_Float(		itemData.mixer.pidParams.timeI, 
//	     																												(Object) itemData.mixer.pidParams, 
//	     																												"Time Integration Factor (°C/ms)",	
//	     																												this);
//	     		dialogTimeIntegration.show(getFragmentManager(), "timeI");
//	     		break;
//	     	case R.id.timeDelay:
//	     		Dialog_Integer									dialogTimeDelay					= new Dialog_Integer(itemData.mixer.pidParams.timeDelay, (Object) itemData.mixer.pidParams, 10, 200, "Time Delay (s)",	this);
//	     		dialogTimeDelay.show(getFragmentManager(), "timeDelay");
//	     		break;
//	     	case R.id.timeProjection:
//	     		Dialog_Integer									dialogTimeProjection			= new Dialog_Integer(itemData.mixer.pidParams.timeProjection, (Object) itemData.mixer.pidParams, 0, 100, "Time Extrapolation (s)",	this);
//	     		dialogTimeProjection.show(getFragmentManager(), "timeProjection");
//	     		break;
//	     	case R.id.marginProjection:
//	     		Dialog_Integer									dialogMarginProjection			= new Dialog_Integer(itemData.mixer.pidParams.marginProjection, (Object) itemData.mixer.pidParams, 0, 10, "Define Temperature Margin on Extrapolation (°C)",	this);
//	     		dialogMarginProjection.show(getFragmentManager(), "marginProjection");
//	     		break;
//		}
 	}
//    @Override
	public void onElementClick(Element_Switch clickedView)
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

