package eRegulation;


@SuppressWarnings("serial")
public class Ctrl_Temperatures extends Ctrl_Abstract
{
	private static final long 			serialVersionUID 			= 1L;

	public Ctrl_Temperatures()
	{
	}
	
	public class Request extends Ctrl_Temperatures
	{
	}
	public class Response extends Ctrl_Temperatures
	{
		private static final long 		serialVersionUID 			= 1L;
		public Long						dateTime;
		public String					date;
		public String					time;
		
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
