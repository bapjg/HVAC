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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import HVAC_Common.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Panel_1_Temperatures 								extends 					Panel_0_Fragment 
{		
	public TCP_Task												task;
	private Ctrl_Temperatures.Data								temperatureData;
	private Element_Standard									tempBoiler;
	private Element_Standard									tempHW;
	private Element_Standard									tempOutside;
	private Element_Standard  									tempLivingRoom;
	private Element_Standard  									tempFloorOut;
	private Element_Standard  									tempFloorIn;
	private Element_Standard  									tempBoilerOut;
	private Element_Standard  									tempRadiatorOut;
	private Element_Standard  									tempRadiatorIn;
	private Element_Standard  									tempBoilerIn;
	
	public Panel_1_Temperatures()
	{
		super();
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
//    	this.panelLayout																	= R.layout.panal_0_standard;
    	this.container																		= container;
		this.panelView																		= inflater.inflate(R.layout.panal_0_standard, container, false);
    	displayTitles("Temperatures", "Readings");
    	TCP_Send(new Ctrl_Temperatures().new Request());
    	LinearLayout 											insertPoint 				= (LinearLayout) panelView.findViewById(R.id.base_insert_point);
    	
    	tempBoiler 																			= new Element_Standard(getActivity(), "Boiler");
    	tempHW 																				= new Element_Standard(getActivity(), "Hot Water");
    	tempOutside 																		= new Element_Standard(getActivity(), "Outside");
    	tempLivingRoom 																		= new Element_Standard(getActivity(), "Living Room");
    	tempFloorOut 																		= new Element_Standard(getActivity(), "Floor Out");
    	tempFloorIn 																		= new Element_Standard(getActivity(), "Floor In");
    	tempBoilerOut 																		= new Element_Standard(getActivity(), "Boiler Out");
    	tempRadiatorOut																		= new Element_Standard(getActivity(), "Radiator Out");
    	tempRadiatorIn																		= new Element_Standard(getActivity(), "Radiator In");
    	tempBoilerIn 																		= new Element_Standard(getActivity(), "Boiler In");

       	Element_Heading											listHeading					= new Element_Heading(getActivity(), "Thermometer", "Temperature");

    	insertPoint.addView(listHeading);
    	insertPoint.addView(tempBoiler);
    	insertPoint.addView(tempHW);
    	insertPoint.addView(tempOutside);
    	insertPoint.addView(new Element_Filler(getActivity()));
    	insertPoint.addView(tempLivingRoom);
    	insertPoint.addView(new Element_Filler(getActivity()));
    	insertPoint.addView(tempFloorOut);
    	insertPoint.addView(tempFloorIn);
    	insertPoint.addView(tempBoilerOut);
    	insertPoint.addView(new Element_Filler(getActivity()));
    	insertPoint.addView(tempRadiatorOut);
    	insertPoint.addView(tempRadiatorIn);
    	insertPoint.addView(new Element_Filler(getActivity()));
    	insertPoint.addView(tempBoilerIn);
          	
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

	    	tempLivingRoom.setTextRight(Global.displayTemperature	(temperatureData.tempLivingRoom));
	    	
	    	tempFloorOut.setTextRight(Global.displayTemperature		(temperatureData.tempFloorOut));
	    	tempFloorIn.setTextRight(Global.displayTemperature		(temperatureData.tempFloorIn));
	    	tempBoilerOut.setTextRight(Global.displayTemperature	(temperatureData.tempBoilerOut));
	    	
	    	tempRadiatorOut.setTextRight(Global.displayTemperature	(temperatureData.tempRadiatorOut));
	    	tempRadiatorIn.setTextRight(Global.displayTemperature	(temperatureData.tempRadiatorIn));
	    	
	    	tempBoilerIn.setTextRight(Global.displayTemperature		(temperatureData.tempBoilerIn));
		}
	}
	public void setListens()
	{
	}
}

