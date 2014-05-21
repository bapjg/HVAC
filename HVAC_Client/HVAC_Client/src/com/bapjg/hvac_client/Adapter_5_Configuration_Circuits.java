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
public class Adapter_5_Configuration_Circuits 					extends 					Adapter_0_Abstract
{
 
    public Adapter_5_Configuration_Circuits(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
    }
    @Override
    public View getView(int position, View adapterView, ViewGroup parent) 
    {
    	RowHolder 												row							= new RowHolder();
    	Ctrl_Configuration.Circuit								listItem					= (Ctrl_Configuration.Circuit) listData.get(position);
        adapterView 																		= inflater.inflate(R.layout.row_5_configuration_circuit, null);
							
        row.name 																			= (TextView) adapterView.findViewById(R.id.name);
        row.gradient 																		= (CheckBox) adapterView.findViewById(R.id.gradient);
        row.mixer	 																		= (CheckBox) adapterView.findViewById(R.id.mixer);
        adapterView.setTag(row);

        row.name.setText					(listItem.name);
       	row.gradient.setChecked				( ! (listItem.tempGradient 	== null));
       	row.mixer.setChecked				( ! (listItem.mixer 		== null));

        return adapterView;
    }
    static class RowHolder 
    {
    	TextView 												name;
    	CheckBox 												gradient;
    	CheckBox 												mixer;
    }	
}