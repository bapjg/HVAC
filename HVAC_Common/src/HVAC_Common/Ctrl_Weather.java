package HVAC_Common;

public class Ctrl_Weather 								extends 					Ctrl__Abstract
{
	private static final long 							serialVersionUID 			= 10L;
	
	public Ctrl_Weather()
	{
	}
	public class Data 									extends 					Ctrl_Weather
	{
		private static final long 						serialVersionUID 			= 10L;
		public Ctrl_WeatherData							weatherData;
	}
	public class Request 								extends 					Ctrl_Weather
	{
		private static final long 						serialVersionUID 			= 10L;
	}
	public class Ack	 								extends 					Ctrl_Weather
	{
		private static final long 						serialVersionUID 			= 10L;
	}
	public class Nack	 								extends 					Ctrl_Weather
	{
		private static final long 						serialVersionUID 			= 10L;
	}
}
