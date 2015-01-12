package com.bapjg.hvac_client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import HVAC_Common.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Element_Button 									extends 					LinearLayout
																implements					View.OnClickListener
{
	public LayoutInflater 										inflater;
	public Button 												button;
	public Element_Interface									listener;
	
//	public Element_Button(Context context, String caption) 
//	{
	public Element_Button(String caption) 
	{
		super(Global.actContext);
		this.inflater 																		= LayoutInflater.from(Global.actContext);
		inflater.inflate(R.layout.element_button, this, true);
		button 																				= (Button) this.findViewById(R.id.button);
		button.setText(caption);
	}
	public void setText(String caption)
	{
		button.setText(caption);
	}
	public String getText()
	{
		return button.getText().toString();
	}
	public void setListener(Element_Interface listener)
	{
		this.listener																		= listener;
		button						.setOnClickListener(this);
	}
	public void onClick(View view)
	{
		listener.onElementClick((View) this);
	}
}

