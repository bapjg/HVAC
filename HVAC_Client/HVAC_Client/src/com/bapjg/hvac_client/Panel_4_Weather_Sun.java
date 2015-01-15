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
public class Panel_4_Weather_Sun 								extends 					Panel_0_Fragment
{
	private String												when;
	private ArrayList <Ctrl_WeatherData.Forecast> 				forecastList;
	
	private Element_Heading										heading;
	private Element_Centered_x_4								times;
	
	
	public Panel_4_Weather_Sun()
	{
		super("Standard");
	}
    public Panel_4_Weather_Sun(String when)
    {
		super();
		this.when																			= when;				
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);

    	heading																				= new Element_Heading("Sunrise", "Sunset");
    	heading.centerColumns();
    	times																				= new Element_Centered_x_4();
    	
    	displayTitles("Weather", "Sunrise/Sunset");
    	
    	panelInsertPoint.addView(heading);
    	panelInsertPoint.addView(times);

    	
    	if (Global.weatherForecast != null)
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
	public void displayContents()
	{
		if (getActivity() != null)			// The user has not changed the screen
		{
			
			if ((Global.weatherForecast.sun          == null)
			||  (Global.weatherForecast.sun.sunRise  == null))
			{
			}
			else
			{
				times.setTextTopLeft(Global.displayTime(Global.weatherForecast.sun.sunRise));
				times.setTextTopRight(Global.displayTime(Global.weatherForecast.sun.sunSet));
				times.setTextBottomLeft("");
				times.setTextBottomRight("");
			}
		}
	}
	public void setListens()
	{
	}
}

