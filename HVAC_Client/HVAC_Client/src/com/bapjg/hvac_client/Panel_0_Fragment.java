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
//	private 	int												menuLayout;
    public 		Activity										activity;
    
    public Panel_0_Fragment()
    {
    }
//    public Panel_0_Fragment(int menuLayout)
//    {
//		this.menuLayout																		= menuLayout;
//    	this.activity																		= getActivity();
//    }
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
//    {
//    	View thisView = inflater.inflate(this.menuLayout, container, false);				// Inflate the menuLayout into container (menu_container)
//		return thisView;
//    }
	public void HTTP_Send(Ctrl__Abstract message)
	{
		HTTP_Task												task						= new HTTP_Task();
	   	task.callBack																		= this;					// processFinish
	   	task.execute(message);		
	}		
	public void TCP_Send(Ctrl__Abstract message)		
	{		
		TCP_Task												task						= new TCP_Task();
	   	task.callBack																		= this;					// processFinish
	   	task.execute(message);
	}

	@Override	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 			{}
	@Override	public void onClick(View v) 																{}
	@Override	public void processFinishTCP(Ctrl__Abstract result) 										{}
	@Override	public void processFinishHTTP(Ctrl__Abstract result) 										{}
	@Override	public void onReturnTemperature			(int fieldId, Integer response)						{}
	@Override	public void onReturnTime 				(int fieldId, String  response)						{}
	@Override	public void onReturnTime   				(int fieldId, Long    response)						{}
	@Override	public void onReturnString 				(int fieldId, String  response)						{}
	@Override	public void onDialogReturn 				()													{}
}

