package eRegulation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import HVAC_Common.*;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class FuelFlow
{
	public Long			timeLastStart;
	public Long			consumption;
	
	// consumption represents the number of milliseconds of fuel flow
	// fuelFlowing is the program's view of whether fuel is flowing or not
	// whether fuel is flowing or not can only be determined by reading the ADC
	//
	// One should avoid writing fuelflow data to disk too often as SD disks age quickly
	// when the same block is over-written many times
	//
	// We need 2 variables :
	//			- consumption which has been written to disk
	//			- consumption which has not yet been accounted for (ie time since timeLastStart)
	
	public  FuelFlow()
	{
		if (!Global.httpSemaphore.semaphoreLock("LogIt.fuelData"))
		{
			LogIt.info("Fuelflow", "constructor", "Lock timedout, owned by " + Global.httpSemaphore.owner);
			return;
		}

		HTTP_Request											httpRequest					= new HTTP_Request <Ctrl_Fuel_Consumption.Request> ("Management");
		Ctrl_Fuel_Consumption.Request	 						messageSend 				= (new Ctrl_Fuel_Consumption()).new Request();
		Msg__Abstract 											messageReceive	 			= httpRequest.sendData(messageSend);
			
		Global.httpSemaphore.semaphoreUnLock();			
		
		Long 													consumptionRemote			= -1L;
		Long 													consumptionLocal			= -1L;
		Long	 												tlmRemote					= -1L;
		Long	 												tlmLocal					= -1L;
		
		if (messageReceive instanceof Ctrl_Fuel_Consumption.Data)
		{
			LogIt.info("Fuelflow", "constructor", "Fuel level recovered from network");
			consumptionRemote																= ((Ctrl_Fuel_Consumption.Data) messageReceive).fuelConsumed;
			tlmRemote																		= ((Ctrl_Fuel_Consumption.Data) messageReceive).dateTime;
	    	timeLastStart																	= -1L;
		}
		LogIt.info("Fuelflow", "constructor", "Recovering from local file");
		try
		{
			File  												tlmFile 					= new File("/home/pi/HVAC_Data/FuelConsumed.txt");
			tlmLocal																		= tlmFile.lastModified();
			InputStream  										file 						= new FileInputStream("/home/pi/HVAC_Data/FuelConsumed.txt");
			DataInputStream										input  						= new DataInputStream (file);
		    try
		    {
		    	consumptionLocal															= input.readLong();
		    }
		    finally
		    {
		    	timeLastStart																= -1L;
		    	input.close();
		    }
		}  
		catch(FileNotFoundException ex)
		{
			// Probably using new SDCard. Just create the file
			LogIt.info("Fuelflow", "constructor", "FuelConsumed.txt not found : creating it");
			consumption																		= 0L;
			saveFuelFlow();
		}
		catch(IOException ex)
		{
			LogIt.info("Fuelflow", "constructor", "I/O error when reading FuelConsumed.txt : " + ex);
		}			

		if ((tlmRemote == -1) && (tlmLocal == -1))
		{
			LogIt.info("Fuelflow", "constructor", "FuelConsumed failed from Network and local copy");
			consumption																		= 0L;
		}
		else if ((tlmRemote == -1) && (tlmLocal > -1))
		{
			LogIt.info("Fuelflow", "constructor", "FuelConsumed failed from Network but available locally");
			consumption																		= consumptionLocal;
		}
		else if ((tlmRemote > -1) && (tlmLocal == -1))
		{
			LogIt.info("Fuelflow", "constructor", "FuelConsumed failed locally but available from Network");
			consumption																		= consumptionRemote;
		}
		else if (tlmRemote < tlmLocal)
		{
			LogIt.info("Fuelflow", "constructor", "FuelConsumed locally is more recent than from Network, using Local");
			consumption																		= consumptionLocal;
		}
		else // Network is higher or both are equal
		{
			LogIt.info("Fuelflow", "constructor", "FuelConsumed locally and network coherent (Network TLM >= Local TLM)");
			LogIt.info("Fuelflow", "constructor", "FuelConsumed value is " + consumptionRemote.toString());
			consumption																		= consumptionRemote;
		}
	}
	public void switchedOn()
	{
		// We basically need to detected state changes
		// if     fuelFlowing  & timeLastStart = -1 means that we have just powered On
		// if Not(fuelFlowing) & timeLastStart > -1 means that we have just powered Off
		
		// TODO We also need a conversion milliseconds of FuelFlow to litres of fuel
		
		if (timeLastStart == -1L)
		{
			timeLastStart 																= Global.DateTime.now();
		}
		else
		{
			LogIt.error("FuelFlow", "switchedOn", "timeLast is alreay set when it shouldn't be (powerOn)");
		}
	}
	public void switchedOff()
	{
		// We basically need to detected state changes
		// if     fuelFlowing & timeLastStart  = -1 means that we have just powered On
		// if Not(fuelFlowing) & timeLastStart > -1 means that we have just powered Off
		
		// TODO We also need a conversion milliseconds of FuelFlow to litres of fuel
		
		if (timeLastStart == -1L)
		{
			// This can happen when controller is stopped, as called to ensure burner is not running
		}
		else
		{
			Long 										thisBurn						= Global.DateTime.now() - timeLastStart;
			consumption																	= consumption + thisBurn;
			timeLastStart																= -1L;
			saveFuelFlow();
			LogIt.fuelData(consumption);
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
