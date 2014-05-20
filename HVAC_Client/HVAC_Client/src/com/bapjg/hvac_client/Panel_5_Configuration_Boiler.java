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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import HVAC_Messages.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Panel_5_Configuration_Boiler 						extends 					Panel_0_Fragment
{		
	public TCP_Task												task;
	private View												panelView;					// This corresponds to the inflated panel (R.layout.panel_n_xxxxxx)
	
	public Panel_5_Configuration_Boiler()
	{
		super();
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	this.panelView																		= inflater.inflate(R.layout.panel_5_configuration_boiler, container, false);

    	if ( (Global.eRegConfiguration 			!= 	null)
        &&   (Global.eRegConfiguration.boiler 	!= 	null)	)
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
	public void onClick(View view)
	{
		if (view.getId() == R.id.tempNeverExceed)
		{
			Integer											temperature					= Global.eRegConfiguration.boiler.tempNeverExceed;
			Dialog_Temperature								df 							= new Dialog_Temperature(this, view.getId(), temperature, 85, 1, 20);
			df.show(getFragmentManager(), "Dialog_Temperature");
		}
		else if (view.getId() == R.id.tempOverShoot)
		{
			Integer											temperature					= Global.eRegConfiguration.boiler.tempOverShoot;
			Dialog_Temperature 								df 							= new Dialog_Temperature(this, view.getId(), temperature, 10, 1, 15);
			df.show(getFragmentManager(), "Dialog_Temperature");
		}
	}
	public void processFinishDialog(int fieldId, Integer temperature)
	{
		if (fieldId == R.id.tempNeverExceed)	Global.eRegConfiguration.boiler.tempNeverExceed 	= temperature;
		if (fieldId == R.id.tempOverShoot)		Global.eRegConfiguration.boiler.tempOverShoot 		= temperature;
    	displayHeader();
		displayContents();
        setListens();
	}
	public void displayHeader()
	{
	}
	public void displayContents()
	{
		((TextView) panelView.findViewById(R.id.thermoName)).setText					(Global.eRegConfiguration.boiler.thermometer);
		((TextView) panelView.findViewById(R.id.tempNeverExceed)).setText				(Global.displayTemperature(Global.eRegConfiguration.boiler.tempNeverExceed));
		((TextView) panelView.findViewById(R.id.tempOverShoot)).setText					(Global.displayTemperature(Global.eRegConfiguration.boiler.tempOverShoot));

	}
	public void setListens()
	{
		((TextView) panelView.findViewById(R.id.tempNeverExceed)).setOnClickListener	(this);
		((TextView) panelView.findViewById(R.id.tempOverShoot)).setOnClickListener		(this);
	}
}

