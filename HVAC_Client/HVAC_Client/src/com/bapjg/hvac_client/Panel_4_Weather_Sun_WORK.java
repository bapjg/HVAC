package com.bapjg.hvac_client;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
public class Panel_4_Weather_Sun_WORK 									extends 					Panel_0_Fragment
{
	private String												when;
	private ArrayList <Ctrl_WeatherData.Forecast> 				forecastList;
	
	public Panel_4_Weather_Sun_WORK()
	{
		super();
	}
    public Panel_4_Weather_Sun_WORK(String when)
    {
		super();
		this.when																			= when;				
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
//    	this.panelLayout																	= R.layout.panel_4_weather_sun;
    	this.container																		= container;
    	this.panelView																		= inflater.inflate(R.layout.panel_4_weather_sun, container, false);

		if (Global.weatherForecast != null)
		{
        	displayTitles("Weather", "Sunrise/Sunset");
			displayContents();
	        setListens();
		}
		else
		{
			Global.toaster("HTTP Response is null,  server must be delaying... try later", true);
		}
        return panelView;
    }
	public void displayContents()
	{
		if (getActivity() != null)			// The user has not changed the screen
		{
			TextView 											sunRiseView					= (TextView) panelView.findViewById(R.id.sunRise);
			TextView 											sunSetView					= (TextView) panelView.findViewById(R.id.sunSet);
			
			Ctrl_WeatherData.Sun 								sun 						= Global.weatherForecast.sun;
			
			if ((Global.weatherForecast.sun          == null)
			||  (Global.weatherForecast.sun.sunRise  == null))
			{
				sunRiseView.setText("Unavailable");
				sunSetView.setText("Unavailable");
			}
			else
			{
				sunRiseView.setText(Global.displayTime(Global.weatherForecast.sun.sunRise));
				sunSetView.setText(Global.displayTime(Global.weatherForecast.sun.sunSet));
			}
		}
	}
	public void setListens()
	{
	}
}

