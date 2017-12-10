package HVAC_Common;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
@SuppressWarnings("serial")
public class Ctrl_Temperatures extends Ctrl__Abstract
{
	private static final long 			serialVersionUID 			= 1L;

	public Ctrl_Temperatures()
	{
	}
	
	public class Request extends Ctrl_Temperatures
	{
	}
	public class Data extends Ctrl_Temperatures
	{
		private static final long 		serialVersionUID 			= 1L;
		public Long						dateTime;
		
		public Integer 					tempBoiler;
		public Integer 					tempBoilerIn;
		public Integer 					tempBoilerOut;
		
		public Integer 					tempFloorIn;
		public Integer 					tempFloorOut;
		
		public Integer 					tempRadiatorOut;
		public Integer 					tempRadiatorIn;

		public Integer 					tempHotWater;
		public Integer 					tempOutside;
		public Integer 					tempLivingRoom;
	}
}
