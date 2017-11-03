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
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

@SuppressLint("ValidFragment")
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class _Dialog_Number_Test_THROW_AWAY 									extends 					DialogFragment
{
	private Dialog_Response										callBack;
	private NumberPicker 										numberPicker;
	private Integer												number;
	private Object												parent;
	private Integer												numberMin;
	private Integer  											numberMax;
	private String  											message;
	
	public _Dialog_Number_Test_THROW_AWAY() 
    {
		super();
		this.number																			= 51;
		this.numberMin																		= 31;
		this.numberMax																		= 66;
		this.message																		= "henry";
    }
	public _Dialog_Number_Test_THROW_AWAY(Integer number, Object parent, Integer numberMin, Integer numberMax, String message, Dialog_Response callBack) 
    {
		super();
		this.number																			= number;
		this.parent																			= parent;
		this.numberMin																		= numberMin;
		this.numberMax																		= numberMax;
		this.callBack																		= callBack;
		this.message																		= message;
		if (number == null    )
		{
			this.number																		= (numberMin + numberMax)/2;
		}
		else
		{
			if (number > numberMax)								this.numberMax 				= number;
			if (number < numberMin)								this.numberMin 				= number;
		}
    }	
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
    	AlertDialog.Builder 									builder 					= new AlertDialog.Builder(getActivity());
        LayoutInflater 											inflater 					= getActivity().getLayoutInflater();
										                                                    
        View													dialogView					= inflater.inflate(R.layout.a_dialog_number_test_throw_away, null);
        
        NumberPicker np1 = new NumberPicker(getActivity());
        np1.setMinValue(0);
        np1.setMaxValue(9);
        np1.setValue(3);  
        np1.setGravity(Gravity.CENTER_HORIZONTAL);
        np1.setMinimumWidth(99);

	    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    params.weight = 1.0f;
	    params.gravity = Gravity.CENTER_HORIZONTAL;

        np1.setLayoutParams(params);
        
        ((LinearLayout) dialogView).addView(np1);
        
        builder.setView(dialogView);
        builder.setTitle(message);
         
	    EditText												tempChild					= (EditText) np1.getChildAt(0);	// Stop keyboard appearing
	    tempChild.setFocusable(false);
	    tempChild.setInputType(InputType.TYPE_NULL);

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
