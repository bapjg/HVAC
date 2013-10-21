package com.bapjg.hvac_client;

import java.util.ArrayList;

import com.bapjg.hvac_client.Mgmt_Msg_Configuration;
import com.bapjg.hvac_client.Mgmt_Msg_Configuration.Thermometer;

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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;


public class Panel_2_Configuration extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener
{
	private Adapter_Thermometers 		adapter;
	private LayoutInflater				myInflater;
	private Activity					myActivity;
	private ViewGroup					myContainer;
	private FragmentManager				myFragmentManager;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        // Inflate the layout for this fragment
        myInflater														= inflater;
        myContainer 													= container;
        myActivity														= getActivity();
        myFragmentManager 												= myActivity.getFragmentManager();
//        View 							myView 							= myInflater.inflate(R.layout.panel_2_configuration, myContainer, false);
        View 							myView 							= myInflater.inflate(R.layout.panel_0_subcontainer, myContainer, false);
        return myView;
    }
    public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
	{
        if (position > 0)
        {
        	System.out.println("position : " + position);
	        ViewGroup 					viewGroup						= (ViewGroup) myActivity.findViewById(R.id.Detail_View);
        	View 						newView 						= myInflater.inflate(R.layout.detail_thermometer, viewGroup, true);

        	FragmentTransaction 		ft 								= myFragmentManager.beginTransaction();
        	Detail_Thermometer 			dt 								= new Detail_Thermometer();
        	dt.me														= Global.configuration.thermometerList.get(position -1);	//position 0 contains titles
        	ft.replace(R.id.panel_container, dt);
        	ft.commit();
        }
        else
        {
        	// We have clicked in title area
        }
   	}
    public void onClick(View myView)
    {
     	// onClick for all buttons in Menu_Pane
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
    	ViewGroup					subContainer						= (ViewGroup) myContainer.getChildAt(0);		
    	View 						newView 							= myInflater.inflate(R.layout.panel_2_configuration, subContainer, true);

    	FragmentTransaction				ft								= myFragmentManager.beginTransaction();
    	Panel_2_Configuration 			dt 								= new Panel_2_Configuration();
    	ft.replace(R.id.panel_subcontainer, dt);
    	ft.commit();

        // Set up the adapter for the data
    	ArrayList  	<Mgmt_Msg_Configuration.Thermometer>	data		= Global.configuration.thermometerList;
        AdapterView <Adapter_Thermometers> 	view						= (AdapterView) myActivity.findViewById(R.id.List_View);
        
        Adapter_Thermometers 				adapter						= new Adapter_Thermometers(Global.actContext, R.id.List_View, data);
        
        view.setAdapter(adapter);
        view.setOnItemClickListener((OnItemClickListener) this);	
    }
}