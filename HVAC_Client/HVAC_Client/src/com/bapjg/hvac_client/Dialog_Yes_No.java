package com.bapjg.hvac_client;

import java.lang.reflect.Field;
import java.util.ArrayList;

import HVAC_Common.*;
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
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

@SuppressLint("ValidFragment")
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Dialog_Yes_No 										extends 					DialogFragment 
{
	private String												message;
	private Object												parentObject;
	public  ArrayList <String>									items;
	public  String												itemSelected;
	private Dialog_Response										callBack;
	
	public Dialog_Yes_No() 
    {
    }
	public Dialog_Yes_No(String message, Dialog_Response callBack) 
    {
		super();
		this.message																		= message;
		this.callBack																		= callBack;
    }
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
        AlertDialog.Builder 									builder 					= new AlertDialog.Builder(getActivity());
        LayoutInflater 											inflater 					= getActivity().getLayoutInflater();
										                                                    
        View													dialogView					= inflater.inflate(R.layout.dialog_yes_no, null);
        TextView												messageView					= (TextView) dialogView.findViewById(R.id.message);
        
        messageView.setText(message);
        builder.setTitle(message);
       
        builder.setPositiveButton("OK",     new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonOk    (d, w);}});
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonCancel(d, w);}});

        return builder.create();
    }
    public void buttonOk (DialogInterface dialog, int which)
    {
    	callBack.onDialogReturn();
    	dialog.dismiss();
    }
    public void buttonCancel (DialogInterface dialog, int which)
    {
    	dialog.dismiss();
    }
}
