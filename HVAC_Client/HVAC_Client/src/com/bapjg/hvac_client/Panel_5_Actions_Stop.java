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
public class Panel_5_Actions_Stop 			extends 	Panel_0_Fragment  
											implements 	TCP_Response
{
	public Panel_5_Actions_Stop()
	{
		super();
	}
    public Panel_5_Actions_Stop(int menuLayout)
    {
		super(menuLayout);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	this.activity											= getActivity();
    	View								thisView			= inflater.inflate(R.layout.panel_5_actions_stop, container, false);
 
    	thisView.findViewById(R.id.buttonStop).setOnClickListener(new View.OnClickListener() 		{@Override public void onClick(View v) {stopHVAC(v);	}});
    	thisView.findViewById(R.id.buttonRestart).setOnClickListener(new View.OnClickListener() 	{@Override public void onClick(View v) {restartHVAC(v);	}});
    	thisView.findViewById(R.id.buttonReboot).setOnClickListener(new View.OnClickListener() 		{@Override public void onClick(View v) {rebootHVAC(v);	}});
   	
        return thisView;
    }
    public void stopHVAC(View v)
    {
    	Ctrl_Actions_Stop.Execute 			stopMessage			= new Ctrl_Actions_Stop().new Execute();
    	stopMessage.exitStatus									= Ctrl_Actions_Stop.EXIT_Stop;
    	TCP_Send(stopMessage);
    }
    public void restartHVAC(View v)
    {
       	Ctrl_Actions_Stop.Execute 			stopMessage			= new Ctrl_Actions_Stop().new Execute();
    	stopMessage.exitStatus									= Ctrl_Actions_Stop.EXIT_Restart;
    	TCP_Send(stopMessage);
    }
    public void rebootHVAC(View v)
    {
       	Ctrl_Actions_Stop.Execute 			stopMessage			= new Ctrl_Actions_Stop().new Execute();
    	stopMessage.exitStatus									= Ctrl_Actions_Stop.EXIT_Reboot;
    	TCP_Send(stopMessage);
    }
    @Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
	}
	public void TCP_Send(Ctrl_Abstract message)
	{
		TCP_Task						task				= new TCP_Task();
	   	task.callBack										= this;					// processFinish
	   	task.execute(message);
	}
	public void processFinish(Ctrl_Abstract result) 
	{  
		Activity 						a					= getActivity();
		
		System.out.println("activity = " + a);
		if (a == null) 
		{
			// Do nothing
		}
		else if (result instanceof Ctrl_Actions_Stop.Ack)
		{
			Global.toaster("Stop Request accepted", true);
		}   
	}
}

