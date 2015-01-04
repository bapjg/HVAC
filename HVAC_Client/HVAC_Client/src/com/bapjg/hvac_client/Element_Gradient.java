package com.bapjg.hvac_client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import HVAC_Common.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Element_Gradient 									extends 					RelativeLayout
																implements					View.OnClickListener
{
	public LayoutInflater 										inflater;
	public TextView 											tempLow;
	public TextView 											tempHigh;
	public TextView 											outsideLow;
	public TextView 											outsideHigh;
	public Panel_0_Interface									listener;
	
	public Element_Gradient(Context context) 
	{
		super(context);
		this.inflater 																		= LayoutInflater.from(context);
		inflater.inflate(R.layout.element_gradient, this, true);
		tempLow 																			= (TextView) this.findViewById(R.id.tempLow);
		tempHigh 																			= (TextView) this.findViewById(R.id.tempHigh);
		outsideLow 																			= (TextView) this.findViewById(R.id.outsideLow);
		outsideHigh 																		= (TextView) this.findViewById(R.id.outsideHigh);
	}
	public void setTempLow(Cmn_Temperature temperature)
	{
		if (temperature != null)		tempLow.setText(temperature.displayInteger());
		else							tempLow.setText("");
	}
	public void setTempHigh(Cmn_Temperature temperature)
	{
		if (temperature != null)		tempHigh.setText(temperature.displayInteger());
		else							tempHigh.setText("");
	}
	public void setOutsideLow(Cmn_Temperature temperature)
	{
		if (temperature != null)		outsideLow.setText(temperature.displayInteger());
		else							outsideLow.setText("");
	}
	public void setOutsideHigh(Cmn_Temperature temperature)
	{
		if (temperature != null)		outsideHigh.setText(temperature.displayInteger());
		else							outsideHigh.setText("");
	}
	public void setListener(OnClickListener listener)
	{
		this.listener																		= (Panel_0_Fragment) listener;
		tempLow							.setOnClickListener(this);
		tempHigh						.setOnClickListener(this);
		outsideLow						.setOnClickListener(this);
		outsideHigh						.setOnClickListener(this);
	}
	public void onClick(View view)
	{
		listener.onElementClick((View) view);
	}
}

