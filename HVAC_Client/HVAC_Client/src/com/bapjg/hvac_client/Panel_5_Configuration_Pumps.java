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

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Panel_5_Configuration_Pumps 						extends 					Panel_0_Fragment 
{
	public Panel_5_Configuration_Pumps()
	{
		super("Add");
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);

    	Element_Heading											listHeading					= new Element_Heading("Pump Name", "Relay Name");
    	Element_ListView										listView 					= new Element_ListView("Henry");
    	panelInsertPoint.addView(listHeading);
    	panelInsertPoint.addView(listView);

        this.adapterView																	= (AdapterView) panelView.findViewById(R.id.listView);
    	
        displayTitles("Actions", "Pumps");

        if ((Global.eRegConfiguration != null)
        &&  (Global.eRegConfiguration.pumpList != null))
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
//	    AdapterView <Panel_5_Configuration_Pumps_Adapter>			adapterViewList				= (AdapterView <Panel_5_Configuration_Pumps_Adapter>) adapterView;
        Panel_5_Configuration_Pumps_Adapter							arrayAdapter				= new Panel_5_Configuration_Pumps_Adapter(Global.actContext, R.id.listView, Global.eRegConfiguration.pumpList);
//        adapterViewList.setAdapter(arrayAdapter);
		((AdapterView <Panel_5_Configuration_Pumps_Adapter>) adapterView).setAdapter(arrayAdapter);
	}
	public void setListens()
	{
		((AdapterView <Panel_5_Configuration_Pumps_Adapter>) adapterView).setOnItemClickListener(this);
	}
	public void onPanelButtonAdd()
    {
    	Ctrl_Configuration.Pump									itemNew						= new Ctrl_Configuration().new Pump();
		itemNew.name																		= "new";
		itemNew.relay																		= "";
		Global.eRegConfiguration.pumpList.add(itemNew);
		displayContents();
		setListens();
    }
    public void onItemClick(AdapterView<?> arg0, View view, int position, long id)
	{
    	Ctrl_Configuration.Pump									itemData					= Global.eRegConfiguration.pumpList.get(position);

    	Panel_5_Configuration_Pumps_Item								itemFragment				= new Panel_5_Configuration_Pumps_Item(itemData);
 
    	FragmentTransaction 									fTransaction 				= getActivity().getFragmentManager().beginTransaction();
   		fTransaction.replace(R.id.panel_container, itemFragment);
   		fTransaction.addToBackStack(null);
   		fTransaction.commit();
   	}
	public void processFinishTCP(Msg__Abstract result) 
	{  
		if (result instanceof Ctrl_Configuration.Data)
		{
			Global.setStatusTCP("Ok");
			Global.eRegConfiguration														= (Ctrl_Configuration.Data) result;
			displayContents();
		}
		else
		{
			Global.setStatusTCP("No Data");
			Global.toaster("P5_Conf_Pump : Data NOTNOTNOT received", true);
		}
	}

}