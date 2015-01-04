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
public class Element_Standard 									extends 					LinearLayout
																implements 					View.OnClickListener
{
	public LayoutInflater 										inflater;
	public TextView 											textLeft;
	public TextView 											textRight;
	public String												units;
	public Panel_0_Interface									listener;
	
	public Element_Standard(String labelTextLeft) 
	{
		super(Global.actContext);
		this.inflater 																		= LayoutInflater.from(Global.actContext);
		this.units 																			= "";
		inflater.inflate(R.layout.element_standard, this, true);
		textLeft 																			= (TextView) this.findViewById(R.id.Left);
		textRight 																			= (TextView) this.findViewById(R.id.Right);
		textLeft.setText(labelTextLeft);
	}
	public Element_Standard(Context context, String labelTextLeft) 
	{
		super(context);
		this.inflater 																		= LayoutInflater.from(context);
		this.units 																			= "";
		inflater.inflate(R.layout.element_standard, this, true);
		textLeft 																			= (TextView) this.findViewById(R.id.Left);
		textRight 																			= (TextView) this.findViewById(R.id.Right);
		textLeft.setText(labelTextLeft);
	}
	public Element_Standard(Context context, String labelTextLeft, String units) 
	{
		super(context);
		this.inflater 																		= LayoutInflater.from(context);
		this.units 																			= " " + units.trim();
		inflater.inflate(R.layout.element_standard, this, true);
		textLeft 																			= (TextView) this.findViewById(R.id.Left);
		textRight 																			= (TextView) this.findViewById(R.id.Right);
		textLeft.setText(labelTextLeft);
	}
	public void setTextLeft(String text)
	{
		textLeft.setText(text);
	}
	public void setTextRight(String text)
	{
		if (text != null)		textRight.setText(text);
		else					textRight.setText("");
	}
	public void setTextRight(Integer number)
	{
		if (number != null)		textRight.setText(number + units);
		else					textRight.setText(0 + units);
	}
	public void setTextRight(Long number)
	{
		if (number != null)		textRight.setText(number + units);
		else					textRight.setText(0 + units);
	}
	public void setTextRight(Float number)
	{
		if (number != null)		textRight.setText(number + units);
		else					textRight.setText(0 + units);
	}
	public void setListener(Panel_0_Interface listener)
	{
		this.listener																		= listener;
		textRight.setOnClickListener(this);
	}
	public void onClick(View view)
	{
		listener.onElementClick((View) this);
	}
}

