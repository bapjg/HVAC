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
import android.widget.LinearLayout;
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
																							Dialog_Response,
																							Panel_0_Interface
{
	public 		ViewGroup										container;
	public 		View											panelView;					// This corresponds to the inflated panel (R.layout.panel_n_xxxxxx)
	private 	String											panelType;					// Can be "Standard", ...
	protected 	LinearLayout									panelInsertPoint;
	public		View											panelButtonOk;
	public		View											panelButtonDelete;
	public		View											panelButtonAdd;
	public 		View											adapterView;				// This corresponds to the inflated list view within the panel view (R.id.List_View)

	
	public Panel_0_Fragment()
    {
    }
	public Panel_0_Fragment(String panelType)
    {
		this.panelType																		= panelType;
    }
	public void panelInitialise(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		this.container																		= container;
		if (panelType.equalsIgnoreCase("Standard"))
		{
			this.panelView																	= inflater.inflate(R.layout.panal_0_standard, container, false);
	    	panelInsertPoint 																= (LinearLayout) panelView.findViewById(R.id.base_insert_point);
		}
		else if (panelType.equalsIgnoreCase("Centered"))
		{
			this.panelView																	= inflater.inflate(R.layout.panal_0_standard_centered, container, false);
	    	panelInsertPoint 																= (LinearLayout) panelView.findViewById(R.id.base_insert_point);
		}
		else if (panelType.equalsIgnoreCase("Add"))
		{
			this.panelView																	= inflater.inflate(R.layout.panal_0_standard_with_buttons_addnew, container, false);
	    	panelInsertPoint 																= (LinearLayout) panelView.findViewById(R.id.base_insert_point);
			this.panelButtonAdd																= this.panelView.findViewById(R.id.buttonAdd);
			this.panelButtonAdd			.setOnClickListener(this);
		}
		else if (panelType.equalsIgnoreCase("Ok_Delete"))
		{
			this.panelView																	= inflater.inflate(R.layout.panal_0_standard_with_buttons_ok_delete, container, false);
	    	panelInsertPoint 																= (LinearLayout) panelView.findViewById(R.id.base_insert_point);
			this.panelButtonOk																= this.panelView.findViewById(R.id.buttonOk);
			this.panelButtonDelete															= this.panelView.findViewById(R.id.buttonDelete);
			this.panelButtonOk			.setOnClickListener(this);
			this.panelButtonDelete		.setOnClickListener(this);
		}
		else
		{
			Global.toaster("Unsupported panelType : " + this.panelType, true);
		}
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
//		super.onConfigurationChanged(newConfig);
//		
//		LayoutInflater inflater 															= (LayoutInflater) Global.actContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	    panelView 																			= inflater.inflate(this.panelLayout, this.container);				// Inflate the menuLayout into container (menu_container)
//	    displayHeader(); displayContents(); setListens();
	}
	public void displayTitles(String title, String subTitle)
	{
		((TextView) panelView.findViewById(R.id.title)).setText		(title);
		((TextView) panelView.findViewById(R.id.subTitle)).setText	(subTitle);
	}
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

	@Override	public void onItemClick					(AdapterView<?> parent, View view, int position, long id) 	{}
				public void onPanelButtonOk				()															{}
				public void onPanelButtonAdd			()															{}
				public void onPanelButtonDelete			()															{}
	@Override
	public void onClick (View clickedView) 													
	{
     	switch(clickedView.getId())
		{
	     	case R.id.buttonDelete:
	     		onPanelButtonDelete();
	     		break;
	     	case R.id.buttonOk:
	     		onPanelButtonOk();
	      		break;
	     	case R.id.buttonAdd:
	     		onPanelButtonAdd();
	      		break;		
  		}
	}
	@Override	public void onDialogReturn 				()															{}
	@Override	public void onDialogReturnWithId		(int id)													{}
	@Override	public void onElementClick				(View view)													{}
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

