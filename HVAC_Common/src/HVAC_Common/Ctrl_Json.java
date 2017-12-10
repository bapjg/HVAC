package HVAC_Common;

import java.util.ArrayList;

import HVAC_Common.Ctrl_Configuration.Boiler;
import HVAC_Common.Ctrl_Configuration.Burner;
import HVAC_Common.Ctrl_Configuration.Circuit;
import HVAC_Common.Ctrl_Configuration.PID_Data;
import HVAC_Common.Ctrl_Configuration.Pump;
import HVAC_Common.Ctrl_Configuration.Relay;
import HVAC_Common.Ctrl_Configuration.Thermometer;
import HVAC_Common.Ctrl_Configuration.Update;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Ctrl_Json 					extends 					Ctrl__Abstract 
{
	public Long dateTime;
	private static final long 					serialVersionUID 			= 1L;
	public static final int 					TYPE_Calendar 				= 1;
	public static final int 					TYPE_Configuration			= 2;
	public  int									type;
	public  String								json;
	
	public Ctrl_Json()
	{
	}
	public Ctrl_Json(int type)
	{
		this.type															= type;
	}
	public class Data							extends 					Ctrl_Json
	{
		private static final long 				serialVersionUID 			= 1L;
	}
	public class Update							extends 					Ctrl_Json
	{
		private static final long 				serialVersionUID 			= 1L;
	}
	public class Request						extends 					Ctrl_Json
	{
		private static final long 				serialVersionUID 			= 1L;
		public Request()
		{
		}
		public Request(int type)
		{
			this.type														= type;
		}
	}
}
