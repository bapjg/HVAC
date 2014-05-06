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
import android.util.Log;
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
public class Panel_2_Immediate 							extends 					Panel_0_Fragment  
														implements 					TCP_Response
{			
	public String										circuitName;
	
    public Panel_2_Immediate(String circuitName)
    {
		super();
		this.circuitName															= circuitName;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	View											thisView					= inflater.inflate(R.layout.panel_2_immediate, container, false);
				
    	Ctrl_Immediate.Request							taskRequest					= new Ctrl_Immediate().new Request();
    	taskRequest.circuitName														= this.circuitName;
    	
    	TCP_Send(taskRequest);

    	thisView.findViewById(R.id.buttonOk).setOnClickListener((OnClickListener) this);
    	thisView.findViewById(R.id.RowTemp).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {rowTempClick(v);}});
    	thisView.findViewById(R.id.RowTime).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {rowTimeClick(v);}});
        return thisView;
    }
	public void rowTempClick(View myView) 
	{
		TextView										writeBack					= (TextView) ((ViewGroup) myView).getChildAt(1);
		Dialog_Temperature 								df 							= new Dialog_Temperature(writeBack, 25, 5, 8);
		df.show(getFragmentManager(), "Dialog_Temperature");
	}
	public void rowTimeClick(View myView) 
	{
		TextView										writeBack					= (TextView) ((ViewGroup) myView).getChildAt(1);
		Dialog_Time		 								df 							= new Dialog_Time(writeBack);
		df.show(getFragmentManager(), "Dialog_Time");
	}
	public void onClick(View myView)
	{
    	Button 											myButton 					= (Button) myView;
    	String											myCaption					= myButton.getText().toString();
    	FragmentManager 								fManager					= getFragmentManager();
    	FragmentTransaction								fTransaction;
    	Fragment 										panelFragment;
    	
    	if (myCaption.equalsIgnoreCase("Start"))
    	{
    		Log.v( "App", "Action "+ this.circuitName + " Start Click");

    		Ctrl_Immediate.Execute						message_out					= new Ctrl_Immediate().new Execute();
					
//			NumberPicker 								np 							= (NumberPicker) getActivity().findViewById(R.id.tempObjective);
	   		message_out.circuitName													= this.circuitName;
	   		TextView									temp						= (TextView) getActivity().findViewById(R.id.TempObjective);
	   		
	   		message_out.tempObjective												= Integer.parseInt(temp.getText().toString()) * 1000;
							
	   		if (this.circuitName.equalsIgnoreCase("Hot_Water"))				
	   		{				
	   			message_out.stopOnObjective											= true;
	   		}				
	   		else				
	   		{				
	   			message_out.stopOnObjective											= false;
	   		}				
	   		message_out.action														= message_out.ACTION_Start;
							
	   		temp																	= (TextView) getActivity().findViewById(R.id.TimeEnd);
	   		message_out.timeEnd														= Global.parseTime(temp.getText().toString());
			
        	TCP_Send(message_out);
    	}
    	else if (myCaption.equalsIgnoreCase("Stop"))
    	{
    		Log.v( "App", "Action "+ this.circuitName + " Stop Click");

    		Ctrl_Immediate.Execute						message_out					= new Ctrl_Immediate().new Execute();
	   		message_out.circuitName													= this.circuitName;
	   		message_out.action														= message_out.ACTION_Stop;

        	TCP_Send(message_out);
    	}
	}
	public void processFinishTCP(Ctrl_Abstract result) 
	{  
		Activity										activity					= getActivity();		
		if 		(result instanceof Ctrl_Immediate.Data)						displayContents((Ctrl_Immediate.Data) result);
		else if (result instanceof Ctrl_Immediate.Ack)						Global.toast("Command accepted", false);
		else if (result instanceof Ctrl_Abstract.Ack)						Global.toast("Command accepted", false);
		else if (result instanceof Ctrl_Temperatures.NoConnection)			Global.toast("No Connection established yet", false);
		else																Global.toast("A Nack has been returned", false);
	}
	public void displayHeader()
	{
	}
	public void displayContents(Ctrl_Immediate.Data msg_received)
	{
		TextView									timeEnd						= (TextView) getActivity().findViewById(R.id.TimeEnd);
		TextView									tempObjective				= (TextView) getActivity().findViewById(R.id.TempObjective);
		
		timeEnd.setText(Global.displayTimeShort(msg_received.timeStart + 60 * 60 * 1000));
		tempObjective.setText(((Integer) (msg_received.tempObjective/1000)).toString());
		
		if (msg_received.executionActive)
		{
			((TextView) 	getActivity().findViewById(R.id.TimeStart)).setText		("Current");
			((TextView) 	getActivity().findViewById(R.id.TargetTemp)).setText	(((Integer) (msg_received.tempObjective/1000)).toString());
			((Button) 		getActivity().findViewById(R.id.buttonOk)).setText		("Stop");
			((View) 		getActivity().findViewById(R.id.RowTitle)).setVisibility(View.GONE);
			((View) 		getActivity().findViewById(R.id.RowTime)).setVisibility	(View.GONE);
			((View) 		getActivity().findViewById(R.id.RowTemp)).setVisibility	(View.GONE);
			
		}
		else if (msg_received.executionPlanned)
		{
			((TextView) 	getActivity().findViewById(R.id.TimeStart)).setText		(Global.displayTimeShort(msg_received.timeStart));
			((TextView) 	getActivity().findViewById(R.id.TargetTemp)).setText	(((Integer) (msg_received.tempObjective/1000)).toString());
			((Button) 		getActivity().findViewById(R.id.buttonOk)).setText		("Start");
		}
		else
		{
			((TextView) 	getActivity().findViewById(R.id.TimeStart)).setText		("No Plan");
			((TextView) 	getActivity().findViewById(R.id.TargetTemp)).setText	(" ");
			((Button) 		getActivity().findViewById(R.id.buttonOk)).setText		("Start");
		}
	}
}

