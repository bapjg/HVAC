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
import android.widget.LinearLayout;
import android.widget.TextView;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Panel_2_Immediate 									extends 					Panel_0_Fragment  
																implements 					TCP_Response
{			
	public String												circuitName;
	private Ctrl_Immediate.Execute								messageExecute				= new Ctrl_Immediate().new Execute();
	private Ctrl_Immediate.Data									messageReceived;
	private Element_Standard									plannedTimeStart;
	private Element_Standard									plannedTimeEnd;
	private Element_Standard									plannedTargetTemp;
	private Element_CheckBox									plannedStopOnObjective;
	private Element_Standard									timeStart;
	private Element_Standard									timeEnd;
	private Element_Standard									targetTemp;
	private Element_CheckBox									stopOnObjective;
	private Element_Button										buttonStartStop;
	private Element_Heading										plannedHeading;
	private Element_Heading										actionHeading;
	
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
    	this.container																		= container;
		this.panelView																		= inflater.inflate(R.layout.panal_0_standard, container, false);
    	displayTitles("Immediate", this.circuitName);
    	
    	Ctrl_Immediate.Request									taskListRequest				= new Ctrl_Immediate().new Request();
    	taskListRequest.circuitName															= this.circuitName;
			
    	TCP_Send(taskListRequest);							// This returns list of what is currently active on each circuit
    	
    	LinearLayout insertPoint = (LinearLayout) panelView.findViewById(R.id.base_insert_point);

    	plannedTimeStart 																	= new Element_Standard(getActivity(), "Time Start");
    	plannedTimeEnd																		= new Element_Standard(getActivity(), "Time End");
    	plannedTargetTemp																	= new Element_Standard(getActivity(), "Temperature");
    	plannedStopOnObjective 																= new Element_CheckBox(getActivity(), "Stop On Objective");
    	timeStart																			= new Element_Standard(getActivity(), "Time Start");
    	timeEnd                 															= new Element_Standard(getActivity(), "Time End");
    	targetTemp          	   															= new Element_Standard(getActivity(), "Temperature");
    	stopOnObjective         															= new Element_CheckBox(getActivity(), "Stop On Objective");
    	buttonStartStop																		= new Element_Button(getActivity(), "");
    	plannedHeading																		= new Element_Heading(getActivity(), "Planned Events");
    	actionHeading																		= new Element_Heading(getActivity(), "Select Parameters");

    	insertPoint.addView(plannedHeading);
    	insertPoint.addView(plannedTimeStart);
    	insertPoint.addView(plannedTimeEnd);
    	insertPoint.addView(plannedTargetTemp);
    	insertPoint.addView(plannedStopOnObjective);
    	
    	insertPoint.addView(actionHeading);
    	insertPoint.addView(timeStart);
    	insertPoint.addView(timeEnd);
    	insertPoint.addView(targetTemp);
    	insertPoint.addView(stopOnObjective);

    	insertPoint.addView(buttonStartStop);

        return panelView;
    }
	public void processFinishTCP(Ctrl__Abstract result) 
	{  
		super.processFinishTCP(result);
		if 		(result instanceof Ctrl_Immediate.Data)
		{
			messageReceived																	= (Ctrl_Immediate.Data) result;
			messageExecute.timeStart 														= new Cmn_Time(Global.displayTimeShort(Global.now()));
			messageExecute.timeEnd 															= new Cmn_Time(Global.displayTimeShort(Global.now() + 3600 * 1000L));
			messageExecute.stopOnObjective 													= true;
			messageExecute.tempObjective 													= new Cmn_Temperature(messageReceived.tempObjective.milliDegrees);
		
			displayContents();
			setListens();
		}
		else if (result instanceof Ctrl_Immediate.Ack)						Global.toast("Command accepted", false);
		else if (result instanceof Ctrl__Abstract.Ack)						Global.toast("Command accepted", false);
		else if (result instanceof Ctrl_Temperatures.NoConnection)			Global.toast("No Connection established yet", false);
		else																Global.toast("A Nack has been returned", false);
	}
	public void displayContents()
	{
		// Top part of the screen : "Planned Calendar Events"
		if (messageReceived.executionActive)
		{
			plannedTimeStart			.setTextRight	("Current");
			plannedTimeEnd				.setTextRight	(Global.displayTimeShort(messageReceived.timeEnd));
			plannedTargetTemp			.setTextRight	(messageReceived.tempObjective.displayInteger());
			plannedStopOnObjective		.setChecked		(messageReceived.stopOnObjective);
			buttonStartStop				.setText		("Stop");
		}
		else if (messageReceived.executionPlanned)
		{
			plannedTimeStart			.setTextRight	(Global.displayTimeShort(messageReceived.timeStart));
			plannedTimeEnd				.setTextRight	(Global.displayTimeShort(messageReceived.timeEnd));
			plannedTargetTemp			.setTextRight	(messageReceived.tempObjective.displayInteger());
			plannedStopOnObjective		.setChecked		(messageReceived.stopOnObjective);
			buttonStartStop				.setText		("Start");
		}
		else
		{
			plannedTimeStart			.setTextRight	("No Plan");
			plannedTimeEnd				.setTextRight	(" ");
			plannedTargetTemp			.setTextRight	(" ");
			plannedStopOnObjective		.setChecked		(false);
			buttonStartStop				.setText		("Start");
		}
		
		// Bottom part of the screen : "Select new parameters"
		if (messageReceived.executionActive)
		{
			actionHeading				.setVisibility	(View.GONE);
		   	timeStart					.setVisibility	(View.GONE);
	    	timeEnd						.setVisibility	(View.GONE);
	    	targetTemp					.setVisibility	(View.GONE);
	    	stopOnObjective				.setVisibility	(View.GONE);
		}
		else
		{
			actionHeading				.setVisibility	(View.VISIBLE);
		   	timeStart					.setVisibility	(View.VISIBLE);
	    	timeEnd						.setVisibility	(View.VISIBLE);
	    	targetTemp					.setVisibility	(View.VISIBLE);
	    	stopOnObjective				.setVisibility	(View.VISIBLE);

	    	timeStart					.setTextRight	(messageExecute.timeStart.displayShort());
			timeEnd						.setTextRight	(messageExecute.timeEnd.displayShort());	
			targetTemp					.setTextRight	(messageExecute.tempObjective.displayInteger());	
			stopOnObjective				.setChecked		(messageExecute.stopOnObjective);
		}
	}
	public void setListens()
	{
    	targetTemp						.setOnClickListener(this);
    	timeStart						.setOnClickListener(this);
    	timeEnd							.setOnClickListener(this);
    	stopOnObjective					.setOnClickListener(this);
    	stopOnObjective.checkBox		.setOnClickListener(this);
    	buttonStartStop.button			.setOnClickListener(this);
 	}
	public void onClick(View clickedview)
	{

		// New code
    	if (clickedview == targetTemp)
    	{
    		Dialog_Temperature 									df 							= new Dialog_Temperature(messageExecute.tempObjective, 25, 45, this);
    		df.show(getFragmentManager(), "Dialog_Temperature");
    	}
    	else if (clickedview == timeStart)
    	{
    		Dialog_Time	 										df 							= new Dialog_Time(messageExecute.timeStart, this);
    		df.show(getFragmentManager(), "Dialog_Time");
    	}
    	else if (clickedview == timeEnd)
    	{
    		Dialog_Time	 										df 							= new Dialog_Time(messageExecute.timeEnd, this);
    		df.show(getFragmentManager(), "Dialog_Time");
    	}
    	else if ((clickedview == stopOnObjective)
        ||       (clickedview == stopOnObjective.checkBox))
        	{
    		messageExecute.stopOnObjective													= ! messageExecute.stopOnObjective;
    		displayContents();
    	}
    	else if (clickedview == buttonStartStop.button)
    	{
	    	if (buttonStartStop.button.getText().toString().equalsIgnoreCase("Start"))
	    	{
	    		if 	((messageExecute.timeStart.milliSeconds >= messageExecute.timeEnd.milliSeconds)
	    		|| 	 (Global.getTimeNowSinceMidnight() > messageExecute.timeStart.milliSeconds))
	    		{
	    			Global.toaster("Time start must be after now and before time end", false);
	    		}
	    		else
	    		{
		    		messageExecute.circuitName												= this.circuitName;
			   		messageExecute.action													= messageExecute.ACTION_Start;
			   		messageExecute.stopOnObjective											= stopOnObjective.isChecked();
					
		        	TCP_Send(messageExecute);
	        	}
	    	}
	    	else if (buttonStartStop.button.getText().toString().equalsIgnoreCase("Stop"))
	    	{
	    		messageExecute.circuitName													= this.circuitName;
	    		messageExecute.action														= messageExecute.ACTION_Stop;
	
	        	TCP_Send(messageExecute);
	    	}
    	}

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//Old code
		
//		if (view.getId() == R.id.tempObjective)
//    	{
//    		Dialog_Temperature 									df 							= new Dialog_Temperature(messageExecute.tempObjective, 25, 45, this);
//    		df.show(getFragmentManager(), "Dialog_Temperature");
//    	}
//    	else if (view.getId() == R.id.timeStart)
//    	{
//    		Dialog_Time	 										df 							= new Dialog_Time(messageExecute.timeStart, this);
//    		df.show(getFragmentManager(), "Dialog_Time");
//    	}
//    	else if (view.getId() == R.id.timeEnd)
//    	{
//    		Dialog_Time	 										df 							= new Dialog_Time(messageExecute.timeEnd, this);
//    		df.show(getFragmentManager(), "Dialog_Time");
//    	}
//    	else if (view.getId() == R.id.stopOnObjective)
//    	{
//    		messageExecute.stopOnObjective													= ! messageExecute.stopOnObjective;
//    		displayContents();
//    	}
//    	else if (view.getId() == R.id.buttonStartStop)
//    	{
//	    	if (((Button) view).getText().toString().equalsIgnoreCase("Start"))
//	    	{
//	    		Log.v ("App", "ts " +  messageExecute.timeStart);
//	    		Log.v ("App", "tes " +  messageExecute.timeEnd);
//	    		Log.v ("App", "tn " +  Global.getTimeNowSinceMidnight());
//
//	    		
//	    		if 	((messageExecute.timeStart.milliSeconds >= messageExecute.timeEnd.milliSeconds))
////	    		|| 	 (Global.getTimeNowSinceMidnight() >= messageExecute.timeStart))
//	    		{
//	    			Global.toaster("Time start must be after now and before time end", false);
//	    		}
//	    		else
//	    		{
//		    		messageExecute.circuitName												= this.circuitName;
//			   		messageExecute.action													= messageExecute.ACTION_Start;
//			   		messageExecute.stopOnObjective											= ((CheckBox) panelView.findViewById(R.id.stopOnObjective)).isChecked();
//					
//		        	TCP_Send(messageExecute);
//	        	}
//	    	}
//	    	else if (((Button) view).getText().toString().equalsIgnoreCase("Stop"))
//	    	{
//	    		messageExecute.circuitName													= this.circuitName;
//	    		messageExecute.action														= messageExecute.ACTION_Stop;
//	
//	        	TCP_Send(messageExecute);
//	    	}
//    	}
	}
	@Override
	public void onDialogReturn()
	{
		displayContents();	
		setListens();	
	}
}

