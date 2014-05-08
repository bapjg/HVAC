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
public class Panel_5_Configuration_Burner 						extends 					Panel_0_Fragment 
{		
	public TCP_Task										task;
	
	public Panel_5_Configuration_Burner()
	{
		super();
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	View											panelView					= inflater.inflate(R.layout.panel_5_configuration_burner, container, false);
    	
        return panelView;
    }
	public void displayHeader()
	{
	}
	public void displayContents(Ctrl_Temperatures.Data msg_received)
	{
		if (getActivity() != null)			// The user has not changed the screen
		{
			((TextView) getActivity().findViewById(R.id.Name)).setText			(Global.eRegConfiguration.burner.relay);
			((TextView) getActivity().findViewById(R.id.Fuel)).setText			(Global.eRegConfiguration.burner.fuelConsumption.toString());
			((TextView) getActivity().findViewById(R.id.TempMax)).setText		("xxx"); //(Global.eRegConfiguration.burner.relay);
			((TextView) getActivity().findViewById(R.id.TempOvershoot)).setText	("xxx"); //(Global.eRegConfiguration.burner.relay);
		}
	}
}

