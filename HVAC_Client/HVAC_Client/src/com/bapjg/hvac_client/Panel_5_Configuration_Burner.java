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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import HVAC_Common.*;
import HVAC_Common.Ctrl_Actions_Relays.Execute;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Panel_5_Configuration_Burner 						extends 					Panel_0_Fragment 
{		
	public TCP_Task												task;
	private Element_Standard									relayName;
	private Element_Standard									fuelConsumption;
	private Element_Standard									minutesPerLitre;
	private Element_Standard									fuelConsumptionLitres;

	public Panel_5_Configuration_Burner()
	{
		super("Standard");
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);
    	displayTitles("Configuration", "Burner");
    	
    	relayName																			= new Element_Standard("Relay Name");
    	fuelConsumption																		= new Element_Standard("Fuel Consumed", "min");
    	minutesPerLitre																		= new Element_Standard("Minutes per Litre");
    	fuelConsumptionLitres																= new Element_Standard("Fuel Consumed", "l");

    	panelInsertPoint.addView(new Element_Heading( "Parameters"));
    	panelInsertPoint.addView(relayName);
    	panelInsertPoint.addView(fuelConsumption);
    	panelInsertPoint.addView(minutesPerLitre);
    	panelInsertPoint.addView(fuelConsumptionLitres);

        if (	(Global.eRegConfiguration 			!= null)
        &&  	(Global.eRegConfiguration.boiler 	!= null)	)
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
	public void displayContents()
	{
		Ctrl_Configuration.Burner							burner						= Global.eRegConfiguration.burner;

		relayName				.setValue(burner.relay);
		fuelConsumption			.setValue(burner.fuelConsumption/1000/60);
		minutesPerLitre			.setValue(burner.minutesPerLitre);
		
		if ((burner.minutesPerLitre != null) && (burner.minutesPerLitre != 0))
		{
			Long 												litresConsumed 				= burner.fuelConsumption/burner.minutesPerLitre;
			fuelConsumptionLitres	.setValue(litresConsumed/1000/60);
		}
		else
		{
			fuelConsumptionLitres	.setValue(0);
		}
	}
	public void setListens()
	{
		relayName				.setListener(this);
		fuelConsumption			.setListener(this);
		minutesPerLitre			.setListener(this);
	}
	@Override
	public void onElementClick(View clickedView) 
	{
		if (clickedView == relayName)
		{
			Ctrl_Configuration.Burner							burner						= Global.eRegConfiguration.burner;

			Dialog_String_List		 							dialog						= new Dialog_String_List(burner.relay, (Object) burner, null, this);

			for (Ctrl_Configuration.Relay relay : Global.eRegConfiguration.relayList)
			{
				dialog.items.add(relay.name);
			}
			dialog.show(getFragmentManager(), "Relay_List");
		}
		else if (clickedView == minutesPerLitre)
		{
			Ctrl_Configuration.Burner							burner						= Global.eRegConfiguration.burner;

			Dialog_Integer			 							dialog						= new Dialog_Integer(burner.minutesPerLitre, (Object) burner, 1, 20, "Minutes per Litre of fuel consummed", this);

			dialog.show(getFragmentManager(), "Minutes_Per_Litre");
		}
		else if (clickedView == fuelConsumption)
		{
			Dialog_Yes_No										messageYesNo				= new Dialog_Yes_No("Reset Fuel Consumption to 0 ?", this, 99);		// Id = 99
			messageYesNo.show(getFragmentManager(), "Dialog_Yes_No");
		}
		else if (clickedView == fuelConsumptionLitres)
		{
		}
	}
    public void onDialogReturn()
    {
    	displayContents();
    }
    public void onDialogReturnWithId(int id)
    {
    	if (id == 99)
    	{
    		Global.eRegConfiguration.burner.fuelConsumption									= 0L;
    		Ctrl_Fuel_Consumption.Update				messageSend							= new Ctrl_Fuel_Consumption().new Update();
    		messageSend.dateTime															= Global.now();
    		messageSend.fuelConsumed														= 0L;

    		TCP_Send(messageSend);
    	}
    	displayContents();
    }
	public void processFinishTCP(Ctrl__Abstract result) 
	{  
		super.processFinishTCP(result);
		
		if (result instanceof Ctrl_Fuel_Consumption.Ack)		
		{		
			Global.toaster("Update successful", true);
		}
		else if (result instanceof Ctrl_Fuel_Consumption.Nack)		
		{		
    		Ctrl_Fuel_Consumption.Nack					messageNack							= (Ctrl_Fuel_Consumption.Nack) result;
			Global.toaster(messageNack.errorMessage, true);
		}
		else
		{
			Global.toaster("Unexpected response", true);
		}
	}
}

