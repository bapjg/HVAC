package eRegulation;

public class Message_Fuel extends Message_Abstract
{
	private static final long 		serialVersionUID 			= 1L;
	
	public Message_Fuel()
	{
	}
	public class Data extends Message_Abstract
	{
		private static final long 	serialVersionUID 			= 1L;
		public Long					dateTime;
		public Long					fuelConsumed;
	}
	public class Request extends Message_Abstract
	{
	}
	public class Update extends Data
	{
	}
}
