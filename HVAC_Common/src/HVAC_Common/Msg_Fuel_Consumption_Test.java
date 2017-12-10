package HVAC_Common;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Msg_Fuel_Consumption_Test 							extends 					Msg__Abstract_Test
{
	private static final long 									serialVersionUID 			= 1L;

	public Msg_Fuel_Consumption_Test()
	{
	}
	public static class Data 									extends 					Msg_Fuel_Consumption_Test
	{
		private static final long 								serialVersionUID 			= 1L;
		public Long												dateTime;
		public Long												fuelConsumed;
	}
	public class Request 										extends 					Msg_Fuel_Consumption_Test
	{
		private static final long 								serialVersionUID 			= 1L;
	}
	public class Update 										extends 					Msg_Fuel_Consumption_Test.Data
	{
		private static final long 								serialVersionUID 			= 1L;
	}
}
