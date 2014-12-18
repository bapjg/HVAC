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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import HVAC_Common.*;
import HVAC_Common.Ctrl_Temperatures.Request;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Element_Switch 									extends 					LinearLayout
{
	public LayoutInflater 										inflater;
	public TextView 											textLeft;
	public Switch		 										onOffSwitch;
	
	public Element_Switch(Context context, String labelTextLeft) 
	{
		super(context);
		this.inflater 																		= LayoutInflater.from(context);
		inflater.inflate(R.layout.element_switch, this, true);
		onOffSwitch																			= (Switch) this.findViewById(R.id.Switch);
		onOffSwitch.setEnabled(false);
		textLeft 																			= (TextView) this.findViewById(R.id.Left);
		textLeft.setText(labelTextLeft);
	}
	public void setTextLeft(String text)
	{
		textLeft.setText(text);
	}
	public void setChecked(Boolean value)
	{
		onOffSwitch.setChecked(value);
	}
	public Boolean isChecked()
	{
		return onOffSwitch.isChecked();
	}
}

