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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import HVAC_Common.*;
import HVAC_Common.Ctrl_Temperatures.Request;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Element_Heading 									extends 					LinearLayout
{
	public LayoutInflater 										inflater;
	public TextView 											textLeft;
	public TextView 											textRight;
	
	public Element_Heading(String labelTextLeft) 
	{
		super(Global.actContext);
		this.inflater 																		= LayoutInflater.from(Global.actContext);
		inflater.inflate(R.layout.element_heading, this, true);
		textLeft 																			= (TextView) this.findViewById(R.id.headingLeft);
		textLeft.setText(labelTextLeft);
		textRight 																			= (TextView) this.findViewById(R.id.headingRight);
		textRight.setText("");
	}
	public Element_Heading(String labelTextLeft, String labelTextRight) 
	{
		this(labelTextLeft);
		if (labelTextRight.substring(labelTextRight.length() - 1).equalsIgnoreCase(" "))	textRight.setText(labelTextRight);
		else																				textRight.setText(labelTextRight + " ");
		checkWidth();
	}
	public void setTextLeft(String text)
	{
		textLeft.setText(text);
	}
	public void setTextRight(String text)
	{
		textRight.setText(text);
		checkWidth();
	}
	public void centerColumns()
	{
		textLeft.setGravity(Gravity.CENTER);
		textRight.setGravity(Gravity.CENTER);
	}
	public void checkWidth()
	{
       	textLeft.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
       	textRight.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
       	int 													width 						= textLeft.getMeasuredWidth() + textRight.getMeasuredWidth();
    	if (width > 300) 
    	{
        	if (Global.deviceName.equalsIgnoreCase("lgPhone"))
        	{
//        		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)         		setVertical();
        	}
    	}
	}
	public void setVertical()
	{
		((LinearLayout) this.getChildAt(0)).setOrientation(LinearLayout.VERTICAL);
	}
}