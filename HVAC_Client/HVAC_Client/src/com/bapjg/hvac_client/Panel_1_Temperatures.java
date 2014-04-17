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
public class Panel_1_Temperatures 			extends 			Panel_0_Fragment 
											implements 			TCP_Response
{
	public TCP_Task							task;
	
	public Panel_1_Temperatures()
	{
		super();
	}
	
	public Panel_1_Temperatures(int menuLayout)
    {
		super(menuLayout);
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
		this.activity											= getActivity();
    	
    	View								thisView			= inflater.inflate(R.layout.panel_1_temperatures, container, false);
    	
    	TCP_Send(new Ctrl_Temperatures().new Request());
    	
        return thisView;
    }
	public void TCP_Send(Ctrl_Abstract message)
	{
		TCP_Task							task				= new TCP_Task();
	   	task.callBack											= this;					// processFinish
	   	task.execute(message);
	}
	public void processFinish(Ctrl_Abstract result) 
	{             
		Activity							activity					= getActivity();		

		if (result instanceof Ctrl_Temperatures.Data)
		{
			Ctrl_Temperatures.Data msg_received 				= (Ctrl_Temperatures.Data) result;
			
			((TextView) a.findViewById(R.id.Date)).setText(Global.displayDate(msg_received.dateTime));
			((TextView) a.findViewById(R.id.Time)).setText(Global.displayTime(msg_received.dateTime));
			
			((TextView) a.findViewById(R.id.Boiler)).setText(Global.displayTemperature(msg_received.tempBoiler));
			((TextView) a.findViewById(R.id.HotWater)).setText(Global.displayTemperature(msg_received.tempHotWater));
			((TextView) a.findViewById(R.id.Outside)).setText(Global.displayTemperature(msg_received.tempOutside));
			((TextView) a.findViewById(R.id.BoilerIn)).setText(Global.displayTemperature(msg_received.tempBoilerIn));
			((TextView) a.findViewById(R.id.BoilerOut)).setText(Global.displayTemperature(msg_received.tempBoilerOut));
			((TextView) a.findViewById(R.id.FloorIn)).setText(Global.displayTemperature(msg_received.tempFloorIn));
			((TextView) a.findViewById(R.id.FloorOut)).setText(Global.displayTemperature(msg_received.tempFloorOut));
			((TextView) a.findViewById(R.id.RadiatorIn)).setText(Global.displayTemperature(msg_received.tempRadiatorIn));
			((TextView) a.findViewById(R.id.RadiatorOut)).setText(Global.displayTemperature(msg_received.tempRadiatorOut));
			((TextView) a.findViewById(R.id.LivingRoom)).setText(Global.displayTemperature(msg_received.tempLivingRoom));
		}
		else if (result instanceof Ctrl_Temperatures.NoConnection)
		{
			Global.toast("No Connection established yet", false);
		}
		else
		{
			Global.toast("A Nack has been returned", false);
		}
    } 
}

