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
public class Adapter_Weather 					extends 			ArrayAdapter
{
    private ArrayList							listData;
    private LayoutInflater 						myInflater;
 
    public Adapter_Weather(Context context, int resource, ArrayList listData) 
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

    	convertView 											= myInflater.inflate(R.layout.row_weather, null);
    	row														= new RowHolder();
       	row.time												= (TextView) convertView.findViewById(R.id.time);
       	row.temperature											= (TextView) convertView.findViewById(R.id.temperature);
    	row.temperatureMinMax									= (TextView) convertView.findViewById(R.id.temperatureMinMax);
    	row.precipitationValue									= (TextView) convertView.findViewById(R.id.precipitationValue);
    	row.precipitationType									= (TextView) convertView.findViewById(R.id.precipitationType);
    	row.windSpeed											= (TextView) convertView.findViewById(R.id.windSpeed);
    	row.windName											= (TextView) convertView.findViewById(R.id.windName);
    	row.windDirection										= (TextView) convertView.findViewById(R.id.windDirection);
    	row.cloudValue											= (TextView) convertView.findViewById(R.id.cloudValue);
    	row.cloudAll											= (TextView) convertView.findViewById(R.id.cloudAll);

    	Ctrl_WeatherData.Forecast				listItem		= (Ctrl_WeatherData.Forecast) listData.get(position);
    	
    	String 									time_from		= Global.displayTimeShort(listItem.dateTime.from);
    	String 									time_to			= Global.displayTimeShort(listItem.dateTime.to);
    	
       	row.time.setText								(time_from + " - " + time_to);											
       	row.temperature.setText							(listItem.temperature.value.toString());											
    	row.temperatureMinMax.setText					(Math.round(listItem.temperature.min) + " - " + Math.round(listItem.temperature.max));								
    	if (listItem.precipitation != null)
    	{
    		row.precipitationValue.setText				(listItem.precipitation.value.toString());									
    		row.precipitationType.setText				(listItem.precipitation.type);
    	}
    	row.windSpeed.setText							(listItem.windSpeed.speed.toString());													
    	row.windName.setText							(listItem.windSpeed.name);													
    	row.windDirection.setText						(listItem.windDirection.code);											
    	row.cloudAll.setText							(listItem.clouds.all.toString());	
    	row.cloudValue.setText							(listItem.clouds.value);												

    	convertView.setTag(row);
    	return convertView;
    }
    static class RowHolder 
    {
    	TextView 							time;
    	TextView 							temperature;
    	TextView 							temperatureMinMax;
    	TextView 							precipitationValue;
    	TextView 							precipitationType;
    	TextView 							windSpeed;
    	TextView 							windName;
    	TextView 							windDirection;
    	TextView 							cloudAll;
    	TextView 							cloudValue;
    }	
//    public void onClick(AdapterView<?> arg0, View view, int position, long arg3)
//    {
//        // Object 		o 								= view.getItemAtPosition(position);
//        // NewsItem 	newsData 						= (NewsItem) o;
//		Toast.makeText(myContext, "Selected Something, perhaps : " + position, Toast.LENGTH_LONG).show();
//    }
}