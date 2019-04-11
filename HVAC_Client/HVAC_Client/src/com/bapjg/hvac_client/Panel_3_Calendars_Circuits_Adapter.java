package com.bapjg.hvac_client;

import java.util.ArrayList;
import java.util.List;

// import com.bapjg.hvac_client.Adapter_5_Configuration_Relays.RowHolder;



import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.*;
import android.widget.*;
import HVAC_Common.*;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Panel_3_Calendars_Circuits_Adapter 				extends 					Panel_0_Adapter
{
    public Panel_3_Calendars_Circuits_Adapter(Context context, int resource, ArrayList listData) 
    {
        super(context, resource, listData);
    }
    @Override
    public View getView(int position, View adapterView, ViewGroup parent) 
    {
    	Ctrl_Calendars.Calendar									listItem					= (Ctrl_Calendars.Calendar) listData.get(position);

    	Element_Linear_Vertical									adapterElement 				= new Element_Linear_Vertical();
    	Element_Slots_WeekDays									slotsweekDays				= new Element_Slots_WeekDays();
    	Element_Slots_DayTimes									slotsDayTimes 				= new Element_Slots_DayTimes();

    	adapterElement.insertPoint.addView(slotsweekDays);
    	adapterElement.insertPoint.addView(slotsDayTimes);

		String 													days						= Global.eRegCalendars.fetchDays(listItem.days);
		
		if (days == null)																	// itemData.days contains eg "123"
		{
			slotsweekDays					.setData		("Selected Days", listItem.days);			
		}
		else
		{
			slotsweekDays					.setData		(listItem.days, Global.eRegCalendars.fetchDays(listItem.days));			
		}
    	slotsDayTimes.setData(listItem);

    	if (position % 2 == 0)			// TODO This dont work
    	{
    		adapterElement.setBackgroundColor(0x800000ff); //  BLUE =  (0xff0000ff) (first byte = intensity)
    	}
        return adapterElement;
    }
}