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
public class Adapter_4_Weather 					extends 			ArrayAdapter
{
    private ArrayList							listData;
    private LayoutInflater 						myInflater;
 
    public Adapter_4_Weather(Context context, int resource, ArrayList listData) 
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

    	convertView 											= myInflater.inflate(R.layout.row_4_weather, null);
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
    	
    	String 									day				= Global.displayDayOfWeek((listItem.dateTime.from));
    	String 									time_from		= Global.displayTimeShort(listItem.dateTime.from);
    	String 									time_to			= Global.displayTimeShort(listItem.dateTime.to);
    	
       	row.time.setText								(day + " " + time_from + " - " + time_to);											
       	row.temperature.setText							(listItem.temperature.value.toString() + " °C");											
    	row.temperatureMinMax.setText					(Math.round(listItem.temperature.min) + "/" + Math.round(listItem.temperature.max));								
    	if (listItem.precipitation != null)
    	{
    		row.precipitationValue.setText				(listItem.precipitation.value.toString() + " " + listItem.precipitation.unit);									
    		row.precipitationType.setText				(listItem.precipitation.type);
    	}
    	row.windSpeed.setText							(listItem.windSpeed.speed.toString() + " m/s");													
    	row.windName.setText							(listItem.windSpeed.name);													
    	row.windDirection.setText						(listItem.windDirection.code);											
    	row.cloudAll.setText							(listItem.clouds.all.toString() + " %");	
    	row.cloudValue.setText							(listItem.clouds.value);
    	
    	if (position % 2 == 1)
    	{
    		convertView.findViewById(R.id.row).setBackgroundColor(0x800000ff); //  BLUE =  (0xff0000ff) (first byte = intensity)
    	}
    	
    	if ( (listItem.dateTime.from 	< Global.now())
		&&   (listItem.dateTime.to 		> Global.now()) )
    	{
    		convertView.findViewById(R.id.row).setBackgroundColor(Color.BLACK);
    	}
    	
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
}