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
import android.text.InputType;
import android.util.Log;
import android.view.*;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

@SuppressLint("ValidFragment")
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Dialog_Temperature_New 							extends 					DialogFragment
{
	private Dialog_Response										callBack;
	private NumberPicker 										temperaturePicker;
	private Type_Temperature											temperature;
	private Integer												tempMin;
	private Integer  											tempMax;
	
	public Dialog_Temperature_New() 
    {
    }
	public Dialog_Temperature_New(Type_Temperature temperature, Integer tempMin, Integer tempMax, Dialog_Response callBack) 
    {
		super();
		this.temperature																	= temperature;
		this.tempMin																		= tempMin;
		this.tempMax																		= tempMax;
		this.callBack																		= callBack;
    }	
//	public interface OnTemperatureSelectedListener 
//    {
//        public void onTemperatureSelected(Integer temperature);
//    }
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
    	AlertDialog.Builder 									builder 					= new AlertDialog.Builder(getActivity());
        LayoutInflater 											inflater 					= getActivity().getLayoutInflater();
										                                                    
        View													dialogView					= inflater.inflate(R.layout.dialog_temperature, null);
        builder.setView(dialogView);
        builder.setTitle("Select temperature");
         
		temperaturePicker 																	= (NumberPicker) dialogView.findViewById(R.id.tempObjective);
	    String[] 												temps 						= new String[tempMax - tempMin + 1];
	    for (int i = 0; i < tempMax - tempMin + 1; i++)
	    {
	    	temps[i] 																		= Integer.toString(i + tempMin);
	    }
	    
	    EditText												tempChild					= (EditText) temperaturePicker.getChildAt(0);	// Stop keyboard appearing
	    tempChild.setFocusable(false);
	    tempChild.setInputType(InputType.TYPE_NULL);
	    
	    temperaturePicker.setMinValue(0);
	    temperaturePicker.setMaxValue(tempMax - tempMin + 1);
	    temperaturePicker.setWrapSelectorWheel(false);
	    temperaturePicker.setDisplayedValues(temps);
	    
	    temperaturePicker.setValue(temperature.milliDegrees/1000 - tempMin);				// index of current temperature in the list
       
        builder.setPositiveButton("OK",     new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonOk    (d, w);}});
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonCancel(d, w);}});

        return builder.create();
    }
    public void buttonOk (DialogInterface dialog, int which)
    {
     	Integer 												newTemperature 				= (temperaturePicker.getValue() + tempMin);
     	temperature.milliDegrees															= newTemperature * 1000;
     	callBack.onDialogReturn();
    	dialog.dismiss();
    }
    public void buttonCancel (DialogInterface dialog, int which)
    {
    	dialog.dismiss();
    }
}
