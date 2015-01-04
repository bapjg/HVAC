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
public class Element_Switch 									extends 					LinearLayout
																implements 					Switch.OnClickListener 
{
	public LayoutInflater 										inflater;
	public TextView 											textLeft;
	public Switch		 										onOffSwitch;
	public Panel_0_Interface									listener;
	
	public Element_Switch(Context context, String labelTextLeft) 
	{
		super(context);
		this.inflater 																		= LayoutInflater.from(context);
		inflater.inflate(R.layout.element_switch, this, true);
		onOffSwitch																			= (Switch) this.findViewById(R.id.Switch);
		textLeft 																			= (TextView) this.findViewById(R.id.Left);
		textLeft.setText(labelTextLeft);
	}
	public void setTextLeft(String text)
	{
		textLeft.setText(text);
	}
	public void setChecked(Boolean value)
	{
		onOffSwitch.setChecked(value);
	}
	public Boolean isChecked()
	{
		return onOffSwitch.isChecked();
	}
	public void setListener(OnClickListener listener)
	{
		this.listener																		= (Panel_0_Fragment) listener;
		onOffSwitch.setOnClickListener(this);
	}
	public void onClick(View view)
	{
		listener.onElementClick((View) this);
	}
}

