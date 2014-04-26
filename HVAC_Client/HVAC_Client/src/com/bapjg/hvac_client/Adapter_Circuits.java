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
    public Ctrl_Configuration.Circuit getItem(int position) 
    {
        return (Ctrl_Configuration.Circuit) listData.get(position - 1);
    }
    @Override
    public long getItemId(int position) 
    {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
    	TitleHolder 							title;
    	RowHolder 								row;

//        if (convertView == null) 
//        {
//        	convertView 											= myInflater.inflate(R.layout.row_circuit, null);
//            holder 													= new ViewHolder();
//            holder.name 											= (TextView) convertView.findViewById(R.id.name);
//            holder.gradient 										= (CheckBox) convertView.findViewById(R.id.gradient);
//
//            convertView.setTag(holder);
//        } 
//        else 
//        {
//            holder = (ViewHolder) convertView.getTag();
//        }
        if (position == 0)
        {
        	convertView 											= myInflater.inflate(R.layout.row_circuit_title, null);
        	title 													= new TitleHolder();
        	title.name 												= (TextView) convertView.findViewById(R.id.name);
        	title.gradient 											= (TextView) convertView.findViewById(R.id.gradient);
        	title.mixer 											= (TextView) convertView.findViewById(R.id.mixer);
            convertView.setTag(title);

            title.name.setTextColor			(Color.YELLOW);
            title.name.setTypeface			(null, Typeface.BOLD);
            
            title.gradient.setTextColor		(Color.YELLOW);
            title.gradient.setTypeface		(null, Typeface.BOLD);

            title.mixer.setTextColor		(Color.YELLOW);
            title.mixer.setTypeface			(null, Typeface.BOLD);
        }
        else
        {
        	convertView 											= myInflater.inflate(R.layout.row_circuit, null);
        	row 													= new RowHolder();
        	row.name 												= (TextView) convertView.findViewById(R.id.name);
        	row.gradient 											= (CheckBox) convertView.findViewById(R.id.gradient);
        	row.mixer	 											= (CheckBox) convertView.findViewById(R.id.mixer);
            convertView.setTag(row);

            row.name.setText				(((Ctrl_Configuration.Circuit) listData.get(position - 1)).name);
	        
        	if ((((Ctrl_Configuration.Circuit) listData.get(position - 1)).tempGradient) == null)
        	{
        		row.gradient.setChecked(false);
        	}
        	else
        	{
        		row.gradient.setChecked(true);
        	}
           	if ((((Ctrl_Configuration.Circuit) listData.get(position - 1)).mixer) == null)
        	{
           		row.mixer.setChecked(false);
        	}
        	else
        	{
        		row.mixer.setChecked(true);
        	}
        }
        return convertView;
    }
    static class TitleHolder 
    {
    	TextView 							name;
    	TextView 							gradient;
    	TextView 							mixer;
    }	
    static class RowHolder 
    {
    	TextView 							name;
    	CheckBox 							gradient;
    	CheckBox 							mixer;
    }	
//    public void onClick(AdapterView<?> arg0, View view, int position, long arg3)
//    {
//        // Object 		o 								= view.getItemAtPosition(position);
//        // NewsItem 	newsData 						= (NewsItem) o;
//		Toast.makeText(myContext, "Selected Something, perhaps : " + position, Toast.LENGTH_LONG).show();
//    }
}