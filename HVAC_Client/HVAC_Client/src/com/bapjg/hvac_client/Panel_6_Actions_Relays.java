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
		super("Standard");
	}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);

    	switchBurner 																		= new Element_Switch("Burner Relay");
    	switchHotWater 																		= new Element_Switch("Hot Water Pump");
    	switchFloor 																		= new Element_Switch("Floor Pump");
    	switchRadiator 																		= new Element_Switch("Radiator Pump");

    	panelInsertPoint.addView(new Element_Heading("Relay Name", "Swtich"));
    	panelInsertPoint.addView(switchBurner);
    	panelInsertPoint.addView(new Element_Filler());
    	panelInsertPoint.addView(switchHotWater);
    	panelInsertPoint.addView(switchFloor);
    	panelInsertPoint.addView(switchRadiator);
    	
    	TCP_Send(new Ctrl_Actions_Relays().new Request());
        
    	displayTitles("Actions", "Relays");
    	displayContents();
        setListens();
   	
        return panelView;
    }
	public void displayContents()
	{
	}
	public void setListens()
	{
		switchBurner				.setListener(this);
		switchHotWater				.setListener(this);
		switchFloor					.setListener(this);
		switchRadiator				.setListener(this);
	}
	@Override
	public void onElementClick(View viewClicked) 
    {
		Element_Switch 											switchClicked 				= (Element_Switch) viewClicked;
		Ctrl_Actions_Relays.Execute								messageSend					= new Ctrl_Actions_Relays().new Execute();
   		
		if 		(switchClicked == switchBurner)		messageSend.relayName		= "Burner";
		else if (switchClicked == switchHotWater)	messageSend.relayName		= "HotWater";
		else if (switchClicked == switchFloor)		messageSend.relayName		= "Floor";
		else if (switchClicked == switchRadiator)	messageSend.relayName		= "Radiator";
		
		switchClicked.setChecked(! switchClicked.isChecked());
		
		if    (switchClicked.isChecked())						messageSend.relayAction		= Ctrl_Actions_Relays.RELAY_Off;
		else													messageSend.relayAction		= Ctrl_Actions_Relays.RELAY_On;

		TCP_Send(messageSend);
    }
	public void processFinishTCP(Msg__Abstract result) 
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

