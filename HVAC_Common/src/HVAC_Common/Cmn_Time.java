package HVAC_Common;

import java.text.SimpleDateFormat;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Cmn_Time 											implements 					java.io.Serializable
{
	private static final long 									serialVersionUID 			= 1L;
	public  Long												milliSeconds;
	public  Integer 											hours						= 0;
	public  Integer 											minutes						= 0;
	public  Integer 											seconds						= 0;
	
	public Cmn_Time(String time)
	{
		String[]												timeParts					= time.split(":");
		
		if (timeParts.length > 3)
		{
			// We have an error
		}

		for (int i = 0; i < timeParts.length; i++)
		{
			timeParts[i] = timeParts[i].trim();
			if (timeParts[i].equalsIgnoreCase(""))
			{
				timeParts[i] 																= "0";
			}
			switch (i)
			{
			case 0:												this.hours					= Integer.parseInt(timeParts[i]); break;
			case 1:												this.minutes				= Integer.parseInt(timeParts[i]); break;									
			case 2:												this.seconds				= Integer.parseInt(timeParts[i]); break;									
			}
		}
		Integer x = 3;
		this.milliSeconds																	= (hours * 24 * 3600 + minutes * 60 + seconds) * 1000L;
	}
	public void setTime(Integer hours, Integer minutes)
	{
		setTime(hours, minutes, 0);
	}
	public void setTime(Integer hours, Integer minutes, Integer seconds)
	{
		this.hours																			= hours;
		this.minutes																		= minutes;
		this.seconds																		= seconds;
		this.milliSeconds																	= (hours * 24 * 3600 + minutes * 60 + seconds) * 1000L;
	}
	public String displayShort()
	{
		return String.format("%02d", hours) + ":" + String.format("%02d", minutes); 
	}
	public String displayLong()
	{
		return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);  
	}
}

