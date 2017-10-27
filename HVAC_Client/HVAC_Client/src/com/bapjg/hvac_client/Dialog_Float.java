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
public class Dialog_Float 										extends 					DialogFragment
{
	private Dialog_Response										callBack;
	private EditText	 										numberPicker;
	private Float												number;
	private Object												parent;
	private String  											message;
	
	public Dialog_Float() 
    {
    }
	public Dialog_Float(Float number, Object parent, String message, Dialog_Response callBack) 
    {
		super();
		this.number																			= number;
		this.parent																			= parent;
		this.callBack																		= callBack;
		this.message																		= message;
    }	
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
    	AlertDialog.Builder 									builder 					= new AlertDialog.Builder(getActivity());
        LayoutInflater 											inflater 					= getActivity().getLayoutInflater();
										                                                    
        View													dialogView					= inflater.inflate(R.layout.dialog_float, null);
        builder.setView(dialogView);
        builder.setTitle(message);
         
		numberPicker 																		= (EditText) dialogView.findViewById(R.id.value);
	    
	    numberPicker.setText("  " + number.toString() + "  ");
	    numberPicker.setSelection(number.toString().indexOf(".") + 2);
       
        builder.setPositiveButton("OK",     new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonOk    (d, w);}});
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonCancel(d, w);}});

        return builder.create();
    }
    public void buttonOk (DialogInterface dialog, int which)
    {
    	Float 												newValue	 					= Float.parseFloat(numberPicker.getText().toString());
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
