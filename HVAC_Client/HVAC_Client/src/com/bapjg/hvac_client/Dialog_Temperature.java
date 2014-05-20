package com.bapjg.hvac_client;

import HVAC_Messages.*;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.*;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

@SuppressLint("ValidFragment")
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Dialog_Temperature 								extends 					DialogFragment
{
	private Dialog_Response										callBack;
	private int						fieldId;
	private NumberPicker 			temperaturePicker;
	private Integer					tempMin;
	private Integer  				step;
	private Integer  				steps;
	private Integer  				tempInitial;
	private Integer					temperature;
	
	public Dialog_Temperature() 
    {
    }
	public Dialog_Temperature(Dialog_Response callBack, int fieldId, Integer temperature, Integer tempMin, Integer step, Integer steps) 
    {
		super();
		this.callBack											= callBack;
		this.fieldId											= fieldId;
		this.temperature										= temperature;
		this.tempMin											= tempMin;
		this.step												= step;
		this.steps												= steps;
		this.tempInitial										= temperature/1000;
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
	    
	    EditText				tempChild						= (EditText) temperaturePicker.getChildAt(0);
//	    tempChild.setOnFocusChangeListener((OnFocusChangeListener) this);
	    tempChild.setFocusable(false);
	    tempChild.setInputType(InputType.TYPE_NULL);
	    
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
     	Integer 				newTemperature 					=(temperaturePicker.getValue() - tempMin) * step + tempMin;
     	temperature												= newTemperature * 1000;
     	callBack.onReturnTemperature(fieldId, temperature);
    	dialog.dismiss();
    }
    public void buttonCancel (DialogInterface dialog, int which)
    {
    	dialog.dismiss();
    }
}
