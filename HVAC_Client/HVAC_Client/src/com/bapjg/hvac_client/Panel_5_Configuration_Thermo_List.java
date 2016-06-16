package com.bapjg.hvac_client;

import java.util.ArrayList;

import HVAC_Common.Ctrl_Actions_Relays;
import HVAC_Common.Ctrl_Calendars;
import HVAC_Common.Ctrl_Configuration;
import HVAC_Common.Ctrl__Abstract;
import HVAC_Common.Ctrl_Calendars.Word;
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
public class Panel_5_Configuration_Thermo_List 				extends 					Panel_0_Fragment 
{
	public Panel_5_Configuration_Thermo_List()
	{
		super("None");
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);
    	
    	Element_Heading											listHeading					= new Element_Heading("Thermometer", "Address");
    	Element_ListView										listView 					= new Element_ListView("Henry");
    	panelInsertPoint.addView(listHeading);
    	panelInsertPoint.addView(listView);

        this.adapterView																	= (AdapterView) panelView.findViewById(R.id.listView);

    	displayTitles("Configuration", "Thermometres Lost & Found");
    	
        if (Global.thermosLostFound 					!= null)
        {
        	displayContents();
            setListens();
        }
        else // we need to reconnect to the server
        {
            Global.toaster("Please wait for data to arrive or refresh", true);
        }
        return panelView;
    }
	public void displayContents()
	{
		Panel_5_Configuration_Thermo_List_Adapter 					arrayAdapter				= new Panel_5_Configuration_Thermo_List_Adapter(Global.actContext, R.id.listView, Global.thermosLostFound.thermos);
		((AdapterView <Panel_5_Configuration_Thermo_List_Adapter>) adapterView).setAdapter(arrayAdapter);
	}
	public void setListens()
	{
		((AdapterView <Panel_5_Configuration_Thermometers_Adapter>) adapterView).setOnItemClickListener(this);
	}    
    @Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
	{
    	Ctrl_Configuration.Thermometer							itemData					= Global.eRegConfiguration.thermometerList.get(position);

    	Panel_5_Configuration_Thermometers_Item					itemFragment				= new Panel_5_Configuration_Thermometers_Item(itemData);
 
    	FragmentTransaction 									fTransaction 				= getActivity().getFragmentManager().beginTransaction();
   		fTransaction.replace(R.id.panel_container, itemFragment);
   		fTransaction.addToBackStack(null);
   		fTransaction.commit();
   	}
	public void onPanelButtonAdd()
    {
    	Ctrl_Configuration.Thermometer							itemNew						= new Ctrl_Configuration().new Thermometer();
		itemNew.name																		= "new";
		itemNew.address																		= "28.";
		itemNew.pidName																		= "";
		Global.eRegConfiguration.thermometerList.add(itemNew);
		displayContents();
		setListens();
    }
}