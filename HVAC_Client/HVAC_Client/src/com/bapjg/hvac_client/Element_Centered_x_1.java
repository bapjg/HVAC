package com.bapjg.hvac_client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import HVAC_Common.*;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Element_Centered_x_1 								extends 					LinearLayout
																implements 					View.OnClickListener
{
	public LayoutInflater 										inflater;
	public TextView 											textCenter;
	public Element_Interface									listener;
	
	public Element_Centered_x_1() 
	{
		super(Global.actContext);
		this.inflater 																		= LayoutInflater.from(Global.actContext);
		inflater.inflate(R.layout.element_centered_x_1, this, true);
		textCenter 																			= (TextView) this.findViewById(R.id.textCenter);
	}
	public Element_Centered_x_1(String text) 
	{
		super(Global.actContext);
		this.inflater 																		= LayoutInflater.from(Global.actContext);
		inflater.inflate(R.layout.element_centered_x_1, this, true);
		textCenter 																			= (TextView) this.findViewById(R.id.textCenter);
		textCenter.setText(text);
	}
	public void setTextCenter(String text)
	{
		textCenter.setText(text);
	}
	public void setTextColor(Integer colour)
	{
		textCenter.setTextColor(colour);;
	}
	public void setListener(Element_Interface listener)
	{
		this.listener																		= listener;
	}
	public void onClick(View view)
	{
		listener.onElementClick((View) this);
	}
}

