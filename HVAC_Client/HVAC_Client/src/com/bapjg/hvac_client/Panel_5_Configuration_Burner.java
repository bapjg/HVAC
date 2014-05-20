package com.bapjg.hvac_client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import HVAC_Messages.*;

@SuppressLint("ValidFragment")
public class Panel_5_Configuration_Burner 						extends 					Panel_0_Fragment 
{		
	public TCP_Task												task;
	public View													panelView;
	
	public Panel_5_Configuration_Burner()
	{
		super();
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	panelView																			= inflater.inflate(R.layout.panel_5_configuration_burner, container, false);
        if ((Global.eRegConfiguration != null)
        &&  (Global.eRegConfiguration.boiler != null))
        {
        	displayHeader();
        	displayContents();
            setListens();
        }
        else // we need to reconnect to the server
        {
        	Global.toaster("Please refresh", true);
        }
        return panelView;
    }
	public void displayHeader()
	{
	}
	public void displayContents()
	{
		if (getActivity() != null)			// The user has not changed the screen
		{
			Long												fuelLong					= 0L;
			String												fuelString					= "nought";
			
			Ctrl_Configuration.Burner							burner						= Global.eRegConfiguration.burner;
			
			Log.v("App", "burner" + burner);
			Log.v("App", "relay" + burner.relay);
			Log.v("App", "fuel" + burner.fuelConsumption);
			
			((TextView) panelView.findViewById(R.id.name)).setText							(burner.relay);
			DecimalFormatSymbols 								decimalFormatsymbol			= DecimalFormatSymbols.getInstance();
			decimalFormatsymbol.setGroupingSeparator(' ');
			DecimalFormat 										decimalFormat 				= new DecimalFormat("###,###", decimalFormatsymbol);
			// Present the fuel consumption in minutes
			if (Global.eRegConfiguration.burner.fuelConsumption != null)
			{
				fuelLong																	= burner.fuelConsumption/1000/60;
				fuelString																	= decimalFormat.format(fuelLong);
			}
			else
			{
				fuelString																	= "nought received";
			}
			((TextView) panelView.findViewById(R.id.fuelConsumption)).setText				(fuelString);
		}
	}
	public void setListens()
	{
	}
}

