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
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import HVAC_Messages.*;


@SuppressLint("ValidFragment")
public class Panel_0_Fragment 		extends 	Fragment
									implements 	View.OnClickListener, AdapterView.OnItemClickListener
{
	private 	int					menuLayout;
    public 		Activity			activity;
    
    public Panel_0_Fragment()
    {
    }
    public Panel_0_Fragment(int menuLayout)
    {
		this.menuLayout									= menuLayout;
    	this.activity									= getActivity();
    }
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	View thisView = inflater.inflate(this.menuLayout, container, false);				// Inflate the menuLayout into container (menu_container)
		return thisView;
    }
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
	}
	@Override
	public void onClick(View v) 
	{
	}
}
