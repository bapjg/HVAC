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
    private Context 						myContext;
 
    public Adapter_Relays(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
        
        this.listData 					= listData;
        this.myContext 					= context;
        this.myInflater					= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        System.out.println("Adapter constructer called");
    }
    @Override
    public int getCount() 
    {
        return listData.size() + 1;
    }
    @Override
    public Ctrl_Parameters.Thermometer getItem(int position) 
    {
        return (Ctrl_Parameters.Thermometer) listData.get(position - 1);
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
        	convertView 				= myInflater.inflate(R.layout.row_thermometer, null);
            holder 						= new ViewHolder();
            holder.name 				= (TextView) convertView.findViewById(R.id.name);
            holder.friendlyName 		= (TextView) convertView.findViewById(R.id.friendlyName);
            holder.address 				= (TextView) convertView.findViewById(R.id.address);
            convertView.setTag(holder);
        } 
        else 
        {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0)
        {
           	holder.name.setText("Name");
            holder.name.setTextColor(Color.YELLOW);
            holder.name.setTypeface(null, Typeface.BOLD);
            holder.friendlyName.setText("Friendly Name");
            holder.friendlyName.setTextColor(Color.YELLOW);
            holder.friendlyName.setTypeface(null, Typeface.BOLD);
            holder.address.setText("Thermo ID");
            holder.address.setTextColor(Color.YELLOW);
            holder.address.setTypeface(null, Typeface.BOLD);
        }
        else
        {
	        holder.name.setText(((Ctrl_Parameters.Thermometer) listData.get(position - 1)).name);
//	        holder.friendlyName.setText(((Ctrl_Parameters.Thermometer) listData.get(position - 1)).friendlyName);
	        holder.address.setText(((Ctrl_Parameters.Thermometer) listData.get(position - 1)).address);
        }
        return convertView;
    }
    static class ViewHolder 
    {
    	TextView 						name;
    	TextView 						friendlyName;
    	TextView 						address;
    }
//    public void onClick(AdapterView<?> arg0, View view, int position, long arg3)
//    {
//        // Object 		o 								= view.getItemAtPosition(position);
//        // NewsItem 	newsData 						= (NewsItem) o;
//		Toast.makeText(myContext, "Selected Something, perhaps : " + position, Toast.LENGTH_LONG).show();
//    }
}