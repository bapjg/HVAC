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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import HVAC_Common.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Panel_0_Fragment 									extends 					Fragment
																implements 					View.OnClickListener, 
																							AdapterView.OnItemClickListener, 
																							TCP_Response,
																							HTTP_Response,
																							Dialog_Response
{
	public int													panelLayout;
	public ViewGroup											container;
	public View													panelView;					// This corresponds to the inflated panel (R.layout.panel_n_xxxxxx)
	public View													adapterView;				// This corresponds to the inflated list view within the panel view (R.id.List_View)

	public Panel_0_Fragment()
    {
    }
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
//		super.onConfigurationChanged(newConfig);
//		
//		LayoutInflater inflater 															= (LayoutInflater) Global.actContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	    panelView 																			= inflater.inflate(this.panelLayout, this.container);				// Inflate the menuLayout into container (menu_container)
//	    displayHeader(); 
//    	displayContents();
//    	setListens();
	}
	public void displayHeader() 											{}
	public void displayContents() 											{}
	public void setListens()												{}
	public void HTTP_Send(Ctrl__Abstract message)
	{
		Global.setStatusHTTP("Waiting");
		HTTP_Task												task						= new HTTP_Task();
	   	task.callBack																		= this;					// processFinish
	   	task.execute(message);		
	}		
	public void TCP_Send(Ctrl__Abstract message)		
	{		
		Global.setStatusTCP("Waiting");
		TCP_Task												task						= new TCP_Task();
	   	task.callBack																		= this;					// processFinish
	   	task.execute(message);
	}

	@Override	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 			{}
	@Override	public void onClick(View v) 																{}
	@Override	public void onDialogReturn 				()													{}
	@Override	public void onDialogReturnWithId(int id)													{}
	public void processFinishHTTP(Ctrl__Abstract result) 										
	{
		Global.setAddressSpace();
		Global.setStatusHTTP(result);
	}
	public void processFinishTCP(Ctrl__Abstract result) 										
	{
		Global.setAddressSpace();
		Global.setStatusTCP(result);
	}
}

