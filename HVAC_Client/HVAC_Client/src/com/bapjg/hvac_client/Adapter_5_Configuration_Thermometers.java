package com.bapjg.hvac_client;

import java.util.ArrayList;

import com.bapjg.hvac_client.Adapter_5_Configuration_Relays.RowHolder;

import HVAC_Common.Ctrl_Configuration;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Adapter_5_Configuration_Thermometers 				extends 					Adapter_0_Abstract
{
 
    public Adapter_5_Configuration_Thermometers(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
    }
    @Override
    public View getView(int position, View adapterView, ViewGroup parent) 
    {
    	RowHolder 												row							= new RowHolder();
    	Ctrl_Configuration.Thermometer							listItem					= (Ctrl_Configuration.Thermometer) listData.get(position);
    	
        adapterView 																		= inflater.inflate(R.layout.row_5_configuration_thermometer, null);
        row.name 																			= (TextView) adapterView.findViewById(R.id.name);
        row.address 																		= (TextView) adapterView.findViewById(R.id.address);
        adapterView.setTag(row);

        row.name.setText					(listItem.name);
        row.address.setText					(listItem.address);

        return adapterView;
    }
    static class RowHolder 
    {
    	TextView 												name;
    	TextView 												address;
    }
}