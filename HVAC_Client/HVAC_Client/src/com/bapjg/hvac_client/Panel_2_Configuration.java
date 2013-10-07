package com.bapjg.hvac_client;

import java.util.ArrayList;

import com.bapjg.hvac_client.Mgmt_Msg_Configuration;
import com.bapjg.hvac_client.Mgmt_Msg_Configuration.Thermometer;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
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


public class Panel_2_Configuration extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener
{
	private Adapter_Thermometers 		adapter;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        // Inflate the layout for this fragment
        View myView =  inflater.inflate(R.layout.panel_2_configuration, container, false);
        //myView.findViewById(R.id.buttonThermometers).setOnClickListener((OnClickListener) this);
        //ViewGroup myViewGroup = (ViewGroup) myView;
        //setButtonOnClick(myViewGroup);
        return myView;
    }
    public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
	{
        if (position > 0)
        {
        	System.out.println("position : " + position);
	        Activity activity											= (Activity) Global.actContext;
	        ViewGroup 					viewGroup						= (ViewGroup) activity.findViewById(R.id.Detail_View);
 	        LayoutInflater 				inflater 						= (LayoutInflater) Global.actContext.getSystemService(Global.actContext.LAYOUT_INFLATER_SERVICE);
        	View 						newView 						= inflater.inflate(R.layout.detail_thermometer, viewGroup, true);
        	
        	FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        	Detail_Thermometers dt = new Detail_Thermometers();
        	
        	ft.replace(R.id.panel_container, dt);
        	ft.commit();
        	
        	Mgmt_Msg_Configuration conf = Global.configuration;

        	
        	Mgmt_Msg_Configuration.Thermometer me = (Mgmt_Msg_Configuration.Thermometer) Global.configuration.thermometerList.get(position -1);

        	((TextView) activity.findViewById(R.id.name)).setText(me.name);
			((TextView) activity.findViewById(R.id.friendlyName)).setText(me.friendlyName);
			((TextView) activity.findViewById(R.id.thermoID)).setText(me.thermoID);
        	
            //Need to setup an update screen
        }
        else
        {
	    	// We have clicked in Title area
        	
        	
        	// This sets height of the listview 0, and item view 100
	    	// Cant remember why - the if clause is just to not execute the code until I remember why
        	if (position == 0)
        	{
        		
        	}
        	else
        	{
		    	AbsListView 				layout_list						= (AbsListView) view.findViewById(R.id.List_View);
		        LinearLayout.LayoutParams	params_list						= (LinearLayout.LayoutParams) layout_list.getLayoutParams();
		        params_list.height											= 0;
		        layout_list.setLayoutParams(params_list);
		        
		        LinearLayout 				layout_item						= (LinearLayout) view.findViewById(R.id.Item_View);
		        LinearLayout.LayoutParams	params_item						= (LinearLayout.LayoutParams) layout_item.getLayoutParams();
		        params_item.height											= 100;
		        layout_item.setLayoutParams(params_item);
        	}
       }
   	}
    public void onClick(View myView)
    {
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
    		buttonThermometersClick(myView);	
    	}
    }
    public void buttonThermometersClick(View myView)
    {
		// This sets up the code to display the panel and get clicks in order to display an update screen
        ArrayList  						data		 				= Global.configuration.thermometerList;
        Activity 						activity					= (Activity) Global.actContext;
        AdapterView <Adapter_Thermometers> view						= (AdapterView) activity.findViewById(R.id.List_View);
        
        Adapter_Thermometers 			adapter						= new Adapter_Thermometers(Global.actContext, R.id.List_View, data);
        
        view.setAdapter(adapter);
        view.setOnItemClickListener((OnItemClickListener) this);	
    }
    static public class Detail_Thermometers extends Fragment 
    {
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
        {
            View myView =  inflater.inflate(R.layout.detail_thermometer, container, false);
            return myView;
        }
         	
    }
}