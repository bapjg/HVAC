package com.bapjg.hvac_client;

import HVAC_Common.*;
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
public class Panel_6_Actions_Test_Mail 							extends 					Panel_0_Fragment  
{
	private	Element_Button										buttonSendMail;
	
	public Panel_6_Actions_Test_Mail()
	{
		super("Centered");
	}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);
    	
    	buttonSendMail																		= new Element_Button("Send a test mail");
    	
    	panelInsertPoint.addView(buttonSendMail);
    	
    	displayTitles("Actions", "Test Mail");
    	displayContents();
        setListens();
   	
        return panelView;
    }
	public void displayContents()
	{
	}
	public void setListens()
	{
		buttonSendMail.setListener(this);
	}
    public void onElementClick(View clickedView)
    {
    	if (clickedView == buttonSendMail)
    	{
    		TCP_Send(new Ctrl_Actions_Test_Mail().new Execute());
    	}
    }
 	public void processFinishTCP(Ctrl__Abstract result) 
	{  
		super.processFinishTCP(result);
		if (result instanceof Ctrl_Actions_Test_Mail.Ack)		Global.toaster("eMail sent", true);
		else													Global.toaster("eMail error " + result.getClass().toString(), true);
	}
}

