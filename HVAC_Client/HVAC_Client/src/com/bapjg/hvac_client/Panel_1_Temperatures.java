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
public class Panel_1_Temperatures 						extends 					Panel_0_Fragment 
{		
	public TCP_Task										task;
	
	public Panel_1_Temperatures()
	{
		super();
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	View											panelView					= inflater.inflate(R.layout.panel_1_temperatures, container, false);
    	
    	TCP_Send(new Ctrl_Temperatures().new Request());
        return panelView;
    }
	public void processFinishTCP(Ctrl_Abstract result) 
	{             
		if 		(result instanceof Ctrl_Temperatures.Data)					displayContents		((Ctrl_Temperatures.Data) result);
		else if (result instanceof Ctrl_Temperatures.NoConnection)			Global.toast		("No Connection established yet", false);
		else																Global.toast		("A Nack has been returned", false);
    } 
	public void displayHeader()
	{
	}
	public void displayContents(Ctrl_Temperatures.Data msg_received)
	{
		if (getActivity() != null)			// The user has not changed the screen
		{
			((TextView) getActivity().findViewById(R.id.Date)).setText			(Global.displayDate			(msg_received.dateTime));
			((TextView) getActivity().findViewById(R.id.Time)).setText			(Global.displayTime			(msg_received.dateTime));
			
			((TextView) getActivity().findViewById(R.id.Boiler)).setText		(Global.displayTemperature	(msg_received.tempBoiler));
			((TextView) getActivity().findViewById(R.id.HotWater)).setText		(Global.displayTemperature	(msg_received.tempHotWater));
			((TextView) getActivity().findViewById(R.id.Outside)).setText		(Global.displayTemperature	(msg_received.tempOutside));
			((TextView) getActivity().findViewById(R.id.BoilerIn)).setText		(Global.displayTemperature	(msg_received.tempBoilerIn));
			((TextView) getActivity().findViewById(R.id.BoilerOut)).setText		(Global.displayTemperature	(msg_received.tempBoilerOut));
			((TextView) getActivity().findViewById(R.id.FloorIn)).setText		(Global.displayTemperature	(msg_received.tempFloorIn));
			((TextView) getActivity().findViewById(R.id.FloorOut)).setText		(Global.displayTemperature	(msg_received.tempFloorOut));
			((TextView) getActivity().findViewById(R.id.RadiatorIn)).setText	(Global.displayTemperature	(msg_received.tempRadiatorIn));
			((TextView) getActivity().findViewById(R.id.RadiatorOut)).setText	(Global.displayTemperature	(msg_received.tempRadiatorOut));
			((TextView) getActivity().findViewById(R.id.LivingRoom)).setText	(Global.displayTemperature	(msg_received.tempLivingRoom));
		}
	}
}

