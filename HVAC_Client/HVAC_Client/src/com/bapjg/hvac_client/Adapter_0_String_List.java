package com.bapjg.hvac_client;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;
import HVAC_Common.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Adapter_0_String_List 								extends 					Adapter_0_Abstract
{
    public String												selectedItem;
    public String												item;
    
	public Adapter_0_String_List(Context context, int resource, ArrayList <String> listData, String item) 
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