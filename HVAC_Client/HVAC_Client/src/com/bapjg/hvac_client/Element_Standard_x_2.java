package com.bapjg.hvac_client;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import HVAC_Common.*;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Element_Standard_x_2 									extends 					LinearLayout
																implements 					View.OnClickListener
{
	public LayoutInflater 										inflater;
	public TextView 											textTopic;
	public TextView 											centerValue;
	public TextView 											rightValue;
	public Element_Interface									listener;
	
	public Element_Standard_x_2(String labelTextLeft) 
	{
		super(Global.actContext);
		this.inflater 																		= LayoutInflater.from(Global.actContext);
		inflater.inflate(R.layout.element_standard_x_2, this, true);
		textTopic 																			= (TextView) this.findViewById(R.id.textTopic);
		centerValue 																		= (TextView) this.findViewById(R.id.centerValue);
		rightValue 																			= (TextView) this.findViewById(R.id.rightValue);
		textTopic.setText(labelTextLeft);
	}
	public void setTopic(String text)
	{
		textTopic.setText(text);
	}
	public void setCenterValue(String text)
	{
		if (text != null)		centerValue.setText(text);
		else					centerValue.setText("");
	}
	public void setRightValue(String text)
	{
		if (text != null)		rightValue.setText(text);
		else					rightValue.setText("");
	}
	public void setRightValue(String text, String units)
	{
		if (text != null)		rightValue.setText(text + " " + units);
		else					rightValue.setText("0 " + units);
	}
	public void setCenterValue(Integer number, String units)
	{
		if (number != null)		centerValue.setText(number + " " + units);
		else					centerValue.setText(0 + " " + units);
	}
	public void setRightValue(Long number, String units)
	{
		if (number != null)		rightValue.setText(number + " " + units);
		else					rightValue.setText("0.0 " + units);
	}
	public void setCenterValue(Float number, String units)
	{
		if (number != null)		centerValue.setText(String.format("%.01f", number) + " " + units);
		else					centerValue.setText("0.0 " + units);
	}
	public void setRightValue(Float number, String units)
	{
		if (number != null)		rightValue.setText(String.format("%.01f", number) + " " + units);
		else					rightValue.setText("0.0 " + units);
	}
	public void setListener(Element_Interface listener)
	{
		this.listener																		= listener;
		centerValue.setOnClickListener(this);
		rightValue.setOnClickListener(this);
	}
	public void onClick(View view)
	{
		listener.onElementClick((View) this);
	}
}

