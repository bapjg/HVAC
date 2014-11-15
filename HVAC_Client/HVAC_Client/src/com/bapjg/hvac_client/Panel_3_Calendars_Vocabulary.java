package com.bapjg.hvac_client;

import HVAC_Common.*;
import HVAC_Common.Ctrl_Configuration.Request;
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
import android.widget.Button;
import android.widget.TextView;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Panel_3_Calendars_Vocabulary 						extends 					Panel_0_Fragment
{		
	private View												panelView;					// This corresponds to the inflated panel (R.layout.panel_n_xxxxxx)
	private View												adapterView;				// This corresponds to the inflated list view within the panel view (R.id.List_View)
	
	public Panel_3_Calendars_Vocabulary()
	{
		super();
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        // Inflate the layout for this fragment
        this.panelView																		= inflater.inflate(R.layout.panel_3_calendars, container, false);
        this.adapterView																	= (AdapterView) panelView.findViewById(R.id.List_View);
 
        if ((Global.eRegCalendars != null)
        &&  (Global.eRegCalendars.wordList != null))
        {
        	displayHeader();
        	displayContents();
            setListens();
        }
        else // we need to reconnect to the server
        {
        	Global.toaster("Please refresh", true);
        }
        
        return panelView;
      }
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
	public void displayHeader()
	{
		((TextView) panelView.findViewById(R.id.title)).setText		("Calendars");
		((TextView) panelView.findViewById(R.id.subTitle)).setText	("Words");
//        TextView 												subTitle					= (TextView) panelView.findViewById(R.id.subTitle);
//        subTitle.setText("Vocabulary");		
	}
	public void displayContents()
	{
    	AdapterView <Adapter_3_Calendars_Words>					adapterViewList				= (AdapterView <Adapter_3_Calendars_Words>) adapterView;
		Adapter_3_Calendars_Words								arrayAdapter				= new Adapter_3_Calendars_Words(Global.actContext, R.id.List_View, Global.eRegCalendars.wordList);
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
			Ctrl_Calendars.Word									itemNew						= new Ctrl_Calendars().new Word();
			itemNew.name																	= "new";
			itemNew.days																	= "";
			Global.eRegCalendars.wordList.add(itemNew);
			displayContents();
			setListens();
		}
	}
	@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
    	Ctrl_Calendars.Word										itemData					= Global.eRegCalendars.wordList.get(position);
			                                                                            
    	Item_3_Calendars_Vocabulary								itemFragment				= new Item_3_Calendars_Vocabulary(itemData);
							                                                            
    	FragmentTransaction 									fTransaction 				= getActivity().getFragmentManager().beginTransaction();
   		fTransaction.replace(R.id.panel_container, itemFragment);
   		fTransaction.addToBackStack(null);
   		fTransaction.commit();
   	}
}

