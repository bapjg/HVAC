package com.bapjg.hvac_client;

import HVAC_Messages.*;
import HVAC_Messages.Ctrl_Actions_Test_Mail.Execute;
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
public class Panel_6_Actions_Stop 						extends 					Panel_0_Fragment  
{
	public Panel_6_Actions_Stop()
	{
		super();
	}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	this.activity																= getActivity();
    	View											thisView					= inflater.inflate(R.layout.panel_6_actions_stop, container, false);
 
    	thisView.findViewById(R.id.buttonStop).setOnClickListener(this);
    	thisView.findViewById(R.id.buttonRestart).setOnClickListener(this);
    	thisView.findViewById(R.id.buttonReboot).setOnClickListener(this);
   	
        return thisView;
    }
    public void onClick(View view)
    {
    	Ctrl_Actions_Stop.Execute 						stopMessage					= new Ctrl_Actions_Stop().new Execute();
    	if (view instanceof Button)
    	{
    		if      (view.getId()==R.id.buttonStop)		stopMessage.exitStatus		= Ctrl_Actions_Stop.EXIT_Stop;
    		else if (view.getId()==R.id.buttonRestart)	stopMessage.exitStatus		= Ctrl_Actions_Stop.EXIT_Restart;
    		else if (view.getId()==R.id.buttonReboot)	stopMessage.exitStatus		= Ctrl_Actions_Stop.EXIT_Reboot;
        	TCP_Send(stopMessage);
    	}
    }
     @Override
	public void processFinishTCP(Ctrl_Abstract result) 
	{  
		if (result instanceof Ctrl_Actions_Stop.Ack)								Global.toaster("Stop Request accepted", true);
		else   																		Global.toaster("Stop generated error " + result.getClass().toString(), true);
	}
}

