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
public class Panel_3_Calendars_Away_Adapter 					extends 					Panel_0_Adapter
{
 
    public Panel_3_Calendars_Away_Adapter(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
    }
    @Override
    public View getView(int position, View adapterView, ViewGroup parent) 
    {
        Ctrl_Calendars.Away 									listItem					= (Ctrl_Calendars.Away) listData.get(position);
    	
    	Element_Centered_x_4									adapterElement 				= new Element_Centered_x_4();

    	adapterElement.setTextTopLeft		(Global.displayDate(listItem.dateTimeStart));
    	adapterElement.setTextBottomLeft	(Global.displayTime(listItem.dateTimeStart));
    	adapterElement.setTextTopRight		(Global.displayDate(listItem.dateTimeEnd));
    	adapterElement.setTextBottomRight	(Global.displayTime(listItem.dateTimeEnd));

        return adapterElement;
    }
}