package com.bapjg.hvac_client;

import HVAC_Messages.*;
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
//Template												NEWNEWNEW					= NEWNEWNEW
//Template												variable					= something
//Template												ext/imp						class
public class Item_3_Calendars_Vocabulary 				extends 					Panel_0_Fragment
{		
//	private LayoutInflater								myInflater;
	private Activity									myActivity;
	private ViewGroup									myContainer;
	private FragmentManager								myFragmentManager;
	private Ctrl_Calendars.Word 						myItemData;
	private ViewGroup									myItemView;
	
	public Item_3_Calendars_Vocabulary(Ctrl_Calendars.Word itemData)
	{
		super();
		myItemData										= itemData;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        // Inflate the layout for this fragment
        myContainer 																= container;
        myActivity																	= getActivity();
        myFragmentManager 															= myActivity.getFragmentManager();
        View 											itemView					= inflater.inflate(R.layout.item_3_calendars_word, container, false);
        myItemView																	= (ViewGroup) itemView;

        displayHeader();
        displayContents();
	
        ((Button) itemView.findViewById(R.id.ok)).setOnClickListener(this);
        ((TextView) itemView.findViewById(R.id.day_1)).setOnClickListener(this);
        ((TextView) itemView.findViewById(R.id.day_2)).setOnClickListener(this);
        ((TextView) itemView.findViewById(R.id.day_3)).setOnClickListener(this);
        ((TextView) itemView.findViewById(R.id.day_4)).setOnClickListener(this);
        ((TextView) itemView.findViewById(R.id.day_5)).setOnClickListener(this);
        ((TextView) itemView.findViewById(R.id.day_6)).setOnClickListener(this);
        ((TextView) itemView.findViewById(R.id.day_7)).setOnClickListener(this);
        
        return itemView;
     }
    @Override
	public void onClick(View myView) 
	{
    	Log.v("App", "We have arrived in onClick/itemView");
    	
    	if (myView.getId() == R.id.ok)
    	{
    		myItemData.name															= ((EditText) myItemView.findViewById(R.id.name)).getText().toString();
    		System.out.println("name " + myItemData.name);
    		Log.v  ("TAG", "name " + myItemData.name);
    		getFragmentManager().popBackStackImmediate();
    	}
    	else if ((myView.getId() == R.id.day_1)
    	||		 (myView.getId() == R.id.day_2)
    	||		 (myView.getId() == R.id.day_3)
    	||		 (myView.getId() == R.id.day_4)
    	||		 (myView.getId() == R.id.day_5)
    	||		 (myView.getId() == R.id.day_6)
    	||		 (myView.getId() == R.id.day_7)
    	||       (myView instanceof TextView)     )
    	{
    		String day = ((TextView) myView).getText().toString();
    		if ((myItemData.days).indexOf(day) > -1)
    		{
    			myItemData.days = myItemData.days.replace(day,"");
    		}
    		else
    		{
    			myItemData.days = myItemData.days + day;
    		}
    		displayContents();
    	}

	}
	public void displayHeader()
	{
//		((TextView) myContainer.findViewById(R.id.name)).setText("Vocabulary");	
	}
	public void displayContents()
	{
		EditText et = (EditText) myItemView.findViewById(R.id.name);
		String ev = myItemData.name;
		
		((EditText) myItemView.findViewById(R.id.name)).setText(myItemData.name);
		
    	TextView 										day_1 						= (TextView) myItemView.findViewById(R.id.day_1);
    	TextView 										day_2 						= (TextView) myItemView.findViewById(R.id.day_2);
    	TextView 										day_3 						= (TextView) myItemView.findViewById(R.id.day_3);
    	TextView 										day_4 						= (TextView) myItemView.findViewById(R.id.day_4);
    	TextView 										day_5 						= (TextView) myItemView.findViewById(R.id.day_5);
    	TextView 										day_6 						= (TextView) myItemView.findViewById(R.id.day_6);
    	TextView 										day_7 						= (TextView) myItemView.findViewById(R.id.day_7);

        if ((myItemData.days).indexOf("1") > -1)	day_1.setBackgroundColor(Color.RED); else day_1.setBackgroundColor(Color.BLUE);
        if ((myItemData.days).indexOf("2") > -1)	day_2.setBackgroundColor(Color.RED); else day_2.setBackgroundColor(Color.BLUE);
        if ((myItemData.days).indexOf("3") > -1)	day_3.setBackgroundColor(Color.RED); else day_3.setBackgroundColor(Color.BLUE);
        if ((myItemData.days).indexOf("4") > -1)	day_4.setBackgroundColor(Color.RED); else day_4.setBackgroundColor(Color.BLUE);
        if ((myItemData.days).indexOf("5") > -1)	day_5.setBackgroundColor(Color.RED); else day_5.setBackgroundColor(Color.BLUE);
        if ((myItemData.days).indexOf("6") > -1)	day_6.setBackgroundColor(Color.RED); else day_6.setBackgroundColor(Color.BLUE);
        if ((myItemData.days).indexOf("7") > -1)	day_7.setBackgroundColor(Color.RED); else day_7.setBackgroundColor(Color.BLUE);
	}
}

