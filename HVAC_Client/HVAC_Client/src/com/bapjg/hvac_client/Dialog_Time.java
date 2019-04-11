package com.bapjg.hvac_client;

import HVAC_Common.*;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

@SuppressLint("ValidFragment")
//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Dialog_Time 										extends 					DialogFragment 
																implements 					View.OnClickListener 
{
	private Dialog_Response										callBack;
	private TimePicker 											timePicker;
	private Cmn_Time											time;
	private Integer  											timeInitialHour;
	private Integer  											timeInitialMinute;
	
	public Dialog_Time() 
    {
    }
	public Dialog_Time(Cmn_Time time, Dialog_Response callBack) 
    {
		super();
		this.callBack																		= callBack;
		this.time																			= time;
    }
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
        AlertDialog.Builder 									builder 					= new AlertDialog.Builder(getActivity());
        LayoutInflater 											inflater 					= getActivity().getLayoutInflater();
										                                            
        View													dialogView					= inflater.inflate(R.layout.dialog_time, null);
        builder.setView(dialogView);
        builder.setTitle("Select time");
         
		timePicker 																			= (TimePicker) dialogView.findViewById(R.id.timeObjective);
		timePicker.setIs24HourView		(true);
		timePicker.setCurrentHour		(time.hours);
		timePicker.setCurrentMinute		(time.minutes);

		((Button) dialogView.findViewById(R.id.button00)).setOnClickListener(this);
		((Button) dialogView.findViewById(R.id.button15)).setOnClickListener(this);
		((Button) dialogView.findViewById(R.id.button30)).setOnClickListener(this);
		((Button) dialogView.findViewById(R.id.button45)).setOnClickListener(this);

        builder.setPositiveButton("OK",     new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonOk    (d, w);}});
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonCancel(d, w);}});

        return builder.create();
    }
    @Override
    public void onClick(View clickedView)
    {
    	switch (clickedView.getId())
    	{
    	case R.id.button00:
    		timePicker.setCurrentMinute		(0);
    		break;
    	case R.id.button15:
    		timePicker.setCurrentMinute		(15);
    		break;
    	case R.id.button30:
    		timePicker.setCurrentMinute		(30);
    		break;
    	case R.id.button45:
    		timePicker.setCurrentMinute		(45);
    		break;
    	}
    }
    public void buttonOk (DialogInterface dialog, int which)
    {
     	Integer h = timePicker.getCurrentHour();
     	Integer m = timePicker.getCurrentMinute();
    	
    	time.setTime(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
     	callBack.onDialogReturn();
    	dialog.dismiss();
    }
    public void buttonCancel (DialogInterface dialog, int which)
    {
    	dialog.dismiss();
    }
}
