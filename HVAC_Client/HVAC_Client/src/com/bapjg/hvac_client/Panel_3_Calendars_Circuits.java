package com.bapjg.hvac_client;

import HVAC_Common.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("ValidFragment")
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Panel_3_Calendars_Circuits 						extends 					Panel_0_Fragment
																implements					AdapterView.OnItemClickListener	
{
	public String												circuitName;

	private View												panelView;					// This corresponds to the inflated panel (R.layout.panel_n_xxxxxx)
	private View												adapterView;				// This corresponds to the inflated list view within the panel view (R.id.List_View)

//	Calendar times are all wrong
	
    public Panel_3_Calendars_Circuits(String circuitName)
    {
		super();
		this.circuitName																	= circuitName;
   }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	this.panelView																		= inflater.inflate(R.layout.panel_3_calendars, container, false);
        this.adapterView																	= (AdapterView) panelView.findViewById(R.id.List_View);
        
        if ((Global.eRegCalendars != null)
        &&  (Global.eRegCalendars.circuitList != null))
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
		((TextView) panelView.findViewById(R.id.title)).setText		("Calendars");
		((TextView) panelView.findViewById(R.id.subTitle)).setText	(this.circuitName);
//		TextView 												subTitle						= (TextView) panelView.findViewById(R.id.subTitle);
//        subTitle.setText(this.circuitName);	
	}
	public void displayContents()
	{
        AdapterView <Adapter_3_Calendars_Circuits>				adapterViewList				= (AdapterView <Adapter_3_Calendars_Circuits>) adapterView;
        Ctrl_Calendars.Circuit									circuit						= Global.eRegCalendars.fetchCircuit(this.circuitName);
        Adapter_3_Calendars_Circuits							arrayAdapter				= new Adapter_3_Calendars_Circuits(Global.actContext, R.id.List_View, circuit.calendarList);
        adapterViewList.setAdapter(arrayAdapter);
	}
	public void setListens()
	{
		((AdapterView<?>) adapterView).setOnItemClickListener(this);
		panelView.findViewById(R.id.buttonAdd).setOnClickListener(this);
	}
	@Override
    public void onClick(View clickedView)
	{
		if (clickedView.getId() == R.id.buttonAdd)
		{
			Ctrl_Calendars.Calendar								itemNew						= new Ctrl_Calendars().new Calendar();

			itemNew.days																	= "";
			itemNew.timeStart																= new Cmn_Time("09:00");
			itemNew.timeEnd																	= new Cmn_Time("10:00");
			itemNew.tempObjective															= new Cmn_Temperature("30");
			itemNew.stopOnObjective															= true;

			Global.eRegCalendars.fetchCircuit(this.circuitName).calendarList.add(itemNew);
			displayContents();
			setListens();
		}
	}
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
    	Ctrl_Calendars.Calendar									itemData					= Global.eRegCalendars.fetchCircuit(this.circuitName).calendarList.get(position);

    	Item_3_Calendars_Circuits								itemFragment				= new Item_3_Calendars_Circuits(itemData, this.circuitName);
 
    	FragmentTransaction 									fTransaction 				= getActivity().getFragmentManager().beginTransaction();
   		fTransaction.replace(R.id.panel_container, itemFragment);
   		fTransaction.addToBackStack(null);
   		fTransaction.commit();
	}
}

