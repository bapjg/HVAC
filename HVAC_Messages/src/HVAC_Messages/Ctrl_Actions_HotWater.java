package HVAC_Messages;

@SuppressWarnings("serial")
public class Ctrl_Actions_HotWater extends Ctrl_Abstract
{
	private static final long 			serialVersionUID 			= 1L;

	public Ctrl_Actions_HotWater()
	{
	}
	
	public class Request extends Ctrl_Actions_HotWater
	{
	}
	public class Data extends Ctrl_Actions_HotWater
	{
		private static final long 		serialVersionUID 			= 1L;
		public Long						dateTime;
		
		public Integer 					targetTemp;
	}
	public class Update extends Ctrl_Actions_HotWater
	{
		private static final long 		serialVersionUID 			= 1L;
		public Long						dateTime;
		
		public Integer 					targetTemp;
	}
}