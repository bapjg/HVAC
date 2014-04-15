package HVAC_Messages;

@SuppressWarnings("serial")
public class Ctrl_Actions_Test_Mail 		extends 				Ctrl_Abstract
{
	private static final long 			serialVersionUID 			= 1L;
	public 	static final int			RELAY_Unchanged				= 0;
	public 	static final int			RELAY_On 					= 1;
	public 	static final int			RELAY_Off 					= 2;

	public Ctrl_Actions_Test_Mail()
	{
	}	
	public class Execute 				extends 					Ctrl_Actions_Test_Mail
	{
		private static final long 		serialVersionUID 			= 1L;
	}
	public class Ack 					extends 					Ctrl_Actions_Test_Mail
	{
		private static final long 		serialVersionUID 			= 1L;
	}
}