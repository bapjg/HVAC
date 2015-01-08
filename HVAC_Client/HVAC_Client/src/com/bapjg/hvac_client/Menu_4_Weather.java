package com.bapjg.hvac_client;

import HVAC_Common.*;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Menu_4_Weather 									extends 					Menu_0_Fragment 
																implements 					View.OnClickListener
{
	public Menu_4_Weather()
	{
		super();
		this.menuLayout																		= R.layout.menu_4_weather;
	}
	public void onClick(View myView) // This is the onClick event from the Menu
	{
		super.onClick(myView);
		
    	String													caption						= ((Button) myView).getText().toString();
    	FragmentTransaction										fTransaction				= getFragmentManager().beginTransaction();
    	Fragment 												panelFragment				= null;
				
    	if      (caption.equalsIgnoreCase("Today")) 			panelFragment 				= new Panel_4_Weather_WORK("Today");
    	else if (caption.equalsIgnoreCase("Tomorrow"))  		panelFragment 				= new Panel_4_Weather_WORK("Tomorrow");
     	else if (caption.equalsIgnoreCase("Beyond"))			panelFragment 				= new Panel_4_Weather_WORK("Beyond");
     	else if (caption.equalsIgnoreCase("Sunrise\nSunset"))	panelFragment 				= new Panel_4_Weather_Sun_WORK();
    	else if (caption.equalsIgnoreCase("Refresh"))			doRefresh();

    	if 		(panelFragment != null)
    	{
    		fTransaction.replace(R.id.panel_container, panelFragment);
    	}
    	fTransaction.commit();  
	}
	public void doRefresh()
	{
        TCP_Send	(new Ctrl_Weather().new Request());
	}
	@Override
	public void processFinishTCP(Ctrl__Abstract result) 
	{  
		super.processFinishTCP(result);
		if (result instanceof Ctrl_Weather.Data)
		{
			Ctrl_Weather.Data									resultWeather				= (Ctrl_Weather.Data) result;
			Global.weatherForecast				 											= (Ctrl_WeatherData) resultWeather.weatherData;
			Global.toaster("Weather report obtained", false);
			clickActiveButton();
		}
		else
		{
			Global.toaster("Weather report request failed", false);
		}
	}
}
