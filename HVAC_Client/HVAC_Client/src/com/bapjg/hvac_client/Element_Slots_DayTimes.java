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

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Element_Slots_DayTimes 									extends 					LinearLayout
																implements 					View.OnClickListener
{
	public LayoutInflater 										inflater;
	public TextView 											textName;
	
	public TextView 											day_1;
	public TextView 											day_2;
	public TextView 											day_3;
	public TextView 											day_4;
	public TextView 											day_5;
	public TextView 											day_6;
	public TextView 											day_7;
	
	public Panel_0_Interface									listener;
	
	public Element_Slots_DayTimes()
	{
		super(Global.actContext);
		this.inflater 																		= LayoutInflater.from(Global.actContext);
		inflater.inflate(R.layout.element_slots_daytimes, this, true);
		
      // Now get the space reserved for the day plan for a calendar entry (timeStart & timeEnd
		ViewGroup 												timeSlots					= (ViewGroup) findViewById(R.id.timeSlots);
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
	}
	public Element_Slots_DayTimes(Ctrl_Calendars.Calendar calendarItem) 
	{
		super(Global.actContext);
		this.inflater 																		= LayoutInflater.from(Global.actContext);
		inflater.inflate(R.layout.element_slots_weekdays, this, true);

		setData(calendarItem);
	}
	public void setData(Ctrl_Calendars.Calendar calendarItem)
	{
////    LayoutParams 											timeSlotLayout				= new LayoutParams(3, LayoutParams.MATCH_PARENT);	//Width=3
//    LinearLayout.LayoutParams 								timeSlotLayout				= new LinearLayout.LayoutParams(3, LayoutParams.MATCH_PARENT);	//Width=3
//    timeSlotLayout.weight = 1;
//    
        Long													quarterHour					= 15 * 60 * 1000L;
        Long													timeStart					= calendarItem.timeStart.milliSeconds;
        Long													timeEnd;
	    if (calendarItem.timeEnd == null)
	    {
	    	timeEnd																			= timeStart + quarterHour + quarterHour;		//just add 1/2 hour
	    }
	    else
	    {
	    	timeEnd																			= calendarItem.timeEnd.milliSeconds;
	    }
	
		ViewGroup 												timeSlots					= (ViewGroup) findViewById(R.id.timeSlots);
	
	    for (int i = 0; i < 96; i++)
	    {
	    	Long												slotStart					= i * quarterHour;
	    	Long												slotEnd						= slotStart + quarterHour;
	        TextView											timeSlot					= (TextView) timeSlots.getChildAt(i);
	        
	        if ((timeEnd 	> slotStart )
	        && 	(timeStart 	< slotEnd ) )
	        {
	        	timeSlot.setBackgroundColor(Color.RED);
	        }
	        else
	        {
	        	timeSlot.setBackgroundColor(Color.BLUE);
	        }
	    }
	}
	public void setListener(Panel_0_Interface listener)
	{
		this.listener																		= listener;
		textName	.setOnClickListener(this);
		day_1		.setOnClickListener(this);
		day_2		.setOnClickListener(this); 	
		day_3		.setOnClickListener(this);
		day_4		.setOnClickListener(this);
		day_5		.setOnClickListener(this);
		day_6		.setOnClickListener(this);
		day_7		.setOnClickListener(this);
	}
	public void onClick(View view)
	{
		listener.onElementClick((View) view);	// Return the actual view which was clicked
	}
}

