package HVAC_Messages;

import java.util.ArrayList;

import HVAC_Messages.Ctrl_Configuration_New.Boiler;
import HVAC_Messages.Ctrl_Configuration_New.Burner;
import HVAC_Messages.Ctrl_Configuration_New.Circuit;
import HVAC_Messages.Ctrl_Configuration_New.PID_Data;
import HVAC_Messages.Ctrl_Configuration_New.Pump;
import HVAC_Messages.Ctrl_Configuration_New.Relay;
import HVAC_Messages.Ctrl_Configuration_New.Thermometer;

public class Ctrl_Calendars 					extends 					Ctrl_Abstract 
{
	public Long dateTime;
	private static final long 					serialVersionUID 			= 1L;
	
	public Ctrl_Calendars()
	{
		
	}
	public static class Data					extends 					Ctrl_Calendars
	{
		private static final long 				serialVersionUID 			= 1L;
		public ArrayList<Word> 					WordList 					= new ArrayList<Word>();

	}
	public static class Word					extends 					Ctrl_Calendars
	{
		private static final long 				serialVersionUID 			= 1L;
		public String 							word;
		public String							days;
		
	}


}
