package com.bapjg.hvac_client;

import java.util.ArrayList;

import HVAC_Common.*;
import HVAC_Common.Ctrl_Calendars.Away;
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

@SuppressLint("ValidFragment")
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Panel_5_Configuration_Circuits 					extends 					Panel_0_Fragment
																implements					AdapterView.OnItemClickListener	
{
	public Panel_5_Configuration_Circuits()
	{
		super();
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
//    	this.panelLayout																	= R.layout.panal_0_standard;
    	this.container																		= container;
    	this.panelView																		= inflater.inflate(R.layout.panal_0_standard_with_buttons, container, false);

    	LinearLayout 											insertPoint 				= (LinearLayout) panelView.findViewById(R.id.base_insert_point);
    	
    	Element_Heading											listHeading					= new Element_Heading(getActivity(), "Circuit Name");
    	Element_ListView										listView 					= new Element_ListView(getActivity(), "Henry");
    	insertPoint.addView(listHeading);
    	insertPoint.addView(listView);

    	this.adapterView																	= listView.findViewById(R.id.List_View);

        if ((Global.eRegConfiguration != null)
        &&  (Global.eRegConfiguration.circuitList != null))
        {
        	displayTitles("Configuration", "Circuits");
        	displayContents();
            setListens();
        }
        else // we need to reconnect to the server
        {
        	Global.toaster("Please wait or refresh if necessary", true);
        }
        return panelView;
    }
	public void displayContents()
	{
		AdapterView <Adapter_5_Configuration_Circuits>			adapterViewList				= (AdapterView <Adapter_5_Configuration_Circuits>) adapterView;
		Adapter_5_Configuration_Circuits						arrayAdapter				= new Adapter_5_Configuration_Circuits(Global.actContext, R.id.List_View, Global.eRegConfiguration.circuitList);
		adapterViewList.setAdapter(arrayAdapter);
	}
	public void setListens()
	{
		((AdapterView <?>) adapterView).setOnItemClickListener(this);
	}
	public void processFinishHTTP(Ctrl__Abstract result) 
	{  
		super.processFinishTCP(result);
		if (result instanceof Ctrl_Configuration.Data)
		{
			Global.eRegConfiguration			 											= (Ctrl_Configuration.Data) result;
			displayContents();
			setListens();
		}
		else
		{
			Global.toaster("P5_Conf_Circuits : Data NOTNOTNOT received", true);
		}
	}
    public void onClick(View clickedView)
    {
    }
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
    	Ctrl_Configuration.Circuit								itemData						= Global.eRegConfiguration.circuitList.get(position);

//    	Item_5_Configuration_Circuit							itemFragment					= new Item_5_Configuration_Circuit(itemData);
    	Panel_5_Configuration_Circuit_Item						itemFragment					= new Panel_5_Configuration_Circuit_Item(itemData);

    	FragmentTransaction 									fTransaction 					= getActivity().getFragmentManager().beginTransaction();
   		fTransaction.replace(R.id.panel_container, itemFragment);
   		fTransaction.addToBackStack(null);
   		fTransaction.commit();
	}
}