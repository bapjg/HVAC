package eRegulation;
 
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
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
//			Thread[] 											threadArray 				= threadSet.toArray(new Thread[threadSet.size()]);
			
			if (threadMainFound)
			{
				threadMainFound																= false;
				
				for (Thread thd : threadSet) 
				{
					if (thd.getName().substring(0,7).equals("Thread_Main"))    	threadMainFound =  true;
				}

//				
//				
//				
//				
//				for (int i = 0; i < threadArray.length; i++) 
//			    {
//			        if (threadArray[i].getName().equals("Thread_Main"))    	threadMainFound =  true;
//			    }
				
				
			    if (! threadMainFound)							LogIt.error("Thread_Debug", "run", "Thread_Main missing");
			}
			
			
			
			
			Global.waitSecondsForStopNow(10);
		}
 		LogIt.info("Thread_Debug", "Run", "Stopping");             
	}
	
}
 