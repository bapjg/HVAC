package HVAC_Common;

import java.text.SimpleDateFormat;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
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
		this.milliSeconds																	= (hours * 3600 + minutes * 60 + seconds) * 1000L;
	}
	public Cmn_Time(Long longTime)
	{
		setTime(longTime);
	}
	public void setTime(Integer hours, Integer minutes)
	{
		setTime(hours, minutes, 0);
	}
	public void setTime(Long milliSeconds)
	{
		Long													seconds						= milliSeconds/1000L;
		Long													hoursLong					= seconds/3600L;
		this.hours																			= hoursLong.intValue();
		Long													secondsRemaining			= seconds + this.hours*3600L;
		Long													minutesLong					= secondsRemaining/60L;
		this.minutes																		= minutesLong.intValue();
		this.seconds																		= 0;
		this.milliSeconds																	= (this.hours * 3600 +this. minutes * 60 + this.seconds) * 1000L;
	}
	public void setTime(Integer hours, Integer minutes, Integer seconds)
	{
		this.hours																			= hours;
		this.minutes																		= minutes;
		this.seconds																		= seconds;
		this.milliSeconds																	= (hours * 3600 + minutes * 60 + seconds) * 1000L;
	}
	public String displayShort()															// Display hh:mm
	{
		return String.format("%02d", hours) + ":" + String.format("%02d", minutes); 
	}
	public String displayLong()																// Display hh:mm:ss
	{
		return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);  
	}
}

