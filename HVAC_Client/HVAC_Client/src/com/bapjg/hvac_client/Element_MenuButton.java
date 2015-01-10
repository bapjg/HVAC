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
public class Element_MenuButton 								extends 					LinearLayout
																implements					View.OnClickListener
{
	public LayoutInflater 										inflater;
	public Button 												button;
	public Menu_0_Fragment										listener;
	
	public Element_MenuButton(String caption) 
	{
		super(Global.actContext);
		
		this.inflater 																		= LayoutInflater.from(Global.actContext);
		inflater.inflate(R.layout.element_menu_button, this, true);
		button 																				= (Button) this.findViewById(R.id.Button);
		button.setText(caption);
	}
	public void setListener(Menu_0_Fragment listener)
	{
		this.listener																		= (Menu_0_Fragment) listener;
		button						.setOnClickListener(this);
	}
	public void onClick(View view)
	{
		listener.onMenuElementClick((View) this);
	}
}

