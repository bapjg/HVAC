package com.bapjg.hvac_client;

import java.util.ArrayList;

public class Mgmt_Msg_Configuration extends Mgmt_Msg_Abstract
{
	public class Data
	{
		public ArrayList <Relay> 				relayList 					= new ArrayList <Relay>();
		public ArrayList <Thermometer> 			thermometerList 			= new ArrayList <Thermometer>();
		public ArrayList <Circuit> 				circuitList 				= new ArrayList <Circuit>();
		public ArrayList <Pump> 				pumpList 					= new ArrayList <Pump>();
		public Params 							params;

		public class Relay
		{
			public String 						name;
			public String 						friendlyName;
			public int 							relayBank;
			public int 							relayNumber;
		}
		public class Thermometer
		{
			public String 						name;
			public String 						friendlyName;
			public String 						thermoID;
		}
		public class Pump
		{
			public String 						name;
			public String 						friendlyName;
			public Relay						relay;
		}
		public class Circuit
		{
			public String 						name;
			public String 						friendlyName;
			public String 						circuitType;
			public Integer 						tempMax;
		}
		public class Params
		{
			public Integer						summerTemp;
			public Integer 						summerPumpDuration;
			public String 						summerPumpTime;
		}
	}
	public class Request
	{
	}
	public class Update extends Data
	{
	}
	public class Ack
	{
	}
	public class Nack
	{
	}
}
