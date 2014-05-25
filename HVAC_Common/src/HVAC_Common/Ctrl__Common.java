package HVAC_Common;

public class Ctrl__Common  						extends 					Ctrl__Abstract 
{
	private static final long 					serialVersionUID 			= 1L;
	
	@SuppressWarnings("serial")
	public class Ack 							extends 					Ctrl__Common
	{
	}
	@SuppressWarnings("serial")
	public class Nack 							extends 					 Ctrl__Common
	{
	}
	@SuppressWarnings("serial")
	public class NoConnection 					extends 					 Ctrl__Common
	{
	}
	@SuppressWarnings("serial")
	public class Time	 						extends 					 Ctrl__Common
	{
		String									value;
		Long									timeSinceMidnight;
		
		public void Time(String value)
		{
			this.value														= value;
			this.timeSinceMidnight											= 33L;
		}
	}
	public class Date	 						extends 					 Ctrl__Common
	{
		Long									value;
		
		public void Date(Long value)
		{
			this.value														= value;
		}
	}

}