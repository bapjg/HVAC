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
	private View												panelView;					// This corresponds to the inflated panel (R.layout.panel_n_xxxxxx)
	private View												adapterView;				// This corresponds to the inflated list view within the panel view (R.id.List_View)

	public Panel_5_Configuration_Circuits()
	{
		super();
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        // Inflate the layout for this fragment
        this.panelView 																		= inflater.inflate(R.layout.panel_5_configuration_circuits, container, false);
        this.adapterView																	= (AdapterView) panelView.findViewById(R.id.List_View);

        if ((Global.eRegConfiguration != null)
        &&  (Global.eRegConfiguration.circuitList != null))
        {
        	displayHeader();
        	displayContents();
            setListens();
        }
        else // we need to reconnect to the server
        {
        	Global.toaster("Please wait or refresh if necessary", true);
        }
        return panelView;
    }
	public void displayHeader()
	{
//		TextView												title						= (TextView) panelView.findViewById(R.id.name);
//		title.setText("Circuits");
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
		if (result instanceof Ctrl_Configuration.Data)
		{
			Global.eRegConfiguration			 											= (Ctrl_Configuration.Data) result;
			displayHeader();
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

    	Item_5_Configuration_Circuit							itemFragment					= new Item_5_Configuration_Circuit(itemData);

    	FragmentTransaction 									fTransaction 					= getActivity().getFragmentManager().beginTransaction();
   		fTransaction.replace(R.id.panel_container, itemFragment);
   		fTransaction.addToBackStack(null);
   		fTransaction.commit();
	}

}