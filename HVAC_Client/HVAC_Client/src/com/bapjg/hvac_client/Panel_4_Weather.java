package com.bapjg.hvac_client;

import java.util.ArrayList;

import HVAC_Messages.*;
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

@SuppressLint("ValidFragment")
public class Panel_4_Weather 							extends 					Panel_0_Fragment
{
	private String										when;
	private ArrayList <Ctrl_WeatherData.Forecast> 		forecastList;
	private View										panelView;
	
	public Panel_4_Weather()
	{
		super();
	}
    public Panel_4_Weather(String when)
    {
		super();
		this.when																	= when;				
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        this.panelView																= inflater.inflate(R.layout.panel_4_weather, container, false);

		if ((Global.weatherForecast != null)
		&&  (Global.weatherForecast.forecasts != null) 
		&&  (Global.weatherForecast.forecasts.size() != 0) )
		{
			displayHeader();
			displayContents();
	        setListens();
		}
		else
		{
			Global.toaster("HTTP Response is null,  server must be delaying... try later", true);
		}
        return panelView;
    }
//  @Override
//	public void onClick(View myView) 
//	{
//    	Log.v("App", "We have arrived in onClick again");
//    	
//    	Button 											myButton 					= (Button) myView;
//    	String											myCaption					= myButton.getText().toString();
//						
//		// Set all textColours to white				
//		ViewGroup 										viewParent					= (ViewGroup) myView.getParent();
//		for (int i = 0; i < viewParent.getChildCount(); i++)
//		{
//			Button										buttonChild 				= (Button) viewParent.getChildAt(i);
//			buttonChild.setTextColor(Color.WHITE);
//		}
//		
//		((Button) myView).setTextColor(Color.YELLOW);
//    	
//    	if (myCaption.equalsIgnoreCase("Thermometers"))
//    	{
//    		// buttonThermometersClick(myView);	
//    	}
//	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
	}
	public void displayHeader()
	{	
		Ctrl_WeatherData xxx = Global.weatherForecast;
		
		
		TextView 										dateTimeObtained			= (TextView) panelView.findViewById(R.id.dateTimeObtained);
        dateTimeObtained.setText 													(Global.displayTimeShort(Global.weatherForecast.dateTimeObtained));
        
        TextView 										dateTime					= (TextView) panelView.findViewById(R.id.dateTime);
		if 		(when.equalsIgnoreCase("Today"))		dateTime.setText 			(Global.displayDateShort(Global.getTimeAtMidnight()));
		else if (when.equalsIgnoreCase("Tomorrow"))		dateTime.setText 			(Global.displayDateShort(Global.getTimeAtMidnight() + 24 * 60 * 60 * 1000L));
		else if (when.equalsIgnoreCase("Beyond"))		dateTime.setText 			("> " + Global.displayDateShort(Global.getTimeAtMidnight() + 24 * 60 * 60 * 1000L));
	}
	public void displayContents()
	{
        AdapterView <Adapter_4_Weather> 		view						= (AdapterView) panelView.findViewById(R.id.List_View);
        
        forecastList														= new ArrayList <Ctrl_WeatherData.Forecast> ();
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
        Adapter_4_Weather						adapter						= new Adapter_4_Weather(Global.actContext, R.id.List_View, forecastList);
        view.setAdapter(adapter);
	}
	public void setListens()
	{
	}
}

