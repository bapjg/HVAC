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
public class Panel_3_Calendars_Words_Adapter 							extends 					Adapter_0_Abstract
{
 
    public Panel_3_Calendars_Words_Adapter(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
    }
    public View getView(int position, View adapterView, ViewGroup parent) 
    {
//    	RowHolder 												row;
//        Ctrl_Calendars.Word										listItem					= (Ctrl_Calendars.Word) listData.get(position);
//    	
//    	adapterView 																		= inflater.inflate(R.layout.row_3_calendars_word, null);
//    	row 																				= new RowHolder();
//    	row.name 																			= (TextView) adapterView.findViewById(R.id.name);
//    	row.day_1 																			= (TextView) adapterView.findViewById(R.id.day_1);
//    	row.day_2 																			= (TextView) adapterView.findViewById(R.id.day_2);
//    	row.day_3 																			= (TextView) adapterView.findViewById(R.id.day_3);
//    	row.day_4 																			= (TextView) adapterView.findViewById(R.id.day_4);
//    	row.day_5 																			= (TextView) adapterView.findViewById(R.id.day_5);
//    	row.day_6 																			= (TextView) adapterView.findViewById(R.id.day_6);
//    	row.day_7 																			= (TextView) adapterView.findViewById(R.id.day_7);
//        adapterView.setTag(row);
//        
//        row.name.setText(listItem.name);
//        
//        if ((listItem.days).indexOf("1") > -1)	row.day_1.setBackgroundColor(Color.RED); else row.day_1.setBackgroundColor(Color.BLUE);
//        if ((listItem.days).indexOf("2") > -1)	row.day_2.setBackgroundColor(Color.RED); else row.day_2.setBackgroundColor(Color.BLUE);
//        if ((listItem.days).indexOf("3") > -1)	row.day_3.setBackgroundColor(Color.RED); else row.day_3.setBackgroundColor(Color.BLUE);
//        if ((listItem.days).indexOf("4") > -1)	row.day_4.setBackgroundColor(Color.RED); else row.day_4.setBackgroundColor(Color.BLUE);
//        if ((listItem.days).indexOf("5") > -1)	row.day_5.setBackgroundColor(Color.RED); else row.day_5.setBackgroundColor(Color.BLUE);
//        if ((listItem.days).indexOf("6") > -1)	row.day_6.setBackgroundColor(Color.RED); else row.day_6.setBackgroundColor(Color.BLUE);
//        if ((listItem.days).indexOf("7") > -1)	row.day_7.setBackgroundColor(Color.RED); else row.day_7.setBackgroundColor(Color.BLUE);
//
//        return adapterView;
    	Ctrl_Calendars.Word										listItem					= (Ctrl_Calendars.Word) listData.get(position);
   	
    	Element_WeekDays										adapterElement 				= new Element_WeekDays(listItem.name, listItem.days);
        return adapterElement;    	
    }
//    static class RowHolder 
//    {
//    	TextView 												name;
//    	TextView 												day_1;
//    	TextView 												day_2;
//    	TextView 												day_3;
//    	TextView 												day_4;
//    	TextView 												day_5;
//    	TextView 												day_6;
//    	TextView 												day_7;
//    }
//    public void onClick(AdapterView<?> arg0, View view, int position, long arg3)
//    {
//        // Object 		o 								= view.getItemAtPosition(position);
//        // NewsItem 	newsData 						= (NewsItem) o;
//		Toast.makeText(myContext, "Selected Something, perhaps : " + position, Toast.LENGTH_LONG).show();
//    }
}