package com.bapjg.hvac_client;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import HVAC_Messages.*;

public class Global 
{
	public static 	Context 					appContext;
	public static 	Context 					actContext;
	public static 	Activity 					activity;
	
	public static	String						serverURL;
	public static	InetSocketAddress			piSocketAddress;
	
	public static	Panel_1_Temperatures		panelTemperatures;
	public static	Panel_2_Immediate			panelImmediate;	
	public static	Panel_3_Calendars			panelCalendars;
	public static	Panel_5_Config_Thermometers	panelConfiguration;
	public static	Panel_6_Actions_Relays		panelActions;	

	public static	Menu_1_Temperatures			menuTemperatures;
	public static	Menu_2_Immediate			menuImmediate;
	public static	Menu_3_Calendars			menuCalendars;
	public static	Menu_4_Weather				menuWeather;
	public static	Menu_5_Config 				menuConfiguration;
	public static	Menu_6_Actions				menuActions;
	
	public static	Ctrl_Configuration.Data		eRegConfiguration;
	public static	String						eRegCalendar;

	public Global() 
	{
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
    		Toast.makeText(actContext, message, Toast.LENGTH_LONG).show();
    	}
    	else
    	{
    		Toast.makeText(actContext, message, Toast.LENGTH_SHORT).show();
    	}
    }
    public static void toast(Context c, String message, Boolean longish)
    {
    	if (longish)
    	{
    		Toast.makeText(c, message, Toast.LENGTH_LONG).show();
    	}
    	else
    	{
    		Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    	}
    }
    public static void toaster(final String message, final Boolean longish)
    {
    	activity.runOnUiThread(new Runnable()
		{
			public void run() 
			{
				final String   myMessage = message;
				final Boolean  mylongish = longish;
				if (longish)
				{
					Toast.makeText(Global.activity, myMessage, Toast.LENGTH_LONG).show();
				}	
				else
				{
					Toast.makeText(Global.activity, myMessage, Toast.LENGTH_SHORT).show();
				}
			}
		}
		);
    }
	public static Long getTimeAtMidnight()
	{
		// Returns the system time last midnight
		Calendar now		 				= Calendar.getInstance();
		
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		Long todayMidnight					= now.getTimeInMillis();
		
		return todayMidnight;
	}
	public static Long parseTime(String characters)
	{
		// Returns a supplied time in string form "hh:mm" 
		// In milliseconds since last midnight
		String splitCharacters[]			= characters.split(":");
		
		Calendar calendar					= Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 	Integer.parseInt(splitCharacters[0]));
		calendar.set(Calendar.MINUTE,  		Integer.parseInt(splitCharacters[1]));
		calendar.set(Calendar.SECOND, 		0);
		calendar.set(Calendar.MILLISECOND, 	0);
		
		return calendar.getTimeInMillis() - Global.getTimeAtMidnight();
	}

}
