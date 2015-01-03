package com.bapjg.hvac_client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import HVAC_Common.*;
import HVAC_Common.Ctrl_Temperatures.Request;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Element_Standard 									extends 					LinearLayout
																implements 					View.OnClickListener
{
	public LayoutInflater 										inflater;
	public TextView 											textLeft;
	public TextView 											textRight;
	public String												units;
	public Panel_0_Interface									listener;
	
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
	public void setListener(OnClickListener listener)
	{
		this.listener																		= (Panel_0_Fragment) listener;
		textRight.setOnClickListener(this);
	}
	public void onClick(View view)
	{
		listener.onPanelItemClick(this);
	}
}

