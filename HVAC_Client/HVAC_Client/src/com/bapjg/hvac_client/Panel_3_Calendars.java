package com.bapjg.hvac_client;

import HVAC_Messages.Ctrl_Abstract;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;


@SuppressLint("ValidFragment")
//Template										variable			= something
//Template										ext/imp				class
public class Panel_3_Calendars 					extends 			Panel_0_Fragment
												implements 			TCP_Response
{
	public Panel_3_Calendars()
	{
		super();
	}
    public Panel_3_Calendars(int menuLayout)
    {
		super(menuLayout);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.panel_3_calendars, container, false);
    }
    @Override
	public void onClick(View myView) 
	{
    	System.out.println("We have arrived in onClick again");
    	
    	Button 									myButton 			= (Button) myView;
    	String									myCaption			= myButton.getText().toString();
    	
		// Set all textColours to white
		ViewGroup 								viewParent			= (ViewGroup) myView.getParent();
		for (int i = 0; i < viewParent.getChildCount(); i++)
		{
			Button								buttonChild 		= (Button) viewParent.getChildAt(i);
			buttonChild.setTextColor(Color.WHITE);
		}
		
		((Button) myView).setTextColor(Color.YELLOW);
    	
    	if (myCaption.equalsIgnoreCase("Thermometers"))
    	{
    		// buttonThermometersClick(myView);	
    	}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		// TODO Auto-generated method stub
		
	}
	public void TCP_Send(Ctrl_Abstract message)
	{
		TCP_Task								task				= new TCP_Task();
	   	task.callBack												= this;					// processFinish
	   	task.execute(message);
	}
	public void processFinish(Ctrl_Abstract result) 
	{
		Activity								activity			= getActivity();		
	}
}

