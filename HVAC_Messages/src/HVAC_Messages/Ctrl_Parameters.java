package HVAC_Messages;

import java.util.ArrayList;


@SuppressWarnings("serial")
public class Ctrl_Parameters 			extends 					Ctrl_Abstract
{
	private static final long 			serialVersionUID 			= 1L;

	public Ctrl_Parameters()
	{
	}
	
	public class Request 				extends 					Ctrl_Parameters
	{
	}
	public class Data 					extends 					Ctrl_Parameters
	{
		private static final long 		serialVersionUID 			= 1L;
		public ArrayList<Thermometer> 	thermometerList 			= new ArrayList<Thermometer>();
		public ArrayList<Relay> 		relayList 					= new ArrayList<Relay>();
		public ArrayList<Pump> 			pumpList 					= new ArrayList<Pump>();
	}
	public class Update 				extends 					Ctrl_Parameters
	{
		private static final long 		serialVersionUID 			= 1L;
		public ArrayList<Thermometer> 	thermometerList 			= new ArrayList<Thermometer>();
		public ArrayList<Relay> 		relayList 					= new ArrayList<Relay>();
		public ArrayList<Pump> 			pumpList 					= new ArrayList<Pump>();
	}
	public class Thermometer 			extends  					Ctrl_Parameters
	{
		private static final long 		serialVersionUID 			= 1L;
		public String 					name;
		public String 					address;
	}
	public class Relay 					extends  					Ctrl_Parameters
	{
		private static final long 		serialVersionUID 			= 1L;
		public String 					name;
		public Integer 					relayBank;
		public Integer 					relayNumber;
	}
	public class Pump 					extends  					Ctrl_Parameters
	{
		private static final long 		serialVersionUID 			= 1L;
		public String 					name;
	}
}
