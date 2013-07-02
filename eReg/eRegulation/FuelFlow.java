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
	public Boolean		fuelFlowing;
	public Long			timeLastStart;
	public Long			consumptionAccounted;
	public Long			consumptionUnAccounted;

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
		    	consumptionAccounted				= input.readLong();
		    	consumptionUnAccounted				= 0L;
		    }
		    finally
		    {
		    	input.close();
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
		    	output.writeLong(consumptionAccounted + consumptionUnAccounted);
		    	consumptionUnAccounted				= 0L;
		    }
		    finally
		    {
		        output.close();
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
		// Fuel was flowing and now isn't, the burner must have stopped (trip or otherwise)
		// If was wasn't flowing and now is, burner has been started and the 
		// 10 second ventilation period has elapsed
		
		if (fuelFlowing)
		{
			consumptionUnAccounted = timeLastStart - Global.now();
		}
		else
		{
			timeLastStart = 0L;
		}
	}
}
