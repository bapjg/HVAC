package eRegulation;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Burner
{
	public 	Relay	   		burnerPower;
	public 	Long			fuelFlowTimeCumulated;
	private Long			fuelFlowTimeLastStart;
	private	Boolean			fuelIsFlowing;
	
	public Burner()
	{
		burnerPower									= Global.burnerPower;
		Global.burnerVoltages 						= new ADC();						// ADC measure fuel flow and burner fault

		burnerPower.off();
		fuelFlowTimeCumulated						= 0L;
		fuelIsFlowing								= false;
		
		// Now recover last saved fuel consumption
		try
		{
			InputStream  	file 					= new FileInputStream("FuelConsumed.txt");
			DataInputStream	input  					= new DataInputStream (file);
		    try
		    {
		    	fuelFlowTimeCumulated				= input.readLong();
		    }
		    finally
		    {
		    	input.close();
		    }
		}  
		catch(IOException ex)
		{
			System.out.println("I/O error");
		}
		System.out.println("Fuel Consumed Instanciation : " + fuelFlowTimeCumulated);
	}
	public void powerOn()
	{
		burnerPower.on();
		System.out.println("Fuel Consumed Burner On     : " + fuelFlowTimeCumulated);
		// Fuel flow will be detected in sequencer
	}
	public void powerOff()
	{
		burnerPower.off();
		
		Global.waitMilliSeconds(100);								// Need to wait a bit for relays to work and ADC to get an average
		
		if (checkFuelFlow())		// This updates Fuel Consumption
		{
			System.out.println("Burner.powerOff and fuel is still flowing");
		}
		System.out.println("Fuel Consumed Burner Off    : " + fuelFlowTimeCumulated);
	}
	public void sequencer()
	{
		if (checkFault())
		{
			powerOff();
			System.out.println("A Burner Fault has been detected by the burner Sequencer");
		}
		// Increment fuel flow
		checkFuelFlow();
		// Must also check max temp;
	}
	public Boolean checkFault()
	{
		if (Global.burnerVoltages.isFault())
		{
			LogIt.error("Burner", "checkFault", "Over 4 volts indicates trip");
			return true;
		}
		else
		{
			return false;
		}
	}
	public Boolean checkFuelFlow()
	{
		Boolean fuelFlowDetected = Global.burnerVoltages.isFuelFlowing();
		
		if (fuelFlowDetected)
		{
			if (fuelIsFlowing)
			{
				// This means that fuel flow start has been previously detected
			}
			else
			{
				// Fuel flow has just been detected
				fuelFlowTimeLastStart	= Global.now();
				
			}
			fuelIsFlowing				= true;
			return true;
		}
		else
		{
			if (fuelIsFlowing)
			{
				// This means that fuel flow start has been previously detected
				// Fuel flow has been turned off
				fuelFlowTimeCumulated	= fuelFlowTimeCumulated + Global.now() - fuelFlowTimeLastStart;
				//
				// This is the place to write to disk
				//
				//
				LogIt.saveFuelConsumption(fuelFlowTimeCumulated);
			}
			else
			{
				// Fuel flow was not on and has been detected as still being not on
				// Nothing to do
			}
			fuelIsFlowing				= false;
			return false;
		}
	}
}
