package com.bapjg.hvac_client;

import java.util.ArrayList;

import HVAC_Messages.Ctrl_Abstract;
import HVAC_Messages.Ctrl_Actions_Relays;
import HVAC_Messages.Ctrl_Configuration;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.EditText;

@SuppressLint("ValidFragment")
//Template										variable			= something
//Template										ext/imp				class

public class Panel_4_Config_Circuits 			extends 			Panel_0_Fragment 
												implements 			TCP_Response
{
	private Adapter_Relays		 				adapter;
	private LayoutInflater						myInflater;
	private Activity							myActivity;
	private ViewGroup							myContainer;
	private View								myAdapterView;
	private FragmentManager						myFragmentManager;

	public Panel_4_Config_Circuits()
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
        View 									myView 				= myInflater.inflate(R.layout.panel_4_config_header, container, false);
        myAdapterView												= (AdapterView) myView.findViewById(R.id.List_View);

        TCP_Send(new Ctrl_Configuration().new Request());
 
        return myView;
    }
    public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
	{
        if (position > 0)
        {
        	System.out.println("position : " + position);
//	        ViewGroup 							viewGroup			= (ViewGroup) myActivity.findViewById(R.id.Detail_View);
//        	View 								newView 			= myInflater.inflate(R.layout.detail_thermometer, viewGroup, true);

        	FragmentTransaction 				ft 					= myFragmentManager.beginTransaction();
        	Ctrl_Configuration.Thermometer 		dt					= Global.eRegConfiguration.thermometerList.get(position -1);

     //   	ft.replace(R.id.panel_container, dt);
        	ft.commit();
        }
        else
        {
        	// We have clicked in title area
        }
   	}
    public void onClick(View myView)
    {
// Template										variable			= something
// Template										ext/imp				class
    	// onClick for all buttons in Menu_Pane
    	Button 									myButton 			= (Button) myView;
    	String									myCaption			= myButton.getText().toString();
    	
		// Set all textColours to white
		ViewGroup 								viewParent			= (ViewGroup) myView.getParent();
		for (int i = 0; i < viewParent.getChildCount(); i++)
		{
			Button								buttonChild			= (Button) viewParent.getChildAt(i);
			buttonChild.setTextColor(Color.WHITE);
		}
		
		((Button) myView).setTextColor(Color.YELLOW);
    	
    	if (myCaption.equalsIgnoreCase("Thermometers"))
    	{
    		menuButtonThermometersClick(myView);	
    	}
    }
    public void menuButtonThermometersClick(View myView)
    {
		// Called by onClick when Thermometers button pressed
    	// This sets up the code to display the panel and get clicks in order to display an update screen

        // First, ensure that correct view is displayed
    	ViewGroup								subContainer		= (ViewGroup) myContainer.getChildAt(0);		
    	View 									newView 			= myInflater.inflate(R.layout.panel_4_config_header, subContainer, true);

    	FragmentTransaction						ft					= myFragmentManager.beginTransaction();
    	//Panel_2_Configuration 				dt 					= new Panel_2_Configuration();
    	ft.replace(R.id.panel_subcontainer, this);
    	ft.commit();

        // Set up the adapter for the data
    	//ArrayList  	<Ctrl_Configuration.Thermometer>	data	= Global.configuration.thermometerList;
        AdapterView <Adapter_Thermometers> 		view				= (AdapterView) myActivity.findViewById(R.id.List_View);
        
        Adapter_Thermometers 					adapter				= new Adapter_Thermometers(Global.actContext, R.id.List_View, Global.eRegConfiguration.thermometerList);
        
        view.setAdapter(adapter);
        view.setOnItemClickListener((OnItemClickListener) this);	
    }
	public void TCP_Send(Ctrl_Abstract message)
	{
		TCP_Task								task				= new TCP_Task();
	   	task.callBack												= this;					// processFinish
	   	task.execute(message);
	}
	public void processFinishTCP(Ctrl_Abstract result) 
	{  
		Activity								activity			= getActivity();		

		if (result instanceof Ctrl_Configuration.Data)
		{
		Global.eRegConfiguration			 						= (Ctrl_Configuration.Data) result;
        AdapterView <Adapter_Circuits> 			view				= (AdapterView) myContainer.findViewById(R.id.List_View);
        Adapter_Circuits						adapter				= new Adapter_Circuits(Global.actContext, R.id.List_View, Global.eRegConfiguration.circuitList);
        view.setAdapter(adapter);
		}
		else
		{
			Global.toaster("Data NOTNOTNOT received", true);
		}
			
	}

}