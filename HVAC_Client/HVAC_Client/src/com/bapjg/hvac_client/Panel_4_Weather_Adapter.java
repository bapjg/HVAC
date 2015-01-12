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
public class Panel_4_Weather_Adapter 							extends 					Panel_0_Adapter
{
 
    public Panel_4_Weather_Adapter(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
    }
    @Override
    public View getView(int position, View adapterView, ViewGroup parent) 
    {
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
    	Element_Standard										cloudAll		 			= new Element_Standard("Clouds", "%");
    	Element_Standard										cloudValue		 			= new Element_Standard("");

    	adapterElement.insertPoint.addView(time);
    	adapterElement.insertPoint.addView(temperature);
    	adapterElement.insertPoint.addView(temperatureMinMax);
    	adapterElement.insertPoint.addView(precipitationValue);
    	adapterElement.insertPoint.addView(precipitationType);
    	adapterElement.insertPoint.addView(windSpeed);
    	adapterElement.insertPoint.addView(windName);
    	adapterElement.insertPoint.addView(windDirection);
    	adapterElement.insertPoint.addView(cloudAll);
    	adapterElement.insertPoint.addView(cloudValue);

    	String 													day							= Global.displayDayOfWeek((listItem.dateTime.from));
    	String 													time_from					= Global.displayTimeShort(listItem.dateTime.from);
    	String 													time_to						= Global.displayTimeShort(listItem.dateTime.to);
    	
    	time					.setValue(time_from + " - " + time_to);
    	temperature				.setValue(listItem.temperature.value);
    	temperatureMinMax		.setValue(Math.round(listItem.temperature.min) + "/" + Math.round(listItem.temperature.max));
    	if (listItem.precipitation != null)
    	{
    		precipitationValue	.setValue(listItem.precipitation.value); // + listItem.precipitation.unit);									
    		precipitationType	.setValue(listItem.precipitation.type);
    	}
    	windSpeed				.setValue(listItem.windSpeed.speed);
    	windName				.setValue(listItem.windSpeed.name);
    	windDirection			.setValue(listItem.windDirection.code);
    	cloudAll				.setValue(listItem.clouds.all);
    	cloudValue				.setValue(listItem.clouds.value);
    	
    	if (position % 2 == 0)
    	{
    		adapterElement.setBackground(0x800000ff); //  BLUE =  (0xff0000ff) (first byte = intensity)
    	}
    	
    	if ( (listItem.dateTime.from 	< Global.now())
		&&   (listItem.dateTime.to 		> Global.now()) )
    	{
    		adapterElement.setBackground(Color.BLACK);
    	}
    	
    	return adapterElement;
    }
}