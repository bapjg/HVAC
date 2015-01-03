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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import HVAC_Common.*;
import HVAC_Common.Ctrl_Temperatures.Request;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Element_Gradient 									extends 					RelativeLayout
{
	public LayoutInflater 										inflater;
	public TextView 											tempLow;
	public TextView 											tempHigh;
	public TextView 											outsideLow;
	public TextView 											outsideHigh;
	public Panel_0_Interface									listener;
	
	public Element_Gradient(Context context) 
	{
		super(context);
		this.inflater 																		= LayoutInflater.from(context);
		inflater.inflate(R.layout.element_gradient, this, true);
		tempLow 																			= (TextView) this.findViewById(R.id.tempLow);
		tempHigh 																			= (TextView) this.findViewById(R.id.tempHigh);
		outsideLow 																			= (TextView) this.findViewById(R.id.outsideLow);
		outsideHigh 																		= (TextView) this.findViewById(R.id.outsideHigh);
	}
	public void setTempLow(Cmn_Temperature temperature)
	{
		if (temperature != null)		tempLow.setText(temperature.displayInteger());
		else							tempLow.setText("");
	}
	public void setTempHigh(Cmn_Temperature temperature)
	{
		if (temperature != null)		tempHigh.setText(temperature.displayInteger());
		else							tempHigh.setText("");
	}
	public void setOutsideLow(Cmn_Temperature temperature)
	{
		if (temperature != null)		outsideLow.setText(temperature.displayInteger());
		else							outsideLow.setText("");
	}
	public void setOutsideHigh(Cmn_Temperature temperature)
	{
		if (temperature != null)		outsideHigh.setText(temperature.displayInteger());
		else							outsideHigh.setText("");
	}
	public void setListener(OnClickListener listener)
	{
		this.listener																		= (Panel_0_Fragment) listener;
		tempLow							.setOnClickListener(listener);
		tempHigh						.setOnClickListener(listener);
		outsideLow						.setOnClickListener(listener);
		outsideHigh						.setOnClickListener(listener);
	}
	public void onClick(View view)
	{
//		listener.onPanelItemClick(this);
	}

}

