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
public class Panel_5_Actions_Relays 		extends 	Panel_0_Fragment  
											implements 	TCP_Response
{
	public Panel_5_Actions_Relays()
	{
		super();
	}
    public Panel_5_Actions_Relays(int menuLayout)
    {
		super(menuLayout);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	this.activity											= getActivity();
    	View								thisView			= inflater.inflate(R.layout.panel_5_actions_relays, container, false);
    	TCP_Task							task				= new TCP_Task();
    	task.callBack											= this;
    	task.execute(new Ctrl_Actions_Relays().new Request());

    	thisView.findViewById(R.id.burner).setOnClickListener(new OnClickListener() 		{@Override public void onClick(View v) {burnerClick(v);		}});
    	thisView.findViewById(R.id.hotwater).setOnClickListener(new View.OnClickListener() 	{@Override public void onClick(View v) {hotWaterClick(v);	}});
    	thisView.findViewById(R.id.floor).setOnClickListener(new View.OnClickListener() 	{@Override public void onClick(View v) {floorClick(v);		}});
    	thisView.findViewById(R.id.radiator).setOnClickListener(new View.OnClickListener() 	{@Override public void onClick(View v) {radiatorClick(v);	}});
        return thisView;
    }
    public void burnerClick(View v)
    {
    	Switch							mySwitch				= (Switch) v;
    	if (mySwitch.isChecked())
    	{
    		Ctrl_Actions_Relays.Execute	messageSend				= new Ctrl_Actions_Relays().new Execute();
    		messageSend.relayName								= "Burner";
    		messageSend.relayAction								= Ctrl_Actions_Relays.RELAY_On;
    		
    		TCP_Task						task				= new TCP_Task();
    	   	task.callBack										= this;
    	   	task.execute(messageSend);
    	}
    	else
    	{
    		Ctrl_Actions_Relays.Execute	messageSend				= new Ctrl_Actions_Relays().new Execute();
    		messageSend.relayName								= "Burner";
    		messageSend.relayAction								= Ctrl_Actions_Relays.RELAY_Off;
    		
    		TCP_Task						task				= new TCP_Task();
    	   	task.callBack										= this;
    	   	task.execute(messageSend);
    	}
    }
    public void hotWaterClick(View v)
    {
    	System.out.println("hotWaterClick");
    }
    public void floorClick(View v)
    {
    	System.out.println("floorClick");
    }
    public void radiatorClick(View v)
    {
    	System.out.println("RadiatorClick");
    }
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
	}
	public void processFinish(Ctrl_Abstract result) 
	{  
		Activity a							= getActivity();
		
		System.out.println("activity = " + a);
		if (a == null) 
		{
			// Do nothing
		}
		else if (result instanceof Ctrl_Actions_Relays.Data)
		{
			Ctrl_Actions_Relays.Data msg_received 	= (Ctrl_Actions_Relays.Data) result;
			

			((Switch) a.findViewById(R.id.burner)).setChecked(msg_received.burner);
			((Switch) a.findViewById(R.id.hotwater)).setChecked(msg_received.pumpHotWater);
			((Switch) a.findViewById(R.id.floor)).setChecked(msg_received.pumpFloor);
			((Switch) a.findViewById(R.id.radiator)).setChecked(msg_received.pumpRadiator);
		}   
	}
}

