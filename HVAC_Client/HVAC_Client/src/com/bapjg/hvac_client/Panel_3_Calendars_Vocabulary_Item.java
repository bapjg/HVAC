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
//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------

public class Panel_3_Calendars_Vocabulary_Item 					extends 					Panel_0_Fragment
{		
	private Ctrl_Calendars.Word 								itemData;
	private	Element_Slots_WeekDays								days;
	
	// TODO
	// TODO Rework button close
	// TODO
	
	public Panel_3_Calendars_Vocabulary_Item(Ctrl_Calendars.Word itemData)
	{
		super("Ok_Delete");
		this.itemData																		= itemData;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);

    	displayTitles("Calendar", "Word");

    	days	= new Element_Slots_WeekDays();
    	
    	panelInsertPoint.addView(days);
    	
        displayContents();
        setListens();
        
        return panelView;
    }
	public void displayContents()
	{
		days.setData(itemData.name, itemData.days);
	}
	public void setListens()
	{
		days.setListener(this);
	}
	public void onElementClick(View clickedView) 
	{
		if (clickedView == days.textName)
		{
	    	Dialog_Text												dialogText;
			dialogText 																	= new Dialog_Text(itemData.name, itemData,  "Choose new Name", this);
	     	dialogText.show(getFragmentManager(), "Vocab Name");
		}
		else
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
		}
		displayContents();
	}
	@Override
	public void onPanelButtonOk() 
	{
      		getFragmentManager().popBackStackImmediate();
    }
    public void onPanelButtonDelete()
	{
 		Global.eRegCalendars.wordList.remove(itemData);
 		getFragmentManager().popBackStackImmediate();
	}
    public void onDialogReturn()
    {
    	displayContents();
    }
}

