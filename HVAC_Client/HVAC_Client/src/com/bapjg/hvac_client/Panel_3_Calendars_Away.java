package com.bapjg.hvac_client;

import java.util.ArrayList;

import HVAC_Common.*;
import HVAC_Common.Ctrl_Calendars.Calendar;
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
public class Panel_3_Calendars_Away 							extends 					Panel_0_Fragment 
{
	private View												panelView;					// This corresponds to the inflated panel (R.layout.panel_n_xxxxxx)
	private View												adapterView;				// This corresponds to the inflated list view within the panel view (R.id.List_View)

	public Panel_3_Calendars_Away()
	{
		super();
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        // Inflate the layout for this fragment
        this.panelView 																		= inflater.inflate(R.layout.panel_3_calendars_away, container, false);
        this.adapterView																	= (AdapterView) panelView.findViewById(R.id.List_View);

        if ((Global.eRegCalendars != null)
        &&  (Global.eRegCalendars.awayList != null))
        {
        	displayHeader();
        	displayContents();
        	setListens();
        }
        else // we need to reconnect to the server
        {
            Global.toaster("please refresh", false);;
        }
 
        return panelView;
    }
	public void displayHeader()
	{
		((TextView) panelView.findViewById(R.id.title)).setText		("Calendars");
		((TextView) panelView.findViewById(R.id.subTitle)).setText	("Away List");
//		TextView												title						= (TextView) panelView.findViewById(R.id.name);
	}
	public void displayContents()
	{
	    AdapterView <Adapter_3_Calendars_Away>					adapterViewList				= (AdapterView <Adapter_3_Calendars_Away>) adapterView;
	    Adapter_3_Calendars_Away								arrayAdapter				= new Adapter_3_Calendars_Away(Global.actContext, R.id.List_View, Global.eRegCalendars.awayList);
		adapterViewList.setAdapter(arrayAdapter);
	}
	public void setListens()
	{
		((AdapterView <Adapter_3_Calendars_Away>) adapterView).setOnItemClickListener(this);
		panelView.findViewById(R.id.buttonAdd).setOnClickListener(this);
	}
	public void processFinishTCP(Ctrl__Abstract result) 
	{  
		super.processFinishTCP(result);
		if (result instanceof Ctrl_Calendars.Data)
		{
			Global.eRegConfiguration			 											= (Ctrl_Configuration.Data) result;
			displayHeader();
			displayContents();
	        setListens();
 		}
		else
		{
			Global.toaster("P5_Cals_Away : Data NOTNOTNOT received", true);
		}
	}
	public void onClick(View clickedView)
	{
		if (clickedView.getId() == R.id.buttonAdd)
		{
			Ctrl_Calendars.Away									itemNew						= new Ctrl_Calendars().new Away();
			
			itemNew.dateTimeStart															= Global.now();
			itemNew.dateTimeEnd																= Global.now() + 24 * 60 * 60 * 1000L;
			
			Global.eRegCalendars.awayList.add(itemNew);
			displayContents();
			setListens();
		}
	}
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
    	Ctrl_Calendars.Away										itemData					= Global.eRegCalendars.awayList.get(position);
		
    	Item_3_Calendars_Away									itemFragment				= new Item_3_Calendars_Away(itemData);
		
    	FragmentTransaction 									fTransaction 				= getActivity().getFragmentManager().beginTransaction();
   		fTransaction.replace(R.id.panel_container, itemFragment);
   		fTransaction.addToBackStack(null);
   		fTransaction.commit();
	}
}