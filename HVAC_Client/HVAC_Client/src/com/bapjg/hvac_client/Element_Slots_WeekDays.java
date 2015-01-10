package com.bapjg.hvac_client;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import HVAC_Common.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Element_Slots_WeekDays 									extends 					LinearLayout
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
	
	public Element_Interface									listener;
	
	public Element_Slots_WeekDays()
	{
		super(Global.actContext);
		this.inflater 																		= LayoutInflater.from(Global.actContext);
		inflater.inflate(R.layout.element_slots_weekdays, this, true);
	}
	public Element_Slots_WeekDays(String nameText, String days) 
	{
		super(Global.actContext);
		this.inflater 																		= LayoutInflater.from(Global.actContext);
		inflater.inflate(R.layout.element_slots_weekdays, this, true);

		setData(nameText, days);
	}
	public void setData(String nameText, String days)
	{
		textName 																			= (TextView) this.findViewById(R.id.Name);
		textName.setText(nameText);

		this.day_1 																			= (TextView) this.findViewById(R.id.day_1);
		this.day_2 																			= (TextView) this.findViewById(R.id.day_2);
		this.day_3 																			= (TextView) this.findViewById(R.id.day_3);
		this.day_4 																			= (TextView) this.findViewById(R.id.day_4);
		this.day_5 																			= (TextView) this.findViewById(R.id.day_5);
		this.day_6 																			= (TextView) this.findViewById(R.id.day_6);
		this.day_7 																			= (TextView) this.findViewById(R.id.day_7);
        
        if ((days).indexOf("1") > -1)	day_1.setBackgroundColor(Color.RED); else day_1.setBackgroundColor(Color.BLUE);
        if ((days).indexOf("2") > -1)	day_2.setBackgroundColor(Color.RED); else day_2.setBackgroundColor(Color.BLUE);
        if ((days).indexOf("3") > -1)	day_3.setBackgroundColor(Color.RED); else day_3.setBackgroundColor(Color.BLUE);
        if ((days).indexOf("4") > -1)	day_4.setBackgroundColor(Color.RED); else day_4.setBackgroundColor(Color.BLUE);
        if ((days).indexOf("5") > -1)	day_5.setBackgroundColor(Color.RED); else day_5.setBackgroundColor(Color.BLUE);
        if ((days).indexOf("6") > -1)	day_6.setBackgroundColor(Color.RED); else day_6.setBackgroundColor(Color.BLUE);
        if ((days).indexOf("7") > -1)	day_7.setBackgroundColor(Color.RED); else day_7.setBackgroundColor(Color.BLUE);
	}
	public void setListener(Element_Interface listener)
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

