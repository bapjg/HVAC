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
public class Adapter_Circuits 					extends 			ArrayAdapter
{
    private ArrayList							listData;
    private LayoutInflater 						myInflater;
 
    public Adapter_Circuits(Context context, int resource, ArrayList listData) 
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
    public Ctrl_Parameters.Circuit getItem(int position) 
    {
        return (Ctrl_Parameters.Circuit) listData.get(position - 1);
    }
    @Override
    public long getItemId(int position) 
    {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
    	ViewHolder 								holder;

        if (convertView == null) 
        {
        	convertView 											= myInflater.inflate(R.layout.row_circuit, null);
            holder 													= new ViewHolder();
            holder.name 											= (TextView) convertView.findViewById(R.id.name);
            holder.pump 											= (TextView) convertView.findViewById(R.id.pump);
            holder.thermometer 										= (TextView) convertView.findViewById(R.id.thermometer);
            holder.type		 										= (TextView) convertView.findViewById(R.id.type);
            convertView.setTag(holder);
        } 
        else 
        {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0)
        {
           	holder.name.setText				("Name");
            holder.name.setTextColor		(Color.YELLOW);
            holder.name.setTypeface			(null, Typeface.BOLD);
            
            holder.pump.setText				("Pump");
            holder.pump.setTextColor		(Color.YELLOW);
            holder.pump.setTypeface			(null, Typeface.BOLD);
            
            holder.thermometer.setText		("Thermometer");
            holder.thermometer.setTextColor	(Color.YELLOW);
            holder.thermometer.setTypeface	(null, Typeface.BOLD);

            holder.type.setText				("Type");
            holder.type.setTextColor		(Color.YELLOW);
            holder.type.setTypeface			(null, Typeface.BOLD);
}
        else
        {
        	holder.name.setText				(((Ctrl_Parameters.Circuit) listData.get(position - 1)).name);
	        holder.pump.setText				(((Ctrl_Parameters.Circuit) listData.get(position - 1)).pump);
	        holder.thermometer.setText		(((Ctrl_Parameters.Circuit) listData.get(position - 1)).thermometer);
	        holder.type.setText				(((Ctrl_Parameters.Circuit) listData.get(position - 1)).type);
        }
        return convertView;
    }
    static class ViewHolder 
    {
    	TextView 							name;
    	TextView 							pump;
    	TextView 							thermometer;
    	TextView 							type;
    }	
//    public void onClick(AdapterView<?> arg0, View view, int position, long arg3)
//    {
//        // Object 		o 								= view.getItemAtPosition(position);
//        // NewsItem 	newsData 						= (NewsItem) o;
//		Toast.makeText(myContext, "Selected Something, perhaps : " + position, Toast.LENGTH_LONG).show();
//    }
}