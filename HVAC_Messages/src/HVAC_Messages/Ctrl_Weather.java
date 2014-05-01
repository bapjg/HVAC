package HVAC_Messages;


public class Ctrl_Weather 								extends 					Ctrl_Abstract
{
	private static final long 							serialVersionUID 			= 1L;
	
	public Ctrl_Weather()
	{
	}
	public static class Data 							extends 					Ctrl_Weather
	{
		private static final long 						serialVersionUID 			= 1L;
		public Ctrl_WeatherData							weatherData;
		public Long										fuelConsumed;
	}
	public class Request 								extends 					Ctrl_Weather
	{
	}
}
