package com.bapjg.hvac_client;

import java.util.ArrayList;

import HVAC_Messages.Ctrl_Calendars;
import HVAC_Messages.Ctrl_Configuration;
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
public class Dialog_String_List 								extends 					DialogFragment 
																implements					AdapterView.OnItemClickListener	
{
	private Dialog_Response										callBack;
	private int													fieldId;
	public  ArrayList <String>									items;
	public  String												itemSelected;
	
	public Dialog_String_List() 
    {
    }
	public Dialog_String_List(Dialog_Response callBack, int fieldId) 
    {
		super();
		this.callBack																		= callBack;
		this.fieldId																		= fieldId;
		this.items 																			= new ArrayList<String>();
    }
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
        AlertDialog.Builder 									builder 					= new AlertDialog.Builder(getActivity());
        LayoutInflater 											inflater 					= getActivity().getLayoutInflater();
										                                                    
        View													dialogView					= inflater.inflate(R.layout.dialog_string_list, null);
        AdapterView												adapterView					= (AdapterView) dialogView.findViewById(R.id.List_View);
        
//        items.add("Henry");
//        items.add("3ry");
        
        AdapterView <Adapter_0_String_List>						adapterViewList				= (AdapterView <Adapter_0_String_List>) adapterView;
        Adapter_0_String_List									arrayAdapter				= new Adapter_0_String_List(Global.actContext, R.id.List_View, items);
       
//        arrayAdapter.add("Me");
        
        adapterView.setAdapter(arrayAdapter);
        builder.setView(dialogView);
        builder.setTitle("Select an item");
        ((AdapterView<?>) adapterViewList).setOnItemClickListener((OnItemClickListener) this);        
       
        builder.setPositiveButton("OK",     new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonOk    (d, w);}});
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonCancel(d, w);}});

        return builder.create();
    }
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
    	itemSelected																		= items.get(position);
	}
    public void buttonOk (DialogInterface dialog, int which)
    {
     	callBack.onReturnString(fieldId, itemSelected);
    	dialog.dismiss();
    }
    public void buttonCancel (DialogInterface dialog, int which)
    {
    	dialog.dismiss();
    }
}
