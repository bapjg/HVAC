package com.bapjg.hvac_client;

import HVAC_Messages.*;
import HVAC_Messages.Ctrl_Actions_Stop.Execute;
import HVAC_Messages.Ctrl_Temperatures.Request;
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
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class Panel_6_Actions_Relays 					extends 					Panel_0_Fragment  
{
	public Panel_6_Actions_Relays()
	{
		super();
	}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	this.activity																= getActivity();
    	View											thisView					= inflater.inflate(R.layout.panel_6_actions_relays, container, false);
    	TCP_Send(new Ctrl_Actions_Relays().new Request());

    	thisView.findViewById(R.id.burner).setOnClickListener(this);
    	thisView.findViewById(R.id.hotwater).setOnClickListener(this);
    	thisView.findViewById(R.id.floor).setOnClickListener(this);
    	thisView.findViewById(R.id.radiator).setOnClickListener(this);
        return thisView;
    }
    public void onClick(View view)
    {
   		Ctrl_Actions_Relays.Execute						messageSend					= new Ctrl_Actions_Relays().new Execute();
    	if (view instanceof Switch)
    	{
			if      (view.getId() == R.id.burner)		messageSend.relayName		= "Burner";
			else if (view.getId() == R.id.hotwater)		messageSend.relayName		= "HotWater";
			else if (view.getId() == R.id.floor)		messageSend.relayName		= "Floor";
			else if (view.getId() == R.id.radiator)		messageSend.relayName		= "Radiator";
    		
    		if    (((Switch) view).isChecked())			messageSend.relayAction		= Ctrl_Actions_Relays.RELAY_On;
    		else										messageSend.relayAction		= Ctrl_Actions_Relays.RELAY_Off;

    		TCP_Send(messageSend);
    	}
    }
 	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
	}
		public void processFinishTCP(Ctrl_Abstract result) 
	{  
		Activity										activity					= getActivity();		
		
		if (result instanceof Ctrl_Actions_Relays.Data)		
		{		
			Ctrl_Actions_Relays.Data 					msg_received 				= (Ctrl_Actions_Relays.Data) result;

			((Switch) activity.findViewById(R.id.burner)).setChecked(msg_received.burner);
			((Switch) activity.findViewById(R.id.hotwater)).setChecked(msg_received.pumpHotWater);
			((Switch) activity.findViewById(R.id.floor)).setChecked(msg_received.pumpFloor);
			((Switch) activity.findViewById(R.id.radiator)).setChecked(msg_received.pumpRadiator);
		}   
	}
}

