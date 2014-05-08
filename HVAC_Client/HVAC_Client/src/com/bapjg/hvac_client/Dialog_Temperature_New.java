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

@SuppressLint("ValidFragment")
public class Dialog_Temperature_New 								extends 		DialogFragment 
{
	public Dialog_Response			callBack;

	public NumberPicker 			temperaturePicker;
	public Integer					tempMin;
	public Integer  				step;
	public Integer  				steps;
	public Integer  				tempInitial;
	public Integer					writeBack;
	
	
	public Dialog_Temperature_New() 
    {
    }
	public Dialog_Temperature_New(Dialog_Response callBack, Integer writeBack, Integer tempMin, Integer step, Integer steps) 
    {
		super();
		this.callBack											= callBack;
		this.writeBack											= writeBack;
		this.tempMin											= tempMin;
		this.step												= step;
		this.steps												= steps;
		this.tempInitial										= writeBack/1000;
    }

	public interface OnTemperatureSelectedListener 
    {
        public void onTemperatureSelected(Integer temperature);
    }

    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
        AlertDialog.Builder 	builder 						= new AlertDialog.Builder(getActivity());
        LayoutInflater 			inflater 						= getActivity().getLayoutInflater();
        
        View					dialogView						= inflater.inflate(R.layout.dialog_temperature, null);
        builder.setView(dialogView);
        builder.setTitle("Select temperature");
         
		temperaturePicker 										= (NumberPicker) dialogView.findViewById(R.id.tempObjective);
	    String[] 				temps 							= new String[steps + 1];
	    for(int i = 0; i < steps + 1; i++)
	    {
	    	temps[i] 											= Integer.toString(i * step + tempMin);
	    }

	    temperaturePicker.setMinValue(tempMin);
	    temperaturePicker.setMaxValue(tempMin + steps);
	    temperaturePicker.setWrapSelectorWheel(false);
	    temperaturePicker.setDisplayedValues(temps);
	    temperaturePicker.setValue(tempMin + (tempInitial - tempMin)/step);		// Min + index
       
        builder.setPositiveButton("OK",     new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonOk    (d, w);}});
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonCancel(d, w);}});

        return builder.create();
    }
    public void buttonOk (DialogInterface dialog, int which)
    {
     	Integer temperature =(temperaturePicker.getValue() - tempMin) * step + tempMin;
     	writeBack												= temperature * 1000;
     	callBack.processFinishDialog();
    	dialog.dismiss();
    }
    public void buttonCancel (DialogInterface dialog, int which)
    {
    	dialog.dismiss();
    }
}
