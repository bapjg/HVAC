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

import HVAC_Common.*;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Thread_Debug 										implements 					Runnable
{

	

	public void run()
    {
		LogIt.info("Thread_Debug", "Run", "Starting");            

		try
		{
			Global 												global 						= new Global();
			Thermometers										thermos						= Global.thermometers;
			Relays												relays						= Global.relays;
			Circuits 											circuits					= Global.circuits;
		}
		catch (Exception ex)
		{
			// Do nothing
		}
		while (!Global.stopNow)
		{
			Boiler 												boiler 						= Global.boiler;
			
			
			
			
			
			
			
			
			Global.waitSecondsForStopNow(10);
		}
 		LogIt.info("Thread_Debug", "Run", "Stopping");             
	}
	
}
 