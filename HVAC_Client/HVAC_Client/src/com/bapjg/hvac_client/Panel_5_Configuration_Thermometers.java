package com.bapjg.hvac_client;

import java.util.ArrayList;

import HVAC_Common.Ctrl_Actions_Relays;
import HVAC_Common.Ctrl_Calendars;
import HVAC_Common.Ctrl_Configuration;
import HVAC_Common.Ctrl__Abstract;
import HVAC_Common.Ctrl_Configuration.Data;
import HVAC_Common.Ctrl_Configuration.Request;
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
	}
	public void setListens()
	{
		((AdapterView<?>) adapterView).setOnItemClickListener(this);
//		panelView.findViewById(R.id.buttonAdd).setOnClickListener(this);
	}    
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
	{
    	Ctrl_Configuration.Thermometer							itemData					= Global.eRegConfiguration.thermometerList.get(position);

    	Item_5_Configuration_Thermometer						itemFragment				= new Item_5_Configuration_Thermometer(itemData);
 
    	FragmentTransaction 									fTransaction 				= getActivity().getFragmentManager().beginTransaction();
   		fTransaction.replace(R.id.panel_container, itemFragment);
   		fTransaction.addToBackStack(null);
   		fTransaction.commit();
   	}
    public void onClick(View myView)
    {
    }
}