package com.bapjg.hvac_client;

import com.bapjg.hvac_client.R.color;

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

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Element_MenuButton 								extends 					LinearLayout
																implements					View.OnClickListener
{
	public LayoutInflater 										inflater;
	public Button 												button;
	public Element_Interface									listener;
	public Boolean												isYellow;
	
	public Element_MenuButton(String caption) 
	{
		super(Global.actContext);
		
		this.inflater 																		= LayoutInflater.from(Global.actContext);
		inflater.inflate(R.layout.element_menu_button, this, true);
		button 																				= (Button) this.findViewById(R.id.button);
		button.setText(caption);
		isYellow																			= false;
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
	public void setYellow()
	{
		button.setTextColor(getResources().getColor(R.color.Yellow));
		isYellow																			= true;
	}
	public void setWhite()
	{
		button.setTextColor(getResources().getColor(R.color.White));	
		isYellow																			= false;
	}
}

