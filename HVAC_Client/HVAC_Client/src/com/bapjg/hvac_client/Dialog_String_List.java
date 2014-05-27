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
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

@SuppressLint("ValidFragment")
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Dialog_String_List 								extends 					DialogFragment 
																implements					AdapterView.OnItemClickListener	
{
	private String												item;
	private Object												parentObject;
	public  ArrayList <String>									items;
	public  String												itemSelected;
	private Dialog_Response										callBack;
	
	public Dialog_String_List() 
    {
    }
	public Dialog_String_List(String item, Object parentObject, ArrayList <String> items, Dialog_Response callBack) 
    {
		super();
		this.item																			= item;
		this.parentObject																	= parentObject;
		// Note that items can be populated after the constructor : this.items.add("Something");
		if (items == null)										this.items 					= new ArrayList<String>();			
		else													this.items 					= items;
		
		this.callBack																		= callBack;
    }
    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
    {
        AlertDialog.Builder 									builder 					= new AlertDialog.Builder(getActivity());
        LayoutInflater 											inflater 					= getActivity().getLayoutInflater();
										                                                    
        View													dialogView					= inflater.inflate(R.layout.dialog_string_list, null);
        AdapterView												adapterView					= (AdapterView) dialogView.findViewById(R.id.List_View);
        
        AdapterView <Adapter_0_String_List>						adapterViewList				= (AdapterView <Adapter_0_String_List>) adapterView;
        Adapter_0_String_List									arrayAdapter				= new Adapter_0_String_List(Global.actContext, R.id.List_View, items);
       
        adapterView.setAdapter(arrayAdapter);
        builder.setView(dialogView);
        builder.setTitle("Select an item");
        ((AdapterView<?>) adapterViewList).setOnItemClickListener((OnItemClickListener) this);        
       
//        builder.setPositiveButton("OK",     new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonOk    (d, w);}});
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()  {@Override public void onClick(DialogInterface d, int w) {buttonCancel(d, w);}});

        return builder.create();
    }
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
    	itemSelected																		= items.get(position);
     	for (Field field : parentObject.getClass().getDeclaredFields())  
     	{
     		try 
     		{
				if (item == field.get(parentObject))
				{
					field.set(parentObject, itemSelected);
			    	callBack.onDialogReturn();
			    	this.dismiss();
				}
			} 
     		catch (Exception e)
     		{
     			// Do nothing as serialversionUID, this$ etc cause exceptions
     		} 
     	}
    	
    	// TODO color the selected text
  	}
//    public void buttonOk (DialogInterface dialog, int which)
//    {
//     	// Identify property within parent to modify
//     	for (Field field : parent.getClass().getDeclaredFields())  
//     	{
//     		try 
//     		{
//				if (item == field.get(parent))
//				{
//					field.set(parent, itemSelected);
//			    	callBack.onDialogReturn();
//			    	dialog.dismiss();
//				}
//			} 
//     		catch (Exception e)
//     		{
//     			// Do nothing as serialversionUID, this$ etc cause exceptions
//     		} 
//     	}
//    }
    public void buttonCancel (DialogInterface dialog, int which)
    {
    	dialog.dismiss();
    }
}
