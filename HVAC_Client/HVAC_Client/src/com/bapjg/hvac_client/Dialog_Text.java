package com.bapjg.hvac_client;

import java.lang.reflect.Field;
import java.util.ArrayList;

import HVAC_Common.Ctrl_Calendars;
import HVAC_Common.Ctrl_Configuration;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

@SuppressLint("ValidFragment")
//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Dialog_Text 										extends 					DialogFragment 
{
	private String												item;
	private Object												parentObject;
	private String												message;
	private Dialog_Response										callBack;
	private	EditText											text;
	
	public Dialog_Text() 
    {
    }
	public Dialog_Text(String item, Object parentObject, String message, Dialog_Response callBack) 
    {
		super();
		this.item																			= item;
		this.parentObject																	= parentObject;
		this.message																		= message;
		this.callBack																		= callBack;
    }
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
        AlertDialog.Builder 									builder 					= new AlertDialog.Builder(getActivity());
        LayoutInflater 											inflater 					= getActivity().getLayoutInflater();
										                                                    
        View													dialogView					= inflater.inflate(R.layout.dialog_text, null);
        text																				= (EditText) dialogView.findViewById(R.id.text);
        text.setText(item);
        builder.setView(dialogView);
        builder.setTitle(message);
       
        builder.setPositiveButton("OK",     new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonOk    (d, w);}});
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonCancel(d, w);}});

        return builder.create();
    }
    public void buttonOk (DialogInterface dialog, int which)
    {
     	String	 												newValue	 				= text.getText().toString();
     	// Identify property within parent to modify
     	for (Field field : parentObject.getClass().getDeclaredFields())  
     	{
     		try 
     		{
				if (item == field.get(parentObject))
				{
					field.set(parentObject, newValue);
			    	callBack.onDialogReturn();
			    	dialog.dismiss();
				}
			} 
     		catch (Exception e)
     		{
     			// Do nothing as serialversionUID, this$ etc cause exceptions
     		} 
     	}
    }
    public void buttonCancel (DialogInterface dialog, int which)
    {
    	dialog.dismiss();
    }
}
