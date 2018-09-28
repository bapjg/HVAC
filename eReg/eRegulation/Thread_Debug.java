package eRegulation;
 
import java.util.Set;

import HVAC_Common.*;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Thread_Debug 										implements 					Runnable
{
	public void run()
    {
		LogIt.info("Thread_Debug", "Run", "Starting");            

		try
		{
			Thermometers										thermos						= Global.thermometers;
			Relays												relays						= Global.relays;
			Circuits 											circuits					= Global.circuits;
			Boiler 												boiler 						= Global.boiler;
			Burner												bruner						= Global.burner;
		}
		catch (Exception ex)
		{
			// Do nothing
		}
		
		Boolean 												threadMainFound				= true;

		while (!Global.stopNow)
		{
			Set <Thread> 										threadSet 					= Thread.getAllStackTraces().keySet();
			
			if (threadMainFound)
			{
				threadMainFound																= false;
				
				for (Thread thd : threadSet) 
				{
					if (thd.getName().contains("_Main"))    	threadMainFound 			=  true;
				}
				
			    if (! threadMainFound)							LogIt.error("Thread_Debug", "run", "Thread_Main missing");
			}
			
			Global.waitSecondsForStopNow(10);
		}
 		LogIt.info("Thread_Debug", "Run", "Stopping");             
	}
}
 