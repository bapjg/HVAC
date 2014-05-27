package com.bapjg.hvac_client;

import HVAC_Common.*;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Panel_2_Immediate 									extends 					Panel_0_Fragment  
																implements 					TCP_Response
{			
	public String												circuitName;
	private View												panelView;					// This corresponds to the inflated panel (R.layout.panel_n_xxxxxx)
	private Ctrl_Immediate.Execute								messageExecute				= new Ctrl_Immediate().new Execute();
	private Ctrl_Immediate.Data									messageReceived;
	
   public Panel_2_Immediate()
    {
		super();
		this.circuitName																	= "";
    }
    public Panel_2_Immediate(String circuitName)
    {
		super();
		this.circuitName																	= circuitName;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	this.panelView																		= inflater.inflate(R.layout.panel_2_immediate, container, false);
									
    	Ctrl_Immediate.Request									taskListRequest					= new Ctrl_Immediate().new Request();
    	taskListRequest.circuitName																= this.circuitName;
			
    	TCP_Send(taskListRequest);							// This returns list of what is currently active on each circuit

       return panelView;
    }
	public void processFinishTCP(Ctrl__Abstract result) 
	{  
		if 		(result instanceof Ctrl_Immediate.Data)
		{
			messageReceived																		= (Ctrl_Immediate.Data) result;
			messageExecute.timeStart 															= Global.getTimeNowSinceMidnight();
			messageExecute.timeEnd 																= Global.getTimeNowSinceMidnight() + 3600 * 1000L;
			messageExecute.stopOnObjective 														= true;
			messageExecute.tempObjective 														= new Cmn_Temperature(messageReceived.tempObjective.milliDegrees);
		
			displayHeader();
			displayContents();
			setListens();
		}
		else if (result instanceof Ctrl_Immediate.Ack)						Global.toast("Command accepted", false);
		else if (result instanceof Ctrl__Abstract.Ack)						Global.toast("Command accepted", false);
		else if (result instanceof Ctrl_Temperatures.NoConnection)			Global.toast("No Connection established yet", false);
		else																Global.toast("A Nack has been returned", false);
	}
	public void displayHeader()
	{
	}
	public void displayContents()
	{
		// Top part of the screen : "Planned Calendar Events"
		if (messageReceived.executionActive)
		{
			((TextView) 	panelView.findViewById(R.id.plannedTimeStart)).setText			("Current");
			((TextView) 	panelView.findViewById(R.id.plannedTimeEnd)).setText			(Global.displayTimeShort(messageReceived.timeEnd));
			((TextView) 	panelView.findViewById(R.id.plannedTargetTemp)).setText			(messageReceived.tempObjective.displayInteger());
			((CheckBox) 	panelView.findViewById(R.id.plannedStopOnObjective)).setChecked	(messageReceived.stopOnObjective);
			((Button) 		panelView.findViewById(R.id.buttonStartStop)).setText			("Stop");
		}
		else if (messageReceived.executionPlanned)
		{
			((TextView) 	panelView.findViewById(R.id.plannedTimeStart)).setText			(Global.displayTimeShort(messageReceived.timeStart));
			((TextView) 	panelView.findViewById(R.id.plannedTimeEnd)).setText			(Global.displayTimeShort(messageReceived.timeEnd));
			((TextView) 	panelView.findViewById(R.id.plannedTargetTemp)).setText			(messageReceived.tempObjective.displayInteger());
			((CheckBox) 	panelView.findViewById(R.id.plannedStopOnObjective)).setChecked	(messageReceived.stopOnObjective);
			((Button) 		panelView.findViewById(R.id.buttonStartStop)).setText			("Start");
		}
		else
		{
			((TextView) 	panelView.findViewById(R.id.plannedTimeStart)).setText			("No Plan");
			((TextView) 	panelView.findViewById(R.id.plannedTargetTemp)).setText			(" ");
			((Button) 		panelView.findViewById(R.id.buttonStartStop)).setText			("Start");
		}
		// Bottom part of the screen : "Select new parameters"
		if (messageReceived.executionActive)
		{
			((ViewGroup) 	panelView.findViewById(R.id.bottomPart)).setVisibility(View.GONE);
		}
		else
		{
			((TextView) 		panelView.findViewById(R.id.timeStart)).setText				(Global.displayTimeShort(messageExecute.timeStart));
			((TextView) 		panelView.findViewById(R.id.timeEnd)).setText				(Global.displayTimeShort(messageExecute.timeEnd));	
                                                                                            
			((TextView) 		panelView.findViewById(R.id.tempObjective)).setText			(messageExecute.tempObjective.displayInteger());	
			((CheckBox) 		panelView.findViewById(R.id.stopOnObjective)).setChecked	(messageExecute.stopOnObjective);
		}
	}
	public void setListens()
	{
        panelView.findViewById(R.id.buttonStartStop)	.setOnClickListener(this);
    	panelView.findViewById(R.id.tempObjective)		.setOnClickListener(this);
    	panelView.findViewById(R.id.timeStart)			.setOnClickListener(this);
    	panelView.findViewById(R.id.timeEnd)			.setOnClickListener(this);
    	panelView.findViewById(R.id.stopOnObjective)	.setOnClickListener(this);
 	}
	public void onClick(View view)
	{
    	if (view.getId() == R.id.tempObjective)
    	{
    		Dialog_Temperature 									df 							= new Dialog_Temperature(messageExecute.tempObjective, 25, 45, this);
    		df.show(getFragmentManager(), "Dialog_Temperature");
    	}
    	else if (view.getId() == R.id.timeStart)
    	{
    		Dialog_Time		 									df 							= new Dialog_Time(this, R.id.timeStart, messageExecute.timeStart);
    		df.show(getFragmentManager(), "Dialog_Time");
    	}
    	else if (view.getId() == R.id.timeEnd)
    	{
    		Dialog_Time		 									df 							= new Dialog_Time(this, R.id.timeEnd, messageExecute.timeEnd);
    		df.show(getFragmentManager(), "Dialog_Time");
    	}
    	else if (view.getId() == R.id.stopOnObjective)
    	{
    		messageExecute.stopOnObjective													= ! messageExecute.stopOnObjective;
    		displayContents();
    	}
    	else if (view.getId() == R.id.buttonStartStop)
    	{
	    	if (((Button) view).getText().toString().equalsIgnoreCase("Start"))
	    	{
	    		Log.v ("App", "ts " +  messageExecute.timeStart);
	    		Log.v ("App", "tes " +  messageExecute.timeEnd);
	    		Log.v ("App", "tn " +  Global.getTimeNowSinceMidnight());

	    		
	    		if 	((messageExecute.timeStart >= messageExecute.timeEnd))
//	    		|| 	 (Global.getTimeNowSinceMidnight() >= messageExecute.timeStart))
	    		{
	    			Global.toaster("Time start must be after now and before time end", false);
	    		}
	    		else
	    		{
		    		messageExecute.circuitName												= this.circuitName;
			   		messageExecute.action													= messageExecute.ACTION_Start;
			   		messageExecute.stopOnObjective											= ((CheckBox) panelView.findViewById(R.id.stopOnObjective)).isChecked();
					
		        	TCP_Send(messageExecute);
	        	}
	    	}
	    	else if (((Button) view).getText().toString().equalsIgnoreCase("Stop"))
	    	{
	    		messageExecute.circuitName													= this.circuitName;
	    		messageExecute.action														= messageExecute.ACTION_Stop;
	
	        	TCP_Send(messageExecute);
	    	}
    	}
	}
	@Override
	public void onReturnTime(int fieldId, Long value)
	{
		if 		(fieldId == R.id.timeStart)    		messageExecute.timeStart 				= value;
		else if	(fieldId == R.id.timeEnd)    		messageExecute.timeEnd 					= value;
    	displayContents();	
	}
	@Override
	public void onDialogReturn()
	{
		displayHeader();
		displayContents();	
		setListens();	
	}
}

