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
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.EditText;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Panel_5_Configuration_Relays 						extends 					Panel_0_Fragment 
{
	public Panel_5_Configuration_Relays()
	{
		super();
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	this.container																		= container;
    	this.panelView																		= inflater.inflate(R.layout.panal_0_standard_with_buttons_addnew, container, false);

    	LinearLayout 											insertPoint 				= (LinearLayout) panelView.findViewById(R.id.base_insert_point);
    	
    	Element_Heading											listHeading					= new Element_Heading(getActivity(), "Relay Name", "Address");
    	Element_ListView										listView 					= new Element_ListView(getActivity(), "Henry");
    	insertPoint.addView(listHeading);
    	insertPoint.addView(listView);

        this.adapterView																	= (AdapterView) panelView.findViewById(R.id.List_View);

        if ((Global.eRegConfiguration != null)
        &&  (Global.eRegConfiguration.relayList != null))
        {
        	displayTitles("Configuration", "Relays");
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
	    AdapterView <Adapter_5_Configuration_Relays>			adapterViewList				= (AdapterView <Adapter_5_Configuration_Relays>) adapterView;
		Adapter_5_Configuration_Relays							arrayAdapter				= new Adapter_5_Configuration_Relays(Global.actContext, R.id.List_View, Global.eRegConfiguration.relayList);
		adapterViewList.setAdapter(arrayAdapter);

	}
	public void setListens()
	{
		((AdapterView<?>) adapterView).setOnItemClickListener(this);
//		panelView.findViewById(R.id.buttonAdd).setOnClickListener(this);
	}
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
	{
    	Ctrl_Configuration.Relay								itemData					= Global.eRegConfiguration.relayList.get(position);

    	Panel_5_Configuration_Relay_Item								itemFragment				= new Panel_5_Configuration_Relay_Item(itemData);
 
    	FragmentTransaction 									fTransaction 				= getActivity().getFragmentManager().beginTransaction();
   		fTransaction.replace(R.id.panel_container, itemFragment);
   		fTransaction.addToBackStack(null);
   		fTransaction.commit();
   	}
    public void onClick(View myView)
    {
    }
}