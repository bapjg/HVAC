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
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class Panel_2_Immediate 		extends 	Panel_0_Fragment  
									implements 	TCP_Response					// , Dialog_Response
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

    	Ctrl_Immediate.Request			taskRequest			= new Ctrl_Immediate().new Request();
    	taskRequest.circuitName								= this.circuitName;
    	
    	TCP_Task						task				= new TCP_Task();
    	task.callBack										= this;
    	task.execute(taskRequest);

    	thisView.findViewById(R.id.buttonOk).setOnClickListener((OnClickListener) this);
    	thisView.findViewById(R.id.RowTime).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {rowTimeClick(v);}});
    	thisView.findViewById(R.id.RowTemp).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {rowTempClick(v);}});
        return thisView;
    }
	public void rowTempClick(View myView) 
	{
		Dialog_Temperature df = new Dialog_Temperature();
		
		df.tempMin		= 30;
		df.step			= 5;
		df.steps		= 7;
		df.tempInitial  = 35;
//		df.callBack	  	= this;
		df.show(getFragmentManager(), "Dialog_Temperature");
		df.writeBack	= (TextView) ((ViewGroup) myView).getChildAt(1);
	}
	public void rowTimeClick(View myView) 
	{
		 
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
 
//			NumberPicker 					np 					= (NumberPicker) getActivity().findViewById(R.id.tempObjective);
	   		message_out.circuitName								= this.circuitName;
	   		TextView						temp				= (TextView) getActivity().findViewById(R.id.TempObjective);
	   		
	   		message_out.tempObjective							= Integer.parseInt(temp.getText().toString()) * 1000;
	   		
	   		if (this.circuitName.equalsIgnoreCase("Hot_Water"))
	   		{
	   			message_out.stopOnObjective						= true;
	   		}
	   		else
	   		{
	   			message_out.stopOnObjective						= false;
	   		}
	   		message_out.action									= message_out.ACTION_Start;
	   		
	   		temp												= (TextView) getActivity().findViewById(R.id.TimeEnd);
	   		message_out.timeEnd									= Global.parseTime(temp.getText().toString());
			
        	TCP_Task						task				= new TCP_Task();
        	task.callBack										= this;
        	task.execute(message_out);
    	}
    	else if (myCaption.equalsIgnoreCase("Stop"))
    	{
    		System.out.println("Action "+ this.circuitName + " Stop Click");

    		Ctrl_Immediate.Execute			message_out			= new Ctrl_Immediate().new Execute();
	   		message_out.circuitName								= this.circuitName;
	   		message_out.action									= message_out.ACTION_Stop;

	
        	TCP_Task						task				= new TCP_Task();
        	task.callBack										= this;
        	task.execute(message_out);
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
			
			TextView		timeEnd				= (TextView) a.findViewById(R.id.TimeEnd);
			TextView		tempObjective		= (TextView) a.findViewById(R.id.TempObjective);
			
			timeEnd.setText(Global.displayTimeShort(msg_received.timeStart + 60 * 60 * 1000));
			tempObjective.setText(((Integer) (msg_received.tempObjective/1000)).toString());
			
//			TimePicker tp 						= (TimePicker) a.findViewById(R.id.timeEnd);
//			tp.setIs24HourView(true);
//

			if (msg_received.executionActive)
			{
				((TextView) a.findViewById(R.id.TimeStart)).setText("Current");
				((TextView) a.findViewById(R.id.TargetTemp)).setText(((Integer) (msg_received.tempObjective/1000)).toString());
				((Button) a.findViewById(R.id.buttonOk)).setText("Stop");
				((View) a.findViewById(R.id.RowTitle)).setVisibility(View.GONE);
				((View) a.findViewById(R.id.RowTime)).setVisibility(View.GONE);
				((View) a.findViewById(R.id.RowTemp)).setVisibility(View.GONE);
				
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
	public void onTemperatureChange(Integer temperature)
	{
//		System.out.println("temp is" + temperature);
//		((TextView) getActivity().findViewById(R.id.TempObjective)).setText(temperature.toString());
	}
}

