package eRegulation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FuelFlow
{
	public Long			timeLastStart;
	public Long			consumption;
	
	// consumption represents the number of milliseconds of fuel flow
	// fuelFlowing is the programs view of whether fuel is flowing or not
	// whether fuel is flowing or not can only be determined by reading the ADC
	//
	// One should avoid writing fuelflow data to disk too often as SD disks age quickly
	// when the same block is over-written many times
	//
	// We need 2 variables :
	//			- consumtion which has been written to disk
	//			- consumption which has not yet been accounted for
	
	public  FuelFlow()
	{
		try
		{
			InputStream  	file 					= new FileInputStream("FuelConsumed.txt");
			DataInputStream	input  					= new DataInputStream (file);
		    try
		    {
		    	consumption							= input.readLong();
		    }
		    finally
		    {
		    	timeLastStart						= -1L;
		    	input.close();
		    	System.out.println("Fuel input" + consumption);
		    }
		}  
		catch(IOException ex)
		{
			System.out.println("I/O error when reading FuelConsumed.txt");
		}
	}
	public void saveFuelFlow()
    {
		try
		{
			OutputStream 		file 				= new FileOutputStream("FuelConsumed.txt");
		    DataOutputStream 	output 				= new DataOutputStream(file);
		    try
		    {
		    	output.writeLong(consumption);
		    }
		    finally
		    {
		    	timeLastStart						= -1L;		// Is this right
		        output.close();
		    	System.out.println("Fuel close" + consumption);
		    }
		}  
		catch(IOException ex)
		{
			System.out.println("I/O error when writing FuelConsumed.txt");
		}
    }
	public void update()
	{
		// We basically need to detected state changes
		// if timeLastStart = -1 means that on last ADC.Read, fuel was not flowing
		// if timeLastStart > -1 means that on last ADC.Read, fuel was flowing
		
		// Note that on powerup, there is an approximate 10s ventilation time
		// before fuel starts flowing
		
		// We also need a convertion milliseconds of FuelFlow to litres of fuel
		
		System.out.println("FuelFlow/update called");
		if (timeLastStart == -1L)
		{
			System.out.println("FuelFlow/update called branch timeLS = -1");
			// last call here had no fuel flowing
			if (Global.burnerVoltages.isFuelFlowing())			// Fuel has just started to flow
			{
				timeLastStart 						= Global.now();
			}
		}
		else
		{
			System.out.println("FuelFlow/update called branch timeLS <> -1");
			// last call here had fuel flowing
			if (Global.burnerVoltages.isFuelFlowing())
			{
				// Nothing has changed, fuel is still flowing
				// Nothing to do until it stops
			}
			else												// Fuel has just stopped flowing
			{
				consumption							= consumption + Global.now() - timeLastStart;;	
				timeLastStart						= -1L;
			}
		}
    	System.out.println("Fuel update" + consumption);
	}
	public Boolean isFuelFlowing()
	{
		if (timeLastStart == -1L)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}
