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
public class Panel_5_Config_Thermometers 				extends 					Panel_0_Fragment 
{
	private Adapter_5_Configuration_Thermometers 		adapter;
	private LayoutInflater								myInflater;
	private Activity									myActivity;
	private ViewGroup									myContainer;
	private View										myAdapterView;
	private FragmentManager								myFragmentManager;
//	private int											menuLayout;

	public Panel_5_Config_Thermometers()
	{
		super();
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        // Inflate the layout for this fragment
        myInflater																	= inflater;
        myContainer 																= container;
        myActivity																	= getActivity();
        myFragmentManager 															= myActivity.getFragmentManager();
        View 											panelView					= myInflater.inflate(R.layout.panel_5_config_header, container, false);
        myAdapterView																= (AdapterView) panelView.findViewById(R.id.List_View);

        return panelView;
    }
    public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
	{
//        if (position > 0)
//        {
//        	System.out.println("position : " + position);
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
 	public void processFinishTCP(Ctrl_Abstract result) 
	{  
		Activity										activity					= getActivity();		

		if (result instanceof Ctrl_Configuration.Data)
		{
			Global.eRegConfiguration			 									= (Ctrl_Configuration.Data) result;
			AdapterView <Adapter_5_Configuration_Thermometers> 	listView			= (AdapterView) myContainer.findViewById(R.id.List_View);
			Adapter_5_Configuration_Thermometers 				adapter				= new Adapter_5_Configuration_Thermometers(Global.actContext, R.id.List_View, Global.eRegConfiguration.thermometerList);
			listView.setAdapter(adapter);
		}
		else
		{
			Global.toaster("Data NOTNOTNOT received", true);
		}
	}
}