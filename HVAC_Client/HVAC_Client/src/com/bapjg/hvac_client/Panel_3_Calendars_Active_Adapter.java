package com.bapjg.hvac_client;

import java.util.ArrayList;

import HVAC_Common.Ctrl_Calendars;
import HVAC_Common.Ctrl_Configuration;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.*;
import android.widget.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Panel_3_Calendars_Active_Adapter 					extends 					Panel_0_Adapter
{
	public Panel_3_Calendars_Active_Adapter(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
    }
    public View getView(int position, View adapterView, ViewGroup parent) 
    {
    	Ctrl_Calendars.Circuit									listItem					= (Ctrl_Calendars.Circuit) listData.get(position);
   	
    	Element_Switch											adapterSwitch 				= new Element_Switch(listItem.name);
    	adapterSwitch.setChecked(listItem.active);
    	((ViewGroup) adapterSwitch).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
    	adapterSwitch.onOffSwitch.setClickable(false);
    	return adapterSwitch;    	
    }
}