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

@SuppressLint("ValidFragment")
public class Panel_5_Configuration_Boiler 						extends 					Panel_0_Fragment
{		
	public TCP_Task												task;
	
	public Panel_5_Configuration_Boiler()
	{
		super();
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	View													panelView					= inflater.inflate(R.layout.panel_5_configuration_boiler, container, false);
    	return panelView;
    }
	public void displayHeader()
	{
	}
	public void displayContents()
	{
		if (getActivity() != null)			// The user has not changed the screen
		{
			((TextView) getActivity().findViewById(R.id.ThermoName)).setText		(Global.eRegConfiguration.boiler.thermometer);
			((TextView) getActivity().findViewById(R.id.TempNeverExceed)).setText	(Global.displayTemperature(Global.eRegConfiguration.boiler.tempNeverExceed));
			((TextView) getActivity().findViewById(R.id.TempOvershoot)).setText		(Global.displayTemperature(Global.eRegConfiguration.boiler.tempOverShoot));

			((TextView) getActivity().findViewById(R.id.TempNeverExceed)).setOnClickListener(this);
			((TextView) getActivity().findViewById(R.id.TempOvershoot)).setOnClickListener(this);
		}
	}
	public void onClick(View view)
	{
		if (view.getId() == R.id.TempNeverExceed)
		{
			Integer											writeBack					= Global.eRegConfiguration.boiler.tempNeverExceed;
			Dialog_Temperature_New							df 							= new Dialog_Temperature_New(this, writeBack, 85, 1, 20);
			df.show(getFragmentManager(), "Dialog_Temperature");
		}
		else if (view.getId() == R.id.TempNeverExceed)
		{
			Integer											writeBack					= Global.eRegConfiguration.boiler.tempNeverExceed;
			Dialog_Temperature_New 							df 							= new Dialog_Temperature_New(this, writeBack, 10, 1, 15);
			df.show(getFragmentManager(), "Dialog_Temperature");
		}
	}
	public void processFinishDialog()
	{
		displayContents();
	}
}

