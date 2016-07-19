package com.bapjg.hvac_client;

import HVAC_Common.*;
import HVAC_Common.Ctrl_Actions_Test_Mail.Execute;
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
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Panel_7_Reset_Stop 								extends 					Panel_0_Fragment  
{
	private	Element_Button										buttonStop;
	private	Element_Button										buttonRestart;
	private	Element_Button										buttonReboot;
	private	Element_Button										buttonShutDown;
	private	Element_Button										buttonDebugWait;
	private	Element_Button										buttonDebugNoWait;

	public Panel_7_Reset_Stop()
	{
		super("Centered");
	}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);
    	
    	buttonStop																			= new Element_Button("Stop HVAC System\n(Go to Bash)");
    	buttonRestart																		= new Element_Button("Restart HVAC Application");
    	buttonReboot																		= new Element_Button("Reboot HVAC Controler Completely");
    	buttonShutDown																		= new Element_Button("ShutDown HVAC Controler Completely");
    	buttonDebugWait																		= new Element_Button("Debug HVAC Controler : Wait");
    	buttonDebugNoWait																	= new Element_Button("Debug HVAC Controler : No Wait");
    	
    	panelInsertPoint.addView(buttonStop);
    	panelInsertPoint.addView(buttonRestart);
    	panelInsertPoint.addView(buttonReboot);
    	panelInsertPoint.addView(buttonShutDown);
    	panelInsertPoint.addView(buttonDebugWait);
    	panelInsertPoint.addView(buttonDebugNoWait);
    	
    	displayTitles("Actions", "Stop");
    	displayContents();
        setListens();
   	
        return panelView;
    }
	public void displayContents()
	{
	}
	public void setListens()
	{
		buttonStop			.setListener(this);
		buttonRestart		.setListener(this);
		buttonReboot		.setListener(this);
		buttonShutDown		.setListener(this);
		buttonDebugWait		.setListener(this);
		buttonDebugNoWait	.setListener(this);
	}
	public void onElementClick(View clickedView)
    {
    	Ctrl_Actions_Stop.Execute 								stopMessage					= new Ctrl_Actions_Stop().new Execute();
		if      (clickedView == buttonStop)			stopMessage.actionRequest	= Ctrl_Actions_Stop.ACTION_Stop;
		else if (clickedView == buttonRestart)		stopMessage.actionRequest	= Ctrl_Actions_Stop.ACTION_Restart;
		else if (clickedView == buttonReboot)		stopMessage.actionRequest	= Ctrl_Actions_Stop.ACTION_Reboot;
		else if (clickedView == buttonShutDown)		stopMessage.actionRequest	= Ctrl_Actions_Stop.ACTION_ShutDown;
		else if (clickedView == buttonDebugWait)	stopMessage.actionRequest	= Ctrl_Actions_Stop.ACTION_Debug_Wait;
		else if (clickedView == buttonDebugNoWait)	stopMessage.actionRequest	= Ctrl_Actions_Stop.ACTION_Debug_NoWait;
    	TCP_Send(stopMessage);
    }
    @Override
	public void processFinishTCP(Ctrl__Abstract result) 
	{  
		super.processFinishTCP(result);
		if (result instanceof Ctrl_Actions_Stop.Ack)			Global.toaster("Stop Request accepted", true);
		else   													Global.toaster("Stop generated error " + result.getClass().toString(), true);
	}
}

