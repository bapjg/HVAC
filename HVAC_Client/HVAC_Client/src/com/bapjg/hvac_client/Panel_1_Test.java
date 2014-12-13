package com.bapjg.hvac_client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import HVAC_Common.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Panel_1_Test 										extends 					Panel_0_Fragment 
{		
	public TCP_Task												task;
	private Ctrl_Temperatures.Data								temperatureData;
	
	private Panel_0_Base_Linear									tempBoiler;
	private Panel_0_Base_Linear									tempHW;
	private Panel_0_Base_Linear									tempOutside;
	
	public Panel_1_Test()
	{
		super();
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	this.panelLayout																	= R.layout.panel_1_temperatures;
    	this.container																		= container;
		this.panelView																		= inflater.inflate(R.layout.panel_0_base_linear, container, false);
    	displayHeader();
    	
    	LinearLayout insertPoint = (LinearLayout) panelView.findViewById(R.id.base_insert_point);
    	
    	Panel_0_Base_Linear										tempBoiler 					= new Panel_0_Base_Linear(getActivity());
    	Panel_0_Base_Linear  									tempHW 						= new Panel_0_Base_Linear(getActivity());
    	Panel_0_Base_Linear  									tempOutside 				= new Panel_0_Base_Linear(getActivity());

    	tempBoiler.setTextLeft("Boiler");
    	tempBoiler.setTextRight("Line1 R");

    	tempHW.setTextLeft("Hot Water");
    	tempHW.setTextRight("Line2 R");

    	tempOutside.setTextLeft("Outside");
    	tempOutside.setTextRight("Line3 R");

    	insertPoint.addView(tempBoiler);
    	insertPoint.addView(tempHW);
    	insertPoint.addView(tempOutside);
          	
        return panelView;
    }
	public void processFinishTCP(Ctrl__Abstract result) 
	{             
		super.processFinishTCP(result);
		if 	(result instanceof Ctrl_Temperatures.Data)
		{
			temperatureData																	= (Ctrl_Temperatures.Data) result;																	
			displayContents();
		}
        setListens();
    } 
	public void displayHeader()
	{
		((TextView) panelView.findViewById(R.id.title)).setText		("Temperatures");
		((TextView) panelView.findViewById(R.id.subTitle)).setText	("Readings");
	}
	public void displayContents()
	{
		if (getActivity() != null)			// The user has not changed the screen
		{
//			((TextView) panelView.findViewById(R.id.Date)).setText				(Global.displayDateShort	(temperatureData.dateTime));
//			((TextView) panelView.findViewById(R.id.Time)).setText				(Global.displayTimeShort	(temperatureData.dateTime));
//			
			tempBoiler.setTextRight 	(Global.displayTemperature	(temperatureData.tempBoiler));
			tempHW.setTextRight			(Global.displayTemperature	(temperatureData.tempHotWater));
			tempOutside.setTextRight	(Global.displayTemperature	(temperatureData.tempOutside));
//			((TextView) panelView.findViewById(R.id.BoilerIn)).setText			(Global.displayTemperature	(temperatureData.tempBoilerIn));
//			((TextView) panelView.findViewById(R.id.BoilerOut)).setText			(Global.displayTemperature	(temperatureData.tempBoilerOut));
//			((TextView) panelView.findViewById(R.id.FloorIn)).setText			(Global.displayTemperature	(temperatureData.tempFloorIn));
//			((TextView) panelView.findViewById(R.id.FloorOut)).setText			(Global.displayTemperature	(temperatureData.tempFloorOut));
//			((TextView) panelView.findViewById(R.id.RadiatorIn)).setText		(Global.displayTemperature	(temperatureData.tempRadiatorIn));
//			((TextView) panelView.findViewById(R.id.RadiatorOut)).setText		(Global.displayTemperature	(temperatureData.tempRadiatorOut));
//			((TextView) panelView.findViewById(R.id.LivingRoom)).setText		(Global.displayTemperature	(temperatureData.tempLivingRoom));
		}
	}
	public void setListens()
	{
	}
}

