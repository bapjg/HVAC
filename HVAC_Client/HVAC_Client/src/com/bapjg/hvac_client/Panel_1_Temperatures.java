package com.bapjg.hvac_client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

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


public class Panel_1_Temperatures 	extends 	Fragment 
									implements 	View.OnClickListener, AdapterView.OnItemClickListener, TCP_Response
{
    public Activity						activity;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	this.activity										= getActivity();
		// Inflate the layout for this fragment
//		HTTP_Req_Temp							httpRequest			= new HTTP_Req_Temp();
		
//		pi.connect();

//		httpRequest.execute(new Mgmt_Msg_Temperatures().new Request());
		
    	TCP_Task						task				= new TCP_Task();
    	task.callBack										= this;
    	task.execute(new Ctrl_Temperatures().new Request());
    	
        return inflater.inflate(R.layout.panel_1_temperatures, container, false);
    }
    public void processFinish(Ctrl_Abstract result) 
	{             
		Activity a							= activity;
		Toast.makeText(a, "processFinish starting", Toast.LENGTH_SHORT).show();
		if (result instanceof Ctrl_Temperatures.Response)
		{
			Ctrl_Temperatures.Response msg_received 	= (Ctrl_Temperatures.Response) result;
			// Need to change this to avoid null pointer exception
			// Probably due to (Activity) a not being current any more (clicking too fast)
			
//			((TextView) a.findViewById(R.id.Date)).setText(displayDate(msg_received.date));
//			((TextView) a.findViewById(R.id.Time)).setText(displayTime(msg_received.time));
			
			((TextView) a.findViewById(R.id.Boiler)).setText(displayTemperature(msg_received.tempBoiler));
			((TextView) a.findViewById(R.id.HotWater)).setText(displayTemperature(msg_received.tempHotWater));
			((TextView) a.findViewById(R.id.Outside)).setText(displayTemperature(msg_received.tempOutside));
			((TextView) a.findViewById(R.id.BoilerIn)).setText(displayTemperature(msg_received.tempBoilerIn));
			((TextView) a.findViewById(R.id.BoilerOut)).setText(displayTemperature(msg_received.tempBoilerOut));
			((TextView) a.findViewById(R.id.FloorIn)).setText(displayTemperature(msg_received.tempFloorIn));
			((TextView) a.findViewById(R.id.FloorOut)).setText(displayTemperature(msg_received.tempFloorOut));
			((TextView) a.findViewById(R.id.RadiatorIn)).setText(displayTemperature(msg_received.tempRadiatorIn));
			((TextView) a.findViewById(R.id.RadiatorOut)).setText(displayTemperature(msg_received.tempRadiatorOut));
			((TextView) a.findViewById(R.id.LivingRoom)).setText(displayTemperature(msg_received.tempLivingRoom));
		}
		else if (result instanceof Ctrl_Temperatures.NoConnection)
		{
			Toast.makeText(a, "No Connection established yet", Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(a, "A Nack has been returned", Toast.LENGTH_SHORT).show();
		}
    }    	


	private String displayTemperature(Integer temperature)
	{
		int degrees = temperature/1000;
		int decimal = (temperature - degrees*1000) / 100;
		return degrees + "." + decimal;
	}
	private String displayDate(String date)
	{
		return date.substring(8,10) + "/" + date.substring(5,7);
	}
	private String displayTime(String time)
	{
		return time.substring(0,8);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {}
	@Override
	public void onClick(View myView) 
	{
    	System.out.println("We have arrived in onClick again");
    	Button 								myButton 					= (Button) myView;
    	String								myCaption					= myButton.getText().toString();
    	
		// Set all textColours to white
		ViewGroup 							viewParent					= (ViewGroup) myView.getParent();
		for (int i = 0; i < viewParent.getChildCount(); i++)
		{
			Button							buttonChild 				= (Button) viewParent.getChildAt(i);
			buttonChild.setTextColor(Color.WHITE);
		}
		
		((Button) myView).setTextColor(Color.YELLOW);
    	
    	if (myCaption.equalsIgnoreCase("Temperatures"))
    	{
    		buttonTemperaturesClick(myView);	
    	}
	}
    public void buttonTemperaturesClick(View myView)
    {
//		if (!Global.initialisationCompleted)
    	Toast.makeText(Global.appContext, "P1_Temperatures : buttonTemperaturesClick called", Toast.LENGTH_LONG).show();
		if (true)
		{
    		if (! Global.piConnection.connect())
    		{
    			System.out.println("P1_Temperatures : Server connection not established");
    			Toast.makeText(Global.appContext, "P1_Temperatures : Server connexion not yet established", Toast.LENGTH_LONG).show();
    		}
    		else
    		{
    			System.out.println("P1_Temperatures : connection established");
    	    	TCP_Task				task							= new TCP_Task();
    	    	task.callBack											= this;
    	    	task.execute(new Ctrl_Temperatures().new Request());
    		}
		}

// This sets up the code to display the panel and get clicks in order to display an update screen
// All this comes from Thermometers
//        ArrayList  	<Mgmt_Msg_Configuration.Thermometer>	data		= Global.configuration.thermometerList;
//        Activity 							activity					= (Activity) Global.actContext;
//        AdapterView <Adapter_Thermometers> 	view						= (AdapterView) activity.findViewById(R.id.List_View);
//        
//        Adapter_Thermometers 				adapter						= new Adapter_Thermometers(Global.actContext, R.id.List_View, data);
//        
//        view.setAdapter(adapter);
//        view.setOnItemClickListener((OnItemClickListener) this);	
    }
    public void update()
    {
    	System.out.println("update called");
    	TCP_Task						task							= new TCP_Task();
    	task.callBack													= this;
    	task.execute(new Ctrl_Temperatures().new Request());
    }
//	private class HTTP_Req_Temp extends AsyncTask <Mgmt_Msg_Abstract, Void, Mgmt_Msg_Abstract> 
//	{
//		public HTTP_Request				http;
//
//		public HTTP_Req_Temp()
//		{
//			http													= new HTTP_Request();
//		}
//		
//		@Override
//		protected Mgmt_Msg_Abstract doInBackground(Mgmt_Msg_Abstract... messageOut) 
//		{
//			if (Global.serverURL == "")
//			{
//				return new Mgmt_Msg_Abstract().new NoConnection();
//			}
//			else
//			{
//				return http.sendData(messageOut[0]);
//			}
//		}	
//		@Override
//		protected void onProgressUpdate(Void... progress)  { }
//		@Override
//	    protected void onPostExecute(Mgmt_Msg_Abstract result) 
//		{             
//			if (result instanceof Mgmt_Msg_Temperatures.Data)
//			{
//				Mgmt_Msg_Temperatures.Data msg_received 	= (Mgmt_Msg_Temperatures.Data) result;
//				Activity a							= getActivity();
//
//				// Need to change this to avoid null pointer exception
//				// Probably due to (Activity) a not being current any more (clicking too fast)
//				
//				((TextView) a.findViewById(R.id.Date)).setText(displayDate(msg_received.date));
//				((TextView) a.findViewById(R.id.Time)).setText(displayTime(msg_received.time));
//				
//				((TextView) a.findViewById(R.id.Boiler)).setText(displayTemperature(msg_received.tempBoiler));
//				((TextView) a.findViewById(R.id.HotWater)).setText(displayTemperature(msg_received.tempHotWater));
//				((TextView) a.findViewById(R.id.Outside)).setText(displayTemperature(msg_received.tempOutside));
//				((TextView) a.findViewById(R.id.BoilerIn)).setText(displayTemperature(msg_received.tempBoilerIn));
//				((TextView) a.findViewById(R.id.BoilerOut)).setText(displayTemperature(msg_received.tempBoilerOut));
//				((TextView) a.findViewById(R.id.FloorIn)).setText(displayTemperature(msg_received.tempFloorIn));
//				((TextView) a.findViewById(R.id.FloorOut)).setText(displayTemperature(msg_received.tempFloorOut));
//				((TextView) a.findViewById(R.id.RadiatorIn)).setText(displayTemperature(msg_received.tempRadiatorIn));
//				((TextView) a.findViewById(R.id.RadiatorOut)).setText(displayTemperature(msg_received.tempRadiatorOut));
//				((TextView) a.findViewById(R.id.LivingRoom)).setText(displayTemperature(msg_received.tempLivingRoom));
//			}
//			else if (result instanceof Mgmt_Msg_Temperatures.NoConnection)
//			{
//				// Toast.makeText(getActivity(), "No Connection established yet", Toast.LENGTH_SHORT).show();
//			}
//			else
//			{
//				Toast.makeText(getActivity(), "A Nack has been returned", Toast.LENGTH_SHORT).show();
//			}
//	    }
//	}
}

