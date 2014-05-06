package com.bapjg.hvac_client;

import HVAC_Messages.*;
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

    	thisView.findViewById(R.id.burner).setOnClickListener(new OnClickListener() 		{@Override public void onClick(View v) {burnerClick(v);		}});
    	thisView.findViewById(R.id.hotwater).setOnClickListener(new View.OnClickListener() 	{@Override public void onClick(View v) {hotWaterClick(v);	}});
    	thisView.findViewById(R.id.floor).setOnClickListener(new View.OnClickListener() 	{@Override public void onClick(View v) {floorClick(v);		}});
    	thisView.findViewById(R.id.radiator).setOnClickListener(new View.OnClickListener() 	{@Override public void onClick(View v) {radiatorClick(v);	}});
        return thisView;
    }
    public void burnerClick(View v)
    {
    	relayClickContinue("Burner",  (Switch) v);
    }
    public void hotWaterClick(View v)
    {
    	relayClickContinue("HotWater",  (Switch) v);
    }
    public void floorClick(View v)
    {
    	relayClickContinue("Floor",  (Switch) v);
    }
    public void radiatorClick(View v)
    {
    	relayClickContinue("Radiator",  (Switch) v);
    }
    public void relayClickContinue(String relayName, Switch switchView)
    {
   		Ctrl_Actions_Relays.Execute	messageSend										= new Ctrl_Actions_Relays().new Execute();
		messageSend.relayName														= relayName;
    	if (switchView.isChecked())				
    	{				
    		messageSend.relayAction													= Ctrl_Actions_Relays.RELAY_On;
    	}				
    	else				
    	{				
    		messageSend.relayAction													= Ctrl_Actions_Relays.RELAY_Off;
    	}
    	TCP_Send(messageSend);
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

