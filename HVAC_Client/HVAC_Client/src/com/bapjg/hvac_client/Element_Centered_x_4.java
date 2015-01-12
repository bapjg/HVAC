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
public class Element_Centered_x_4 									extends 					LinearLayout
																implements 					View.OnClickListener
{
	public LayoutInflater 										inflater;
	public TextView 											textTopLeft;
	public TextView 											textTopRight;
	public TextView 											textBottomLeft;
	public TextView 											textBottomRight;
	public Element_Interface									listener;
	
	public Element_Centered_x_4() 
	{
		super(Global.actContext);
		this.inflater 																		= LayoutInflater.from(Global.actContext);
		inflater.inflate(R.layout.element_centered_x_4, this, true);
		textTopLeft 																		= (TextView) this.findViewById(R.id.topLeft);
		textTopRight 																		= (TextView) this.findViewById(R.id.topRight);
		textBottomLeft 																		= (TextView) this.findViewById(R.id.bottomLeft);
		textBottomRight 																	= (TextView) this.findViewById(R.id.bottomRight);
	}
	public void setTextTopLeft(String text)
	{
		textTopLeft.setText(text);
	}
	public void setTextTopRight(String text)
	{
		textTopRight.setText(text);
	}
	public void setTextBottomLeft(String text)
	{
		textBottomLeft.setText(text);
	}
	public void setTextBottomRight(String text)
	{
		textBottomRight.setText(text);
	}
	public void setListener(Element_Interface listener)
	{
		this.listener																		= listener;
//		textTopLeft		.setOnClickListener(this);
//		textTopRight	.setOnClickListener(this);
//		textBottomLeft	.setOnClickListener(this);
//		textBottomRight	.setOnClickListener(this);
	}
	public void onClick(View view)
	{
		listener.onElementClick((View) this);
	}
}

