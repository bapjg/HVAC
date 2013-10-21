package com.bapjg.hvac_client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import com.bapjg.hvac_client.Mgmt_Msg_Temperatures.Request;


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

public class Panel_1_Temperatures extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	// Inflate the layout for this fragment
		HTTP_Req_Temp							httpRequest			= new HTTP_Req_Temp();

		httpRequest.execute(new Mgmt_Msg_Temperatures().new Request());
		
        return inflater.inflate(R.layout.panel_1_temperatures, container, false);
    }
	private class HTTP_Req_Temp extends AsyncTask <Mgmt_Msg_Abstract, Void, Mgmt_Msg_Abstract> 
	{
		public HTTP_Request				http;

		public HTTP_Req_Temp()
		{
			http													= new HTTP_Request();
		}
		
		@Override
		protected Mgmt_Msg_Abstract doInBackground(Mgmt_Msg_Abstract... messageOut) 
		{
			if (Global.serverURL == "")
			{
				return new Mgmt_Msg_Abstract().new NoConnection();
			}
			else
			{
				return http.sendData(messageOut[0]);
			}
		}	
		@Override
		protected void onProgressUpdate(Void... progress)  { }
		@Override
	    protected void onPostExecute(Mgmt_Msg_Abstract result) 
		{             
			if (result instanceof Mgmt_Msg_Temperatures.Data)
			{
				Mgmt_Msg_Temperatures.Data msg_received 	= (Mgmt_Msg_Temperatures.Data) result;
				Activity a							= getActivity();

				// Need to change this to avoid null pointer exception
				// Probably due to (Activity) a not being current any more (clicking too fast)
				
				((TextView) a.findViewById(R.id.Date)).setText(displayDate(msg_received.date));
				((TextView) a.findViewById(R.id.Time)).setText(displayTime(msg_received.time));
				
				((TextView) a.findViewById(R.id.Boiler)).setText(displayTemperature(msg_received.tempBoiler));
				((TextView) a.findViewById(R.id.HotWater)).setText(displayTemperature(msg_received.tempHotWater));
				((TextView) a.findViewById(R.id.Outside)).setText(displayTemperature(msg_received.tempOutside));
				((TextView) a.findViewById(R.id.BoilerIn)).setText(displayTemperature(msg_received.tempBoilerIn));
				((TextView) a.findViewById(R.id.FloorOut)).setText(displayTemperature(msg_received.tempFloorOut));
				((TextView) a.findViewById(R.id.FloorHot)).setText(displayTemperature(msg_received.tempFloorHot));
				((TextView) a.findViewById(R.id.FloorCold)).setText(displayTemperature(msg_received.tempFloorCold));
				((TextView) a.findViewById(R.id.RadiatorOut)).setText(displayTemperature(msg_received.tempRadiatorOut));
				((TextView) a.findViewById(R.id.RadiatorIn)).setText(displayTemperature(msg_received.tempRadiatorIn));
				((TextView) a.findViewById(R.id.LivingRoom)).setText(displayTemperature(msg_received.tempLivingRoom));
			}
			else if (result instanceof Mgmt_Msg_Temperatures.NoConnection)
			{
				Toast.makeText(getActivity(), "No Connection established here", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(getActivity(), "A Nack has been returned", Toast.LENGTH_SHORT).show();
			}
	    }
	}
	private String displayTemperature(Integer temperature)
	{
		int degrees = temperature/10;
		int decimal = temperature - degrees*10;
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
	}
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
    		// buttonThermometersClick(myView);	
    		if (Global.serverURL.equalsIgnoreCase(""))
    		{
    			System.out.println("1 Server connection not established");
    			Toast.makeText(Global.appContext, "45 Server connexion not yet established", Toast.LENGTH_LONG).show();
    		}
    		else
    		{
    			System.out.println("2 connection established");
    			HTTP_Req_Temp						httpRequest			= new HTTP_Req_Temp();
    			httpRequest.execute(new Mgmt_Msg_Temperatures().new Request());
    		}
    	}
	}    
}

