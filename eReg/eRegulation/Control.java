package eRegulation;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import HVAC_Common.*;
import HVAC_Common.Ctrl_Configuration.Relay;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Control
{
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
		
		if (System.getProperty("os.name").equalsIgnoreCase("windows 7"))
		{
			System.out.println("libraries not loaded");
		}
		else
		{
			System.loadLibrary("Interfaces");
		}
		
		//
		//============================================================

		//============================================================
		//
		// Any tests can be done here
		//
		
		
//		System.out.println("Starting test");
//		gpio.setInput();
//		
//		int i;
//		Float voltage;
//		
//		for (i = 0; i < 21; i++)
//		{
//			boolean signal = gpio.isHigh();
//			
//			if (signal)
//			{
//				System.out.println("Iteration : " + i + " signal is high");
//			}
//			else
//			{
//				System.out.println("Iteration : " + i + " signal is low");
//			}
//
//			Global.waitSeconds(5);
//		}

		//
		//============================================================
		
		//============================================================
		//
		// Initialising : Note that "Initialising" message on LCD is handled
		// in the Global constructor when the LCD display has been created
		//

		@SuppressWarnings("unused")
		Global 													global 						= new Global();
		Global.stopNow																		= false;
		LogIt.info("Control", "main", "Starting/BootSequence");

		//
		//============================================================

		
		//============================================================
		//
		// Read Calendar file
		//
		
		Global.display.writeAtPosition(2, 0, " Calendar");
		@SuppressWarnings("unused")
		Calendars												calendars 					= new Calendars();
		Global.display.writeAtPosition(2, 18, "Ok");

		calendars																			= null;				// To avoid memory use in the event of a new calendar later
		
		//
		//============================================================

		//============================================================
		//
		// Start threads 
		//
		
		Global.display.writeAtPosition(3, 0, " Thermometers");
		new Thread(new Thread_Thermometers(), 								"Thread_Thermometers").start();
		Global.waitSeconds(15);														// Must wait 15 secs for all thermometers to be read and have values + allow for retries
		Global.display.writeAtPosition(3, 18, "Ok");

		new Thread(new Thread_UserInterface(), 								"Thread_UserInteface").start();
		for (Circuit_Abstract circuit : Global.circuits.circuitList)
		{
			if (circuit.mixer != null)
			{
				new Thread(new Thread_Mixer((Circuit_Mixer) circuit), 		"Thread_Mixer_" + circuit.name).start();
			}
		}
		new Thread(new Thread_TCPListen(), 									"Thread_TCPListen").start();
// TODO wait for Thread_Mixer to finish setting it to zero
		Global.waitSeconds(90);														// Must wait 15 secs for all thermometers to be read and have values + allow for retries
		new Thread(new Thread_BackgroundTasks(), 							"Thread_BackgroundTasks").start();
		
		//
		//============================================================
		
		//=============================================================
		//
		// Main Code
		//
		
		Heat_Required 											globalHeatRequired			= new Heat_Required();
		
		while (!Global.stopNow)
		{
			Global.waitSeconds(5);

			// Sequence each circuit and get the heat requirements
			globalHeatRequired.setZero();

			for (Circuit_Abstract circuit : Global.circuits.circuitList)
			{
				circuit.taskScheduler();					// In Circuit_Abstract
				circuit.sequencer();
				if (circuit.heatRequired.tempMinimum > globalHeatRequired.tempMinimum)
				{
					globalHeatRequired.tempMinimum											= circuit.heatRequired.tempMinimum;
				}
				
				if (circuit.heatRequired.tempMaximum > globalHeatRequired.tempMaximum)
				{
					globalHeatRequired.tempMaximum											= circuit.heatRequired.tempMaximum;
				}
			}
//			if (! globalHeatRequired.isZero())			Global.boiler.requestHeat(globalHeatRequired);
			
			// Give heatRequirements to boiler and then run the sequencer
			Global.boiler.heatRequired														= globalHeatRequired;
			Global.boiler.sequencer();
		}
		
		//
		// End of Main Code
		//
		//=============================================================

		Global.boiler.requestIdle();
		Global.relays.offAll();
		Global.waitThreadTermination();							// Ensure that this is the last thread to stop
		
		switch (Global.exitStatus)
		{
		case Ctrl_Actions_Stop.ACTION_Stop:											// Value 0 : Stop App
 			LogIt.info("Thread_Main", "main", "Stopping", true);
 			System.exit(Ctrl_Actions_Stop.ACTION_Stop);
 			break;
		case Ctrl_Actions_Stop.ACTION_Restart:											// Value 1 : Restart App
	 		LogIt.info("Thread_Main", "main", "Stopping and restarting application", true); 
 			System.exit(Ctrl_Actions_Stop.ACTION_Restart);
 			break;		
 		case Ctrl_Actions_Stop.ACTION_Reboot:											// Value 2 : Reboot Pi
	 		LogIt.info("Thread_Main", "main", "Stopping and rebooting", true); 
 			System.exit(Ctrl_Actions_Stop.ACTION_Reboot);
 			break;
 		}
	}
 }
