package com.bapjg.hvac_client;

public class Mgmt_Msg_Temperatures   extends Mgmt_Msg_Abstract
{
	private static final long 		serialVersionUID 			= 1091L;
	
	public  String					dateTime;
	public  Integer					tempBoiler;
	public  Integer					tempHotWater;
	public  Integer					tempBoilerIn;
	public  Integer					tempFloorOut;
	public  Integer					tempFloorCold;
	public  Integer					tempFloorHot;
	public  Integer					tempRadiatorOut;
	public  Integer					tempRadiatorIn;
	public  Integer					tempOutside;
	public  Integer					tempLivingRoom;
	
	public class Data
	{
		public  String				dateTime;
		public  Integer				tempBoiler;
		public  Integer				tempHotWater;
		public  Integer				tempBoilerIn;
		public  Integer				tempFloorOut;
		public  Integer				tempFloorCold;
		public  Integer				tempFloorHot;
		public  Integer				tempRadiatorOut;
		public  Integer				tempRadiatorIn;
		public  Integer				tempOutside;
		public  Integer				tempLivingRoom;
	}
	public class Request
	{
	}

}
