package HVAC_Common;

@SuppressWarnings("serial")
public class Ctrl_Actions_Stop 			extends 					Ctrl__Abstract
{
	private static final long 			serialVersionUID 			= 1L;
	public 	static final int			ACTION_Stop					= 0;
	public 	static final int			ACTION_Restart				= 1;
	public 	static final int			ACTION_Reboot				= 2;
	public 	static final int			ACTION_Reload_Configuration	= 3;
	public 	static final int			ACTION_Reload_Calendars		= 4;
	public 	static final int			ACTION_ShutDown				= 5;
	public 	static final int			ACTION_Debug_Wait			= 6;
	public 	static final int			ACTION_Debug_NoWait			= 7;

	public Ctrl_Actions_Stop()
	{
	}	
	public class Execute 				extends 					Ctrl_Actions_Stop
	{
		private static final long 		serialVersionUID 			= 1L;
		public  int						actionRequest				= 0;
	}
	public class Ack 					extends 					Ctrl_Actions_Stop
	{
		private static final long 		serialVersionUID 			= 1L;
	}
	public class Nack 					extends 					Ctrl_Actions_Stop
	{
		private static final long 		serialVersionUID 			= 1L;
	}

}