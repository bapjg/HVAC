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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Panel_3_Calendars_Circuits_Item_WORK 				extends 					Panel_0_Fragment
{		
	private Ctrl_Calendars.Calendar 							itemData;
	private	String												daysWord;
	private	String												daysNumbers;
	private	String												daysWordNumbers;
	private String												circuitName;
	
	private	Element_Slots_WeekDays									weekDays;
	private	Element_Standard									timeStart;
	private	Element_Standard									timeEnd;
	private	Element_Standard									tempObjective;
	private	Element_CheckBox									stopOnObjective;
	
	
	public Panel_3_Calendars_Circuits_Item_WORK(Ctrl_Calendars.Calendar itemData, String circuitName)
	{
		super("Ok_Delete");
		this.itemData																		= itemData;

//		this.daysWord																		= "";
//		this.daysNumbers																	= itemData.days; //name or days
//		this.daysWordNumbers																= ""; //name or days
		this.circuitName																	= circuitName;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	super.panelInitialise(inflater, container, savedInstanceState);
//        this.panelView																		= inflater.inflate(R.layout.item_3_calendars_circuit, container, false);

    	displayTitles("Calendar", circuitName);
    	
    	weekDays																			= new Element_Slots_WeekDays();
    	timeStart																			= new Element_Standard("Time Start");
    	timeEnd																				= new Element_Standard("Time End");
    	tempObjective																		= new Element_Standard("Temperature Objective");
    	stopOnObjective																		= new Element_CheckBox("Stop on Objective");
    	
    	panelInsertPoint.addView(weekDays);
    	panelInsertPoint.addView(timeStart);
    	panelInsertPoint.addView(timeEnd);
    	panelInsertPoint.addView(tempObjective);
    	panelInsertPoint.addView(stopOnObjective);
    	

        displayContents();
        setListens();
        
        return panelView;
    }
	public void displayContents()
	{
		weekDays						.setData		(itemData.days, Global.eRegCalendars.fetchDays(itemData.days));
		timeStart						.setValue		(itemData.timeStart);
		timeEnd							.setValue		(itemData.timeEnd);
		tempObjective					.setValue		(itemData.tempObjective);
		stopOnObjective					.setChecked		(itemData.stopOnObjective);
		
 	}
	public void setListens()
	{
//		panelView.findViewById(R.id.buttonOk)		.setOnClickListener(this);
//		panelView.findViewById(R.id.buttonDelete)	.setOnClickListener(this);
//		panelView.findViewById(R.id.days)			.setOnClickListener(this);
	}
    @Override
	public void onClick(View clickedView) 
	{
     	//TODO Cleanup
    	
    	
    	if (clickedView.getId() == R.id.buttonOk)
    	{
    		itemData.days																	= daysWord;				// ((EditText) itemView.findViewById(R.id.name)).getText().toString();
       		itemData.days																	= itemData.days + daysNumbers;
       		itemData.stopOnObjective														= ((CheckBox) panelView.findViewById(R.id.stopOnObjective)).isChecked();
       	    getFragmentManager().popBackStackImmediate();
    	}
     	else if (clickedView.getId() == R.id.buttonDelete)
    	{
     		Global.eRegCalendars.fetchCircuit(circuitName).calendarList.remove(itemData);
     		getFragmentManager().popBackStackImmediate();
    	}
     	else if (clickedView.getId() == R.id.days)
    	{
    		Dialog_String_List		 							df 							= new Dialog_String_List(itemData.days, (Object) itemData, null, this);
    		df.items.add("Select days");
    		df.itemSelected																	= "";

    		for (Ctrl_Calendars.Word word : Global.eRegCalendars.wordList)
    		{
    			if(word.name.equalsIgnoreCase(daysWord))
    			{
    				df.itemSelected															= daysWord;
    			}
    			df.items.add(word.name);
    		}
    		df.show(getFragmentManager(), "Dialog_List");
    	}
    	else if ((clickedView.getId() == R.id.day_1)
    	||		 (clickedView.getId() == R.id.day_2)
    	||		 (clickedView.getId() == R.id.day_3)
    	||		 (clickedView.getId() == R.id.day_4)
    	||		 (clickedView.getId() == R.id.day_5)
    	||		 (clickedView.getId() == R.id.day_6)
    	||		 (clickedView.getId() == R.id.day_7)   )
    	{
    		String day = ((TextView) clickedView).getText().toString();
    		if ((daysNumbers).indexOf(day) > -1)
    		{
    			daysNumbers = daysNumbers.replace(day,"");
    		}
    		else
    		{
    			daysNumbers = daysNumbers + day;
    		}
    		displayContents();
    	}
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
     	else if (clickedView.getId() == R.id.timeStart)
    	{
     		Dialog_Time	 									df 							= new Dialog_Time(itemData.timeStart, this);
    		df.show(getFragmentManager(), "Dialog_Time");
    	}
     	else if (clickedView.getId() == R.id.timeEnd)
    	{
     		Dialog_Time										df 							= new Dialog_Time(itemData.timeEnd, this);
    		df.show(getFragmentManager(), "Dialog_Time");
    	}
     	else if (clickedView.getId() == R.id.tempObjective)
    	{
    		Dialog_Temperature 									df 							= new Dialog_Temperature(itemData.tempObjective, 25, 40, this);
    		df.show(getFragmentManager(), "Dialog_Temperature");
    	}
     	else if (clickedView.getId() == R.id.stopOnObjective)
    	{
     		((CheckBox) panelView.findViewById(R.id.stopOnObjective)).isChecked();
    		displayContents();
    	}
	}
    public void onReturnString(int fieldId, String value)
    {
    	if (fieldId == R.id.days)
    	{
    		itemData.days																	= value;
    		((TextView) panelView.findViewById(fieldId))			.setText(value);
    	}
    }
    public void onDialogReturn()
    {
        displayContents();
    }
}

