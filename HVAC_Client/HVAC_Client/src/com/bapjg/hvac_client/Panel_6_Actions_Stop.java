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
public class Panel_6_Actions_Stop 								extends 					Panel_0_Fragment  
{
	private	Element_Button										buttonStop;
	private	Element_Button										buttonRestart;
	private	Element_Button										buttonReboot;

	public Panel_6_Actions_Stop()
	{
		super("Centered");
/*
 * 	TODO TODO TODO
		<RelativeLayout
    	android:layout_width					= "match_parent"
   	 	android:layout_height					= "match_parent"
    	android:layout_gravity					= "center_vertical|center_horizontal"
    	android:background						= "@color/background"
    	android:baselineAligned					= "false"
    	android:orientation						= "vertical" 
	>


	    <Button
	        android:id							= "@+id/buttonStop"
	        android:layout_width				= "wrap_content"
	        android:layout_height				= "wrap_content"
	        android:layout_alignParentTop		= "true"
	        android:layout_centerHorizontal		= "true"
	        android:layout_marginTop			= "30dp"
	        android:text						= "Stop HVAC System\n(Go to Bash)"
	        android:textColor					= "#FFFFFF" 
		/>
*/
	}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);
    	
    	buttonStop																			= new Element_Button("Stop HVAC System\n(Go to Bash)");
    	buttonRestart																		= new Element_Button("Restart HVAC Application");
    	buttonReboot																		= new Element_Button("Reboot HVAC Controler Completely");
    	
    	panelInsertPoint.addView(buttonStop);
    	panelInsertPoint.addView(buttonRestart);
    	panelInsertPoint.addView(buttonReboot);
    	
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
		buttonStop		.setListener(this);
		buttonRestart	.setListener(this);
		buttonReboot	.setListener(this);
	}
	public void onElementClick(View clickedView)
    {
    	Ctrl_Actions_Stop.Execute 								stopMessage					= new Ctrl_Actions_Stop().new Execute();
		if      (clickedView == buttonStop)		stopMessage.actionRequest	= Ctrl_Actions_Stop.ACTION_Stop;
		else if (clickedView == buttonRestart)	stopMessage.actionRequest	= Ctrl_Actions_Stop.ACTION_Restart;
		else if (clickedView == buttonReboot)	stopMessage.actionRequest	= Ctrl_Actions_Stop.ACTION_Reboot;
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

