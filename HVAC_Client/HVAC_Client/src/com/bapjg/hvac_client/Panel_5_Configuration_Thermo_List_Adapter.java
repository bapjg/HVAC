package com.bapjg.hvac_client;

import java.util.ArrayList;

import HVAC_Common.Ctrl_Configuration;
import HVAC_Common.Ctrl_Thermo_List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Panel_5_Configuration_Thermo_List_Adapter 			extends 					Panel_0_Adapter
{
    public Panel_5_Configuration_Thermo_List_Adapter(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
    }
    @Override
    public View getView(int position, View adapterView, ViewGroup parent) 
    {
    	Ctrl_Thermo_List.Thermo									listItem					= (Ctrl_Thermo_List.Thermo) listData.get(position);
        
       	Element_Linear_Vertical									adapterElement 				= new Element_Linear_Vertical();
        Element_Standard										nameElement 				= new Element_Standard("Name");
    	Element_Standard										addressElement 				= new Element_Standard("Address");
    	Element_Switch											isNewElement 				= new Element_Switch("New Thermo");
    	Element_Switch											isLostElement 				= new Element_Switch("Lost Thermo");
    	Element_Standard										tempElement 				= new Element_Standard("Temperature");

    	adapterElement.insertPoint.addView(nameElement);
    	adapterElement.insertPoint.addView(addressElement);
    	adapterElement.insertPoint.addView(isNewElement);
    	adapterElement.insertPoint.addView(isLostElement);
    	adapterElement.insertPoint.addView(tempElement);
    	
    	nameElement		.setValue(listItem.name);
    	addressElement	.setValue(listItem.address);
    	isNewElement	.setChecked(listItem.isNew);
    	isLostElement	.setChecked(listItem.isLost);
    	tempElement		.setValue(Global.displayTemperature	(listItem.temperature));

        if (Global.deviceName == "lgPhone")
        {
        	((LinearLayout) adapterElement.findViewById(R.id.element)).setOrientation(LinearLayout.VERTICAL);
        }
        return adapterElement;
    }
}