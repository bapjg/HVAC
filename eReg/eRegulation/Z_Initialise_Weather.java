package eRegulation;


import HVAC_Messages.*;

public class Z_Initialise_Weather
{
	public static void main(String[] args)
	{
		
		//================================================================================================================================
		//
		// Create Weather Object object 
		//
		try
		{
			Ctrl_WeatherData 								weather 					= new Ctrl_WeatherData();
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		//
		//================================================================================================================================
    }
}
