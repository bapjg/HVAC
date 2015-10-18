package com.bapjg.hvac_client;

import HVAC_Common.*;
import HVAC_Common.Ctrl_Actions_Stop.Execute;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")

public class Menu_5_Configuration 								extends 					Menu_0_Fragment 
																implements 					Dialog_Response
{
	Element_MenuButton											buttonThermometers;
	Element_MenuButton											buttonRelays;
	Element_MenuButton											buttonPumps;
	Element_MenuButton											buttonCircuits;
	Element_MenuButton											buttonBurner;
	Element_MenuButton											buttonBoiler;
	Element_MenuButton											buttonPIDs;
	
	public Menu_5_Configuration()
	{
		super(true, true);
	}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.onCreateView(inflater, container, savedInstanceState);
    	
    	buttonThermometers																	= new Element_MenuButton("Thermometers");
    	buttonRelays																		= new Element_MenuButton("Relays");
    	buttonPumps																			= new Element_MenuButton("Pumps");
    	buttonCircuits																		= new Element_MenuButton("Circuits");
    	buttonBurner																		= new Element_MenuButton("Burner");
    	buttonBoiler																		= new Element_MenuButton("Boiler");
    	buttonPIDs																			= new Element_MenuButton("PIDs");

    	menuInsertPoint			.addView(buttonThermometers);
    	menuInsertPoint			.addView(buttonRelays);
    	menuInsertPoint			.addView(buttonPumps);
    	menuInsertPoint			.addView(buttonCircuits);
    	menuInsertPoint			.addView(buttonBurner);
    	menuInsertPoint			.addView(buttonBoiler);
    	menuInsertPoint			.addView(buttonPIDs);
    	
    	buttonThermometers		.setListener(this);
    	buttonRelays			.setListener(this);
    	buttonPumps				.setListener(this);
    	buttonCircuits			.setListener(this);
    	buttonBurner			.setListener(this);
    	buttonBoiler			.setListener(this);
    	buttonPIDs				.setListener(this);

    	onElementClick(buttonThermometers);
    	
    	return menuView;
    }
    public void onElementClick(View clickedView)
	{
		super.onElementClick(clickedView);
		
    	FragmentTransaction										fTransaction				= getFragmentManager().beginTransaction();
    	Fragment 												panelFragment				= null;
   	
    	if      (clickedView == buttonThermometers)				panelFragment 				= new Panel_5_Configuration_Thermometers();
    	else if (clickedView == buttonRelays) 					panelFragment 				= new Panel_5_Configuration_Relays();
     	else if (clickedView == buttonPumps)					panelFragment 				= new Panel_5_Configuration_Pumps();
    	else if (clickedView == buttonCircuits)					panelFragment 				= new Panel_5_Configuration_Circuits();
    	else if (clickedView == buttonBurner)					panelFragment 				= new Panel_5_Configuration_Burner();
    	else if (clickedView == buttonBoiler)					panelFragment 				= new Panel_5_Configuration_Boiler();
    	else if (clickedView == buttonPIDs)						panelFragment 				= new Panel_5_Configuration_PIDs();

    	if 		(panelFragment != null)
    	{
    		fTransaction.replace(R.id.panel_container, panelFragment);
    	}
    	fTransaction.commit();  
	}	
	public void doRefresh()
	{
		HTTP_Send	(new Ctrl_Json().new Request(Ctrl_Json.TYPE_Configuration));				// Fire these async actions as soon as possible
	}
	public void doUpdate()
	{
		Dialog_Yes_No												messageYesNo			= new Dialog_Yes_No("Are you certain ?", this);
		messageYesNo.show(getFragmentManager(), "Dialog_Yes_No");
	}
	public void onDialogReturn()
	{
		if (Global.eRegConfiguration != null)
		{
            Gson gson 																		= new GsonBuilder().setPrettyPrinting().create();
			Ctrl_Json.Update										sendUpdate				= new Ctrl_Json().new Update();
			sendUpdate.type																	= Ctrl_Json.TYPE_Configuration;
            sendUpdate.json 																= gson.toJson((Ctrl_Configuration.Data) Global.eRegConfiguration);

			HTTP_Send	(sendUpdate);
		}
		else
		{
			Global.toaster("No data to send, do a Refresh", false);
		}
	}
	public void onDialogReturnWithId(int id)
	{
		if (id == 1)
		{
	    	Ctrl_Actions_Stop.Execute 								stopMessage				= new Ctrl_Actions_Stop().new Execute();
	    	stopMessage.actionRequest														= Ctrl_Actions_Stop.ACTION_Restart;
	    	TCP_Send	(stopMessage);
		}
	}
	public void processFinishHTTP(Ctrl__Abstract messageReturned)
	{
		super.processFinishHTTP(messageReturned);
		if (messageReturned instanceof Ctrl_Json.Data)
		{
			String													JsonString				= ((Ctrl_Json.Data) messageReturned).json;
			Global.eRegConfiguration														= new Gson().fromJson(JsonString, Ctrl_Configuration.Data.class);
			
			HTTP_Send(new Ctrl_Fuel_Consumption().new Request());
		}
		else if (messageReturned instanceof Ctrl_Fuel_Consumption.Data)
		{
			Ctrl_Fuel_Consumption.Data								fuel					= ((Ctrl_Fuel_Consumption.Data) messageReturned);
			Global.eRegConfiguration.burner.fuelConsumption									= fuel.fuelConsumed;
			Global.toaster("Configuration & Fuel data received", false);
			clickActiveButton();
		}
		else if (messageReturned instanceof Ctrl__Abstract.Ack)
		{
			Global.toaster("Server updated", false);
			Dialog_Yes_No											messageYesNo			= new Dialog_Yes_No("Update controller with new configuration now ?", this, 1);	// id = 1
			messageYesNo.show(getFragmentManager(), "Dialog_Yes_No");
		}
		else 
		{
			Global.toaster("Unexpected response : " + messageReturned.getClass().toString(), false);
		}
	}
	public void processFinishTCP(Ctrl__Abstract messageReturn)
	{
		super.processFinishTCP(messageReturn);
		if (messageReturn instanceof Ctrl_Actions_Stop.Ack)
		{
			Global.toaster("Controler accepted the request", false);
		}
		else if (messageReturn instanceof Ctrl_Actions_Stop.Nack)
		{
			Global.toaster("Controler refused the request", true);
		}
		else
		{
			Global.toaster("Unexpected response : " + messageReturn.getClass().toString(), false);
		}
	}
}
