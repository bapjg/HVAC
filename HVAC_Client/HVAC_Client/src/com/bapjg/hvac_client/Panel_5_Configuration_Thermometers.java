package com.bapjg.hvac_client;

import java.util.ArrayList;

import HVAC_Messages.Ctrl__Abstract;
import HVAC_Messages.Ctrl_Actions_Relays;
import HVAC_Messages.Ctrl_Configuration;
import HVAC_Messages.Ctrl_Configuration.Data;
import HVAC_Messages.Ctrl_Configuration.Request;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
public class Panel_5_Configuration_Thermometers 				extends 					Panel_0_Fragment 
{
	private View												panelView;					// This corresponds to the inflated panel (R.layout.panel_n_xxxxxx)
	private View												adapterView;				// This corresponds to the inflated list view within the panel view (R.id.List_View)

	public Panel_5_Configuration_Thermometers()
	{
		super();
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        // Inflate the layout for this fragment
		this.panelView 																		= inflater.inflate(R.layout.panel_5_configuration_thermometers, container, false);
        this.adapterView																	= (AdapterView) panelView.findViewById(R.id.List_View);
 
        Data x = Global.eRegConfiguration;
        
        if ((Global.eRegConfiguration != null)
        &&  (Global.eRegConfiguration.thermometerList != null))
        {
        	displayHeader();
        	displayContents();
            setListens();
                    }
        else // we need to reconnect to the server
        {
            Global.toaster("Please wait for data to arrive or refresh", true);
        }

        return panelView;
    }
	public void displayHeader()
	{
//		TextView												title				= (TextView) panelView.findViewById(R.id.name);
//		title.setText("Circuits");
	}
	public void displayContents()
	{
	    AdapterView <Adapter_5_Configuration_Thermometers>		adapterViewList		= (AdapterView <Adapter_5_Configuration_Thermometers>) adapterView;
		Adapter_5_Configuration_Thermometers 					arrayAdapter		= new Adapter_5_Configuration_Thermometers(Global.actContext, R.id.List_View, Global.eRegConfiguration.thermometerList);
		adapterViewList.setAdapter(arrayAdapter);
//		adapterViewList.setOnItemClickListener(this);
	}
	public void setListens()
	{
	}    public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
	{
//        if (position > 0)
//        {
//        	Log.v("App", "position : " + position);
////	        ViewGroup 								viewGroup					= (ViewGroup) myActivity.findViewById(R.id.Detail_View);
////        	View 									newView 					= myInflater.inflate(R.layout.detail_thermometer, viewGroup, true);
//
//        	FragmentTransaction 						ft 							= myFragmentManager.beginTransaction();
//        	Ctrl_Parameters.Thermometer 				dt							= Global.eRegConfiguration.thermometerList.get(position -1);
//
//     //   	ft.replace(R.id.panel_container, dt);
//        	ft.commit();
//        }
//        else
//        {
//        	// We have clicked in title area
//        }
   	}
    public void onClick(View myView)
    {

    	// onClick for all buttons in Menu_Pane
//    	Button 									myButton 			= (Button) myView;
//    	String									myCaption			= myButton.getText().toString();
//    	
//		// Set all textColours to white
//		ViewGroup 								viewParent			= (ViewGroup) myView.getParent();
//		for (int i = 0; i < viewParent.getChildCount(); i++)
//		{
//			Button								buttonChild			= (Button) viewParent.getChildAt(i);
//			buttonChild.setTextColor(Color.WHITE);
//		}
//		
//		((Button) myView).setTextColor(Color.YELLOW);
//    	
//    	if (myCaption.equalsIgnoreCase("Thermometers"))
//    	{
//    		menuButtonThermometersClick(myView);	
//    	}
    }
// 	public void processFinishHTTP(Ctrl_Abstract result) 
//	{  
//		Activity												activity			= getActivity();		
//
//		if (result instanceof Ctrl_Configuration.Data)
//		{
//			Global.eRegConfiguration			 									= (Ctrl_Configuration.Data) result;
//			View											 		listView		= (View) adapterView.findViewById(R.id.List_View);
//			Adapter_5_Configuration_Thermometers 					adapter			= new Adapter_5_Configuration_Thermometers(Global.actContext, R.id.List_View, Global.eRegConfiguration.thermometerList);
//			((AdapterView <Adapter_5_Configuration_Thermometers>) listView).setAdapter(adapter);
//		}
//		else
//		{
//			Global.toaster("P5_Conf_Thermo : Data NOTNOTNOT received", true);
//		}
//	}
}