package com.bapjg.hvac_client;

import HVAC_Common.*;
import HVAC_Common.Ctrl_Calendars.Word;
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
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("ValidFragment")
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------

public class Item_3_Calendars_Vocabulary 						extends 					Panel_0_Fragment
{		
	private Ctrl_Calendars.Word 								itemData;
	private Ctrl_Calendars.Word 								itemDataWork;
	private ViewGroup											itemView;
	
	// TODO
	// TODO Rework button close
	// TODO
	
	public Item_3_Calendars_Vocabulary(Ctrl_Calendars.Word itemData)
	{
		super();
		this.itemData																		= itemData;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        View 													itemView					= inflater.inflate(R.layout.item_3_calendars_word, container, false);
        this.itemView																		= (ViewGroup) itemView;

        displayHeader();
        displayContents();
        setListens();
        
        return itemView;
    }
	public void displayHeader()
	{
	}
	public void displayContents()
	{
		
		((EditText) itemView.findViewById(R.id.name)).setText(itemData.name);
		
    	TextView 												day_1 						= (TextView) itemView.findViewById(R.id.day_1);
    	TextView 												day_2 						= (TextView) itemView.findViewById(R.id.day_2);
    	TextView 												day_3 						= (TextView) itemView.findViewById(R.id.day_3);
    	TextView 												day_4 						= (TextView) itemView.findViewById(R.id.day_4);
    	TextView 												day_5 						= (TextView) itemView.findViewById(R.id.day_5);
    	TextView 												day_6 						= (TextView) itemView.findViewById(R.id.day_6);
    	TextView 												day_7 						= (TextView) itemView.findViewById(R.id.day_7);

        if ((itemData.days).indexOf("1") > -1)	day_1.setBackgroundColor(Color.RED); else day_1.setBackgroundColor(Color.BLUE);
        if ((itemData.days).indexOf("2") > -1)	day_2.setBackgroundColor(Color.RED); else day_2.setBackgroundColor(Color.BLUE);
        if ((itemData.days).indexOf("3") > -1)	day_3.setBackgroundColor(Color.RED); else day_3.setBackgroundColor(Color.BLUE);
        if ((itemData.days).indexOf("4") > -1)	day_4.setBackgroundColor(Color.RED); else day_4.setBackgroundColor(Color.BLUE);
        if ((itemData.days).indexOf("5") > -1)	day_5.setBackgroundColor(Color.RED); else day_5.setBackgroundColor(Color.BLUE);
        if ((itemData.days).indexOf("6") > -1)	day_6.setBackgroundColor(Color.RED); else day_6.setBackgroundColor(Color.BLUE);
        if ((itemData.days).indexOf("7") > -1)	day_7.setBackgroundColor(Color.RED); else day_7.setBackgroundColor(Color.BLUE);
	}
	public void setListens()
	{
		itemView.findViewById(R.id.buttonOk).setOnClickListener(this);
		itemView.findViewById(R.id.buttonDelete).setOnClickListener(this);
	    itemView.findViewById(R.id.day_1).setOnClickListener(this);
	    itemView.findViewById(R.id.day_2).setOnClickListener(this);
	    itemView.findViewById(R.id.day_3).setOnClickListener(this);
	    itemView.findViewById(R.id.day_4).setOnClickListener(this);
	    itemView.findViewById(R.id.day_5).setOnClickListener(this);
	    itemView.findViewById(R.id.day_6).setOnClickListener(this);
	    itemView.findViewById(R.id.day_7).setOnClickListener(this);
	}
    @Override
	public void onClick(View clickedView) 
	{
     	if (clickedView.getId() == R.id.buttonOk)
    	{
     		itemData.name																	= ((EditText) itemView.findViewById(R.id.name)).getText().toString();
      		getFragmentManager().popBackStackImmediate();
    	}
     	else if (clickedView.getId() == R.id.buttonDelete)
    	{
     		Global.eRegCalendars.wordList.remove(itemData);
     		getFragmentManager().popBackStackImmediate();
    	}
    	else if ((clickedView.getId() == R.id.day_1)
    	||		 (clickedView.getId() == R.id.day_2)
    	||		 (clickedView.getId() == R.id.day_3)
    	||		 (clickedView.getId() == R.id.day_4)
    	||		 (clickedView.getId() == R.id.day_5)
    	||		 (clickedView.getId() == R.id.day_6)
    	||		 (clickedView.getId() == R.id.day_7)
    	||       (clickedView instanceof TextView)     )
    	{
    		String day = ((TextView) clickedView).getText().toString();
    		if ((itemData.days).indexOf(day) > -1)
    		{
    			itemData.days = itemData.days.replace(day,"");								// Remove the day from the list
    		}
    		else
    		{
    			itemData.days = itemData.days + day;										// Add the day from the list
    		}
    		itemData.name 																	= ((EditText) itemView.findViewById(R.id.name)).getText().toString();		//It may have changed
    		displayContents();
    	}
	}
}

