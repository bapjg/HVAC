package eRegulation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import HVAC_Common.*;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
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
		if (!Global.httpSemaphore.semaphoreLock("LogIt.fuelData"))
		{
			LogIt.info("Fuelflow", "constructor", "Lock timedout, owned by " + Global.httpSemaphore.owner);
			return;
		}

		HTTP_Request											httpRequest					= new HTTP_Request <Ctrl_Fuel_Consumption.Request> ("Management");
		Ctrl_Fuel_Consumption.Request	 						messageSend 				= (new Ctrl_Fuel_Consumption()).new Request();
		Ctrl__Abstract 											messageReceive	 			= httpRequest.sendData(messageSend);
			
		Global.httpSemaphore.semaphoreUnLock();			

		if (messageReceive instanceof Ctrl_Fuel_Consumption.Data)
		{
			LogIt.info("Fuelflow", "constructor", "Fuel level recovered from network");
			consumption																		= ((Ctrl_Fuel_Consumption.Data) messageReceive).fuelConsumed;
	    	timeLastStart																	= -1L;		// Is this right
		}
		else
		{
			LogIt.info("Fuelflow", "constructor", "Network failed, recovering from local file");
			try
			{
				InputStream  									file 						= new FileInputStream("/home/pi/HVAC_Data/FuelConsumed.txt");
				DataInputStream									input  						= new DataInputStream (file);
			    try
			    {
			    	consumption																= input.readLong();
			    }
			    finally
			    {
			    	timeLastStart															= -1L;
			    	input.close();
			    }
			}  
			catch(FileNotFoundException ex)
			{
				LogIt.info("Fuelflow", "constructor", "FuelConsumed.txt not found : creating it");
	    
				consumption																	= 0L;
				saveFuelFlow();
			}
			catch(IOException ex)
			{
				LogIt.info("Fuelflow", "constructor", "I/O error when reading FuelConsumed.txt : " + ex);
			}			
		}
	}
	public void update(Boolean fuelFlowing)
	{
		// We basically need to detected state changes
		// if timeLastStart = -1 means that on last Update call, fuel was not flowing
		// if timeLastStart > -1 means that on last Update call, fuel was flowing
		
		// We also need a convertion milliseconds of FuelFlow to litres of fuel
		
		if (timeLastStart == -1L)
		{
			// last call here had no fuel flowing
			if (fuelFlowing)																// Fuel has just started to flow
			{
				timeLastStart 																= Global.DateTime.now();
			}
		}
		else
		{
			// last call here had fuel flowing

			if (fuelFlowing)
			{
				// Nothing has changed, fuel is still flowing
				// Nothing to do until it stops
			}
			else																// Fuel has just stopped flowing
			{
				// Just log consumption now
				consumption																	= consumption + Global.DateTime.now() - timeLastStart;
				timeLastStart																= -1L;
				saveFuelFlow();
				LogIt.fuelData(consumption);
			}
		}
	}
	public void saveFuelFlow()
    {
		try
		{
			OutputStream 										file 						= new FileOutputStream("/home/pi/HVAC_Data/FuelConsumed.txt");
		    DataOutputStream 									output 						= new DataOutputStream(file);
		    try
		    {
		    	output.writeLong(consumption);
		    	output.writeByte(13);							// Carriage return
		    	output.writeByte(10);							// Line Feed
		    	output.writeChars(consumption.toString());		//And the same again in readable format
		    }
		    finally
		    {
		        output.close();
		    }
		}  
		catch(IOException ex)
		{
			LogIt.info("Fuelflow", "saveFuelFlow", "I/O error when writing FuelConsumed.txt : " + ex);
		}
    }
}
