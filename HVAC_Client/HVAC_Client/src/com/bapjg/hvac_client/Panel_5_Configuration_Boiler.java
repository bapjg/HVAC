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
public class Panel_5_Configuration_Boiler 						extends 					Panel_0_Fragment
{		
	public TCP_Task												task;
	private Element_Standard									thermoName;
	private Element_Standard									tempNeverExceed;
	private Element_Standard									tempOverShoot;
	
	public Panel_5_Configuration_Boiler()
	{
		super("Standard");
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	displayTitles("Configuration", "Boiler");
    	
    	thermoName																			= new Element_Standard("Thermometer");
    	tempNeverExceed																		= new Element_Standard("Temp Never Exceed", "°C");
    	tempOverShoot																		= new Element_Standard("Temp Overshoot", "°C");
    	
    	panelInsertPoint.addView(new Element_Heading( "Parameters"));
    	panelInsertPoint.addView(thermoName);
    	panelInsertPoint.addView(tempNeverExceed);
    	panelInsertPoint.addView(tempOverShoot);
    	
    	if ( (Global.eRegConfiguration 			!= 	null)
        &&   (Global.eRegConfiguration.boiler 	!= 	null)	)
        {
        	displayContents();
            setListens();
        }
        else // we need to reconnect to the server
        {
        	Global.toaster("Please refresh", true);
        }

    	return panelView;
    }
	public void onElementClick(View view)
	{
		if (view == thermoName)
		{
			Dialog_String_List dialogList 													= new Dialog_String_List(Global.eRegConfiguration.boiler.thermometer, Global.eRegConfiguration.boiler, null, this);
     		dialogList.itemSelected															= Global.eRegConfiguration.boiler.thermometer;

    		for (Ctrl_Configuration.Thermometer thermometer : Global.eRegConfiguration.thermometerList)
    		{
    			dialogList.items.add(thermometer.name);
    		}
    		dialogList.show(getFragmentManager(), "Dialog_List");
		}
		else if (view == tempNeverExceed)
		{
			Cmn_Temperature										temperature					= Global.eRegConfiguration.boiler.tempNeverExceed;
			Dialog_Temperature									df 							= new Dialog_Temperature(temperature, 85, 100, this);
			df.show(getFragmentManager(), "Dialog_Temperature");
		}
		else if (view == tempOverShoot)
		{
			Cmn_Temperature										temperature					= Global.eRegConfiguration.boiler.tempOverShoot;
			Dialog_Temperature									df 							= new Dialog_Temperature(temperature, 10, 25, this);
			df.show(getFragmentManager(), "Dialog_Temperature");
		}
	}
	public void onDialogReturn()
	{
		displayContents();
        setListens();
	}
	public void displayContents()
	{
		thermoName			.setTextRight(Global.eRegConfiguration.boiler.thermometer);
		tempNeverExceed		.setTextRight(Global.eRegConfiguration.boiler.tempNeverExceed.displayInteger());
		tempOverShoot		.setTextRight(Global.eRegConfiguration.boiler.tempOverShoot.displayInteger());
	}
	public void setListens()
	{
		thermoName			.setListener	(this);
		tempNeverExceed		.setListener	(this);
		tempOverShoot		.setListener	(this);
	}
}

