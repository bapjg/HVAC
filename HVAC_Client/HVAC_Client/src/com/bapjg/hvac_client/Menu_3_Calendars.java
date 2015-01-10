package com.bapjg.hvac_client;

import HVAC_Common.*;
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
public class Menu_3_Calendars 									extends 					Menu_0_Fragment 
																implements 					Dialog_Response
{
	Element_MenuButton											buttonHotWater;
	Element_MenuButton											buttonRadiator;
	Element_MenuButton											buttonFloor;
	Element_MenuButton											buttonVocabulary;
	Element_MenuButton											buttonAwayList;
	Element_MenuButton											buttonBackgroudTasks;
	
	
	public Menu_3_Calendars()
	{
		super(true);		// true means we want to keep the buttons visble at bottom
	}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.onCreateView(inflater, container, savedInstanceState);
    	
    	buttonHotWater																		= new Element_MenuButton("Hot Water");
    	buttonRadiator																		= new Element_MenuButton("Radiator");
    	buttonFloor																			= new Element_MenuButton("Floor");
    	buttonVocabulary																	= new Element_MenuButton("Vocabulary");
    	buttonAwayList																		= new Element_MenuButton("Away List");
    	buttonBackgroudTasks																= new Element_MenuButton("Backgroud Tasks");

    	menuInsertPoint.addView(buttonHotWater);
    	menuInsertPoint.addView(buttonRadiator);
    	menuInsertPoint.addView(buttonFloor);
    	menuInsertPoint.addView(buttonVocabulary);
    	menuInsertPoint.addView(buttonAwayList);
    	menuInsertPoint.addView(buttonBackgroudTasks);
    	
    	buttonHotWater			.setListener(this);
    	buttonRadiator			.setListener(this);
    	buttonFloor				.setListener(this);
    	buttonVocabulary		.setListener(this);
    	buttonAwayList			.setListener(this);
    	buttonBackgroudTasks	.setListener(this);

    	onElementClick(buttonHotWater);
    	
    	return menuView;
    }	
    @Override
    public void onElementClick(View clickedView)
	{
		super.onElementClick(clickedView);
		
    	FragmentTransaction										fTransaction				= getFragmentManager().beginTransaction();
    	Fragment 												panelFragment				= null;
   	
    	if      (clickedView == buttonVocabulary)				panelFragment 				= new Panel_3_Calendars_Vocabulary();
    	else if (clickedView == buttonHotWater) 				panelFragment 				= new Panel_3_Calendars_Circuits("Hot_Water");
     	else if (clickedView == buttonRadiator)					panelFragment 				= new Panel_3_Calendars_Circuits("Radiator");
    	else if (clickedView == buttonFloor)					panelFragment 				= new Panel_3_Calendars_Circuits("Floor");
    	else if (clickedView == buttonAwayList)					panelFragment 				= new Panel_3_Calendars_Away();
    	else if (clickedView == buttonBackgroudTasks)			panelFragment 				= new Panel_3_Calendars_Background_Tasks();

    	if (panelFragment != null)
    	{
    		fTransaction.replace(R.id.panel_container, panelFragment);
    	}
    	fTransaction.commit();  
	}
    @Override
	public void doRefresh()
	{
		HTTP_Send	(new Ctrl_Json().new Request(Ctrl_Json.TYPE_Calendar));				// Fire these async actions as soon as possible
	}
    @Override
	public void doUpdate()
	{
		Dialog_Yes_No												messageYesNo			= new Dialog_Yes_No("Are you certain ?", this);
		messageYesNo.show(getFragmentManager(), "Dialog_Yes_No");
	}
	public void onDialogReturn()
	{
		if (Global.eRegCalendars != null)
		{
            Gson gson 																		= new GsonBuilder().setPrettyPrinting().create();
			Ctrl_Json.Update										sendUpdate				= new Ctrl_Json().new Update();
			sendUpdate.type																	= Ctrl_Json.TYPE_Calendar;
            sendUpdate.json 																= gson.toJson((Ctrl_Calendars.Data) Global.eRegCalendars);

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
			Global.eRegCalendars															= new Gson().fromJson(JsonString, Ctrl_Calendars.Data.class);
			Global.toaster("Configuration data received", false);
			clickActiveButton();
		}
		else if (messageReturned instanceof Ctrl__Abstract.Ack)
		{
			Global.toaster("Server updated", false);
			Dialog_Yes_No											messageYesNo			= new Dialog_Yes_No("Update controller with new calendar now ?", this, 1);	// id = 1
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
