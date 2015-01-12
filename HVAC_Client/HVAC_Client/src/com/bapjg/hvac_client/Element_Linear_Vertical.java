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
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import HVAC_Common.*;
import HVAC_Common.Ctrl_Temperatures.Request;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Element_Linear_Vertical 							extends 					LinearLayout
{
	public LayoutInflater 										inflater;
	public Element_Interface									listener;
	public LinearLayout											insertPoint;
	
	public Element_Linear_Vertical() 
	{
		super(Global.actContext);
		this.inflater 																		= LayoutInflater.from(Global.actContext);
		inflater.inflate(R.layout.element_linear_vertical, this, true);
		insertPoint																			= (LinearLayout) this.findViewById(R.id.insertPoint);
	}
	public void setBackground(Integer color)
	{
		setBackground((View) this, color);
	}
	public void setBackground(View view, Integer color)
	{
		view.setBackgroundColor(color);
		if (view instanceof TextView)
		{
			return;
		}
		int 													kids 						= ((LinearLayout) view).getChildCount();
		int 													i 							= 0;
		for (i = 0; i < kids; i++)
		{
			View 												subView 					= ((LinearLayout) view).getChildAt(i);
			setBackground(subView, color);
		}
	}
}

