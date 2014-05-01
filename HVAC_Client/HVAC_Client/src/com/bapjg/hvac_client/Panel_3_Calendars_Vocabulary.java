package com.bapjg.hvac_client;

import HVAC_Messages.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;


@SuppressLint("ValidFragment")
//Template										variable			= something
//Template										ext/imp				class
public class Panel_3_Calendars_Vocabulary 		extends 			Panel_0_Fragment
												implements 			HTTP_Response
{
//	private Adapter_Words		 				adapter;
	private LayoutInflater						myInflater;
	private Activity							myActivity;
	private ViewGroup							myContainer;
	private View								myAdapterView;
	private FragmentManager						myFragmentManager;
	
	public Panel_3_Calendars_Vocabulary()
	{
		super();
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        // Inflate the layout for this fragment
        myInflater													= inflater;
        myContainer 												= container;
        myActivity													= getActivity();
        myFragmentManager 											= myActivity.getFragmentManager();
        View 									panelView			= myInflater.inflate(R.layout.panel_5_config_header, container, false);
        myAdapterView												= (AdapterView) panelView.findViewById(R.id.List_View);

        // This part can be in processFinishTCP/HTTP 
        HTTP_Send(new Ctrl_Calendars().new Request());
        
        return panelView;
     }
    @Override
	public void onClick(View myView) 
	{
    	System.out.println("We have arrived in onClick/panel3Calendars again");
    	
    	Button 									myButton 			= (Button) myView;
    	String									myCaption			= myButton.getText().toString();
    	
		// Set all textColours to white
		ViewGroup 								viewParent			= (ViewGroup) myView.getParent();
		for (int i = 0; i < viewParent.getChildCount(); i++)
		{
			Button								buttonChild 		= (Button) viewParent.getChildAt(i);
			buttonChild.setTextColor(Color.WHITE);
		}
		
		((Button) myView).setTextColor(Color.YELLOW);
    	
    	if (myCaption.equalsIgnoreCase("Thermometers"))
    	{
    		// buttonThermometersClick(myView);	
    	}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		// TODO Auto-generated method stub
		
	}
	public void HTTP_Send(Ctrl_Abstract message)
	{
		HTTP_Task								task				= new HTTP_Task();
	   	task.callBack												= this;					// processFinish
	   	task.execute(message);
	}
	public void processFinishHTTP(Ctrl_Abstract result) 
	{  
		Activity								activity			= getActivity();		

		if (result instanceof Ctrl_Calendars.Data)
		{
			Global.eRegCalendars				 						= (Ctrl_Calendars.Data) result;
	        AdapterView <Adapter_Words> 			view				= (AdapterView) myContainer.findViewById(R.id.List_View);
	        Adapter_Words							adapter				= new Adapter_Words(Global.actContext, R.id.List_View, Global.eRegCalendars.wordList);
	        view.setAdapter(adapter);
		}
		else
		{
			Global.toaster("Data NOTNOTNOT received", true);
		}
			
	}

}

