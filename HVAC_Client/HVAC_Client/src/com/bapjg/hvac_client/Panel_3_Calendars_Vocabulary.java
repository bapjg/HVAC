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
	public Panel_3_Calendars_Vocabulary()
	{
		super();
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	this.container																		= container;
    	this.panelView																		= inflater.inflate(R.layout.panel_3_calendars, container, false);
        this.adapterView																	= (AdapterView) panelView.findViewById(R.id.List_View);
        
        
        
        
 
        if ((Global.eRegCalendars != null)
        &&  (Global.eRegCalendars.wordList != null))
        {
        	displayTitles("Calendars", "Words");
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
	public void displayContents()
	{
    	AdapterView <Panel_3_Calendars_Vocabulary_Adapter>					adapterViewList				= (AdapterView <Panel_3_Calendars_Vocabulary_Adapter>) adapterView;
		Panel_3_Calendars_Vocabulary_Adapter								arrayAdapter				= new Panel_3_Calendars_Vocabulary_Adapter(Global.actContext, R.id.List_View, Global.eRegCalendars.wordList);
//		adapterViewList.setAdapter(arrayAdapter);
		((AdapterView <Panel_3_Calendars_Vocabulary_Adapter>) adapterView).setAdapter(arrayAdapter);
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
			                                                                            
    	Panel_3_Calendars_Vocabulary_Item_New								itemFragment				= new Panel_3_Calendars_Vocabulary_Item_New(itemData);
							                                                            
    	FragmentTransaction 									fTransaction 				= getActivity().getFragmentManager().beginTransaction();
   		fTransaction.replace(R.id.panel_container, itemFragment);
   		fTransaction.addToBackStack(null);
   		fTransaction.commit();
   	}
}

