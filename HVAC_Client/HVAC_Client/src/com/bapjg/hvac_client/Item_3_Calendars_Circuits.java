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
public class Item_3_Calendars_Circuits 							extends 					Panel_0_Fragment
{		
	private Ctrl_Calendars.Calendar 							itemData;
	private View												itemView;
	private	String												daysWord;
	private	String												daysNumbers;
	private	String												daysWordNumbers;
	private String												circuitName;
	
	public Item_3_Calendars_Circuits(Ctrl_Calendars.Calendar itemData, String circuitName)
	{
		super();
		this.itemData																		= itemData;
		this.daysWord																		= "";
		this.daysNumbers																	= itemData.days; //name or days
		this.daysWordNumbers																= ""; //name or days
		this.circuitName																	= circuitName;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        this.itemView																		= inflater.inflate(R.layout.item_3_calendars_circuit, container, false);

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
		for (Ctrl_Calendars.Word word : Global.eRegCalendars.wordList)
		{
			if (word.name.indexOf(itemData.days) > -1)
			{
				daysWord																	= word.name;
				daysNumbers																	= "";
				daysWordNumbers																= word.days;
			}
		}

		TextView												days 						= (TextView) itemView.findViewById(R.id.days);
    	TextView 												day_1 						= (TextView) itemView.findViewById(R.id.day_1);
    	TextView 												day_2 						= (TextView) itemView.findViewById(R.id.day_2);
    	TextView 												day_3 						= (TextView) itemView.findViewById(R.id.day_3);
    	TextView 												day_4 						= (TextView) itemView.findViewById(R.id.day_4);
    	TextView 												day_5 						= (TextView) itemView.findViewById(R.id.day_5);
    	TextView 												day_6 						= (TextView) itemView.findViewById(R.id.day_6);
    	TextView 												day_7 						= (TextView) itemView.findViewById(R.id.day_7);

        ((TextView) itemView.findViewById(R.id.days))			.setText(daysWord);

    	if ((daysWordNumbers).indexOf("1") > -1)	day_1.setBackgroundColor(Color.RED); else day_1.setBackgroundColor(Color.BLUE);
        if ((daysWordNumbers).indexOf("2") > -1)	day_2.setBackgroundColor(Color.RED); else day_2.setBackgroundColor(Color.BLUE);
        if ((daysWordNumbers).indexOf("3") > -1)	day_3.setBackgroundColor(Color.RED); else day_3.setBackgroundColor(Color.BLUE);
        if ((daysWordNumbers).indexOf("4") > -1)	day_4.setBackgroundColor(Color.RED); else day_4.setBackgroundColor(Color.BLUE);
        if ((daysWordNumbers).indexOf("5") > -1)	day_5.setBackgroundColor(Color.RED); else day_5.setBackgroundColor(Color.BLUE);
        if ((daysWordNumbers).indexOf("6") > -1)	day_6.setBackgroundColor(Color.RED); else day_6.setBackgroundColor(Color.BLUE);
        if ((daysWordNumbers).indexOf("7") > -1)	day_7.setBackgroundColor(Color.RED); else day_7.setBackgroundColor(Color.BLUE);       

    	if ((daysNumbers).indexOf("1") > -1)		day_1.setBackgroundColor(Color.RED);
        if ((daysNumbers).indexOf("2") > -1)		day_2.setBackgroundColor(Color.RED);
        if ((daysNumbers).indexOf("3") > -1)		day_3.setBackgroundColor(Color.RED);
        if ((daysNumbers).indexOf("4") > -1)		day_4.setBackgroundColor(Color.RED);
        if ((daysNumbers).indexOf("5") > -1)		day_5.setBackgroundColor(Color.RED);
        if ((daysNumbers).indexOf("6") > -1)		day_6.setBackgroundColor(Color.RED);
        if ((daysNumbers).indexOf("7") > -1)		day_7.setBackgroundColor(Color.RED);      

        ((TextView) itemView.findViewById(R.id.timeStart))			.setText(itemData.timeStart.displayShort());
        ((TextView) itemView.findViewById(R.id.timeEnd))			.setText(itemData.timeEnd.displayShort());
        
        ((TextView) itemView.findViewById(R.id.tempObjective))		.setText(itemData.tempObjective.displayInteger());
        ((CheckBox) itemView.findViewById(R.id.stopOnObjective))	.setChecked(itemData.stopOnObjective);
 	}
	public void setListens()
	{
		itemView.findViewById(R.id.buttonOk)		.setOnClickListener(this);
		itemView.findViewById(R.id.buttonDelete)	.setOnClickListener(this);
	    itemView.findViewById(R.id.days)			.setOnClickListener(this);
	    itemView.findViewById(R.id.day_1)			.setOnClickListener(this);
	    itemView.findViewById(R.id.day_2)			.setOnClickListener(this);
	    itemView.findViewById(R.id.day_3)			.setOnClickListener(this);
	    itemView.findViewById(R.id.day_4)			.setOnClickListener(this);
	    itemView.findViewById(R.id.day_5)			.setOnClickListener(this);
	    itemView.findViewById(R.id.day_6)			.setOnClickListener(this);
	    itemView.findViewById(R.id.day_7)			.setOnClickListener(this);
	    itemView.findViewById(R.id.timeStart)		.setOnClickListener(this);
	    itemView.findViewById(R.id.timeEnd)			.setOnClickListener(this);
	    itemView.findViewById(R.id.tempObjective)	.setOnClickListener(this);
	    itemView.findViewById(R.id.stopOnObjective)	.setOnClickListener(this);
	}
    @Override
	public void onClick(View clickedView) 
	{
     	if (clickedView.getId() == R.id.buttonOk)
    	{
    		itemData.days																	= daysWord;				// ((EditText) itemView.findViewById(R.id.name)).getText().toString();
       		itemData.days																	= itemData.days + daysNumbers;
//       		itemData.timeStart																= ((TextView) itemView.findViewById(R.id.timeStart)).getText().toString();
//       		itemData.timeEnd																= ((TextView) itemView.findViewById(R.id.timeEnd)).getText().toString();
       		
// TODO Check functions ok
       		
//       		String												tempObjective				= ((TextView) itemView.findViewById(R.id.tempObjective)).getText().toString();
//       		tempObjective																	= tempObjective.replace(" °C", "");
//       		itemData.tempObjective															= (Integer) (Integer.parseInt(tempObjective) * 1000);
       		itemData.stopOnObjective														= ((CheckBox) itemView.findViewById(R.id.stopOnObjective)).isChecked();
       	    getFragmentManager().popBackStackImmediate();
    	}
     	else if (clickedView.getId() == R.id.buttonDelete)
    	{
     		Global.eRegCalendars.fetchCircuit(circuitName).calendarList.remove(itemData);
     		getFragmentManager().popBackStackImmediate();
    	}
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
     	else if (clickedView.getId() == R.id.days)
    	{
//    		Dialog_String_List		 							df 							= new Dialog_String_List(this, R.id.days);
    		Dialog_String_List_New		 							df 							= new Dialog_String_List_New(itemData.days, (Object) itemData, null, this);
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
     		Dialog_Time_New	 									df 							= new Dialog_Time_New(itemData.timeStart, this);
    		df.show(getFragmentManager(), "Dialog_Time");
    	}
     	else if (clickedView.getId() == R.id.timeEnd)
    	{
     		Dialog_Time_New										df 							= new Dialog_Time_New(itemData.timeEnd, this);
    		df.show(getFragmentManager(), "Dialog_Time");
    	}
     	else if (clickedView.getId() == R.id.tempObjective)
    	{
    		Dialog_Temperature 									df 							= new Dialog_Temperature(itemData.tempObjective, 25, 40, this);
    		df.show(getFragmentManager(), "Dialog_Temperature");
    	}
     	else if (clickedView.getId() == R.id.stopOnObjective)
    	{
     		((CheckBox) itemView.findViewById(R.id.stopOnObjective)).isChecked();
    		displayContents();
    	}
	}
    public void onReturnString(int fieldId, String value)
    {
    	if (fieldId == R.id.days)
    	{
    		itemData.days																	= value;
    		((TextView) itemView.findViewById(fieldId))			.setText(value);
    	}
    }
    public void onDialogReturn()
    {
        displayHeader();
        displayContents();
    }
}

