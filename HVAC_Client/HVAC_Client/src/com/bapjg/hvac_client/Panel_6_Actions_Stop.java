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
	private View										panelView;				// This corresponds to the inflated panel (R.layout.panel_n_xxxxxx)

	public Panel_6_Actions_Stop()
	{
		super();
	}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
     	this.panelView															= inflater.inflate(R.layout.panel_6_actions_stop, container, false);

    	displayHeader();
    	displayContents();
        setListens();
   	
        return panelView;
    }
	public void displayHeader()
	{
	}
	public void displayContents()
	{
	}
	public void setListens()
	{
		panelView.findViewById(R.id.buttonStop).setOnClickListener(this);
		panelView.findViewById(R.id.buttonRestart).setOnClickListener(this);
		panelView.findViewById(R.id.buttonReboot).setOnClickListener(this);
	}
	public void onClick(View clickedView)
    {
    	Ctrl_Actions_Stop.Execute 						stopMessage					= new Ctrl_Actions_Stop().new Execute();
    	if (clickedView instanceof Button)
    	{
    		if      (clickedView.getId()==R.id.buttonStop)		stopMessage.actionRequest		= Ctrl_Actions_Stop.ACTION_Stop;
    		else if (clickedView.getId()==R.id.buttonRestart)	stopMessage.actionRequest		= Ctrl_Actions_Stop.ACTION_Restart;
    		else if (clickedView.getId()==R.id.buttonReboot)	stopMessage.actionRequest		= Ctrl_Actions_Stop.ACTION_Reboot;
        	TCP_Send(stopMessage);
    	}
    }
    @Override
	public void processFinishTCP(Ctrl__Abstract result) 
	{  
		if (result instanceof Ctrl_Actions_Stop.Ack)								Global.toaster("Stop Request accepted", true);
		else   																		Global.toaster("Stop generated error " + result.getClass().toString(), true);
	}
}

