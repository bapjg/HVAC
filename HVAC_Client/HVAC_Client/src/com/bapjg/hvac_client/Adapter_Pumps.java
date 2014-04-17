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
public class Adapter_Pumps 						extends 			ArrayAdapter
{
    private ArrayList							listData;
    private LayoutInflater 						myInflater;
 
    public Adapter_Pumps(Context context, int resource, ArrayList listData) 
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
    public Ctrl_Parameters.Pump getItem(int position) 
    {
        return (Ctrl_Parameters.Pump) listData.get(position - 1);
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
            holder.relay 											= (TextView) convertView.findViewById(R.id.relay);
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
            
            holder.relay.setText			("Bank");
            holder.relay.setTextColor		(Color.YELLOW);
            holder.relay.setTypeface		(null, Typeface.BOLD);
            
         }
        else
        {
        	holder.name.setText				(((Ctrl_Parameters.Pump) listData.get(position - 1)).name);
	        holder.relay.setText			(((Ctrl_Parameters.Pump) listData.get(position - 1)).relay);
        }
        return convertView;
    }
    static class ViewHolder 
    {
    	TextView 							name;
    	TextView 							relay;
    }	
//    public void onClick(AdapterView<?> arg0, View view, int position, long arg3)
//    {
//        // Object 		o 								= view.getItemAtPosition(position);
//        // NewsItem 	newsData 						= (NewsItem) o;
//		Toast.makeText(myContext, "Selected Something, perhaps : " + position, Toast.LENGTH_LONG).show();
//    }
}