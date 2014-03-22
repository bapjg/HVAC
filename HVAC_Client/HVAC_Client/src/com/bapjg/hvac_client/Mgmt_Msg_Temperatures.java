package com.bapjg.hvac_client;

public class Mgmt_Msg_Temperatures   extends Mgmt_Msg_Abstract
{
	public class Data   extends Mgmt_Msg_Abstract
	{
		public  Long				dateTime;
		public  String				date;
		public  String				time;
		public  Integer				tempHotWater;
		public  Integer				tempBoiler;
		public  Integer				tempBoilerIn;
		public  Integer				tempBoilerOut;
		public  Integer				tempFloorIn;
		public  Integer				tempFloorOut;
		public  Integer				tempRadiatorOut;
		public  Integer				tempRadiatorIn;
		public  Integer				tempOutside;
		public  Integer				tempLivingRoom;
	}
	public class Request   extends Mgmt_Msg_Abstract
	{
	}
}
