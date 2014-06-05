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
public class Dialog_Temperature 							extends 					DialogFragment
{
	private Dialog_Response										callBack;
	private NumberPicker 										temperaturePicker;
	private Cmn_Temperature										temperature;
	private Integer												tempValue;
	private Integer												tempMin;
	private Integer  											tempMax;
	
	public Dialog_Temperature() 
    {
    }
	public Dialog_Temperature(Cmn_Temperature temperature, Integer tempMin, Integer tempMax, Dialog_Response callBack) 
    {
		super();
		this.temperature																	= temperature;
		this.tempMin																		= tempMin;
		this.tempMax																		= tempMax;
		this.tempValue																		= temperature.milliDegrees/1000;
		this.callBack																		= callBack;
		if (this.tempValue > tempMax)							this.tempMax 				= this.tempValue;
		if (this.tempValue < tempMin)							this.tempMin 				= this.tempValue;
    }	
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
    	AlertDialog.Builder 									builder 					= new AlertDialog.Builder(getActivity());
        LayoutInflater 											inflater 					= getActivity().getLayoutInflater();
										                                                    
        View													dialogView					= inflater.inflate(R.layout.dialog_temperature, null);
        builder.setView(dialogView);
        builder.setTitle("Select temperature");
         
		temperaturePicker 																	= (NumberPicker) dialogView.findViewById(R.id.tempObjective);
	    
	    EditText												tempChild					= (EditText) temperaturePicker.getChildAt(0);	// Stop keyboard appearing
	    tempChild.setFocusable(false);
	    tempChild.setInputType(InputType.TYPE_NULL);
	    
	    temperaturePicker.setMinValue(0);
	    temperaturePicker.setMaxValue(tempMax - tempMin + 1);
	    temperaturePicker.setValue(tempValue - tempMin);		
	    temperaturePicker.setFormatter(new NumberPicker.Formatter() 
	    	{
	        	@Override
	        	public String format(int index) 
	        	{
	        		return Integer.toString(index + tempMin);
	        	}
	    	}
	    );	
	    temperaturePicker.setWrapSelectorWheel(false);
       
        builder.setPositiveButton("OK",     new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonOk    (d, w);}});
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonCancel(d, w);}});

        return builder.create();
    }
    public void buttonOk (DialogInterface dialog, int which)
    {
     	Integer 												newTemperature 				= temperaturePicker.getValue() + tempMin;
     	temperature.milliDegrees															= newTemperature * 1000;
     	callBack.onDialogReturn();
    	dialog.dismiss();
    }
    public void buttonCancel (DialogInterface dialog, int which)
    {
    	dialog.dismiss();
    }
}
