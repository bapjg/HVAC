package com.bapjg.hvac_client;

import java.util.ArrayList;

import HVAC_Common.Ctrl_Calendars;
import HVAC_Common.Ctrl_Configuration;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Panel_3_Calendars_Vocabulary_Adapter 				extends 					Panel_0_Adapter
{
    public Panel_3_Calendars_Vocabulary_Adapter(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
    }
    public View getView(int position, View adapterView, ViewGroup parent) 
    {
    	Ctrl_Calendars.Word										listItem					= (Ctrl_Calendars.Word) listData.get(position);
   	
    	Element_Slots_WeekDays									adapterElement 				= new Element_Slots_WeekDays(listItem.name, listItem.days);
        return adapterElement;    	
    }
}