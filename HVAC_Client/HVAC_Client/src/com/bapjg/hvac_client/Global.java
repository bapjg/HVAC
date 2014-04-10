package com.bapjg.hvac_client;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class Global 
{
	public static 	Context 					appContext;
	public static 	Context 					actContext;
	public static 	Activity 					activity;
	
	public static	String						serverURL;
	public static	InetAddress					piAddressV4;
	
	public static 	Mgmt_Msg_Configuration 		configuration;
	public static 	Mgmt_Msg_Calendar 			calendar;
	
	public static	Panel_1_Temperatures		panelTemperatures;
	public static	Panel_2_Immediate			panelImmediate;	
	public static	Panel_3_Calendars			panelCalendars;
	public static	Panel_4_Configuration		panelConfiguration;
	public static	Panel_5_Action				panelActions;	

	public static	Menu_1_Temperatures			menuTemperatures;
	public static	Menu_2_Immediate			menuImmediate;
	public static	Menu_3_Calendars			menuCalendars;
	public static	Menu_4_Config 				menuConfiguration;
	public static	Menu_5_Actions				menuActions;

	public static	Boolean						initialisationCompleted;

	public Global() 
	{
		initialisationCompleted					= false;
	}
    public static String displayDate(Long dateTime)
    {
    	String					dateTimeString		= "";
 
        SimpleDateFormat 		sdf 				= new SimpleDateFormat("dd/MM");
        GregorianCalendar 		calendar 			= new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);
        dateTimeString								= sdf.format(dateTime);
    	
    	return dateTimeString;
    }
    public static String displayTime(Long dateTime)
    {
    	String					dateTimeString		= "";
 
        SimpleDateFormat 		sdf 				= new SimpleDateFormat("HH:mm:ss");
        GregorianCalendar 		calendar 			= new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);
        dateTimeString								= sdf.format(dateTime); 
    	
    	return dateTimeString;
    }
    public static String displayTimeShort(Long dateTime)
    {
    	String					dateTimeString		= "";
 
        SimpleDateFormat 		sdf 				= new SimpleDateFormat("HH:mm");
        GregorianCalendar 		calendar 			= new GregorianCalendar();
        calendar.setTimeInMillis(dateTime);
        dateTimeString								= sdf.format(dateTime); 
    	
    	return dateTimeString;
    }
    
    public static String displayTemperature(Integer temperature)
	{
		int degrees = temperature/1000;
		int decimal = (temperature - degrees*1000) / 100;
		return degrees + "." + decimal;
	}
    public static void toast(String message, Boolean longish)
    {
    	if (longish)
    	{
    		Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    	}
    	else
    	{
    		Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    	}
    }
}
