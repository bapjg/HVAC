package com.bapjg.hvac_client;

import HVAC_Messages.*;
import HVAC_Messages.Ctrl_Temperatures.Request;
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
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class Panel_4_Action 		extends 	Panel_0_Fragment  
									implements 	TCP_Response
{
	public Panel_4_Action()
	{
		super();
	}
    public Panel_4_Action(int menuLayout)
    {
		super(menuLayout);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	this.activity										= getActivity();
    	System.out.println("++Before inflate");
    	View							thisView			= inflater.inflate(R.layout.panel_4_actions_hotwater, container, false);
    	System.out.println("++Before task create");
    	TCP_Task						task				= new TCP_Task();
    	System.out.println("++Before task callback");
    	task.callBack										= this;
    	System.out.println("++Before task execute");
    	task.execute(new Ctrl_Actions_HotWater().new Request());
    	System.out.println("++Before return");

        return thisView;
    }
	public void onClick(View myView) 
	{
    	System.out.println("nonono We have arrived in onClick again");
    	
//    	Button 								myButton 					= (Button) myView;
//    	String								myCaption					= myButton.getText().toString();
//    	
//		// Set all textColours to white
//		ViewGroup 							viewParent					= (ViewGroup) myView.getParent();
//		for (int i = 0; i < viewParent.getChildCount(); i++)
//		{
//			Button							buttonChild 				= (Button) viewParent.getChildAt(i);
//			buttonChild.setTextColor(Color.WHITE);
//		}
//		
//		((Button) myView).setTextColor(Color.YELLOW);
//    	
//    	if (myCaption.equalsIgnoreCase("Thermometers"))
//    	{
//    		// buttonThermometersClick(myView);	
//    	}
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
		else if (result instanceof Ctrl_Actions_HotWater.Data)
		{
			Ctrl_Actions_HotWater.Data msg_received 	= (Ctrl_Actions_HotWater.Data) result;
			
			if (msg_received.executionActive)
			{
				((TextView) a.findViewById(R.id.TimeStart)).setText("Current");
				((TextView) a.findViewById(R.id.TargetTemp)).setText((msg_received.tempObjective/1000));
			}
			else if (msg_received.executionPlanned)
			{
				((TextView) a.findViewById(R.id.TimeStart)).setText(Global.displayTime(msg_received.timeStart));
				((TextView) a.findViewById(R.id.TargetTemp)).setText((msg_received.tempObjective/1000));
			}
			else
			{
				((TextView) a.findViewById(R.id.TimeStart)).setText(" ");
				((TextView) a.findViewById(R.id.TargetTemp)).setText(" ");
			}
			String x = Global.displayTemperature(msg_received.tempObjective);
			System.out.println("x = " + x);
			
			NumberPicker np = (NumberPicker) a.findViewById(R.id.tempObjective);
		    String[] temps = new String[10];
		    for(int i=0; i < temps.length; i++)
		    {
		    	temps[i] = Integer.toString(i*5 + 30);
		    }

		    np.setMinValue(30);
		    np.setMaxValue(80);
		    np.setWrapSelectorWheel(false);
		    np.setDisplayedValues(temps);
		    np.setValue(35);
		}
		else if (result instanceof Ctrl_Actions_HotWater.NoConnection)
		{
			Toast.makeText(a, "No Connection established yet", Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(a, "A Nack has been returned", Toast.LENGTH_SHORT).show();
		}		   
	}

}

