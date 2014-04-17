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
	public TimePicker 	tp;
	public Integer		tempMin;
	public Integer  	step;
	public Integer  	steps;
	public Integer  	tempInitial;
	public TextView		writeBack;
	
	
	public Dialog_Time() 
    {
    }
	public Dialog_Time(TextView	writeBack, Integer	tempMin, Integer  step,Integer  steps) 
    {
		super();
		this.writeBack											= writeBack;
		this.tempMin											= tempMin;
		this.step												= step;
		this.steps												= steps;
		this.tempInitial										= Integer.parseInt(writeBack.getText().toString());
    }

    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
        AlertDialog.Builder 	builder 						= new AlertDialog.Builder(getActivity());
        LayoutInflater 			inflater 						= getActivity().getLayoutInflater();
        
        View					dialogView						= inflater.inflate(R.layout.dialog_time, null);
        builder.setView(dialogView);

        
        builder.setTitle("Select time");
        
        
		tp 														= (TimePicker) dialogView.findViewById(R.id.timeObjective);
		tp.setIs24HourView(true);

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
