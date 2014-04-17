package com.bapjg.hvac_client;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;

import HVAC_Messages.*;

public class Adapter_Relays extends ArrayAdapter
{
    private ArrayList						listData;
    private LayoutInflater 					myInflater;
 
    public Adapter_Relays(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
        
        this.listData 					= listData;
        this.myInflater					= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        System.out.println("Adapter constructer called");
    }
    @Override
    public int getCount() 
    {
        return listData.size() + 1;
    }
    @Override
    public Ctrl_Parameters.Relay getItem(int position) 
    {
        return (Ctrl_Parameters.Relay) listData.get(position - 1);
    }
    @Override
    public long getItemId(int position) 
    {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
    	ViewHolder holder;
    	
        if (convertView == null) 
        {
        	convertView 				= myInflater.inflate(R.layout.row_relay, null);
            holder 						= new ViewHolder();
            holder.name 				= (TextView) convertView.findViewById(R.id.name);
            holder.relayBank 			= (TextView) convertView.findViewById(R.id.relayBank);
            holder.relayNumber 			= (TextView) convertView.findViewById(R.id.relayNumber);
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
            holder.relayBank.setText		("Bank");
            holder.relayBank.setTextColor	(Color.YELLOW);
            holder.relayBank.setTypeface	(null, Typeface.BOLD);
            holder.relayNumber.setText		("Number");
            holder.relayNumber.setTextColor	(Color.YELLOW);
            holder.relayNumber.setTypeface	(null, Typeface.BOLD);
        }
        else
        {
	        holder.name.setText				(((Ctrl_Parameters.Relay) listData.get(position - 1)).name);
	        holder.relayBank.setText		(((Ctrl_Parameters.Relay) listData.get(position - 1)).relayBank);
	        holder.relayNumber.setText		(((Ctrl_Parameters.Relay) listData.get(position - 1)).relayNumber);
        }
        return convertView;
    }
    static class ViewHolder 
    {
    	TextView 						name;
    	TextView 						relayBank;
    	TextView 						relayNumber;
    }
//    public void onClick(AdapterView<?> arg0, View view, int position, long arg3)
//    {
//        // Object 		o 								= view.getItemAtPosition(position);
//        // NewsItem 	newsData 						= (NewsItem) o;
//		Toast.makeText(myContext, "Selected Something, perhaps : " + position, Toast.LENGTH_LONG).show();
//    }
}