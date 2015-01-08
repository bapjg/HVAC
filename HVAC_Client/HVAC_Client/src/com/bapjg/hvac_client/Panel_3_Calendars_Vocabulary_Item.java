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

public class Panel_3_Calendars_Vocabulary_Item 				extends 					Panel_0_Fragment
{		
	private Ctrl_Calendars.Word 								itemData;
	private	Element_WeekDays									days;
	//	private ViewGroup											itemView;
	
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

    	days	= new Element_WeekDays();
    	
    	panelInsertPoint.addView(days);
    	
        displayContents();
        setListens();
        
        return panelView;
    }
	public void displayContents()
	{
		days.setData(itemData.name, itemData.days);
		
		
//		((EditText) itemView.findViewById(R.id.name)).setText(itemData.name);
//		
//    	TextView 												day_1 						= (TextView) itemView.findViewById(R.id.day_1);
//    	TextView 												day_2 						= (TextView) itemView.findViewById(R.id.day_2);
//    	TextView 												day_3 						= (TextView) itemView.findViewById(R.id.day_3);
//    	TextView 												day_4 						= (TextView) itemView.findViewById(R.id.day_4);
//    	TextView 												day_5 						= (TextView) itemView.findViewById(R.id.day_5);
//    	TextView 												day_6 						= (TextView) itemView.findViewById(R.id.day_6);
//    	TextView 												day_7 						= (TextView) itemView.findViewById(R.id.day_7);
//
//        if ((itemData.days).indexOf("1") > -1)	day_1.setBackgroundColor(Color.RED); else day_1.setBackgroundColor(Color.BLUE);
//        if ((itemData.days).indexOf("2") > -1)	day_2.setBackgroundColor(Color.RED); else day_2.setBackgroundColor(Color.BLUE);
//        if ((itemData.days).indexOf("3") > -1)	day_3.setBackgroundColor(Color.RED); else day_3.setBackgroundColor(Color.BLUE);
//        if ((itemData.days).indexOf("4") > -1)	day_4.setBackgroundColor(Color.RED); else day_4.setBackgroundColor(Color.BLUE);
//        if ((itemData.days).indexOf("5") > -1)	day_5.setBackgroundColor(Color.RED); else day_5.setBackgroundColor(Color.BLUE);
//        if ((itemData.days).indexOf("6") > -1)	day_6.setBackgroundColor(Color.RED); else day_6.setBackgroundColor(Color.BLUE);
//        if ((itemData.days).indexOf("7") > -1)	day_7.setBackgroundColor(Color.RED); else day_7.setBackgroundColor(Color.BLUE);
	}
	public void setListens()
	{
		panelView.findViewById(R.id.buttonOk).setOnClickListener(this);
		panelView.findViewById(R.id.buttonDelete).setOnClickListener(this);
		days.setListener(this);
//	    itemView.findViewById(R.id.day_2).setOnClickListener(this);
//	    itemView.findViewById(R.id.day_3).setOnClickListener(this);
//	    itemView.findViewById(R.id.day_4).setOnClickListener(this);
//	    itemView.findViewById(R.id.day_5).setOnClickListener(this);
//	    itemView.findViewById(R.id.day_6).setOnClickListener(this);
//	    itemView.findViewById(R.id.day_7).setOnClickListener(this);
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
//     		itemData.name																	= ((EditText) itemView.findViewById(R.id.name)).getText().toString();
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

