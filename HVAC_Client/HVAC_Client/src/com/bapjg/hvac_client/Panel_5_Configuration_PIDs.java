package com.bapjg.hvac_client;

import java.util.ArrayList;

import HVAC_Common.*;
import HVAC_Common.Ctrl_Configuration.Request;
import HVAC_Common.Ctrl_Configuration.Thermometer;
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
public class Panel_5_Configuration_PIDs 						extends 					Panel_0_Fragment 
{
	public Panel_5_Configuration_PIDs()
	{
		super("Add");
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);
    	
    	Element_Heading											listHeading					= new Element_Heading("PID Name", "Depth");
    	Element_ListView										listView 					= new Element_ListView("Henry");
    	panelInsertPoint.addView(listHeading);
    	panelInsertPoint.addView(listView);

    	this.adapterView																	= (AdapterView) panelView.findViewById(R.id.listView);

    	displayTitles("Configuration", "PIDs");

        if ((Global.eRegConfiguration != null)
        &&  (Global.eRegConfiguration.pidList != null))
        {
        	displayContents();
            setListens();
        }
        else // we need to reconnect to the server
        {
            Global.toaster("please refresh", false);
        }
        
        return panelView;
    }
	public void displayContents()
	{
        Panel_5_Configuration_PIDs_Adapter							arrayAdapter					= new Panel_5_Configuration_PIDs_Adapter(Global.actContext, R.id.listView, Global.eRegConfiguration.pidList);
		((AdapterView <Panel_5_Configuration_PIDs_Adapter>) adapterView).setAdapter(arrayAdapter);
	}
	public void setListens()
	{
		((AdapterView <Panel_5_Configuration_PIDs_Adapter>) adapterView).setOnItemClickListener(this);
	}
    public void onItemClick(AdapterView<?> arg0, View view, int position, long id)
	{
    	Ctrl_Configuration.PID_Data								itemData						= Global.eRegConfiguration.pidList.get(position);

    	Panel_5_Configuration_PIDs_Item							itemFragment					= new Panel_5_Configuration_PIDs_Item(itemData);

    	FragmentTransaction 									fTransaction 					= getActivity().getFragmentManager().beginTransaction();
   		fTransaction.replace(R.id.panel_container, itemFragment);
   		fTransaction.addToBackStack(null);
   		fTransaction.commit();
   	}
	public void onPanelButtonAdd()
    {
    	Ctrl_Configuration.PID_Data								itemNew							= new Ctrl_Configuration().new PID_Data();
		itemNew.name																			= "New";
		itemNew.depth																			= 10;
		itemNew.sampleIncrement																	= 1;
		Global.eRegConfiguration.pidList.add(itemNew);
		displayContents();
		setListens();
    }
}