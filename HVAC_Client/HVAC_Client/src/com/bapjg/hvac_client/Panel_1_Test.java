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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import HVAC_Common.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
@SuppressLint("ValidFragment")
public class Panel_1_Test 										extends 					Panel_0_Fragment 
{		
	public TCP_Task												task;
	private Ctrl_Temperatures.Data								temperatureData;
	
	public Panel_1_Test()
	{
		super();
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	this.panelLayout																	= R.layout.panel_1_temperatures;
    	this.container																		= container;
		this.panelView																		= inflater.inflate(R.layout.panel_0_base_linear, container, false);
    	displayHeader();
//    	TCP_Send(new Ctrl_Temperatures().new Request());
    	
    	LinearLayout insertPoint = (LinearLayout) panelView.findViewById(R.id.base_insert_point);
    	
    	LinearLayout  	line1 = (LinearLayout ) inflater.inflate(R.layout.panel_0_linear_line, null, false);
    	LinearLayout  	line2 = (LinearLayout ) inflater.inflate(R.layout.panel_0_linear_line, null, false);
    	Panel_0_Base_Linear			line3 = new Panel_0_Base_Linear(getActivity());

    	LinearLayout	line4 = new LinearLayout(getActivity());
    	line4.setOrientation(LinearLayout.HORIZONTAL);
    	line4.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    	line4.setBackgroundColor(0x00009900);
    	line4.setPadding(0, 5, 10, 5);    	
    	
        TextView textLeft = new TextView(getActivity());
        LinearLayout.LayoutParams paramsLeft = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1F);
        textLeft.setLayoutParams(paramsLeft);
        textLeft.setText("Hallo Welt!");
        textLeft.setTextSize(20F);
        textLeft.setTextColor(Color.WHITE);
        textLeft.setGravity(Gravity.LEFT);
        
        
        
        
        
        line4.addView(textLeft);

        
        
        TextView textRight = new TextView(getActivity());
        LinearLayout.LayoutParams paramsRight = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1F);
        textRight.setLayoutParams(paramsRight);
        textRight.setText("Henry");
        textRight.setTextSize(20F);
        textRight.setTextColor(Color.YELLOW);
        textRight.setGravity(Gravity.RIGHT);
        line4.addView(textRight);

    	
    	
    	
    	
    	insertPoint.addView(line2);
    	insertPoint.addView(line3);
    	insertPoint.addView(line1);
       	insertPoint.addView(line4);
          	
    	((TextView) line1.findViewById(R.id.Left)).setTag("T1");
    	((TextView) line1.findViewById(R.id.Right)).setTag("T2");
    	((TextView) line2.findViewById(R.id.Left)).setTag("T3");
    	((TextView) line2.findViewById(R.id.Right)).setTag("T4");

    	
    	((TextView) insertPoint.findViewWithTag("T1")).setText("Line1 Left");
    	((TextView) insertPoint.findViewWithTag("T2")).setText("Line1 Right");
    	((TextView) insertPoint.findViewWithTag("T3")).setText("Line2 Left");
    	((TextView) insertPoint.findViewWithTag("T4")).setText("Line2 Right");
        return panelView;
    }
	public void processFinishTCP(Ctrl__Abstract result) 
	{             
		super.processFinishTCP(result);
		if 	(result instanceof Ctrl_Temperatures.Data)
		{
			temperatureData																	= (Ctrl_Temperatures.Data) result;																	
			displayContents();
		}
        setListens();
    } 
	public void displayHeader()
	{
		((TextView) panelView.findViewById(R.id.title)).setText		("Testing Testing");
		((TextView) panelView.findViewById(R.id.subTitle)).setText	("Test");
	}
	public void displayContents()
	{
		if (getActivity() != null)			// The user has not changed the screen
		{
//			((TextView) panelView.findViewById(R.id.Date)).setText				(Global.displayDateShort	(temperatureData.dateTime));
//			((TextView) panelView.findViewById(R.id.Time)).setText				(Global.displayTimeShort	(temperatureData.dateTime));
//			
//			((TextView) panelView.findViewById(R.id.Boiler)).setText			(Global.displayTemperature	(temperatureData.tempBoiler));
//			((TextView) panelView.findViewById(R.id.HotWater)).setText			(Global.displayTemperature	(temperatureData.tempHotWater));
//			((TextView) panelView.findViewById(R.id.Outside)).setText			(Global.displayTemperature	(temperatureData.tempOutside));
//			((TextView) panelView.findViewById(R.id.BoilerIn)).setText			(Global.displayTemperature	(temperatureData.tempBoilerIn));
//			((TextView) panelView.findViewById(R.id.BoilerOut)).setText			(Global.displayTemperature	(temperatureData.tempBoilerOut));
//			((TextView) panelView.findViewById(R.id.FloorIn)).setText			(Global.displayTemperature	(temperatureData.tempFloorIn));
//			((TextView) panelView.findViewById(R.id.FloorOut)).setText			(Global.displayTemperature	(temperatureData.tempFloorOut));
//			((TextView) panelView.findViewById(R.id.RadiatorIn)).setText		(Global.displayTemperature	(temperatureData.tempRadiatorIn));
//			((TextView) panelView.findViewById(R.id.RadiatorOut)).setText		(Global.displayTemperature	(temperatureData.tempRadiatorOut));
//			((TextView) panelView.findViewById(R.id.LivingRoom)).setText		(Global.displayTemperature	(temperatureData.tempLivingRoom));
		}
	}
	public void setListens()
	{
	}
}

