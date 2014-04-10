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
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class Panel_2_Immediate 		extends 	Panel_0_Fragment  
									implements 	TCP_Response
{
	public String						circuitName;
	
	public Panel_2_Immediate()
	{
		super();
		circuitName											= "";
	}
    public Panel_2_Immediate(int menuLayout, String circuitName)
    {
		super(menuLayout);
		this.circuitName									= circuitName;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	this.activity										= getActivity();
    	View							thisView			= inflater.inflate(R.layout.panel_2_immediate, container, false);
    	TCP_Task						task				= new TCP_Task();
    	task.callBack										= this;
    	task.execute(new Ctrl_Actions_HotWater().new Request());

    	thisView.findViewById(R.id.buttonOk).setOnClickListener((OnClickListener) this);
        return thisView;
    }
	public void onClick(View myView) 
	{
    	Button 								myButton 			= (Button) myView;
    	String								myCaption			= myButton.getText().toString();
    	FragmentManager 					fManager			= getFragmentManager();
    	FragmentTransaction					fTransaction;
    	Fragment 							panelFragment;
    	
    	if (myCaption.equalsIgnoreCase("Start"))
    	{
    		System.out.println("Action "+ this.circuitName + " Start Click");

    		Ctrl_Immediate.Execute			message_out			= new Ctrl_Immediate().new Execute();
 
			NumberPicker 					np 					= (NumberPicker) getActivity().findViewById(R.id.tempObjective);
	   		message_out.circuitName								= this.circuitName;
	   		message_out.tempObjective							= ((np.getValue() - 30) * 5 + 30) * 1000; // getValue returns an index
	   		message_out.start									= true;
	   		message_out.stopOnObjective							= true;
//	   		message_out.timeEnd									= 3L;
			
        	TCP_Task						task				= new TCP_Task();
        	task.callBack										= this;
//        	task.execute(message_out);
    	}
    	else if (myCaption.equalsIgnoreCase("Stop"))
    	{
    		System.out.println("Action "+ this.circuitName + " Stop Click");

    		Ctrl_Immediate.Execute			message_out			= new Ctrl_Immediate().new Execute();
	   		message_out.circuitName								= this.circuitName;
	   		message_out.start									= false;

	
        	TCP_Task						task				= new TCP_Task();
        	task.callBack										= this;
//        	task.execute(message_out);
    	}

	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
	}
	public void processFinish(Ctrl_Abstract result) 
	{  
		Activity a							= getActivity();
		
		if (a == null) 
		{
			// Do nothing
		}
		else if (result instanceof Ctrl_Immediate.Data)
		{
			Ctrl_Immediate.Data msg_received 	= (Ctrl_Immediate.Data) result;
			
			TimePicker tp 						= (TimePicker) a.findViewById(R.id.timeEnd);
			tp.setIs24HourView(true);

			NumberPicker np 					= (NumberPicker) a.findViewById(R.id.tempObjective);
		    String[] temps 						= new String[10];
		    for(int i =0; i < temps.length; i++)
		    {
		    	temps[i] 						= Integer.toString(i*5 + 30);
		    }

		    np.setMinValue(30);
		    np.setMaxValue(80);
		    np.setWrapSelectorWheel(false);
		    np.setDisplayedValues(temps);
		    np.setValue(31);									// Min value + increment 5 = 35

			if (msg_received.executionActive)
			{
				((TextView) a.findViewById(R.id.TimeStart)).setText("Current");
				((TextView) a.findViewById(R.id.TargetTemp)).setText(((Integer) (msg_received.tempObjective/1000)).toString());
				((Button) a.findViewById(R.id.buttonOk)).setText("Stop");
				tp.setVisibility(View.GONE);
				np.setVisibility(View.GONE);
			}
			else if (msg_received.executionPlanned)
			{
				((TextView) a.findViewById(R.id.TimeStart)).setText(Global.displayTimeShort(msg_received.timeStart));
				((TextView) a.findViewById(R.id.TargetTemp)).setText(((Integer) (msg_received.tempObjective/1000)).toString());
				((Button) a.findViewById(R.id.buttonOk)).setText("Start");
			}
			else
			{
				((TextView) a.findViewById(R.id.TimeStart)).setText("No Plan");
				((TextView) a.findViewById(R.id.TargetTemp)).setText(" ");
				((Button) a.findViewById(R.id.buttonOk)).setText("Start");
			}
		}
		else if (result instanceof Ctrl_Immediate.NoConnection)
		{
			Global.toast("No Connection established yet", true);
		}
		else if ((result instanceof Ctrl_Immediate.Ack) || (result instanceof Ctrl_Abstract.Ack))
		{
			Global.toast("Command accepted", true);
		}
		else
		{
			Global.toast("A Nack has been returned", true);
		}		   
	}
}

