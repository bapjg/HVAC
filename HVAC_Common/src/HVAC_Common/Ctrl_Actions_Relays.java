package HVAC_Common;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
@SuppressWarnings("serial")
//public class Ctrl_Actions_Relays 		extends 					Ctrl__Abstract
public class Ctrl_Actions_Relays 		extends 					Msg__Abstract
{
	private static final long 			serialVersionUID 			= 1L;
	public 	static final int			RELAY_Unchanged				= 0;
	public 	static final int			RELAY_On 					= 1;
	public 	static final int			RELAY_Off 					= 2;

	public Ctrl_Actions_Relays()
	{
	}	
	public class Request 				extends 					Ctrl_Actions_Relays
	{
	}
	public class Data 					extends 					Ctrl_Actions_Relays
	{
		private static final long 		serialVersionUID 			= 1L;
		public Boolean					burner;
		public Boolean					pumpHotWater;
		public Boolean 					pumpFloor;
		public Boolean 					pumpRadiator;
	}
	public class Execute 				extends 					Ctrl_Actions_Relays
	{
		private static final long 		serialVersionUID 			= 1L;
		public String					relayName;
		public Integer					relayAction;
	}
//	public class Ack 					extends 					Ctrl_Actions_Relays
//	{
//		private static final long 		serialVersionUID 			= 1L;
//	}
}