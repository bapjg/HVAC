package eRegulation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import HVAC_Messages.Ctrl_Abstract;
import HVAC_Messages.Ctrl_Fuel_Consumption;
import HVAC_Messages.Ctrl_Fuel_Consumption.Update;

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
			System.out.println(LogIt.dateTimeStamp() + " Fuelflow.Constructor Lock timedout, owned by " + Global.httpSemaphore.owner);
			return;
		}

		HTTP_Request							httpRequest			= new HTTP_Request <Ctrl_Fuel_Consumption.Request> ("Management");
			
		Ctrl_Fuel_Consumption.Request	 		messageSend 		= (new Ctrl_Fuel_Consumption()).new Request();
			
		Ctrl_Abstract 							messageReceive	 	= httpRequest.sendData(messageSend);
			
		Global.httpSemaphore.semaphoreUnLock();			

		if (messageReceive instanceof Ctrl_Fuel_Consumption.Data)
		{
			System.out.println(LogIt.dateTimeStamp() + " Fuelflow.Constructor Fuel level recovered from network");
			consumption												= ((Ctrl_Fuel_Consumption.Data) messageReceive).fuelConsumed;
	    	timeLastStart											= -1L;		// Is this right
		}
		else
		{
			System.out.println(LogIt.dateTimeStamp() + " Fuelflow.Constructor Network failed, recovering from local file");
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
			    }
			}  
			catch(FileNotFoundException ex)
			{
				System.out.println(" Fuelflow.Constructor File FuelConsumed.txt not found : creating it");
	    
				consumption								= 0L;
				saveFuelFlow();
			}
			catch(IOException ex)
			{
				System.out.println(" Fuelflow.Constructor I/O error when reading FuelConsumed.txt");
			}			
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
		
		if (timeLastStart == -1L)
		{
			// last call here had no fuel flowing
			if (Global.burnerVoltages.isFuelFlowing())			// Fuel has just started to flow
			{
				timeLastStart 						= Global.now();
			}
		}
		else
		{
			// last call here had fuel flowing
			if (Global.burnerVoltages.isFuelFlowing())
			{
				// Nothing has changed, fuel is still flowing
				// Nothing to do until it stops
				LogIt.fuelData(consumption + Global.now() - timeLastStart);
			}
			else												// Fuel has just stopped flowing
			{
				// There is a case for just logging consumption at end point rather than evry few seconds
				consumption							= consumption + Global.now() - timeLastStart;
				timeLastStart						= -1L;
				saveFuelFlow();
				LogIt.fuelData(consumption);
			}
		}
		
		if (timeLastStart == -1L)
		{
			// System.out.println("FuelFlow/update : exit with consumption : " + consumption + " unaccounted : " + 0);
		}
		else
		{
			// Long unaccounted = Global.now() - timeLastStart;
			// System.out.println("FuelFlow/update : exit with consumption : " + consumption + " unaccounted : " + unaccounted);
		}
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
