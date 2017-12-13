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
public class Panel_7_Reset_Fuel 								extends 					Panel_0_Fragment 
{		
	public  TCP_Task											task;
	private Element_Standard									fuelConsumptionMinutes;
	private Element_Standard									minutesPerLitre;
	private Element_Standard									fuelConsumptionLitres;
	private Element_Standard									fuelDeliveredLitres;
	private Element_Standard									minutesPerLitreCalculated;
	private Element_Button										buttonDeliveryReset;
	private Element_Button										buttonConfirm;
	
	private Dialog_Integer			 							dialogLitres;
	private Dialog_Object										dialogObject;

	public Panel_7_Reset_Fuel()
	{
		super("Standard");
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);
    	displayTitles("Reset", "Fuel");
    	
    	fuelConsumptionMinutes																= new Element_Standard("Fuel Consumed", "min");
    	minutesPerLitre																		= new Element_Standard("Minutes per Litre");
    	fuelConsumptionLitres																= new Element_Standard("Fuel Consumed", "l");

    	panelInsertPoint.addView(new Element_Heading( "Current Values"));
    	panelInsertPoint.addView	(fuelConsumptionMinutes);
    	panelInsertPoint.addView	(minutesPerLitre);
    	panelInsertPoint.addView	(fuelConsumptionLitres);

    	buttonDeliveryReset																	= new Element_Button("Delivery Reset");
    	buttonConfirm																		= new Element_Button("Confirm");

    	if (	(Global.eRegConfiguration 			!= null)
        &&  	(Global.eRegConfiguration.boiler 	!= null)	)
        {
        	panelInsertPoint.addView(buttonDeliveryReset);
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
		Ctrl_Configuration.Burner								burner						= Global.eRegConfiguration.burner;

		fuelConsumptionMinutes	.setValue(burner.fuelConsumption/1000/60);					// From milliseconds to minutes
		minutesPerLitre			.setValue(burner.minutesPerLitre);
		
		if ((burner.minutesPerLitre != null) && (burner.minutesPerLitre != 0))
		{
			float 												litresConsumedFloat			= burner.fuelConsumption/burner.minutesPerLitre;
			Long 												litresConsumedLong			= (long) litresConsumedFloat;
			fuelConsumptionLitres	.setValue(litresConsumedLong/1000/60);
		}
		else
		{
			fuelConsumptionLitres	.setValue(0);
		}
	}
	public void setListens()
	{
		buttonDeliveryReset		.setListener(this);
		buttonConfirm			.setListener(this);
	}
	@Override
	public void onElementClick(View clickedView) 
	{
		if (clickedView == buttonDeliveryReset)
		{
			Ctrl_Configuration.Burner							burner						= Global.eRegConfiguration.burner;
			
			dialogObject																	= new Dialog_Object();
			dialogObject.valueInteger														= 0;
			if ((burner.minutesPerLitre != null) && (burner.minutesPerLitre != 0))
			{
				float 											litresConsumedFloat			= burner.fuelConsumption/burner.minutesPerLitre/1000/60;
				dialogObject.valueInteger													= (int) litresConsumedFloat;
			}
			
			dialogLitres																	= new Dialog_Integer(dialogObject.valueInteger, dialogObject, "Litres of fuel delivered", this);
			dialogLitres.show(getFragmentManager(), "Litres_Delivered");
		}
		else if (clickedView == buttonConfirm)
		{
    		Global.eRegConfiguration.burner.fuelConsumption									= 0L;
    		Ctrl_Fuel_Consumption.Update				messageSend							= new Ctrl_Fuel_Consumption().new Update();
    		messageSend.dateTime															= Global.now();
    		messageSend.fuelConsumed														= 0L;

    		TCP_Send(messageSend);
		}
//		else if (clickedView == fuelConsumptionMinutes)
//		{
//			Dialog_Yes_No										messageYesNo				= new Dialog_Yes_No("Reset Fuel Consumption to 0 ?", this, 99);		// Id = 99
//			messageYesNo.show(getFragmentManager(), "Dialog_Yes_No");
//		}
//		else if (clickedView == fuelConsumptionLitres)
//		{
//		}
	}
    public void onDialogReturn()
    {
//    	this.container.removeView(buttonDeliveryReset);
    	
    	buttonDeliveryReset.setVisibility(TRIM_MEMORY_UI_HIDDEN);
 
    	fuelDeliveredLitres																	= new Element_Standard("Fuel Delivered", "l");
    	minutesPerLitreCalculated															= new Element_Standard("Minutes per Litre (calc)");

    	panelInsertPoint.addView(new Element_Heading( "Reset/New Values"));
    	panelInsertPoint.addView	(fuelDeliveredLitres);
    	panelInsertPoint.addView	(minutesPerLitreCalculated);
    	panelInsertPoint.addView	(buttonConfirm);

    	fuelDeliveredLitres			.setValue(dialogObject.valueInteger);

    	Long 						burnerMinutes											= Global.eRegConfiguration.burner.fuelConsumption/1000/60;
    	Float 						minutesPerLitreCalculatedFloat 							= (float) burnerMinutes/dialogObject.valueInteger;
    	minutesPerLitreCalculatedFloat														= Math.round(minutesPerLitreCalculatedFloat * 100f)/100f;
    	
    	minutesPerLitreCalculated	.setValue(minutesPerLitreCalculatedFloat);
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
	public void processFinishTCP(Msg__Abstract result) 
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

