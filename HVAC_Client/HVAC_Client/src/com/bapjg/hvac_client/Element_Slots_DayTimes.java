package com.bapjg.hvac_client;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import HVAC_Common.*;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Element_Slots_DayTimes 							extends 					LinearLayout
																implements 					View.OnClickListener
{
	public LayoutInflater 										inflater;
	public Element_Interface									listener;
	
	private	ViewGroup 											timeSlots;
	private	TextView 											timeStart;
	private	TextView 											timeEnd;
	private	TextView 											tempObjective;
	
	public Element_Slots_DayTimes()
	{
		super(Global.actContext);
		this.inflater 																		= LayoutInflater.from(Global.actContext);
		inflater.inflate(R.layout.element_slots_daytimes, this, true);
		
      // Now get the space reserved for the day plan for a calendar entry (timeStart & timeEnd
		this.timeSlots																		= (ViewGroup) findViewById(R.id.timeSlots);
//		LayoutParams 											timeSlotLayout				= new LayoutParams(3, LayoutParams.MATCH_PARENT);	//Width=3
		LinearLayout.LayoutParams 								timeSlotLayout				= new LinearLayout.LayoutParams(3, LayoutParams.MATCH_PARENT);	//Width=3
		timeSlotLayout.weight = 1;
//      
		Long													quarterHour					= 15 * 60 * 1000L;
	
	  	
	  	for (int i = 0; i < 96; i++)
		{
			TextView											timeSlot					= new TextView(getContext());
			timeSlot.setText			("");
			timeSlot.setLayoutParams	(timeSlotLayout);
			  

			timeSlots.addView(timeSlot);
		}
	  	timeStart 																			= (TextView) findViewById(R.id.timeStart);
	  	timeEnd  																			= (TextView) findViewById(R.id.timeEnd);
	  	tempObjective  																		= (TextView) findViewById(R.id.tempObjective);
	}
	public void setData(Ctrl_Calendars.Calendar calendarItem)
	{
        Long													quarterHour					= 15 * 60 * 1000L;
        Long													calendarTimeStart			= calendarItem.timeStart.milliSeconds;
        Long													calendarTimeEnd;
	    if (calendarItem.timeEnd == null)
	    {
	    	calendarTimeEnd																	= calendarTimeStart + quarterHour + quarterHour;		//just add 1/2 hour
	    }
	    else
	    {
	    	calendarTimeEnd																	= calendarItem.timeEnd.milliSeconds;
	    }
	
	    for (int i = 0; i < 96; i++)
	    {
	    	Long												slotStart					= i * quarterHour;
	    	Long												slotEnd						= slotStart + quarterHour;
	        TextView											timeSlot					= (TextView) timeSlots.getChildAt(i);
	        
	        if ((calendarTimeEnd 	> slotStart )
	        && 	(calendarTimeStart 	< slotEnd ) )
	        {
	        	timeSlot.setBackgroundColor(Color.RED);
	        }
	        else
	        {
	        	timeSlot.setBackgroundColor(Color.BLUE);
	        }
	    }
	    timeStart				.setText(calendarItem.timeStart.displayShort());
	    timeEnd					.setText(calendarItem.timeEnd.displayShort());
	    tempObjective			.setText(calendarItem.tempObjective.displayDecimal());
	}
	public void setListener(Element_Interface listener)
	{
		this.listener																		= listener;
	}
	public void onClick(View view)
	{
		listener.onElementClick((View) view);	// Return the actual view which was clicked
	}
}

