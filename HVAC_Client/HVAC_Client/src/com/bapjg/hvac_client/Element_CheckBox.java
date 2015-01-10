package com.bapjg.hvac_client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import HVAC_Common.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Element_CheckBox 									extends 					LinearLayout
																implements					View.OnClickListener
{
	public LayoutInflater 										inflater;
	public CheckBox		 										checkBox;
	public TextView 											textLeft;
	public Element_Interface									listener;
	
//	public Element_CheckBox(Context context, String labelTextLeft) 
//	{
	public Element_CheckBox(String labelTextLeft) 
	{
		super(Global.actContext);
		this.inflater 																		= LayoutInflater.from(Global.actContext);
		inflater.inflate(R.layout.element_checkbox, this, true);
		checkBox 																			= (CheckBox) this.findViewById(R.id.CheckBox);
		textLeft 																			= (TextView) this.findViewById(R.id.Left);
		textLeft.setText(labelTextLeft);
	}
	public void setTextLeft(String text)
	{
		textLeft.setText(text);
	}
	public void setChecked(Boolean value)
	{
		checkBox.setChecked(value);
	}
	public Boolean isChecked()
	{
		return checkBox.isChecked();
	}
	public void setListener(Element_Interface listener)
	{
		this.listener																		= listener;
		checkBox						.setOnClickListener(this);
	}
	public void onClick(View view)
	{
		listener.onElementClick((View) this);
	}
}

