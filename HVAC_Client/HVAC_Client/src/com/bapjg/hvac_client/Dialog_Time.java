package com.bapjg.hvac_client;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.*;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

@SuppressLint("ValidFragment")
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Dialog_Time 										extends 					DialogFragment 
{
	private Dialog_Response										callBack;
	private int													fieldId;
	private	Boolean												callBackLong;
	private TimePicker 											timePicker;
	private Integer  											timeInitialHour;
	private Integer  											timeInitialMinute;
	
	public Dialog_Time() 
    {
    }
	public Dialog_Time(Dialog_Response callBack, int fieldId, Long time) 
    {
		super();
		this.callBack																		= callBack;
		this.fieldId																		= fieldId;
		
		Long 													hours						= time/3600/1000;
		Long 													minutes						= time/60/1000 - hours * 60L;
		this.timeInitialHour																= hours.intValue();
		this.timeInitialMinute																= minutes.intValue();
		this.callBackLong																	= true;
    }
	public Dialog_Time(Dialog_Response callBack, int fieldId, String time) 
    {
		super();
		this.callBack											= callBack;
		this.fieldId											= fieldId;
		
		String[] 				timeInitialParts				= time.split(":");
		
		this.timeInitialHour									= Integer.parseInt(timeInitialParts[0]);
		this.timeInitialMinute									= Integer.parseInt(timeInitialParts[1]);
		this.callBackLong										= false;
    }
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
        AlertDialog.Builder 	builder 						= new AlertDialog.Builder(getActivity());
        LayoutInflater 			inflater 						= getActivity().getLayoutInflater();
        
        View					dialogView						= inflater.inflate(R.layout.dialog_time, null);
        builder.setView(dialogView);
        builder.setTitle("Select time");
         
		timePicker 												= (TimePicker) dialogView.findViewById(R.id.timeObjective);
		timePicker.setIs24HourView		(true);
		timePicker.setCurrentHour		(timeInitialHour);
		timePicker.setCurrentMinute		(timeInitialMinute);

        builder.setPositiveButton("OK",     new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonOk    (d, w);}});
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonCancel(d, w);}});
        return builder.create();
    }
    public void buttonOk (DialogInterface dialog, int which)
    {
     	Integer 				hour 							= timePicker.getCurrentHour();
     	Integer 				minute 							= timePicker.getCurrentMinute();
     	if (callBackLong)
     	{
    		callBack.onReturnTime(fieldId, hour * 3600 * 1000L + minute * 60 * 1000L);
    	}
     	else
     	{
     		callBack.onReturnTime(fieldId, String.format("%02d", hour)  + ":" +String.format("%02d", minute));
     	}
    	dialog.dismiss();
    }
    public void buttonCancel (DialogInterface dialog, int which)
    {
    	dialog.dismiss();
    }
}
