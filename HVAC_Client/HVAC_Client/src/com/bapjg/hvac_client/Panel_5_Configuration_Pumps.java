package com.bapjg.hvac_client;

import java.util.ArrayList;

import HVAC_Common.*;
import HVAC_Common.Ctrl_Configuration.Request;
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
public class Panel_5_Configuration_Pumps 						extends 					Panel_0_Fragment 
{
//	private Adapter_5_Configuration_Relays		 				adapter;
//	private LayoutInflater										myInflater;
//	private Activity											myActivity;
//	private ViewGroup											myContainer;
//	private View												myAdapterView;
//	private FragmentManager										myFragmentManager;
			
	private View												panelView;					// This corresponds to the inflated panel (R.layout.panel_n_xxxxxx)
	private View												adapterView;				// This corresponds to the inflated list view within the panel view (R.id.List_View)

	public Panel_5_Configuration_Pumps()
	{
		super();
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        // Inflate the layout for this fragment
        this.panelView																		= inflater.inflate(R.layout.panel_5_configuration_pumps, container, false);
        this.adapterView																	= (AdapterView) panelView.findViewById(R.id.List_View);

        if ((Global.eRegConfiguration != null)
        &&  (Global.eRegConfiguration.pumpList != null))
        {
        	displayHeader();
        	displayContents();
            setListens();
        }
        else // we need to reconnect to the server
        {
            Global.toaster("please refresh", false);
        }
        
        return panelView;
    }
    public void onItemClick(AdapterView<?> arg0, View view, int position, long id)
	{
    	Ctrl_Configuration.Pump									itemData					= Global.eRegConfiguration.pumpList.get(position);

    	Item_5_Configuration_Pump								itemFragment				= new Item_5_Configuration_Pump(itemData);
 
    	FragmentTransaction 									fTransaction 				= getActivity().getFragmentManager().beginTransaction();
   		fTransaction.replace(R.id.panel_container, itemFragment);
   		fTransaction.addToBackStack(null);
   		fTransaction.commit();
   	}
    public void onClick(View myView)
    {
    }
    public void menuButtonThermometersClick(View myView)
    {
    }
	public void processFinishTCP(Ctrl__Abstract result) 
	{  
		if (result instanceof Ctrl_Configuration.Data)
		{
			Global.eRegConfiguration														= (Ctrl_Configuration.Data) result;
			displayHeader();
			displayContents();
		}
		else
		{
			Global.toaster("P5_Conf_Pump : Data NOTNOTNOT received", true);
		}
	}
	public void displayHeader()
	{
		((TextView) panelView.findViewById(R.id.title)).setText		("Configuration");
		((TextView) panelView.findViewById(R.id.subTitle)).setText	("Pumps");
	}
	public void displayContents()
	{
	    AdapterView <Adapter_5_Configuration_Pumps>				adapterViewList				= (AdapterView <Adapter_5_Configuration_Pumps>) adapterView;
        Adapter_5_Configuration_Pumps							arrayAdapter				= new Adapter_5_Configuration_Pumps(Global.actContext, R.id.List_View, Global.eRegConfiguration.pumpList);
        adapterViewList.setAdapter(arrayAdapter);
	}
	public void setListens()
	{
		((AdapterView<?>) adapterView).setOnItemClickListener(this);
	}
}