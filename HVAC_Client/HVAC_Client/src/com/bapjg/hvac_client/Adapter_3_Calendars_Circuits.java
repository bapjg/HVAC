package com.bapjg.hvac_client;

import java.util.ArrayList;
import java.util.List;

import com.bapjg.hvac_client.Adapter_5_Configuration_Relays.RowHolder;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;

import HVAC_Messages.*;

public class Adapter_3_Calendars_Circuits 				extends 					Adapter_0_Abstract
{
 
    public Adapter_3_Calendars_Circuits(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
    }
    @Override
    public View getView(int position, View adapterView, ViewGroup parent) 
    {
    	RowHolder 										row							= new RowHolder();
    	Ctrl_Calendars.Calendar							listItem					= (Ctrl_Calendars.Calendar) listData.get(position);

    	adapterView 																= inflater.inflate(R.layout.row_3_calendar_circuit, null);
     	row.days																	= (TextView) adapterView.findViewById(R.id.days);
     	row.timeStart																= (TextView) adapterView.findViewById(R.id.timeStart);
    	row.timeEnd																	= (TextView) adapterView.findViewById(R.id.timeEnd);
    	row.tempObjective															= (TextView) adapterView.findViewById(R.id.tempObjective);

    	// Update the entries using standard ArrayAdapter technique
    	Integer									tempObjective						= listItem.tempObjective/1000;
      	row.tempObjective.setText				(tempObjective.toString() + " °C");
    	row.days.setText						(listItem.days);
        row.timeStart.setText					(listItem.timeStart);
        row.timeEnd.setText						(listItem.timeEnd);

    	if (position % 2 == 0)
    	{
    		adapterView.findViewById(R.id.row).setBackgroundColor(0x800000ff); //  BLUE =  (0xff0000ff) (first byte = intensity)
    	}
        // Now handle the non-standard stuff
     	// Replace word in listItem.days be their corresponding day numbers from Vocabulary
    	String											days						= listItem.days;
    	
    	for (Ctrl_Calendars.Word 						word 						: Global.eRegCalendars.wordList)
    	{
    		days																	= days.replace(word.name, word.days);
    	}
    	// Get the space reserved for days of week, create and colour the texts view (Red=Used, Blue=Unused
        ViewGroup 										daySlots					= (ViewGroup) adapterView.findViewById(R.id.daySlots);
        LayoutParams 									daySlotLayout				= new LayoutParams(40, LayoutParams.MATCH_PARENT);	//Width=20
        TextView										daySlot;
    	
    	for (Integer i = 1; i < 8; i++)	//Monday to Sunday
    	{
            daySlot																	= new TextView(getContext());
            daySlot.setText								(i.toString());
            daySlot.setTextColor						(Color.YELLOW);
            daySlot.setTextSize							(20);
            daySlot.setLayoutParams						(daySlotLayout);
            daySlot.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            
            if (days.contains(i.toString()))
            {
            	daySlot.setBackgroundColor(Color.RED);
            }
            else
            {
            	daySlot.setBackgroundColor(Color.BLUE);
            }
            daySlots.addView(daySlot);
            // Add seperator between Friday and Saturday
            if (i == 5)  
            {
                daySlot																= new TextView(getContext());
                daySlot.setLayoutParams					(daySlotLayout);
                daySlots.addView(daySlot);
            }
    	}
       
        // Now get the space reserved for the day plan for a calendar entry (timeStart & timeEnd
        ViewGroup 										timeSlots					= (ViewGroup) adapterView.findViewById(R.id.timeSlots);
        LayoutParams 									timeSlotLayout				= new LayoutParams(3, LayoutParams.MATCH_PARENT);	//Width=3
        
        Long											QuarterHour					= 15 * 60 * 1000L;
    	Long											timeStart					= Global.parseTime(listItem.timeStart);
    	Long											timeEnd;
        if (listItem.timeEnd == null)
        {
        	timeEnd																	= timeStart + QuarterHour + QuarterHour;		//just add 1/2 hour
        }
        else
        {
        	timeEnd																	= Global.parseTime(listItem.timeEnd);
        }
    	
    	
    	for (int i = 0; i < 96; i++)
        {
        	Long										slotStart					= i * QuarterHour;
        	Long										slotEnd						= slotStart + QuarterHour;
            TextView									timeSlot					= new TextView(getContext());
            timeSlot.setText							("");
            timeSlot.setLayoutParams					(timeSlotLayout);
            
            if ((timeEnd 	> slotStart )
            && 	(timeStart 	< slotEnd ) )
            {
            	timeSlot.setBackgroundColor(Color.RED);
            }
            else
            {
            	timeSlot.setBackgroundColor(Color.BLUE);
            }
            timeSlots.addView(timeSlot);
        }
        adapterView.setTag(row);
        return adapterView;
    }
    static class RowHolder 
    {
    	TextView 										days;
    	TextView 										timeStart;
    	TextView 										timeEnd;
    	TextView 										tempObjective;
    }
}