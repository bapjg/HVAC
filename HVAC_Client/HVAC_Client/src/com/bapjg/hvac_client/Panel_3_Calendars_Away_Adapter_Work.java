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
public class Panel_3_Calendars_Away_Adapter_Work 					extends 					Panel_0_Adapter
{
 
    public Panel_3_Calendars_Away_Adapter_Work(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
    }
    public View getView(int position, View adapterView, ViewGroup parent) 
    {
    	RowHolder 												row;
        Ctrl_Calendars.Away 									listItem					= (Ctrl_Calendars.Away) listData.get(position);
    	
    	adapterView 																		= inflater.inflate(R.layout.row_3_calendars_away, null);
    	row 																				= new RowHolder();
    	row.dateStart 																		= (TextView) adapterView.findViewById(R.id.dateStart);
    	row.timeStart 																		= (TextView) adapterView.findViewById(R.id.timeStart);
    	row.dateEnd																			= (TextView) adapterView.findViewById(R.id.dateEnd);
    	row.timeEnd																			= (TextView) adapterView.findViewById(R.id.timeEnd);

        adapterView.setTag(row);
        
        Long													dateTimeStart				= listItem.dateTimeStart;
        Long													dateTimeEnd					= listItem.dateTimeEnd;
        
        String													stringDateStart				= Global.displayDate(dateTimeStart);
        String													stringTimeStart				= Global.displayTime(dateTimeStart);
        String													stringDateEnd				= Global.displayDate(dateTimeEnd);
        String													stringTimeEnd				= Global.displayTime(dateTimeEnd);

        row.dateStart.setText(stringDateStart);
        row.timeStart.setText(stringTimeStart);
        row.dateEnd.setText(stringDateEnd);
        row.timeEnd.setText(stringTimeEnd);
       
         return adapterView;
    }
    static class RowHolder 
    {
    	TextView 												dateStart;
       	TextView 												timeStart;
    	TextView 												dateEnd;
    	TextView 												timeEnd;
    }
}