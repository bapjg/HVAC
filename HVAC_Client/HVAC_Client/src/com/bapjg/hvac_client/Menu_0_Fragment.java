package com.bapjg.hvac_client;

import HVAC_Common.Ctrl_Actions_Relays;
import HVAC_Common.Ctrl_Actions_Stop;
import HVAC_Common.Ctrl_Actions_Test_Mail;
import HVAC_Common.Ctrl_Calendars;
import HVAC_Common.Ctrl_Configuration;
import HVAC_Common.Ctrl_Temperatures;
import HVAC_Common.Ctrl_Weather;
import HVAC_Common.Ctrl_WeatherData;
import HVAC_Common.Ctrl__Abstract;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
	private View												menuView;
	private View												listView;
	
	public Menu_0_Fragment()
	{
		super();
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
		menuView 																			= inflater.inflate(this.menuLayout, container, false);				// Inflate the menuLayout into container (menu_container)
    	listView																			= menuView.findViewById(R.id.buttons_container);
    	Button													firstButton					= (Button) ((ViewGroup) listView).getChildAt(0);
    	this.container																		= container;
    	allButtonsSetup((ViewGroup) menuView);
		((OnClickListener) this).onClick(firstButton);																		// Execute the onClickListener of the first menu button
    	return menuView;
    }
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
	    LayoutInflater inflater 															= (LayoutInflater) Global.actContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	menuView 																			= inflater.inflate(this.menuLayout, container, false);				// Inflate the menuLayout into container (menu_container)
	}
	public void onClick(View myView) 																						// This is the onClick event from the Menu
	{
    	Button 													myButton 					= (Button) myView;
		ViewGroup 												viewParent					= (ViewGroup) myView.getParent();									// Set all textColours to white
		
		if (viewParent.getId() == R.id.buttons_refresh)
		{
			Global.toaster("Communicating with server",  true);
		}
		else
		{
			allButtonsSetup(viewParent);
			//		allButtonsSetup(container);
			myButton.setTextColor(Color.YELLOW);
		}
	}
	public void allButtonsSetup(ViewGroup thisView)
	{
		for (int i = 0; i < thisView.getChildCount(); i++)																	
		{
			View												viewChild					= (View) thisView.getChildAt(i);
			if (viewChild instanceof Button)
			{
				Button											buttonChild 				= (Button) thisView.getChildAt(i);
				buttonChild.setOnClickListener((OnClickListener) this);															// Set the OnClickListener to the menuFragment object (ie this)
				buttonChild.setTextColor(Color.WHITE);																			// Colour white
			}
			else if (viewChild instanceof ViewGroup)
			{
				allButtonsSetup((ViewGroup) viewChild);
			}
		}
	}
	public void clickActiveButton()
	{
		ViewGroup												menuViewGroup				= (ViewGroup) menuView;
		for (int i = 0; i < menuViewGroup.getChildCount(); i++)																	
		{
			View												viewChild					= (View) menuViewGroup.getChildAt(i);
			if ( (viewChild instanceof Button)
			&&   (((Button) viewChild).getCurrentTextColor() == Color.YELLOW) )
			{
				((OnClickListener) this).onClick(viewChild);
				return;
			}
		}
	}
	public void HTTP_Send(Ctrl__Abstract message)
	{
		Global.setStatusHTTP("Waiting");
		HTTP_Task												task						= new HTTP_Task();
	   	task.callBack																		= this;					// processFinish
	   	task.execute(message);		
	}		
	public void TCP_Send(Ctrl__Abstract message)		
	{		
		Global.setStatusTCP("Waiting");
		TCP_Task												task						= new TCP_Task();
	   	task.callBack																		= this;					// processFinish
	   	task.execute(message);									// processFinishHTTP is in the instaciated class
	}
	public void processFinishHTTP(Ctrl__Abstract result) 
	{  
		Global.setAddressSpace();
		Global.setStatusHTTP(result);
	}
	public void processFinishTCP(Ctrl__Abstract result) 						// Overridden in subclass
	{  
		Global.setAddressSpace();
		Global.setStatusTCP(result);
	}							
}

