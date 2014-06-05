package com.bapjg.hvac_client;

import java.lang.reflect.Field;

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
public class Dialog_Integer 									extends 					DialogFragment
{
	private Dialog_Response										callBack;
	private NumberPicker 										numberPicker;
	private Integer												number;
	private Object												parent;
	private Integer												numberMin;
	private Integer  											numberMax;
	private String  											message;
	
	public Dialog_Integer() 
    {
    }
	public Dialog_Integer(Integer number, Object parent, Integer numberMin, Integer numberMax, String message, Dialog_Response callBack) 
    {
		super();
		this.number																			= number;
		this.parent																			= parent;
		this.numberMin																		= numberMin;
		this.numberMax																		= numberMax;
		this.callBack																		= callBack;
		this.message																		= message;
		if (number > numberMax)									this.numberMax 				= number;
		if (number < numberMin)									this.numberMin 				= number;
    }	
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
    	AlertDialog.Builder 									builder 					= new AlertDialog.Builder(getActivity());
        LayoutInflater 											inflater 					= getActivity().getLayoutInflater();
										                                                    
        View													dialogView					= inflater.inflate(R.layout.dialog_integer, null);
        builder.setView(dialogView);
        builder.setTitle(message);
         
		numberPicker 																		= (NumberPicker) dialogView.findViewById(R.id.value);
	    
	    EditText												tempChild					= (EditText) numberPicker.getChildAt(0);	// Stop keyboard appearing
	    tempChild.setFocusable(false);
	    tempChild.setInputType(InputType.TYPE_NULL);
	    
	    numberPicker.setMinValue(numberMin);
	    numberPicker.setMaxValue(numberMax);
	    numberPicker.setValue(number);				
	    numberPicker.setWrapSelectorWheel(false);
       
        builder.setPositiveButton("OK",     new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonOk    (d, w);}});
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonCancel(d, w);}});

        return builder.create();
    }
    public void buttonOk (DialogInterface dialog, int which)
    {
     	Integer 												newValue	 				= numberPicker.getValue();
     	// Identify property within parent to modify
     	for (Field field : parent.getClass().getDeclaredFields())  
     	{
     		try 
     		{
				if (number == field.get(parent))
				{
					field.set(parent, newValue);
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
