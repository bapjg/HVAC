package com.bapjg.hvac_client;

import java.lang.reflect.Field;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.*;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

@SuppressLint("ValidFragment")
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Dialog_DateTime 									extends 					DialogFragment 
{
	public DatePicker 											datePicker;
	public TimePicker 											timePicker;
	public Long													dateTime;
	public Integer  											dateInitialDay;
	public Integer  											dateInitialMonth;
	public Integer  											dateInitialYear;
	public Integer  											timeInitialHour;
	public Integer  											timeInitialMinute;
	public Object												parent;
	public Dialog_Response										callBack;
	
	public Dialog_DateTime() 
    {
    }
	public Dialog_DateTime(Long dateTime, Object parent, Dialog_Response callBack) 
    {
		super();
		this.callBack																		= callBack;
		this.parent																			= parent;

		this.dateTime																		= dateTime;

		Calendar 												calendar					= Calendar.getInstance();
		calendar.setTimeInMillis(dateTime);

		this.dateInitialDay																	= calendar.get(Calendar.DAY_OF_MONTH);
		this.dateInitialMonth																= calendar.get(Calendar.MONTH) + 1;
		this.dateInitialYear																= calendar.get(Calendar.YEAR);
		this.timeInitialHour																= calendar.get(Calendar.HOUR);
		this.timeInitialMinute																= calendar.get(Calendar.MINUTE);
		
    }
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
        AlertDialog.Builder 	builder 													= new AlertDialog.Builder(getActivity());
        LayoutInflater 			inflater 													= getActivity().getLayoutInflater();
									
        View					dialogView													= inflater.inflate(R.layout.dialog_datetime, null);
        builder.setView(dialogView);							
        builder.setTitle("Select date and time");							
									
        datePicker 																			= (DatePicker) dialogView.findViewById(R.id.dateObjective);
        datePicker.init(dateInitialYear, dateInitialMonth, dateInitialDay, null);

		timePicker 																			= (TimePicker) dialogView.findViewById(R.id.timeObjective);
		timePicker.setIs24HourView		(true);
		timePicker.setCurrentHour		(timeInitialHour);
		timePicker.setCurrentMinute		(timeInitialMinute);

        builder.setPositiveButton("OK",     new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonOk    (d, w);}});
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonCancel(d, w);}});
        return builder.create();
    }
    public void buttonOk (DialogInterface dialog, int which)
    {
     	for (Field field : parent.getClass().getDeclaredFields())  
     	{
     		try 
     		{
				if (dateTime == field.get(parent))
				{
			    	
			    	Calendar 									calendar					= Calendar.getInstance();
			    	calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute()) ;
			    	Long newDateTime = calendar.getTimeInMillis();

			    	field.set(parent, newDateTime);
			    	callBack.onDialogReturn();
			    	dialog.dismiss();
			    	return;
				}
			} 
     		catch (Exception e)
     		{
     			// Do nothing as serialversionUID, this$ etc cause exceptions
     		} 
     	}
     	Global.toaster("Object cannot be identified", false);
    }
    public void buttonCancel (DialogInterface dialog, int which)
    {
    	dialog.dismiss();
    }
}
