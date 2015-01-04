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
    	
    	relayName																			= new Element_Standard(getActivity(), "Relay Name");
    	fuelConsumption																		= new Element_Standard(getActivity(), "Fuel Consumed", "min");
    	minutesPerLitre																		= new Element_Standard(getActivity(), "Minutes per Litre");
    	fuelConsumptionLitres																= new Element_Standard(getActivity(), "Fuel Consumed", "l");

    	panelInsertPoint.addView(new Element_Heading(getActivity(), "Parameters"));
    	panelInsertPoint.addView(relayName);
    	panelInsertPoint.addView(fuelConsumption);
    	panelInsertPoint.addView(minutesPerLitre);
    	panelInsertPoint.addView(fuelConsumptionLitres);

        if ((Global.eRegConfiguration != null)
        &&  (Global.eRegConfiguration.boiler != null))
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

		relayName				.setTextRight(burner.relay);
		fuelConsumption			.setTextRight(burner.fuelConsumption/1000/60);
		minutesPerLitre			.setTextRight(burner.minutesPerLitre);
		
		
		if ((burner.minutesPerLitre != null) && (burner.minutesPerLitre != 0))
		{
			Long 												litresConsumed 				= burner.fuelConsumption/burner.minutesPerLitre;
			fuelConsumptionLitres	.setTextRight(litresConsumed/1000/60);
		}
		else
		{
			fuelConsumptionLitres	.setTextRight(0);
		}
//
//		
//		
//		
//		
//		
//		
//		if (getActivity() != null)			// The user has not changed the screen
//		{
//			Integer												fuelMinutes					= 0;
//			Integer												fuelLitres					= 0;
//			String												fuelMinutesString			= "nought";
//			String												fuelLitresString			= "nought";
//			
//			
//			((TextView) panelView.findViewById(R.id.name)).setText							(burner.relay);
//			DecimalFormatSymbols 								decimalFormatsymbol			= DecimalFormatSymbols.getInstance();
//			decimalFormatsymbol.setGroupingSeparator(' ');
//			DecimalFormat 										decimalFormat 				= new DecimalFormat("###,###", decimalFormatsymbol);
//			// Present the fuel consumption in minutes
//			if (burner.fuelConsumption != null)
//			{
//				fuelMinutes																	= (int) (burner.fuelConsumption/1000/60);
//				fuelMinutesString															= decimalFormat.format(fuelMinutes);
//				fuelLitres																	= fuelMinutes/burner.minutesPerLitre;
//				fuelLitresString															= decimalFormat.format(fuelLitres);
//			}
//			else
//			{
//				fuelMinutesString															= "nought received";
//			}
//			((TextView) panelView.findViewById(R.id.fuelConsumption)).setText				(fuelMinutesString);
//			((TextView) panelView.findViewById(R.id.minutesPerLitre)).setText				(burner.minutesPerLitre.toString());
//			((TextView) panelView.findViewById(R.id.fuelConsumptionLitres)).setText			(fuelLitresString);
//		}
	}
	public void setListens()
	{
		relayName				.setOnClickListener(this);
		fuelConsumption			.setOnClickListener(this);
		minutesPerLitre			.setOnClickListener(this);
	}
	@Override
	public void onClick(View clickedView) 
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
			Dialog_Yes_No												messageYesNo		= new Dialog_Yes_No("Reset Fuel Consumption to 0 ?", this, 99);		// Id = 99
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
    	}
    	displayContents();
    }
}

