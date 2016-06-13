package HVAC_Common;

public class Ctrl_Thermo_List 							extends 					Ctrl__Abstract
{
	private static final long 							serialVersionUID 			= 1L;
	
	public Ctrl_Thermo_List()
	{
	}
	public class Data 									extends 					Ctrl_Thermo_List
	{
		private static final long 						serialVersionUID 			= 1L;
		public String[]									thermoAddresses;
	}
	public class Request 								extends 					Ctrl_Thermo_List
	{
		private static final long 						serialVersionUID 			= 1L;
	}
	public class Nack 									extends 					Ctrl_Thermo_List
	{
		private static final long 						serialVersionUID 			= 1L;
		public String									errorMessage;
		public Nack (String errorMessage)
		{
			this.errorMessage														= errorMessage;
		}
	}
}
