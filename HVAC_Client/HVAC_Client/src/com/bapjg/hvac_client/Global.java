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
	public static	Panel_2_Configuration		fragmentConfiguration;
	public static	Panel_1_Temperatures		fragmentTemperatures;
	public static	Panel_3_Calendars			fragmentCalendars;
	public static	Panel_4_Actions			fragmentActions;	

	public Global() 
	{
	}

}
