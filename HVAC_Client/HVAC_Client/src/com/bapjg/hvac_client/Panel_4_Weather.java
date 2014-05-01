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


@SuppressLint("ValidFragment")
//Template										variable			= something
//Template										ext/imp				class
public class Panel_4_Weather 					extends 			Panel_0_Fragment
												implements 			TCP_Response
{
	private LayoutInflater						myInflater;
	private Activity							myActivity;
	private ViewGroup							myContainer;
	private View								myAdapterView;
	private FragmentManager						myFragmentManager;
	private String								when;
	private ArrayList <Ctrl_WeatherData.Forecast> forecastList;			
	
	public Panel_4_Weather()
	{
		super();
	}
    public Panel_4_Weather(String when)
    {
		super();
		this.when													= when;				
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        myInflater													= inflater;
        myContainer 												= container;
        myActivity													= getActivity();
        myFragmentManager 											= myActivity.getFragmentManager();
        View 									panelView			= myInflater.inflate(R.layout.panel_4_weather, container, false);
        TextView 								textview			= (TextView) panelView.findViewById(R.id.dateTime);
		if (when.equalsIgnoreCase("Today"))
		{
	        textview.setText (Global.displayDate(Global.getTimeAtMidnight()));
		}
		else if (when.equalsIgnoreCase("Tomorrow"))
		{
	        textview.setText (Global.displayDate(Global.getTimeAtMidnight() + 24 * 60 * 60 * 1000L));
		}
		else if (when.equalsIgnoreCase("Beyond"))
		{
	        textview.setText ("> " + Global.displayDate(Global.getTimeAtMidnight() + 24 * 60 * 60 * 1000L));
		}
        myAdapterView												= (AdapterView) panelView.findViewById(R.id.List_View);

        TCP_Send(new Ctrl_Weather().new Request());
        return panelView;
    }
    @Override
	public void onClick(View myView) 
	{
    	System.out.println("We have arrived in onClick again");
    	
    	Button 									myButton 			= (Button) myView;
    	String									myCaption			= myButton.getText().toString();
    	
		// Set all textColours to white
		ViewGroup 								viewParent			= (ViewGroup) myView.getParent();
		for (int i = 0; i < viewParent.getChildCount(); i++)
		{
			Button								buttonChild 		= (Button) viewParent.getChildAt(i);
			buttonChild.setTextColor(Color.WHITE);
		}
		
		((Button) myView).setTextColor(Color.YELLOW);
    	
    	if (myCaption.equalsIgnoreCase("Thermometers"))
    	{
    		// buttonThermometersClick(myView);	
    	}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		// TODO Auto-generated method stub
		
	}
	public void TCP_Send(Ctrl_Abstract message)
	{
		TCP_Task								task				= new TCP_Task();
	   	task.callBack												= this;					// processFinish
	   	task.execute(message);
	}
	public void processFinishTCP(Ctrl_Abstract result) 
	{
		Activity								activity			= getActivity();	
		System.out.println("gc " + result.getClass().toString());

		if (result instanceof Ctrl_Weather.Data)
		{
			Ctrl_Weather.Data						resulatWeather		= (Ctrl_Weather.Data) result;
			Global.weatherForecast				 						= (Ctrl_WeatherData) resulatWeather.weatherData;
	        AdapterView <Adapter_Weather> 			view				= (AdapterView) myContainer.findViewById(R.id.List_View);
	        
			if (when.equalsIgnoreCase("Today"))
			{
		        forecastList											= new ArrayList <Ctrl_WeatherData.Forecast> ();
		        for (Ctrl_WeatherData.Forecast forcastItem : Global.weatherForecast.forecasts)
		        {
		        	if  ((forcastItem.dateTime.from > Global.getTimeAtMidnight())
		        	&& 	 (forcastItem.dateTime.from < Global.getTimeAtMidnight() + 24 * 60 * 60 * 1000L))
		        	{
		        		forecastList.add(forcastItem);
		        	}
		        }
			}
			else if (when.equalsIgnoreCase("Tomorrow"))
			{
		        forecastList											= new ArrayList <Ctrl_WeatherData.Forecast> ();
		        for (Ctrl_WeatherData.Forecast forcastItem : Global.weatherForecast.forecasts)
		        {
		        	if  ((forcastItem.dateTime.from > Global.getTimeAtMidnight() + 24 * 60 * 60 * 1000L)
		        	&& 	 (forcastItem.dateTime.from < Global.getTimeAtMidnight() + 24 * 60 * 60 * 1000L * 2))
		        	{
		        		forecastList.add(forcastItem);
		        	}
		        }
			}
			else if (when.equalsIgnoreCase("Beyond"))
			{
		        forecastList											= new ArrayList <Ctrl_WeatherData.Forecast> ();
		        for (Ctrl_WeatherData.Forecast forcastItem : Global.weatherForecast.forecasts)
		        {
		        	if  (forcastItem.dateTime.from > Global.getTimeAtMidnight() + 24 * 60 * 60 * 1000L * 3)
		        	{
		        		forecastList.add(forcastItem);
		        	}
		        }
			}
	        Adapter_Weather							adapter				= new Adapter_Weather(Global.actContext, R.id.List_View, forecastList);
	        view.setAdapter(adapter);
		}
		else
		{
			Global.toaster("Data NOTNOTNOT received", true);
		}
	}
}

