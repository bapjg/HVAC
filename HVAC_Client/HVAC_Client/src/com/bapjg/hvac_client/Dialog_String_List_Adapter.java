package com.bapjg.hvac_client;

import java.util.ArrayList;					// Used for displaying Lists e.g.Words, Pumps, Relays etc

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;
import HVAC_Common.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Dialog_String_List_Adapter 								extends 					Panel_0_Adapter
{
    public String												selectedItem;
    public String												item;
    
	public Dialog_String_List_Adapter(Context context, int resource, ArrayList <String> listData, String item) 
    {
        super(context, resource, listData);
        this.item																			= item;
    }
    @Override
    public View getView(int position, View adapterView, ViewGroup parent) 
    {
    	RowHolder 												row							= new RowHolder();
    	String													listItem					= (String) listData.get(position);
        adapterView 																		= inflater.inflate(R.layout.row_0_string_list, null);

        row.rowItem 																		= (TextView) adapterView.findViewById(R.id.rowItem);

//    	String													listItem					= (String) listData.get(position);
//    	Element_Centered_x_1									adapterElement 				= new Element_Centered_x_1();

//    	adapterElement.setText				(listItem);
//        if (listItem.equalsIgnoreCase(item))					adapterElement.setTextColor(Color.YELLOW);
//        else 													adapterElement.setTextColor(Color.BLACK);
//        return adapterElement;

        
        
        
        adapterView.setTag(row);

        row.rowItem.setText(listItem);
        
        if (listItem.equalsIgnoreCase(item))					row.rowItem.setTextColor(Color.YELLOW);
        else 													row.rowItem.setTextColor(Color.BLACK);

        return adapterView;
    }
    static class RowHolder 
    {
    	TextView 												rowItem;
    }
}