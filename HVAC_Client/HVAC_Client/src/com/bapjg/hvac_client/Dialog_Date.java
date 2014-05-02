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
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

@SuppressLint("ValidFragment")
public class Dialog_Date 										extends 		DialogFragment 
{
	public DatePicker 	datePicker;
	public Integer  	dateInitialDay;
	public Integer  	dateInitialMonth;
	public Integer  	dateInitialYear;
	public TextView		writeBack;
	
	public Dialog_Date() 
    {
    }
	public Dialog_Date(TextView	writeBack) 
    {
		super();
		this.writeBack											= writeBack;
		
		String 					dateInitial						= writeBack.getText().toString();
		String[] 				dateInitialParts				= dateInitial.split("/");
		
		this.dateInitialDay										= Integer.parseInt(dateInitialParts[0]);
		this.dateInitialMonth									= Integer.parseInt(dateInitialParts[1]);
		this.dateInitialYear									= Integer.parseInt(dateInitialParts[2]);
    }
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
        AlertDialog.Builder 	builder 						= new AlertDialog.Builder(getActivity());
        LayoutInflater 			inflater 						= getActivity().getLayoutInflater();
        
        View					dialogView						= inflater.inflate(R.layout.dialog_date, null);
        builder.setView(dialogView);
        builder.setTitle("Select date");
         
        datePicker 												= (DatePicker) dialogView.findViewById(R.id.dateObjective);
        datePicker.init(dateInitialYear, dateInitialMonth, dateInitialDay, null);

        builder.setPositiveButton("OK",     new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonOk    (d, w);}});
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonCancel(d, w);}});
        return builder.create();
    }
    public void buttonOk (DialogInterface dialog, int which)
    {
     	Integer 				day 							= datePicker.getDayOfMonth();
     	Integer 				month 							= datePicker.getMonth();
     	Integer 				year 							= datePicker.getYear();
     	writeBack.setText(day.toString() + "/" + ((Integer) (month + 1)).toString() + "/" + year.toString());
    	dialog.dismiss();
    }
    public void buttonCancel (DialogInterface dialog, int which)
    {
    	dialog.dismiss();
    }
}
