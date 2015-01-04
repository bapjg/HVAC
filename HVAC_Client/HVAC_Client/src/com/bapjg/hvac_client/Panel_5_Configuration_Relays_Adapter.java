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
public class Panel_5_Configuration_Relays_Adapter 				extends 					Adapter_0_Abstract
{
 
    public Panel_5_Configuration_Relays_Adapter(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
    }
    @Override
    public View getView(int position, View adapterView, ViewGroup parent) 
    {
    	Ctrl_Configuration.Relay								listItem					= (Ctrl_Configuration.Relay) listData.get(position);
    
    	Element_Standard										adapterElement 				= new Element_Standard(listItem.name);
    	adapterElement.setTextRight(listItem.relayNumber);
    
        return adapterElement;
    }
}