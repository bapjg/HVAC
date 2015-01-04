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
public class Adapter_5_Configuration_PIDs 						extends 					Adapter_0_Abstract
{
  
    public Adapter_5_Configuration_PIDs(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
    }
    @Override
    public View getView(int position, View adapterView, ViewGroup parent) 
    {
    	RowHolder 												row							= new RowHolder();
    	Ctrl_Configuration.PID_Data								listItem					= (Ctrl_Configuration.PID_Data) listData.get(position);
        adapterView 																		= inflater.inflate(R.layout.element_standard, null);
        
        row.leftText 																		= (TextView) adapterView.findViewById(R.id.Left);
        row.rightText 																		= (TextView) adapterView.findViewById(R.id.Right);
        adapterView.setTag(row);

        row.leftText.setText					(listItem.name);
        row.rightText.setText					(listItem.depth.toString());

        return adapterView;
    }
    static class RowHolder 
    {
    	TextView 												leftText;
    	TextView 												rightText;
    }	
}