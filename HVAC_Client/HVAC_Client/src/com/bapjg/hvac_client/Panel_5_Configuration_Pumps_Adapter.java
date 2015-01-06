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
public class Panel_5_Configuration_Pumps_Adapter 				extends 					Panel_0_Adapter
{
    public Panel_5_Configuration_Pumps_Adapter(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
    }
    @Override
    public View getView(int position, View adapterView, ViewGroup parent) 
    {
    	Ctrl_Configuration.Pump									listItem					= (Ctrl_Configuration.Pump) listData.get(position);
        
    	Element_Standard										adapterElement 				= new Element_Standard(listItem.name);
    	adapterElement.setTextRight(listItem.relay);
    
        return adapterElement;
    }
}