package HVAC_Messages;

@SuppressWarnings("serial")
public class Ctrl_Actions_Stop 			extends 					Ctrl_Abstract
{
	private static final long 			serialVersionUID 			= 1L;
	public 	static final int			EXIT_Stop					= 0;
	public 	static final int			EXIT_Restart				= 1;
	public 	static final int			EXIT_Reboot					= 2;
//	public 	static final int			EXIT_Reload_Configuration	= 3;
	public 	static final int			EXIT_Reload_Calendars		= 3;

	public Ctrl_Actions_Stop()
	{
	}	
	public class Execute 				extends 					Ctrl_Actions_Stop
	{
		private static final long 		serialVersionUID 			= 1L;
		public  int						exitStatus					= 0;
	}
	public class Ack 					extends 					Ctrl_Actions_Stop
	{
		private static final long 		serialVersionUID 			= 1L;
	}
}