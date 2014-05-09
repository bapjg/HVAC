package com.bapjg.hvac_client;

import HVAC_Messages.*;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


@SuppressLint("ValidFragment")
public class Panel_2_Immediate 							extends 					Panel_0_Fragment  
														implements 					TCP_Response
{			
	public String										circuitName;
	private View										panelView;				// This corresponds to the inflated panel (R.layout.panel_n_xxxxxx)
	private Ctrl_Immediate.Execute						messageExecute				= new Ctrl_Immediate().new Execute();
	private Ctrl_Immediate.Data							messageReceived;
	
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

    	panelView.findViewById(R.id.buttonOkCancel).setOnClickListener(this);
    	panelView.findViewById(R.id.tempObjective).setOnClickListener(this);
    	panelView.findViewById(R.id.timeStart).setOnClickListener(this);
    	panelView.findViewById(R.id.timeEnd).setOnClickListener(this);
        return panelView;
    }
	public void onClick(View view)
	{
    	if (view.getId() == R.id.tempObjective)
    	{
    		Dialog_Temperature 							df 							= new Dialog_Temperature(this, R.id.tempObjective, 25, 25, 5, 8);
    		df.show(getFragmentManager(), "Dialog_Temperature");
    	}
    	else if (view.getId() == R.id.timeStart)
    	{
    		Dialog_Time		 							df 							= new Dialog_Time(this, R.id.timeStart, messageExecute.timeStart);
    		df.show(getFragmentManager(), "Dialog_Time");
    	}
    	else if (view.getId() == R.id.timeEnd)
    	{
    		Dialog_Time		 							df 							= new Dialog_Time(this, R.id.timeEnd, messageExecute.timeEnd);
    		df.show(getFragmentManager(), "Dialog_Time");
    	}
    	else if (view.getId() == R.id.buttonOkCancel)
    	{
	    	if (((Button) view).getText().toString().equalsIgnoreCase("Start"))
	    	{
	    		if 	((messageExecute.timeStart >= messageExecute.timeEnd))
//	    		|| 	 (Global.getTimeNowSinceMidnight() >= messageExecute.timeStart))
	    		{
	    			Global.toaster("Time start must be after now and before time end", false);
	    		}
	    		else
	    		{
		    		messageExecute.circuitName											= this.circuitName;
			   		messageExecute.action												= messageExecute.ACTION_Start;
			   		messageExecute.stopOnObjective										= ((CheckBox) panelView.findViewById(R.id.stopOnObjective)).isChecked();
					
		        	TCP_Send(messageExecute);
	        	}
	    	}
	    	else if (((Button) view).getText().toString().equalsIgnoreCase("Stop"))
	    	{
	    		messageExecute.circuitName											= this.circuitName;
	    		messageExecute.action												= messageExecute.ACTION_Stop;
	
	        	TCP_Send(messageExecute);
	    	}
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
		messageReceived																= msg_received;

		messageExecute.timeStart 													= Global.getTimeNowSinceMidnight();
		messageExecute.timeEnd 														= Global.getTimeNowSinceMidnight() + 3600 * 1000L;
		messageExecute.stopOnObjective 												= true;
		messageExecute.tempObjective 												= 25 * 1000;
		
		displayContents();
	}
	public void displayContents()
	{
		
		if (messageReceived.executionActive)
		{
			((TextView) 	panelView.findViewById(R.id.plannedTimeStart)).setText			("Current");
			((TextView) 	panelView.findViewById(R.id.plannedTimeEnd)).setText			(Global.displayTimeShort(messageReceived.timeEnd));
			((TextView) 	panelView.findViewById(R.id.plannedTargetTemp)).setText			(((Integer) (messageReceived.tempObjective/1000)).toString());
			((CheckBox) 	panelView.findViewById(R.id.plannedStopOnObjective)).setChecked	(messageReceived.stopOnObjective);
			((Button) 		panelView.findViewById(R.id.buttonOkCancel)).setText			("Stop");
		}
		else if (messageReceived.executionPlanned)
		{
			((TextView) 	panelView.findViewById(R.id.plannedTimeStart)).setText			(Global.displayTimeShort(messageReceived.timeStart));
			((TextView) 	panelView.findViewById(R.id.plannedTimeEnd)).setText			(Global.displayTimeShort(messageReceived.timeEnd));
			((TextView) 	panelView.findViewById(R.id.plannedTargetTemp)).setText			(((Integer) (messageReceived.tempObjective/1000)).toString());
			((CheckBox) 	panelView.findViewById(R.id.plannedStopOnObjective)).setChecked	(messageReceived.stopOnObjective);
			((Button) 		panelView.findViewById(R.id.buttonOkCancel)).setText			("Start");
		}
		else
		{
			((TextView) 	panelView.findViewById(R.id.plannedTimeStart)).setText			("No Plan");
			((TextView) 	panelView.findViewById(R.id.plannedTargetTemp)).setText			(" ");
			((Button) 		panelView.findViewById(R.id.buttonOkCancel)).setText			("Start");
		}
		((TextView) 		panelView.findViewById(R.id.timeStart)).setText					(Global.displayTimeShort(messageExecute.timeStart));
		((TextView) 		panelView.findViewById(R.id.timeEnd)).setText					(Global.displayTimeShort(messageExecute.timeEnd));	

		Integer x = ((Integer) (messageExecute.tempObjective / 1000));	
		((TextView) 		panelView.findViewById(R.id.tempObjective)).setText				(((Integer) (messageExecute.tempObjective / 1000)).toString());	
		((CheckBox) 		panelView.findViewById(R.id.stopOnObjective)).setChecked		(messageExecute.stopOnObjective);
	}
	@Override
	public void processFinishDialogLong(int fieldId, Long value)
	{
		if 		(fieldId == R.id.timeStart)    		messageExecute.timeStart 				= value;
		else if	(fieldId == R.id.timeEnd)    		messageExecute.timeEnd 					= value;
    	displayContents();	
	}
	public void processFinishDialogInteger(int fieldId, Integer value)
	{
    	if      (fieldId == R.id.tempObjective)		messageExecute.tempObjective 			= value;
    	displayContents();
	}
}

