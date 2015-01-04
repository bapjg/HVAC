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
	public Panel_0_Interface									listener;
	
	public Element_Button(Context context, String caption) 
	{
		super(context);
		this.inflater 																		= LayoutInflater.from(context);
		inflater.inflate(R.layout.element_button, this, true);
		button 																				= (Button) this.findViewById(R.id.Button);
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
	public void setListener(OnClickListener listener)
	{
		this.listener																		= (Panel_0_Fragment) listener;
		button						.setOnClickListener(this);
	}
	public void onClick(View view)
	{
		listener.onElementClick((View) this);
	}
}

