package com.bapjg.hvac_client;

import java.util.ArrayList;

public class Mgmt_Msg_Calendar extends Mgmt_Msg_Abstract
{
	public class Data extends Mgmt_Msg_Abstract
	{
		public String										dateTime;
		public ArrayList <Circuit> 							circuitList 				= new ArrayList <Circuit>();
		
		public class Circuit
		{
			public String									circuitName;
			public ArrayList <Task> 						taskList 					= new ArrayList <Task>();
			
			public class Task
			{
				public String								days;
				public Long									timeStart;
				public Long  								timeEnd;
				public Integer								tempObjective;
				public Boolean								stopOnObjective;
			}
		}
	}
	public class Request
	{
	}
	public class Update extends Data
	{
	}
	public class Ack  extends Mgmt_Msg_Abstract
	{
	}
	public class Nack extends Mgmt_Msg_Abstract
	{
	}
}
