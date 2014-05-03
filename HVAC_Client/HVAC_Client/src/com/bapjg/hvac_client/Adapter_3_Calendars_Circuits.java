package com.bapjg.hvac_client;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import HVAC_Messages.*;

//Template										variable			= something
//Template										ext/imp				class
public class Adapter_3_Calendars_Circuits 		extends 			ArrayAdapter
												implements			View.OnClickListener
{
    private ArrayList							listData;
    private LayoutInflater 						myInflater;
 
    public Adapter_3_Calendars_Circuits(Context context, int resource, ArrayList listData) 
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
    public Ctrl_Calendars.Calendar getItem(int position) 
    {
        return (Ctrl_Calendars.Calendar) listData.get(position);
    }
    @Override
    public long getItemId(int position) 
    {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
     	RowHolder 								row;

    	convertView 											= myInflater.inflate(R.layout.row_3_calendar_circuit, null);
    	row														= new RowHolder();
    	row.name												= (TextView) convertView.findViewById(R.id.name);
    	row.days												= (TextView) convertView.findViewById(R.id.days);
       	row.day_1 												= (TextView) convertView.findViewById(R.id.day_1);
    	row.day_2 												= (TextView) convertView.findViewById(R.id.day_2);
    	row.day_3 												= (TextView) convertView.findViewById(R.id.day_3);
    	row.day_4 												= (TextView) convertView.findViewById(R.id.day_4);
    	row.day_5 												= (TextView) convertView.findViewById(R.id.day_5);
    	row.day_6 												= (TextView) convertView.findViewById(R.id.day_6);
    	row.day_7 												= (TextView) convertView.findViewById(R.id.day_7);
     	row.timeStart											= (TextView) convertView.findViewById(R.id.timeStart);
    	row.timeEnd												= (TextView) convertView.findViewById(R.id.timeEnd);
    	row.tempObjective										= (TextView) convertView.findViewById(R.id.tempObjective);
    	row.stopOnObjective										= (CheckBox) convertView.findViewById(R.id.stopOnObjective);

    	Ctrl_Calendars.Calendar					listItem		= (Ctrl_Calendars.Calendar) listData.get(position);

    	row.name.setText			(listItem.name);
        row.days.setText			(listItem.days);
        if ((listItem.days).indexOf("1") > -1)	row.day_1.setBackgroundColor(Color.RED); else row.day_1.setBackgroundColor(Color.BLUE);
        if ((listItem.days).indexOf("2") > -1)	row.day_2.setBackgroundColor(Color.RED); else row.day_2.setBackgroundColor(Color.BLUE);
        if ((listItem.days).indexOf("3") > -1)	row.day_3.setBackgroundColor(Color.RED); else row.day_3.setBackgroundColor(Color.BLUE);
        if ((listItem.days).indexOf("4") > -1)	row.day_4.setBackgroundColor(Color.RED); else row.day_4.setBackgroundColor(Color.BLUE);
        if ((listItem.days).indexOf("5") > -1)	row.day_5.setBackgroundColor(Color.RED); else row.day_5.setBackgroundColor(Color.BLUE);
        if ((listItem.days).indexOf("6") > -1)	row.day_6.setBackgroundColor(Color.RED); else row.day_6.setBackgroundColor(Color.BLUE);
        if ((listItem.days).indexOf("7") > -1)	row.day_7.setBackgroundColor(Color.RED); else row.day_7.setBackgroundColor(Color.BLUE);
        
        row.timeStart.setText		(listItem.timeStart);
        row.timeEnd.setText			(listItem.stopCriterion.timeEnd);
        
        Integer									tempObjective	= listItem.tempObjective/1000;
        row.tempObjective.setText	(tempObjective.toString());
        
        if ((listItem.stopCriterion.stopOnObjective == null)
        ||  (listItem.stopCriterion.stopOnObjective == false))
    	{
       		row.stopOnObjective.setChecked(false);
    	}
    	else
    	{
    		row.stopOnObjective.setChecked(true);
    	}
        
        row.day_1.setOnClickListener((OnClickListener) this);
//        row.day_2.setOnClickListener((View.OnClickListener) this);
//        row.day_3.setOnClickListener((View.OnClickListener) this);
//        row.day_4.setOnClickListener((View.OnClickListener) this);
        
        convertView.setTag(row);
        return convertView;
    }
    static class RowHolder 
    {
    	TextView 							name;
    	TextView 							days;
    	TextView 							day_1;
    	TextView 							day_2;
    	TextView 							day_3;
    	TextView 							day_4;
    	TextView 							day_5;
    	TextView 							day_6;
    	TextView 							day_7;
    	TextView 							timeStart;
    	TextView 							timeEnd;
    	TextView 							tempObjective;
    	CheckBox 							stopOnObjective;
    }
    public void  onClick(View view)
    {
    	System.out.println("onclick simple");
        int position = this.getPosition(this);
        
        System.out.println("pos " + position);
    }
    public void onClick(AdapterView<?> arg0, View view, int position, long arg3)
    {
        // Object 		o 								= view.getItemAtPosition(position);
        // NewsItem 	newsData 						= (NewsItem) o;
		Global.toaster("Selected Something, perhaps : " + position, true);
    }
}