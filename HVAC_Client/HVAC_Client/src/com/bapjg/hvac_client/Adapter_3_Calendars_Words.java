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

//Template										variable			= something
//Template										ext/imp				class
public class Adapter_3_Calendars_Words 						extends 			ArrayAdapter
{
    private ArrayList							listData;
    private LayoutInflater 						myInflater;
 
    public Adapter_3_Calendars_Words(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
        
        this.listData 												= listData;
        this.myInflater												= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() 
    {
        return listData.size();
    }
    @Override
    public Ctrl_Calendars.Word    				getItem(int position) 
    {
        return (Ctrl_Calendars.Word) listData.get(position);
    }
    @Override
    public long getItemId(int position) 
    {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
    	ViewHolder 							row;
    	
    	convertView 											= myInflater.inflate(R.layout.row_3_calendars_word, null);
    	row 													= new ViewHolder();
    	row.name 												= (TextView) convertView.findViewById(R.id.name);
    	row.day_1 												= (TextView) convertView.findViewById(R.id.day_1);
    	row.day_2 												= (TextView) convertView.findViewById(R.id.day_2);
    	row.day_3 												= (TextView) convertView.findViewById(R.id.day_3);
    	row.day_4 												= (TextView) convertView.findViewById(R.id.day_4);
    	row.day_5 												= (TextView) convertView.findViewById(R.id.day_5);
    	row.day_6 												= (TextView) convertView.findViewById(R.id.day_6);
    	row.day_7 												= (TextView) convertView.findViewById(R.id.day_7);
        convertView.setTag(row);
    
        Ctrl_Calendars.Word					listItem			= (Ctrl_Calendars.Word) listData.get(position);

        row.name.setText					(listItem.name);
        if ((listItem.days).indexOf("1") > -1)	row.day_1.setBackgroundColor(Color.RED); else row.day_1.setBackgroundColor(Color.BLUE);
        if ((listItem.days).indexOf("2") > -1)	row.day_2.setBackgroundColor(Color.RED); else row.day_2.setBackgroundColor(Color.BLUE);
        if ((listItem.days).indexOf("3") > -1)	row.day_3.setBackgroundColor(Color.RED); else row.day_3.setBackgroundColor(Color.BLUE);
        if ((listItem.days).indexOf("4") > -1)	row.day_4.setBackgroundColor(Color.RED); else row.day_4.setBackgroundColor(Color.BLUE);
        if ((listItem.days).indexOf("5") > -1)	row.day_5.setBackgroundColor(Color.RED); else row.day_5.setBackgroundColor(Color.BLUE);
        if ((listItem.days).indexOf("6") > -1)	row.day_6.setBackgroundColor(Color.RED); else row.day_6.setBackgroundColor(Color.BLUE);
        if ((listItem.days).indexOf("7") > -1)	row.day_7.setBackgroundColor(Color.RED); else row.day_7.setBackgroundColor(Color.BLUE);

        return convertView;
    }
    static class ViewHolder 
    {
    	TextView 							name;
    	TextView 							day_1;
    	TextView 							day_2;
    	TextView 							day_3;
    	TextView 							day_4;
    	TextView 							day_5;
    	TextView 							day_6;
    	TextView 							day_7;
    }
//    public void onClick(AdapterView<?> arg0, View view, int position, long arg3)
//    {
//        // Object 		o 								= view.getItemAtPosition(position);
//        // NewsItem 	newsData 						= (NewsItem) o;
//		Toast.makeText(myContext, "Selected Something, perhaps : " + position, Toast.LENGTH_LONG).show();
//    }
}