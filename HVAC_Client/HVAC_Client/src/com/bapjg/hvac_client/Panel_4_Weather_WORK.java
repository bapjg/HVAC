package com.bapjg.hvac_client;

import java.util.ArrayList;

import HVAC_Common.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Panel_4_Weather_WORK 									extends 					Panel_0_Fragment
{
	private String												when;
	private ArrayList <Ctrl_WeatherData.Forecast> 				forecastList;
	
	public Panel_4_Weather_WORK()
	{
		super("Standard");
	}
    public Panel_4_Weather_WORK(String when)
    {
		super("Standard");
		this.when																			= when;				
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
//    	this.panelLayout																	= R.layout.panel_4_weather;
//    	this.container																		= container;
//    	this.panelView																		= inflater.inflate(R.layout.panel_4_weather, container, false);

    	super.panelInitialise(inflater, container, savedInstanceState);

    	Element_ListView										listView 					= new Element_ListView("Henry");
    	panelInsertPoint.addView(listView);

    	this.adapterView																	= (AdapterView) panelView.findViewById(R.id.List_View);

    	displayTitles("Weather", this.when);
    	
		if ((Global.weatherForecast != null)
		&&  (Global.weatherForecast.forecasts != null) 
		&&  (Global.weatherForecast.forecasts.size() != 0) )
		{

			displayContents();
	        setListens();
		}
		else
		{
			Global.toaster("HTTP Response is null,  server must be delaying... try later", true);
		}
        return panelView;
    }
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
	}
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
	public void displayContents()
	{
//		((TextView) panelView.findViewById(R.id.dateTimeObtained)).setText(Global.displayTimeShort(Global.weatherForecast.dateTimeObtained));
		
		AdapterView <Panel_4_Weather_Adapter_WORK> 				view						= (AdapterView) panelView.findViewById(R.id.List_View);
        
        forecastList																		= new ArrayList <Ctrl_WeatherData.Forecast> ();
        if (when.equalsIgnoreCase("Today"))
		{
	        for (Ctrl_WeatherData.Forecast forecastItem : Global.weatherForecast.forecasts)
	        {
	        	if  ((forecastItem.dateTime.from > Global.getTimeAtMidnight())							// timeStamp > last midnight
	        	&& 	 (forecastItem.dateTime.from < Global.getTimeAtMidnight() + 24 * 60 * 60 * 1000L))	// timeStamp < next midnight
	        	{
	        		forecastList.add(forecastItem);
	        	}
	        }
		}
		else if (when.equalsIgnoreCase("Tomorrow"))
		{
	        for (Ctrl_WeatherData.Forecast forecastItem : Global.weatherForecast.forecasts)
	        {
	        	if  ((forecastItem.dateTime.from > Global.getTimeAtMidnight() + 24 * 60 * 60 * 1000L)
	        	&& 	 (forecastItem.dateTime.from < Global.getTimeAtMidnight() + 24 * 60 * 60 * 1000L * 2))
	        	{
	        		forecastList.add(forecastItem);
	        	}
	        }
		}
		else if (when.equalsIgnoreCase("Beyond"))
		{
	        for (Ctrl_WeatherData.Forecast forecastItem : Global.weatherForecast.forecasts)
	        {
	        	if  (forecastItem.dateTime.from > Global.getTimeAtMidnight() + 24 * 60 * 60 * 1000L * 3)
	        	{
	        		forecastList.add(forecastItem);
	        	}
	        }
		}
        Panel_4_Weather_Adapter_WORK								adapter						= new Panel_4_Weather_Adapter_WORK(Global.actContext, R.id.List_View, forecastList);
        view.setAdapter(adapter);
	}
	public void setListens()
	{
	}
}

