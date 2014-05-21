package com.bapjg.hvac_client;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;

import HVAC_Messages.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Adapter_5_Configuration_Pumps 						extends 					Adapter_0_Abstract
{
  
    public Adapter_5_Configuration_Pumps(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
    }
    @Override
    public View getView(int position, View adapterView, ViewGroup parent) 
    {
    	RowHolder 												row;
    	Ctrl_Configuration.Pump									listItem					= (Ctrl_Configuration.Pump) listData.get(position);

        adapterView 																		= inflater.inflate(R.layout.row_5_configuration_pump, null);
        row 																				= new RowHolder();
        row.name 																			= (TextView) adapterView.findViewById(R.id.name);
        row.relay 																			= (TextView) adapterView.findViewById(R.id.relay);
        adapterView.setTag(row);
    	row.name.setText					(listItem.name);
        row.relay.setText					(listItem.relay);
        
        return adapterView;
    }
    static class RowHolder 
    {
    	TextView 												name;
    	TextView 												relay;
    }	
}