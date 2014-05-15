package com.bapjg.hvac_client;

import java.util.ArrayList;

import HVAC_Messages.Ctrl_Calendars;
import HVAC_Messages.Ctrl_Configuration;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;

public class Adapter_3_Calendars_Away 						extends 			Adapter_0_Abstract
{
 
    public Adapter_3_Calendars_Away(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
    }
    public View getView(int position, View adapterView, ViewGroup parent) 
    {
    	RowHolder 											row;
        Ctrl_Calendars.Away 								listItem			= (Ctrl_Calendars.Away) listData.get(position);
    	
    	adapterView 															= inflater.inflate(R.layout.row_3_calendars_away, null);
    	row 																	= new RowHolder();
    	row.dateStart 															= (TextView) adapterView.findViewById(R.id.dateStart);
    	row.timeStart 															= (TextView) adapterView.findViewById(R.id.timeStart);
    	row.dateEnd																= (TextView) adapterView.findViewById(R.id.dateEnd);
    	row.timeEnd																= (TextView) adapterView.findViewById(R.id.timeEnd);

        adapterView.setTag(row);
        
        Long												dateTimeStart		= listItem.dateTimeStart;
        Long												dateTimeEnd			= listItem.dateTimeEnd;
        
        String												stringDateStart		= Global.displayDate(dateTimeStart);
        String												stringTimeStart		= Global.displayTime(dateTimeEnd);
        String												stringDateEnd		= Global.displayDate(dateTimeStart);
        String												stringTimeSEnd		= Global.displayTime(dateTimeEnd);

        row.dateStart.setText(stringDateStart);
        row.timeStart.setText(stringTimeStart);
        row.dateEnd.setText(stringDateEnd);
        row.timeEnd.setText(stringTimeSEnd);
       
         return adapterView;
    }
    static class RowHolder 
    {
    	TextView 							dateStart;
       	TextView 							timeStart;
    	TextView 							dateEnd;
    	TextView 							timeEnd;
    }
//    public void onClick(AdapterView<?> arg0, View view, int position, long arg3)
//    {
//        // Object 		o 								= view.getItemAtPosition(position);
//        // NewsItem 	newsData 						= (NewsItem) o;
//		Toast.makeText(myContext, "Selected Something, perhaps : " + position, Toast.LENGTH_LONG).show();
//    }
}