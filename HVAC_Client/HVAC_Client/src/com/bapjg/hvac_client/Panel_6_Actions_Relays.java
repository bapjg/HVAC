package com.bapjg.hvac_client;

import HVAC_Common.*;
import HVAC_Common.Ctrl_Actions_Stop.Execute;
import HVAC_Common.Ctrl_Temperatures.Request;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Panel_6_Actions_Relays 							extends 					Panel_0_Fragment  
{
	private Element_Switch										switchBurner;
	private Element_Switch										switchHotWater;
	private Element_Switch										switchFloor;
	private Element_Switch										switchRadiator;

	public Panel_6_Actions_Relays()
	{
		super();
	}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	this.panelLayout																	= R.layout.panal_0_standard;
    	this.container																		= container;
    	this.panelView																		= inflater.inflate(R.layout.panal_0_standard, container, false);

    	LinearLayout 											insertPoint 				= (LinearLayout) panelView.findViewById(R.id.base_insert_point);

    	switchBurner 																		= new Element_Switch(getActivity(), "Burner Relay");
    	switchHotWater 																		= new Element_Switch(getActivity(), "Hot Water Pump");
    	switchFloor 																		= new Element_Switch(getActivity(), "Floor Pump");
    	switchRadiator 																		= new Element_Switch(getActivity(), "Radiator Pump");

    	insertPoint.addView(switchBurner);
    	insertPoint.addView(new Element_Filler(getActivity()));
    	insertPoint.addView(switchHotWater);
    	insertPoint.addView(switchFloor);
    	insertPoint.addView(switchRadiator);
    	
    	TCP_Send(new Ctrl_Actions_Relays().new Request());
        
    	displayTitles("Actions", "Relays");
    	displayContents();
        setListens();
   	
        return panelView;
    }
	public void displayHeader()
	{
		((TextView) panelView.findViewById(R.id.title)).setText		("Actions");
		((TextView) panelView.findViewById(R.id.subTitle)).setText	("Relays");
	}
	public void displayContents()
	{
	}
	public void setListens()
	{
//		switchBurner				.setOnClickListener(this);
//		switchHotWater				.setListener(this);
//		switchFloor					.setOnClickListener(this);
//		switchRadiator				.setOnClickListener(this);
		switchBurner.onOffSwitch	.setOnClickListener(this);
		switchHotWater.onOffSwitch	.setOnClickListener(this);
		switchFloor.onOffSwitch		.setOnClickListener(this);
		switchRadiator.onOffSwitch	.setOnClickListener(this);
	}
    public void onClick(View clickedView)
    {
   		Ctrl_Actions_Relays.Execute								messageSend					= new Ctrl_Actions_Relays().new Execute();
  		Switch													switchClicked				= (Switch) clickedView;
   		
		if 		(switchClicked == switchBurner.onOffSwitch)		messageSend.relayName		= "Burner";
		else if (switchClicked == switchHotWater.onOffSwitch)	messageSend.relayName		= "HotWater";
		else if (switchClicked == switchFloor.onOffSwitch)		messageSend.relayName		= "Floor";
		else if (switchClicked == switchRadiator.onOffSwitch)	messageSend.relayName		= "Radiator";
		
		switchClicked.setChecked(! switchClicked.isChecked());
		
		if    (switchClicked.isChecked())						messageSend.relayAction		= Ctrl_Actions_Relays.RELAY_Off;
		else													messageSend.relayAction		= Ctrl_Actions_Relays.RELAY_On;

		TCP_Send(messageSend);
    }
	public void processFinishTCP(Ctrl__Abstract result) 
	{  
		super.processFinishTCP(result);
		
		if (result instanceof Ctrl_Actions_Relays.Data)		
		{		
			Ctrl_Actions_Relays.Data 							msg_received 				= (Ctrl_Actions_Relays.Data) result;

			switchBurner		.setChecked(msg_received.burner);
			switchHotWater		.setChecked(msg_received.pumpHotWater);
			switchFloor			.setChecked(msg_received.pumpFloor);
			switchRadiator		.setChecked(msg_received.pumpRadiator);
		}   
	}
}

