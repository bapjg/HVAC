package com.bapjg.hvac_client;

import android.app.Activity;
import android.content.Context;

public class Global 
{
	public static 	Context 					appContext;
	public static 	Context 					actContext;
	public static 	Activity 					activity;
	public static	String						serverURL;
	public static 	Mgmt_Msg_Configuration 		configuration;
	public static 	Mgmt_Msg_Calendar 			calendar;
	public static	Fragment_Configuration		fragmentConfiguration;
	public static	Fragment_Temperatures		fragmentTemperatures;
	public static	Fragment_Calendars			fragmentCalendars;
	public static	Fragment_Actions			fragmentActions;	

	public Global() 
	{
	}

}
