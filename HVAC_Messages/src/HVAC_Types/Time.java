package HVAC_Types;

import java.text.SimpleDateFormat;
import java.util.Date;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Time 												implements 					java.io.Serializable
{
	Long	milliSeconds;
	Integer hours;
	Integer minutes;
	Integer seconds;
	
	public void Time(String time)
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
				timeParts[i] = "0";
			}
			switch (i)
			{
			case 1:
				this.hours																	= Integer.parseInt(timeParts[i]);
				if (this.hours > 23)
				{
					// We have an error
				}
			case 2:
				this.minutes																= Integer.parseInt(timeParts[i]);									
				if (this.minutes > 59)
				{
					// We have an error
				}
			case 3:
				this.seconds																= Integer.parseInt(timeParts[i]);									
				if (this.seconds > 59)
				{
					// We have an error
				}
			}
		}
		this.milliSeconds																	= (hours * 24 * 3600 + minutes * 60 + seconds) * 1000L;
	}
	public String displayShort()
	{
		return new SimpleDateFormat("HH:mm").format(milliSeconds);
	}
	public String displayLong()
	{
		return new SimpleDateFormat("HH:mm:ss").format(milliSeconds);
	}
}
