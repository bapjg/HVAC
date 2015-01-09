package com.bapjg.hvac_client;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;
import HVAC_Common.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Panel_4_Weather_Adapter_WORK 							extends 					Panel_0_Adapter
{
 
    public Panel_4_Weather_Adapter_WORK(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
    }
    @Override
    public View getView(int position, View adapterView, ViewGroup parent) 
    {
     	RowHolder 												row;
    	Ctrl_WeatherData.Forecast								listItem					= (Ctrl_WeatherData.Forecast) listData.get(position);

    	adapterView 																		= inflater.inflate(R.layout.row_4_weather, null);
    	row																					= new RowHolder();
       	row.time																			= (TextView) adapterView.findViewById(R.id.time);
       	row.temperature																		= (TextView) adapterView.findViewById(R.id.temperature);
    	row.temperatureMinMax																= (TextView) adapterView.findViewById(R.id.temperatureMinMax);
    	row.precipitationValue																= (TextView) adapterView.findViewById(R.id.precipitationValue);
    	row.precipitationType																= (TextView) adapterView.findViewById(R.id.precipitationType);
    	row.windSpeed																		= (TextView) adapterView.findViewById(R.id.windSpeed);
    	row.windName																		= (TextView) adapterView.findViewById(R.id.windName);
    	row.windDirection																	= (TextView) adapterView.findViewById(R.id.windDirection);
    	row.cloudValue																		= (TextView) adapterView.findViewById(R.id.cloudValue);
    	row.cloudAll																		= (TextView) adapterView.findViewById(R.id.cloudAll);
    	
    	String 													day							= Global.displayDayOfWeek((listItem.dateTime.from));
    	String 													time_from					= Global.displayTimeShort(listItem.dateTime.from);
    	String 													time_to						= Global.displayTimeShort(listItem.dateTime.to);
    	
//       	row.time.setText								(day + " " + time_from + " - " + time_to);											
       	row.time.setText								(time_from + " - " + time_to);											
       	row.temperature.setText							(listItem.temperature.value.toString() + " °C");											
    	row.temperatureMinMax.setText					(Math.round(listItem.temperature.min) + "/" + Math.round(listItem.temperature.max));								
    	if (listItem.precipitation != null)
    	{
    		row.precipitationValue.setText				(listItem.precipitation.value.toString() + " mm"); // + listItem.precipitation.unit);									
    		row.precipitationType.setText				(listItem.precipitation.type);
    	}
    	row.windSpeed.setText							(listItem.windSpeed.speed.toString() + " m/s");													
    	row.windName.setText							(listItem.windSpeed.name);													
    	row.windDirection.setText						(listItem.windDirection.code);											
    	row.cloudAll.setText							(listItem.clouds.all.toString() + " %");	
    	row.cloudValue.setText							(listItem.clouds.value);
    	
    	if (position % 2 == 0)
    	{
    		adapterView.findViewById(R.id.row).setBackgroundColor(0x800000ff); //  BLUE =  (0xff0000ff) (first byte = intensity)
    	}
    	
    	if ( (listItem.dateTime.from 	< Global.now())
		&&   (listItem.dateTime.to 		> Global.now()) )
    	{
    		adapterView.findViewById(R.id.row).setBackgroundColor(Color.BLACK);
    	}
    	
    	adapterView.setTag(row);
    	return adapterView;
    }
    static class RowHolder 
    {
    	TextView 												time;
    	TextView 												temperature;
    	TextView 												temperatureMinMax;
    	TextView 												precipitationValue;
    	TextView 												precipitationType;
    	TextView 												windSpeed;
    	TextView 												windName;
    	TextView 												windDirection;
    	TextView 												cloudAll;
    	TextView 												cloudValue;
    }	
}