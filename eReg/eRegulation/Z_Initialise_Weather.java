package eRegulation;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import HVAC_Messages.*;

public class Z_Initialise_Weather
{
	public static void main(String[] args)
	{
		
		Long SIX_HOURS 			= 6 * 60 * 60 * 1000L;
		Long Inc_6h   			= Global.getTimeNowSinceMidnight()/SIX_HOURS;
		Long Time_Last_Inc		= Global.getTimeAtMidnight() + SIX_HOURS * Inc_6h;
		
		
		System.out.println ("midnight : " + Global.getTimeAtMidnight());
		System.out.println ("6h       : " + SIX_HOURS);
		System.out.println ("Inc       : " + Global.getTimeNowSinceMidnight()/SIX_HOURS);
		System.out.println ("Inc.t     : " + Time_Last_Inc);
		
		
		
		
		
		
		
		
		
		String utct = "2014-05-04T15:00:00";
		

		//================================================================================================================================
		//
		// Create Weather Object object 
		//
		try
		{
			Ctrl_WeatherData 								weather 					= new Ctrl_WeatherData();
			Long x =weather.dateTimeObtained;
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		//
		//================================================================================================================================
		
    }
	public static Long dateTimeFromUTC(String utc)
	{
		SimpleDateFormat 	utcFormat						= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		GregorianCalendar 	calendar					= new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		try
		{
			calendar.setTime(utcFormat.parse(utc));
			return calendar.getTimeInMillis();
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0L;
	}
}
