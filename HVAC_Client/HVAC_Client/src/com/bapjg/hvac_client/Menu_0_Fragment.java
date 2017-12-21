package com.bapjg.hvac_client;

import HVAC_Common.Ctrl_Actions_Relays;
import HVAC_Common.Ctrl_Actions_Stop;
import HVAC_Common.Ctrl_Actions_Test_Mail;
import HVAC_Common.Ctrl_Calendars;
import HVAC_Common.Ctrl_Configuration;
import HVAC_Common.Ctrl_Temperatures;
import HVAC_Common.Ctrl_Weather;
import HVAC_Common.Ctrl_WeatherData;
import HVAC_Common.Msg__Abstract;
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
import android.widget.LinearLayout;

@SuppressLint("ValidFragment")
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------

public class Menu_0_Fragment 									extends 					Fragment 
																implements					TCP_Response,
																							HTTP_Response,
																							Element_Interface
{
	protected	Boolean											hasButtonRefresh;
	protected	Boolean											hasButtonUpdate;
	protected 	ViewGroup										container;
	protected 	View											menuView;
	protected 	LinearLayout									menuInsertPoint;
	protected 	LinearLayout									refreshInsertPoint;
	protected	Element_MenuButton								buttonRefresh;
	protected	Element_MenuButton								buttonUpdate;
	
	public Menu_0_Fragment(Boolean hasButtonRefresh, Boolean hasButtonUpdate)
	{
		super();
		this.hasButtonRefresh																= hasButtonRefresh;
		this.hasButtonUpdate																= hasButtonUpdate;
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	this.container																		= container;
    	this.menuView																		= inflater.inflate(R.layout.menu_0_base, container, false);				// Inflate the menuLayout into container (menu_container)
    	this.menuInsertPoint																= (LinearLayout) menuView.findViewById(R.id.menuInsertPoint);
    	
    	refreshInsertPoint																	= (LinearLayout) menuView.findViewById(R.id.buttonsRefresh);

    	if ((! hasButtonRefresh) && (! hasButtonUpdate))
    	{
			refreshInsertPoint.setVisibility(View.GONE);
    	}
		else
		{
			if (hasButtonRefresh)
			{
				buttonRefresh 																= new Element_MenuButton("Refresh");
				refreshInsertPoint.addView(buttonRefresh);
			}
			if (hasButtonUpdate)	// This is erroneous, as repeated below								buttonUpdate 				= new Element_MenuButton("Update");
			{
				buttonUpdate 																= new Element_MenuButton("Update");
				refreshInsertPoint.addView(buttonUpdate);
			}
	    	buttonRefresh	.setListener(this); 
	    	buttonUpdate	.setListener(this); 
		}

		return menuView;
    }
	public void onElementClick(View clickedView)
	{
       	if 		(clickedView == buttonRefresh)					doRefresh();				// Handle refresh buttons if present
    	else if (clickedView == buttonUpdate)					doUpdate();					// Handle refresh buttons if present
    	else																				// Handle menu buttons
    	{
			for (int i = 0; i <  menuInsertPoint.getChildCount();  i++)
			{
				Element_MenuButton menuButton =  (Element_MenuButton) menuInsertPoint.getChildAt(i);
				
				if (menuButton == clickedView)							menuButton.setYellow();
				else													menuButton.setWhite();
			}
    	}
	}
	public void doRefresh() {}
	public void doUpdate() {}
//	public void onConfigurationChanged(Configuration newConfig)
//	{
//		super.onConfigurationChanged(newConfig);
//	    LayoutInflater inflater 															= (LayoutInflater) Global.actContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    	menuView 																			= inflater.inflate(this.menuLayout, container, false);				// Inflate the menuLayout into container (menu_container)
//    	
//    	clickActiveButton();
//	}
	public void clickActiveButton()
	{
		for (int i = 0; i < menuInsertPoint.getChildCount(); i++)																	
		{
			Element_MenuButton									buttonChild					= (Element_MenuButton) menuInsertPoint.getChildAt(i);
			if (buttonChild.isYellow)
			{
				buttonChild.onClick(buttonChild);
				return;
			}
		}
	}
	public void HTTP_Send(Msg__Abstract message)
	{
		Global.setStatusHTTP("Waiting");
		HTTP_Task												task						= new HTTP_Task();
	   	task.callBack																		= this;					// processFinish
	   	task.execute(message);		
	}		
	public void TCP_Send(Msg__Abstract message)		
	{		
		Global.setStatusTCP("Waiting");
		TCP_Task												task						= new TCP_Task();
	   	task.callBack																		= this;					// processFinish
	   	task.execute(message);									// processFinishHTTP is in the instaciated class
	}
	public void processFinishHTTP(Msg__Abstract result) 
	{  
		Global.setAddressSpace();
		Global.setStatusHTTP(result);
	}
	public void processFinishTCP(Msg__Abstract result) 						// Overridden in subclass
	{  
		Global.setAddressSpace();
		Global.setStatusTCP(result);
	}	
}

