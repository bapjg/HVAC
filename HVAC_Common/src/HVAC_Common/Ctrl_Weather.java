package HVAC_Common;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Ctrl_Weather 								extends 					Ctrl__Abstract
{
	private static final long 							serialVersionUID 			= 1L;
	
	public Ctrl_Weather()
	{
	}
	public class Data 									extends 					Ctrl_Weather
	{
		private static final long 						serialVersionUID 			= 1L;
		public Ctrl_WeatherData							weatherData;
	}
	public class Request 								extends 					Ctrl_Weather
	{
		private static final long 						serialVersionUID 			= 1L;
	}
	public class Ack	 								extends 					Ctrl_Weather
	{
		private static final long 						serialVersionUID 			= 1L;
	}
	public class Nack	 								extends 					Ctrl_Weather
	{
		private static final long 						serialVersionUID 			= 1L;
	}
}
