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
//     	RowHolder 												row;
    	Ctrl_WeatherData.Forecast								listItem					= (Ctrl_WeatherData.Forecast) listData.get(position);

    	Element_Linear_Vertical									adapterElement 				= new Element_Linear_Vertical();
    	Element_Standard										time						= new Element_Standard("Forecast");
    	Element_Standard										temperature	 				= new Element_Standard("Temp", "°C");
    	Element_Standard										temperatureMinMax			= new Element_Standard("Min/Max");
    	Element_Standard										precipitationValue		 	= new Element_Standard("Precip", "mm");
    	Element_Standard										precipitationType		 	= new Element_Standard("");
    	Element_Standard										windSpeed		 			= new Element_Standard("Wind", "m/s");
    	Element_Standard										windName		 			= new Element_Standard("");
    	Element_Standard										windDirection		 		= new Element_Standard("");
    	Element_Standard										cloudValue		 			= new Element_Standard("Clouds", "%");
    	Element_Standard										cloudAll		 			= new Element_Standard("");

    	adapterElement.insertPoint.addView(time);
    	adapterElement.insertPoint.addView(temperature);
    	adapterElement.insertPoint.addView(temperatureMinMax);
    	adapterElement.insertPoint.addView(precipitationValue);
    	adapterElement.insertPoint.addView(precipitationType);
    	adapterElement.insertPoint.addView(windSpeed);
    	adapterElement.insertPoint.addView(windName);
    	adapterElement.insertPoint.addView(windDirection);
    	adapterElement.insertPoint.addView(cloudValue);
    	adapterElement.insertPoint.addView(cloudAll);

    	String 													day							= Global.displayDayOfWeek((listItem.dateTime.from));
    	String 													time_from					= Global.displayTimeShort(listItem.dateTime.from);
    	String 													time_to						= Global.displayTimeShort(listItem.dateTime.to);
    	
    	time					.setValue(time_from + " - " + time_to);
    	temperature				.setValue(listItem.temperature.value);
    	temperatureMinMax		.setValue(Math.round(listItem.temperature.min) + "/" + Math.round(listItem.temperature.max));
    	if (listItem.precipitation != null)
    	{
    		precipitationValue	.setValue(listItem.precipitation.value.toString()); // + listItem.precipitation.unit);									
    		precipitationType	.setValue(listItem.precipitation.type);
    	}
    	windSpeed				.setValue(listItem.windSpeed.speed.toString());
    	windName				.setValue(listItem.windSpeed.name);
    	windDirection			.setValue(listItem.windDirection.code);
    	cloudValue				.setValue(listItem.clouds.value);
    	cloudAll				.setValue(listItem.clouds.all.toString());

    	
//       	row.time																			= (TextView) adapterView.findViewById(R.id.time);
//       	row.temperature																		= (TextView) adapterView.findViewById(R.id.temperature);
//    	row.temperatureMinMax																= (TextView) adapterView.findViewById(R.id.temperatureMinMax);
//    	row.precipitationValue																= (TextView) adapterView.findViewById(R.id.precipitationValue);
//    	row.precipitationType																= (TextView) adapterView.findViewById(R.id.precipitationType);
//    	row.windSpeed																		= (TextView) adapterView.findViewById(R.id.windSpeed);
//    	row.windName																		= (TextView) adapterView.findViewById(R.id.windName);
//    	row.windDirection																	= (TextView) adapterView.findViewById(R.id.windDirection);
//    	row.cloudValue																		= (TextView) adapterView.findViewById(R.id.cloudValue);
//    	row.cloudAll																		= (TextView) adapterView.findViewById(R.id.cloudAll);
//    	
//    	String 													day							= Global.displayDayOfWeek((listItem.dateTime.from));
//    	String 													time_from					= Global.displayTimeShort(listItem.dateTime.from);
//    	String 													time_to						= Global.displayTimeShort(listItem.dateTime.to);
//    	
////       	row.time.setText								(day + " " + time_from + " - " + time_to);											
//       	row.time.setText								(time_from + " - " + time_to);											
//       	row.temperature.setText							(listItem.temperature.value.toString() + " °C");											
//    	row.temperatureMinMax.setText					(Math.round(listItem.temperature.min) + "/" + Math.round(listItem.temperature.max));								
//    	if (listItem.precipitation != null)
//    	{
//    		row.precipitationValue.setText				(listItem.precipitation.value.toString() + " mm"); // + listItem.precipitation.unit);									
//    		row.precipitationType.setText				(listItem.precipitation.type);
//    	}
//    	row.windSpeed.setText							(listItem.windSpeed.speed.toString() + " m/s");													
//    	row.windName.setText							(listItem.windSpeed.name);													
//    	row.windDirection.setText						(listItem.windDirection.code);											
//    	row.cloudAll.setText							(listItem.clouds.all.toString() + " %");	
//    	row.cloudValue.setText							(listItem.clouds.value);
    	
    	if (position % 2 == 0)
    	{
    		adapterElement.insertPoint.setBackgroundColor(0x800000ff); //  BLUE =  (0xff0000ff) (first byte = intensity)
    	}
    	
    	if ( (listItem.dateTime.from 	< Global.now())
		&&   (listItem.dateTime.to 		> Global.now()) )
    	{
    		adapterElement.insertPoint.setBackgroundColor(Color.BLACK);
    	}
    	
    	return adapterView;
    }
//    static class RowHolder 
//    {
//    	TextView 												time;
//    	TextView 												temperature;
//    	TextView 												temperatureMinMax;
//    	TextView 												precipitationValue;
//    	TextView 												precipitationType;
//    	TextView 												windSpeed;
//    	TextView 												windName;
//    	TextView 												windDirection;
//    	TextView 												cloudAll;
//    	TextView 												cloudValue;
//    }	
}