package eRegulation;

import eReg_Forms.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.text.DecimalFormat;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import HVAC_Common.*;
import HVAC_Common.Ctrl_Configuration.Relay;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Control
{
   @SuppressWarnings("unused")
   public static void main(String[] args) 						throws 						IOException, 
																							SAXException, 
																							ParserConfigurationException
   {
    	//============================================================
		//
		// Instantiate this class (required for JNI)
    	// and Set the current thread name
		//
    	
		@SuppressWarnings("unused")
		Control 												Me 							= new Control();
		Thread.currentThread().setName("Thread_Main");
		
		//
		//============================================================
		
		//============================================================
		//
		// For debugging on a Windows machine without the necessary hardware
		//
		
		String 													osName 						= System.getProperty("os.name").toLowerCase();
		
		if (osName.contains("windows"))							System.exit(Ctrl_Actions_Stop.ACTION_Stop);
		
		System.loadLibrary("Interfaces_SPI");
//		System.loadLibrary("Interfaces_I2C");					// I2C removed (except for thermometers) as screen removed/replaced by HDMI

		if (false)
		{

		}
		
		
		//
		//============================================================

		//============================================================
		//
		// Initialising : Note that "Initialising" message on LCD is handled
		// in the Global constructor when the LCD display has been created
		//

		Global 													global 						= new Global();
		Global.stopNow																		= false;
		LogIt.info("Control", "main", "Starting/BootSequence");

		//
		//============================================================

		//============================================================
		//
		// Debug Code
		//
	
		//
		//============================================================
		
		//============================================================
		//
		// Read Calendar file
		//
		
		@SuppressWarnings("unused")
		Calendars												calendars 					= new Calendars();
		calendars																			= null;				// To avoid memory use in the event of a new calendar later
		
		//
		//============================================================

		//============================================================
		//
		// Start threads (start with mixer, as it takes a long time
		//
		
//		Global.display.writeAtPosition(3, 0, " Thermometers");
		for (Circuit_Abstract circuit : Global.circuits.circuitList)
		{
			if (circuit.mixer != null)
			{
				new Thread(new Thread_Mixer((Circuit_Mixer) circuit), 		"Thread_Mixer_" + circuit.name).start();
			}
		}
		new Thread(new Thread_Thermometers(), 								"Thread_Thermometers").start();
		Global.waitSeconds(15);												// Must wait 15 secs for all thermometers to be read and have values + allow for retries

//*		new Thread(new Thread_UserInterface(), 								"Thread_UserInteface").start();
		new Thread(new Thread_TCPListen(), 									"Thread_TCPListen").start();

		Global.waitSecondsForStopNow(90);									// Must wait 90 secs for all thermometers to be read and have values + allow for retries
		new Thread(new Thread_BackgroundTasks(), 							"Thread_BackgroundTasks").start();
		
		//
		//============================================================
		
		//=============================================================
		//
		// Main Code
		//
		
		Heat_Required 											globalHeatRequired			= new Heat_Required();
		Global.boiler.heatRequired															= globalHeatRequired;	// Avoid null pointer if stopNow actionned immediately
		
		while (!Global.stopNow)
		{
			Global.waitSecondsForStopNow(5);

			// Sequence each circuit and get the heat requirements
			globalHeatRequired.setZero();

			for (Circuit_Abstract circuit : Global.circuits.circuitList)
			{
//				if (circuit.active)								// Modified 30/06/2017 as radiator/floor were running
//				{
					circuit.taskScheduler();					// In Circuit_Abstract
					circuit.sequencer();
					if (circuit.heatRequired.tempMinimum > globalHeatRequired.tempMinimum)
					{
						globalHeatRequired.tempMinimum										= circuit.heatRequired.tempMinimum;
					}
					
					if (circuit.heatRequired.tempMaximum > globalHeatRequired.tempMaximum)
					{
						globalHeatRequired.tempMaximum										= circuit.heatRequired.tempMaximum;
					}
//				}
			}
//			if (! globalHeatRequired.isZero())			Global.boiler.requestHeat(globalHeatRequired);
			
			// Give heatRequirements to boiler and then run the sequencer
			Global.boiler.heatRequired														= globalHeatRequired;
			Global.boiler.sequencer();
			Global.formControl.showTemperatures();
		}
		
		//
		// End of Main Code : while (!Global.stopNow)
		//
		//=============================================================

		Global.boiler.requestIdle();
		Global.relays.offAll();
		Global.waitThreadTermination();							// Ensure that this is the last thread to stop

//		Global.exitStatus
//		0 : Stop goto Bash
//		1 : Restart eRegulation
//		2 : Reboot
//		3 : Reload Configuration	Not Handled here
//		4 : Reload Calendars		Not Handled here
//		5 : ShutDown
//		6 : Debug and Wait
//		7 : Debug no wait

		Runtime 												runtime 					= Runtime.getRuntime();
 		ProcessBuilder 											processBuilder;
 		Process 												process;
		
		switch (Global.exitStatus)
		{
		case Ctrl_Actions_Stop.ACTION_Stop:													// Value 0 : Stop App and return to X11/GUI
 			LogIt.info("Thread_Main", "main", "Stopping", true);
	 		Global.waitSeconds(5);
 			System.exit(Ctrl_Actions_Stop.ACTION_Stop);
 			break;
		case Ctrl_Actions_Stop.ACTION_Restart:												// Value 1 : Restart App
	 		LogIt.info("Thread_Main", "main", "Stopping and restarting application", true);	
	 		Global.waitSeconds(5);
//	 		processBuilder 																	= new ProcessBuilder("/home/pi/HVAC/eReg/eRegulation/HVAC_Run.sh");
//	 		process 																		= processBuilder.start();     // Start the process.
 			System.exit(Ctrl_Actions_Stop.ACTION_Restart);
 			break;		
 		case Ctrl_Actions_Stop.ACTION_Reboot:												// Value 2 : Reboot Pi
	 		LogIt.info("Thread_Main", "main", "Stopping and rebooting", true);
	 		Global.waitSeconds(5);
 		    process 																		= runtime.exec("sudo shutdown -r now");
 		    System.exit(Ctrl_Actions_Stop.ACTION_Reboot);									// Exit code irrelevant as process above does the work
 			break;
 		case Ctrl_Actions_Stop.ACTION_ShutDown:												// Value 5 : Reboot Pi
	 		LogIt.info("Thread_Main", "main", "Stopping and shutting down", true); 
	 		Global.waitSeconds(5);
	 		process												 							= runtime.exec("sudo shutdown -h now");
 		    System.exit(Ctrl_Actions_Stop.ACTION_ShutDown);									// Exit code irrelevant as process above does the work
 			break;
 		case Ctrl_Actions_Stop.ACTION_Debug_Wait:											// Value 6 : Reboot Pi
	 		LogIt.info("Thread_Main", "main", "Stopping and restarting application with Debug Wait", true); 
	 		Global.waitSeconds(5);
//	 		processBuilder 																	= new ProcessBuilder("/home/pi/HVAC/eReg/eRegulation/HVAC_Debug_Wait.sh &");
//	 		process 																		= processBuilder.start();     // Start the process.
 			System.exit(Ctrl_Actions_Stop.ACTION_Debug_Wait);
 			break;
 		case Ctrl_Actions_Stop.ACTION_Debug_NoWait:											// Value 7 : Reboot Pi
	 		LogIt.info("Thread_Main", "main", "Stopping and restarting application with Debug NoWait", true); 
	 		Global.waitSeconds(5);
//	 		processBuilder 																	= new ProcessBuilder("/home/pi/HVAC/eReg/eRegulation/HVAC_Debug_NoWait.sh");
//	 		process 																		= processBuilder.start();     // Start the process.
 			System.exit(Ctrl_Actions_Stop.ACTION_Debug_NoWait);
 			break;
		}
	}
}
