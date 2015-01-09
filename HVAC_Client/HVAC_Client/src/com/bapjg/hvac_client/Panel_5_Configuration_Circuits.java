package com.bapjg.hvac_client;

import java.util.ArrayList;

import HVAC_Common.*;
import HVAC_Common.Ctrl_Calendars.Away;
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
		super("Add");
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);
    	
    	Element_Heading											listHeading					= new Element_Heading("Circuit Name", "Type");
    	Element_ListView										listView 					= new Element_ListView("Henry");
    	panelInsertPoint.addView(listHeading);
    	panelInsertPoint.addView(listView);

    	this.adapterView																	= listView.findViewById(R.id.List_View);

    	displayTitles("Configuration", "Circuits");

        if ((Global.eRegConfiguration != null)
        &&  (Global.eRegConfiguration.circuitList != null))
        {
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
		Panel_5_Configuration_Circuits_Adapter						arrayAdapter				= new Panel_5_Configuration_Circuits_Adapter(Global.actContext, R.id.List_View, Global.eRegConfiguration.circuitList);
		((AdapterView <Panel_5_Configuration_Circuits_Adapter>) adapterView).setAdapter(arrayAdapter);
	}
	public void setListens()
	{
		((AdapterView <?>) adapterView).setOnItemClickListener(this);
	}
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
    	Ctrl_Configuration.Circuit								itemData						= Global.eRegConfiguration.circuitList.get(position);

    	Panel_5_Configuration_Circuits_Item						itemFragment					= new Panel_5_Configuration_Circuits_Item(itemData);

    	FragmentTransaction 									fTransaction 					= getActivity().getFragmentManager().beginTransaction();
   		fTransaction.replace(R.id.panel_container, itemFragment);
   		fTransaction.addToBackStack(null);
   		fTransaction.commit();
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

}