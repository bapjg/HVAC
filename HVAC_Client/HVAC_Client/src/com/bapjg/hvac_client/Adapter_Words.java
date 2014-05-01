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
public class Adapter_Words 						extends 			ArrayAdapter
{
    private ArrayList							listData;
    private LayoutInflater 						myInflater;
 
    public Adapter_Words(Context context, int resource, ArrayList listData) 
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
    public Ctrl_Calendars.Word    				getItem(int position) 
    {
        return (Ctrl_Calendars.Word) listData.get(position - 1);
    }
    @Override
    public long getItemId(int position) 
    {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
    	ViewHolder 							holder;
    	
        if (convertView == null) 
        {
        	convertView 											= myInflater.inflate(R.layout.row_word, null);
            holder 													= new ViewHolder();
            holder.name 											= (TextView) convertView.findViewById(R.id.name);
            holder.day_1 											= (TextView) convertView.findViewById(R.id.day_1);
            holder.day_2 											= (TextView) convertView.findViewById(R.id.day_2);
            holder.day_3 											= (TextView) convertView.findViewById(R.id.day_3);
            holder.day_4 											= (TextView) convertView.findViewById(R.id.day_4);
            holder.day_5 											= (TextView) convertView.findViewById(R.id.day_5);
            holder.day_6 											= (TextView) convertView.findViewById(R.id.day_6);
            holder.day_7 											= (TextView) convertView.findViewById(R.id.day_7);
            convertView.setTag(holder);
        } 
        else 
        {
            holder = (ViewHolder) convertView.getTag();
        }
        
        if (position == 0)
        {
           	holder.name.setText				("Word");
            holder.name.setTextColor		(Color.YELLOW);
            holder.name.setTypeface			(null, Typeface.BOLD);
        }
        else
        {
	        holder.name.setText				(((Ctrl_Calendars.Word) listData.get(position - 1)).name);
	        if ((((Ctrl_Calendars.Word) listData.get(position - 1)).days).indexOf("1") > -1)	holder.day_1.setBackgroundColor(Color.RED); else holder.day_1.setBackgroundColor(Color.BLUE);
	        if ((((Ctrl_Calendars.Word) listData.get(position - 1)).days).indexOf("2") > -1)	holder.day_2.setBackgroundColor(Color.RED); else holder.day_2.setBackgroundColor(Color.BLUE);
	        if ((((Ctrl_Calendars.Word) listData.get(position - 1)).days).indexOf("3") > -1)	holder.day_3.setBackgroundColor(Color.RED); else holder.day_3.setBackgroundColor(Color.BLUE);
	        if ((((Ctrl_Calendars.Word) listData.get(position - 1)).days).indexOf("4") > -1)	holder.day_4.setBackgroundColor(Color.RED); else holder.day_4.setBackgroundColor(Color.BLUE);
	        if ((((Ctrl_Calendars.Word) listData.get(position - 1)).days).indexOf("5") > -1)	holder.day_5.setBackgroundColor(Color.RED); else holder.day_5.setBackgroundColor(Color.BLUE);
	        if ((((Ctrl_Calendars.Word) listData.get(position - 1)).days).indexOf("6") > -1)	holder.day_6.setBackgroundColor(Color.RED); else holder.day_6.setBackgroundColor(Color.BLUE);
	        if ((((Ctrl_Calendars.Word) listData.get(position - 1)).days).indexOf("7") > -1)	holder.day_7.setBackgroundColor(Color.RED); else holder.day_7.setBackgroundColor(Color.BLUE);
        }
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