package com.bapjg.hvac_client;

import java.util.ArrayList;

import HVAC_Messages.*;
import HVAC_Messages.Ctrl_Configuration.Request;
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

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Panel_5_Configuration_Relays 						extends 					Panel_0_Fragment 
{
	private View												panelView;					// This corresponds to the inflated panel (R.layout.panel_n_xxxxxx)
	private View												adapterView;				// This corresponds to the inflated list view within the panel view (R.id.List_View)

	public Panel_5_Configuration_Relays()
	{
		super();
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        // Inflate the layout for this fragment
        this.panelView 																		= inflater.inflate(R.layout.panel_5_configuration_relays, container, false);
        this.adapterView																	= (AdapterView) panelView.findViewById(R.id.List_View);

        if ((Global.eRegConfiguration != null)
        &&  (Global.eRegConfiguration.relayList != null))
        {
        	displayHeader();
        	displayContents();
            setListens();
        }
        else // we need to reconnect to the server
        {
            HTTP_Send(new Ctrl_Configuration().new Request());
        }
 
        return panelView;
    }
	public void displayHeader()
	{
//		TextView												title						= (TextView) panelView.findViewById(R.id.name);
//		title.setText("Relays");
	}
	public void displayContents()
	{
	    AdapterView <Adapter_5_Configuration_Relays>			adapterViewList				= (AdapterView <Adapter_5_Configuration_Relays>) adapterView;
		Adapter_5_Configuration_Relays							arrayAdapter				= new Adapter_5_Configuration_Relays(Global.actContext, R.id.List_View, Global.eRegConfiguration.relayList);
		adapterViewList.setAdapter(arrayAdapter);
//		adapterViewList.setOnItemClickListener(this);

	}
	public void setListens()
	{
	}
//    public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
//	{
////	        ViewGroup 									viewGroup					= (ViewGroup) myActivity.findViewById(R.id.Detail_View);
////        	View 										newView 					= myInflater.inflate(R.layout.detail_thermometer, viewGroup, true);
//			
//    	FragmentTransaction 							ft 							= myFragmentManager.beginTransaction();
//    	Ctrl_Configuration.Thermometer 					dt							= Global.eRegConfiguration.thermometerList.get(position -1);
//
// //   	ft.replace(R.id.panel_container, dt);
//    	ft.commit();
//   	}
//    public void onClick(View myView)
//    {
//    	// onClick for all buttons in Menu_Pane				
//    	Button 											myButton 					= (Button) myView;
//    	String											myCaption					= myButton.getText().toString();
//						
//		// Set all textColours to white				
//		ViewGroup 										viewParent					= (ViewGroup) myView.getParent();
//		for (int i = 0; i < viewParent.getChildCount(); i++)
//		{
//			Button										buttonChild					= (Button) viewParent.getChildAt(i);
//			buttonChild.setTextColor(Color.WHITE);
//		}
//		
//		((Button) myView).setTextColor(Color.YELLOW);
//    	
//    	if (myCaption.equalsIgnoreCase("Thermometers"))
//    	{
//    		menuButtonThermometersClick(myView);	
//    	}
//    }
//    public void menuButtonThermometersClick(View myView)
//    {
//		// Called by onClick when Thermometers button pressed
//    	// This sets up the code to display the panel and get clicks in order to display an update screen
//
//        // First, ensure that correct view is displayed
//    	ViewGroup										subContainer				= (ViewGroup) myContainer.getChildAt(0);		
//    	View 											newView 					= myInflater.inflate(R.layout.panel_5_config_header, subContainer, true);
//				
//    	FragmentTransaction								ft							= myFragmentManager.beginTransaction();
//    	//Panel_2_Configuration 						dt 							= new Panel_2_Configuration();
//    	ft.replace(R.id.panel_subcontainer, this);
//    	ft.commit();
//
//        // Set up the adapter for the data
//    	//ArrayList  	<Ctrl_Configuration.Thermometer>	data	= Global.configuration.thermometerList;
//        AdapterView <Adapter_5_Configuration_Thermometers> 	view					= (AdapterView) myActivity.findViewById(R.id.List_View);
//        
//        Adapter_5_Configuration_Thermometers 			adapter						= new Adapter_5_Configuration_Thermometers(Global.actContext, R.id.List_View, Global.eRegConfiguration.thermometerList);
//        
//        view.setAdapter(adapter);
//        view.setOnItemClickListener((OnItemClickListener) this);	
//    }
	public void processFinishTCP(Ctrl__Abstract result) 
	{  
		if (result instanceof Ctrl_Configuration.Data)
		{
			Global.eRegConfiguration			 											= (Ctrl_Configuration.Data) result;
			displayHeader();
			displayContents();
	        setListens();
 		}
		else
		{
			Global.toaster("P5_Conf PUMP : Data NOTNOTNOT received", true);
		}
	}
}