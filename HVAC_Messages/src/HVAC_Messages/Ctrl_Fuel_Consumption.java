package HVAC_Messages;

@SuppressWarnings("serial")
public class Ctrl_Fuel_Consumption extends Ctrl_Abstract
{
	private static final long 			serialVersionUID 			= 1L;

	public Ctrl_Fuel_Consumption()
	{
	}
	
	public class Request extends Ctrl_Fuel_Consumption
	{
	}
	public class Data extends Ctrl_Fuel_Consumption
	{
		private static final long 		serialVersionUID 			= 1L;
		public Long						dateTime;
		public Long 					consumption;
	}
}
