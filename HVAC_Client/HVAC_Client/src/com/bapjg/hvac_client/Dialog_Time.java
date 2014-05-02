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
public class Dialog_Time 										extends 		DialogFragment 
{
	public TimePicker 	timePicker;
	public Integer  	timeInitialHours;
	public Integer  	timeInitialMinutes;
	public TextView		writeBack;
	
	public Dialog_Time() 
    {
    }
	public Dialog_Time(TextView	writeBack) 
    {
		super();
		this.writeBack											= writeBack;
		
		String timeInitial										= writeBack.getText().toString();
		String[] timeInitialParts								= timeInitial.split(":");
		
		this.timeInitialHours									= Integer.parseInt(timeInitialParts[0]);
		this.timeInitialMinutes									= Integer.parseInt(timeInitialParts[1]);
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
		timePicker.setCurrentHour		(timeInitialHours);
		timePicker.setCurrentMinute		(timeInitialMinutes);
		timePicker.setIs24HourView		(true);

        builder.setPositiveButton("OK",     new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonOk    (d, w);}});
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonCancel(d, w);}});
        return builder.create();
    }
    public void buttonOk (DialogInterface dialog, int which)
    {
//     	Integer temperature =(np.getValue() - tempMin) * step + tempMin;
//     	writeBack.setText(temperature.toString());
    	dialog.dismiss();
    }
    public void buttonCancel (DialogInterface dialog, int which)
    {
    	dialog.dismiss();
    }
}
