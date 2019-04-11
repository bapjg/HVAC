package com.bapjg.hvac_client;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;
import HVAC_Common.*;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Panel_5_Configuration_Circuits_Adapter 			extends 					Panel_0_Adapter
{
    public Panel_5_Configuration_Circuits_Adapter(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
    }
    @Override
    public View getView(int position, View adapterView, ViewGroup parent) 
    {
    	Ctrl_Configuration.Circuit								listItem					= (Ctrl_Configuration.Circuit) listData.get(position);
        
    	Element_Standard										adapterElement 				= new Element_Standard(listItem.name);
    	adapterElement.setValue(listItem.type);
    
        return adapterElement;
    }
}