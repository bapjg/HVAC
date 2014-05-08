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
	private View										panelView;				// This corresponds to the inflated panel (R.layout.panel_n_xxxxxx)
	private Ctrl_Immediate.Execute						messageExecute				= new Ctrl_Immediate().new Execute();
	
    public Panel_2_Immediate(String circuitName)
    {
		super();
		this.circuitName															= circuitName;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	this.panelView																= inflater.inflate(R.layout.panel_2_immediate, container, false);
				
    	Ctrl_Immediate.Request							taskRequest					= new Ctrl_Immediate().new Request();
    	taskRequest.circuitName														= this.circuitName;
    	
    	TCP_Send(taskRequest);							// This returns list of what is currently active on each circuit

    	panelView.findViewById(R.id.buttonOk).setOnClickListener((OnClickListener) this);
    	panelView.findViewById(R.id.RowTemp).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {rowTempClick(v);}});
    	panelView.findViewById(R.id.RowTime).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {rowTimeClick(v);}});
        return panelView;
    }
	public void rowTempClick(View myView) 
	{
		TextView										writeBack					= (TextView) ((ViewGroup) myView).getChildAt(1);
		Dialog_Temperature_Old 							df 							= new Dialog_Temperature_Old(writeBack, 25, 5, 8);
		df.show(getFragmentManager(), "Dialog_Temperature");
	}
	public void rowTimeClick(View myView) 
	{
		TextView										writeBack					= (TextView) ((ViewGroup) myView).getChildAt(1);
		Dialog_Time_Old		 							df 							= new Dialog_Time_Old(writeBack);
		df.show(getFragmentManager(), "Dialog_Time");
	}
	public void onClick(View myView)
	{
    	Button 											myButton 					= (Button) myView;
    	String											myCaption					= myButton.getText().toString();
    	
    	if (myCaption.equalsIgnoreCase("Start"))
    	{
    		messageExecute.circuitName												= this.circuitName;
	   		TextView									temp						= (TextView) getActivity().findViewById(R.id.TempObjective);
	   		
	   		messageExecute.tempObjective											= Integer.parseInt(temp.getText().toString()) * 1000;
							
	   		if (this.circuitName.equalsIgnoreCase("Hot_Water"))				
	   		{				
	   			messageExecute.stopOnObjective										= true;
	   		}				
	   		else				
	   		{				
	   			messageExecute.stopOnObjective										= false;
	   		}				
	   		messageExecute.action													= messageExecute.ACTION_Start;
							
	   		temp																	= (TextView) getActivity().findViewById(R.id.TimeEnd);
	   		messageExecute.timeEnd													= Global.parseTime(temp.getText().toString());
			
        	TCP_Send(messageExecute);
    	}
    	else if (myCaption.equalsIgnoreCase("Stop"))
    	{
    		messageExecute.circuitName												= this.circuitName;
    		messageExecute.action													= messageExecute.ACTION_Stop;

        	TCP_Send(messageExecute);
    	}
	}
	public void processFinishTCP(Ctrl_Abstract result) 
	{  
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
		
		messageExecute.tempObjective = 3;
		messageExecute.timeEnd = "zz";
		
		
		
		
		TextView									timeEnd						= (TextView) panelView.findViewById(R.id.TimeEnd);
		TextView									tempObjective				= (TextView) panelView.findViewById(R.id.TempObjective);
		
		timeEnd.setText(Global.displayTimeShort(msg_received.timeStart + 60 * 60 * 1000));
		tempObjective.setText(((Integer) (msg_received.tempObjective/1000)).toString());
		
		if (msg_received.executionActive)
		{
			((TextView) 	panelView.findViewById(R.id.TimeStart)).setText		("Current");
			((TextView) 	panelView.findViewById(R.id.TargetTemp)).setText	(((Integer) (msg_received.tempObjective/1000)).toString());
			((Button) 		panelView.findViewById(R.id.buttonOk)).setText		("Stop");
			((View) 		panelView.findViewById(R.id.RowTitle)).setVisibility(View.GONE);
			((View) 		panelView.findViewById(R.id.RowTime)).setVisibility	(View.GONE);
			((View) 		panelView.findViewById(R.id.RowTemp)).setVisibility	(View.GONE);
			
		}
		else if (msg_received.executionPlanned)
		{
			((TextView) 	panelView.findViewById(R.id.TimeStart)).setText		(Global.displayTimeShort(msg_received.timeStart));
			((TextView) 	panelView.findViewById(R.id.TargetTemp)).setText	(((Integer) (msg_received.tempObjective/1000)).toString());
			((Button) 		panelView.findViewById(R.id.buttonOk)).setText		("Start");
		}
		else
		{
			((TextView) 	panelView.findViewById(R.id.TimeStart)).setText		("No Plan");
			((TextView) 	panelView.findViewById(R.id.TargetTemp)).setText	(" ");
			((Button) 		panelView.findViewById(R.id.buttonOk)).setText		("Start");
		}
	}
}

