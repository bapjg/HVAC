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

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Menu_3_Calendars 									extends 					Menu_0_Fragment 
																implements 					View.OnClickListener,
																							HTTP_Response,
																							TCP_Response
{
	public Menu_3_Calendars()
	{
		super();
		this.menuLayout																		= R.layout.menu_3_calendars;
	}
	public void onClick(View myView)
	{
		super.onClick(myView);
		
    	String													caption						= ((Button) myView).getText().toString();
    	FragmentTransaction										fTransaction				= getFragmentManager().beginTransaction();
    	Fragment 												panelFragment				= null;
   	
    	if      (caption.equalsIgnoreCase("Vocabulary"))		panelFragment 				= new Panel_3_Calendars_Vocabulary();
    	else if (caption.equalsIgnoreCase("Hot Water")) 		panelFragment 				= new Panel_3_Calendars_Circuits("Hot_Water");
     	else if (caption.equalsIgnoreCase("Radiator"))			panelFragment 				= new Panel_3_Calendars_Circuits("Radiator");
    	else if (caption.equalsIgnoreCase("Floor"))				panelFragment 				= new Panel_3_Calendars_Circuits("Floor");
    	else if (caption.equalsIgnoreCase("Away List"))			panelFragment 				= new Panel_3_Calendars_Away();
       	else if (caption.equalsIgnoreCase("Refresh"))			doRefresh();
    	else if (caption.equalsIgnoreCase("Update"))			doUpdate();

    	if 		(panelFragment != null)
    	{
    		fTransaction.replace(R.id.panel_container, panelFragment);
    	}
    	fTransaction.commit();  
	}
	public void doRefresh()
	{
        HTTP_Send	(new Ctrl_Calendars().new Request());				// Fire these async actions as soon as possible
		Global.toaster("doRefresh", false);
	}
	public void doUpdate()
	{
		Ctrl_Calendars.Data											sendData				= Global.eRegCalendars;
		Ctrl_Calendars.Update										sendUpdate				= new Ctrl_Calendars().new Update();
		sendUpdate.wordList																	= sendData.wordList;
		sendUpdate.circuitList																= sendData.circuitList;
		sendUpdate.awayList																	= sendData.awayList;
		sendUpdate.tasksBackGround															= sendData.tasksBackGround;
		
		HTTP_Send	(sendUpdate);
	}
	public void processFinishHTTP(Ctrl__Abstract messageReturn)
	{
		if (messageReturn instanceof Ctrl__Abstract.Ack)
		{
			Global.toaster("Server updated", false);
	    	Ctrl_Actions_Stop.Execute 								stopMessage				= new Ctrl_Actions_Stop().new Execute();
// TODO	    	stopMessage.actionRequest													= Ctrl_Actions_Stop.ACTION_Reload_Calendars;
	    	stopMessage.actionRequest														= Ctrl_Actions_Stop.ACTION_Restart;
//	    	TCP_Send	(stopMessage);
		}
		else if (messageReturn instanceof Ctrl_Calendars.Data)
		{
			Global.eRegCalendars															= (Ctrl_Calendars.Data) messageReturn;
		}
	}
	public void processFinishTCP(Ctrl__Abstract messageReturn)
	{
		if (messageReturn instanceof Ctrl_Actions_Stop.Ack)
		{
			Global.toaster("Controler accepted the request", false);
		}
		else
		{
			Global.toaster("Controler refused (Nack)", false);
		}
	}

}
