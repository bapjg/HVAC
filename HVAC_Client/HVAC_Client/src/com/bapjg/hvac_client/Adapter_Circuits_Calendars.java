package com.bapjg.hvac_client;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;

import HVAC_Messages.*;

//Template										variable			= something
//Template										ext/imp				class
public class Adapter_Circuits_Calendars 		extends 			ArrayAdapter
{
    private ArrayList							listData;
    private LayoutInflater 						myInflater;
 
    public Adapter_Circuits_Calendars(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
        
        this.listData 												= listData;
        this.myInflater												= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() 
    {
        return listData.size() + 1;
    }
    @Override
    public Ctrl_Calendars.Calendar getItem(int position) 
    {
        return (Ctrl_Calendars.Calendar) listData.get(position - 1);
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

        if (position == 0)
        {
        	convertView 											= myInflater.inflate(R.layout.row_calendar_circuit_title, null);
//            convertView.setTag(title);

        }
        else
        {
        	convertView 											= myInflater.inflate(R.layout.row_calendar_circuit, null);
        	row														= new RowHolder();
        	row.name												= (TextView) convertView.findViewById(R.id.name);
        	row.days												= (TextView) convertView.findViewById(R.id.days);
        	row.timeStart											= (TextView) convertView.findViewById(R.id.timeStart);
        	row.timeEnd												= (TextView) convertView.findViewById(R.id.timeEnd);
        	row.tempObjective										= (TextView) convertView.findViewById(R.id.tempObjective);
        	row.stopOnObjective										= (CheckBox) convertView.findViewById(R.id.stopOnObjective);
        	
        	row.name.setText			(((Ctrl_Calendars.Calendar) listData.get(position - 1)).name);
            row.days.setText			(((Ctrl_Calendars.Calendar) listData.get(position - 1)).days);
            row.timeStart.setText		(((Ctrl_Calendars.Calendar) listData.get(position - 1)).timeStart);
            row.timeEnd.setText			(((Ctrl_Calendars.Calendar) listData.get(position - 1)).stopCriterion.timeEnd);
            
            Integer					tempObjective					= ((Ctrl_Calendars.Calendar) listData.get(position - 1)).tempObjective/1000;
            row.tempObjective.setText	(tempObjective.toString());
	        
            if (((((Ctrl_Calendars.Calendar) listData.get(position - 1)).stopCriterion.stopOnObjective) == null)
            ||  ((((Ctrl_Calendars.Calendar) listData.get(position - 1)).stopCriterion.stopOnObjective) == false))
        	{
           		row.stopOnObjective.setChecked(false);
        	}
        	else
        	{
        		row.stopOnObjective.setChecked(true);
        	}
            convertView.setTag(row);
       }
        return convertView;
    }
    static class RowHolder 
    {
    	TextView 							name;
    	TextView 							days;
    	TextView 							timeStart;
    	TextView 							timeEnd;
    	TextView 							tempObjective;
    	CheckBox 							stopOnObjective;
    }	
//    public void onClick(AdapterView<?> arg0, View view, int position, long arg3)
//    {
//        // Object 		o 								= view.getItemAtPosition(position);
//        // NewsItem 	newsData 						= (NewsItem) o;
//		Toast.makeText(myContext, "Selected Something, perhaps : " + position, Toast.LENGTH_LONG).show();
//    }
}