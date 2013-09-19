package com.bapjg.hvac_client;

import java.util.ArrayList;

import eRegulation.Relay;

public class Mgmt_Msg_Configuration extends Mgmt_Msg_Abstract
{
	private static final long 				serialVersionUID 			= 1099L;
	 
	public ArrayList<Mgmt_Msg_Relay> 		relayList 					= new ArrayList<Mgmt_Msg_Relay>();
	public ArrayList<Mgmt_Msg_Thermometer> 	thrmometreList 				= new ArrayList<Mgmt_Msg_Thermometer>();
	public ArrayList<Mgmt_Msg_Circuit> 		circuitList 				= new ArrayList<Mgmt_Msg_Circuit>();
	public ArrayList<Mgmt_Msg_Pump> 		pumpList 					= new ArrayList<Mgmt_Msg_Pump>();
	public Mgmt_Msg_Params 					params;
	
	
	
	public Mgmt_Msg_Configuration ()
	{
	}

	Public class Mgmt_Msg_Relay
	{
		public String 						name;
		public String 						friendlyName;
		public int 							relayBank;
		public int 							relayNumber;
		
		Public  Mgmt_Msg_Relay ()
		{
		}
		
	}
	Public class Mgmt_Msg_Thermometer
	{
		public String 						thermoFile;
		public String 						thermoRadical;
		public String 						name;

	 	Public  Mgmt_Msg_Thermometer ()
		{
		}

	}
	Public class Mgmt_Msg_Pump
	{
		public String 						name;
		public Relay						relay;

		Public  Mgmt_Msg_Pump ()
		{
		}

	}
	Public class Mgmt_Msg_Circuit
	{
		public String 					name;
		public String 					friendlyName;
		public String 					circuitType;
		public Integer 					tempMax;

		Public  Mgmt_Msg_Circuit()
		{
		}

	}
	Public class Mgmt_Msg_Params
	{
		public Integer					summerTemp;
		public Integer 					summerPumpDuration;
		public String 					summerPumpTime;

		Public  Mgmt_Msg_Params()
		{
		}

	}

}
