package HVAC_Common;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
//public class Ctrl_Fuel_Consumption 								extends 					Ctrl__Abstract
public class Ctrl_Fuel_Consumption 								extends 					Msg__Abstract
{
	private static final long 									serialVersionUID 			= 1L;
	
	public Ctrl_Fuel_Consumption()
	{
	}
	public static class Data 									extends 					Ctrl_Fuel_Consumption
	{
		private static final long 								serialVersionUID 			= 1L;
		public Long												dateTime;
		public Long												fuelConsumed;
	}
	public class Request 										extends 					Ctrl_Fuel_Consumption
	{
		private static final long 								serialVersionUID 			= 1L;
	}
	public class Update 										extends 					Ctrl_Fuel_Consumption.Data
	{
		private static final long 								serialVersionUID 			= 1L;
	}
//	public class Ack 													extends 					Ctrl_Fuel_Consumption
//	{
//		private static final long 										serialVersionUID 			= 1L;
//	}
//	public class Nack 													extends 					Ctrl_Fuel_Consumption
//	{
//		private static final long 										serialVersionUID 			= 1L;
//		public String													errorMessage;
//		public Nack (String errorMessage)
//		{
//			this.errorMessage														= errorMessage;
//		}
//	}
}
