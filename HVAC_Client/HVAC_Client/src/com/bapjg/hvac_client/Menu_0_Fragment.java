package com.bapjg.hvac_client;

import HVAC_Messages.Ctrl__Abstract;
import HVAC_Messages.Ctrl_Calendars;
import HVAC_Messages.Ctrl_Configuration;
import HVAC_Messages.Ctrl_Weather;
import HVAC_Messages.Ctrl_WeatherData;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

@SuppressLint("ValidFragment")
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------

public class Menu_0_Fragment 									extends 					Fragment 
																implements					TCP_Response,
																							HTTP_Response
{
	public 	int													menuLayout;
	private ViewGroup											container;
	
	public Menu_0_Fragment()
	{
		super();
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	View 													thisView 					= inflater.inflate(this.menuLayout, container, false);				// Inflate the menuLayout into container (menu_container)
    	Button													firstButton					= (Button) ((ViewGroup) thisView).getChildAt(0);
    	this.container																		=container;
    	allButtonsSetup((ViewGroup) thisView);
		((OnClickListener) this).onClick(firstButton);																		// Execute the onClickListener of the first menu button
    	return thisView;
    }
	public void onClick(View myView) 																						// This is the onClick event from the Menu
	{
    	Button 											myButton 					= (Button) myView;
		ViewGroup 										viewParent					= (ViewGroup) myView.getParent();									// Set all textColours to white
//		allButtonsSetup(viewParent);
		allButtonsSetup(container);
    	myButton.setTextColor(Color.YELLOW);
	}
	public void allButtonsSetup(ViewGroup thisView)
	{
		for (int i = 0; i < thisView.getChildCount(); i++)																	
		{
			View											viewChild				= (View) thisView.getChildAt(i);
			if (viewChild instanceof Button)
			{
				Button										buttonChild 			= (Button) thisView.getChildAt(i);
				buttonChild.setOnClickListener((OnClickListener) this);															// Set the OnClickListener to the menuFragment object (ie this)
				buttonChild.setTextColor(Color.WHITE);																			// Colour white
			}
			else if (viewChild instanceof ViewGroup)
			{
				allButtonsSetup((ViewGroup) viewChild);
			}
		}
	}
	public void HTTP_Send(Ctrl__Abstract message)
	{
		HTTP_Task										task						= new HTTP_Task();
	   	task.callBack																= this;					// processFinish
	   	task.execute(message);		
	}		
	public void TCP_Send(Ctrl__Abstract message)		
	{		
		TCP_Task										task						= new TCP_Task();
	   	task.callBack																= this;					// processFinish
	   	task.execute(message);
	}
//	@Override	public void processFinishTCP(Ctrl_Abstract result) 											{}
//	@Override	public void processFinishHTTP(Ctrl_Abstract result) 										{}
	public void processFinishHTTP(Ctrl__Abstract result) 
	{  
		if 		(result instanceof Ctrl_Calendars.Data)		Global.eRegCalendars		= (Ctrl_Calendars.Data) result;
		else if (result instanceof Ctrl_Configuration.Data)	Global.eRegConfiguration	= (Ctrl_Configuration.Data) result;
		else if (result instanceof Ctrl__Abstract.Ack)		/* All is Ok */ ;
		else												Global.toaster("Data NOTNOTNOT received", true);
	}
	public void processFinishTCP(Ctrl__Abstract result) 
	{  
//		if (result instanceof Ctrl_Configuration.Data)
//		{
//			Global.eRegConfiguration												= (Ctrl_Configuration.Data) result;
//		}
//		else if (result instanceof Ctrl_Weather.Data)
		if (result instanceof Ctrl_Weather.Data)
		{
			Ctrl_Weather.Data							resultWeather				= (Ctrl_Weather.Data) result;
			Global.weatherForecast				 									= (Ctrl_WeatherData) resultWeather.weatherData;
		}
		else
		{
			Global.toaster("Data NOTNOTNOT received", true);
		}
	}
}

