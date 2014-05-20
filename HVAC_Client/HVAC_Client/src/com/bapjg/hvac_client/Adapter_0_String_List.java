package com.bapjg.hvac_client;

import java.util.ArrayList;

import com.bapjg.hvac_client.Adapter_5_Configuration_Thermometers.RowHolder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;

import HVAC_Messages.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Adapter_0_String_List 								extends 					Adapter_0_Abstract
{
 
    public Adapter_0_String_List(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
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
 
        return adapterView;
    }
    static class RowHolder 
    {
    	TextView 												rowItem;
    }	
}