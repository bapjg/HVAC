package com.bapjg.hvac_client;

import com.bapjg.hvac_client.Mgmt_Msg_Configuration.Mgmt_Msg_Relay;
import com.bapjg.hvac_client.Mgmt_Msg_Configuration.Mgmt_Msg_Thermometer;

public class Mgmt_Msg_Calendars   extends Mgmt_Msg_Abstract
{
	private static final long 						serialVersionUID 			= 1098L;

	public ArrayList<Mgmt_Msg_Cal_Circuit> 			circuitList 				= new ArrayList<Mgmt_Msg_Cal_Circuit>();
	
	public Mgmt_Msg_Calendars ()
	{

	}
	public class Mgmt_Msg_Cal_Circuit
	{
		public String								circuitName;
		public ArrayList<Mgmt_Msg_Cal_Task> 		circuitList 				= new ArrayList<Mgmt_Msg_Cal_Task>();
		
		public Mgmt_Msg_Cal_Circuit ()
		{
		}
	}
	public class Mgmt_Msg_Cal_Task
	{
		public String								days;
		public Long									timeStart;
		public Long  								timeEnd;
		public Integer								tempObjective;
		public Boolean								stopOnObjective;
		
		public Mgmt_Msg_Cal_Task()
		{
		}

	}
}
