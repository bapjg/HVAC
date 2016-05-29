package HVAC_Common;

public class Ctrl_Thermometer_List 							extends 					Ctrl__Abstract
{
	private static final long 							serialVersionUID 			= 1L;
	
	public Ctrl_Thermometer_List()
	{
	}
	public class Data 									extends 					Ctrl_Thermometer_List
	{
		private static final long 						serialVersionUID 			= 1L;
		public String[]									thermoAddress;
		public Integer xx;
	}
	public class Request 								extends 					Ctrl_Thermometer_List
	{
		private static final long 						serialVersionUID 			= 1L;
	}
	public class Nack 									extends 					Ctrl_Thermometer_List
	{
		private static final long 						serialVersionUID 			= 1L;
		public String									errorMessage;
		public Nack (String errorMessage)
		{
			this.errorMessage														= errorMessage;
		}
	}
}
