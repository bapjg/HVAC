package com.bapjg.hvac_client;

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
	
	public Panel_4_Weather()
	{
		super();
	}
    public Panel_4_Weather(int menuLayout)
    {
		super(menuLayout);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        myInflater													= inflater;
        myContainer 												= container;
        myActivity													= getActivity();
        myFragmentManager 											= myActivity.getFragmentManager();
        View 									panelView			= myInflater.inflate(R.layout.panel_5_config_header, container, false);
        myAdapterView												= (AdapterView) panelView.findViewById(R.id.List_View);

        // This part can be in processFinishTCP/HTTP 
        TCP_Send(new Ctrl_Weather().new Request());
        
        return panelView;    }
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
	        Adapter_Weather							adapter				= new Adapter_Weather(Global.actContext, R.id.List_View, Global.weatherForecast.forecasts);
	        view.setAdapter(adapter);
		}
		else
		{
			Global.toaster("Data NOTNOTNOT received", true);
		}
	}
}

