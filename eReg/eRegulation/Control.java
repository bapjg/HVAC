package eRegulation;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import HVAC_Messages.Ctrl_Actions_Stop;

public class Control
{
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException
	{
    	//============================================================
		//
		// Instantiate this class (required for JNI)
    	// and Set the current thread name
		//
    	
		@SuppressWarnings("unused")
		Control 		Me 							= new Control();
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
		// Initialising : Note that "Initialising" message on LCD is handled
		// in the Global constructor when the LCD display has been created
		//

		@SuppressWarnings("unused")
		Global 			global 						= new Global();
		
		Global.stopNow								= false;
		LogIt.info("Control", "main", "Starting/BootSequence");

		//
		//============================================================
	
		
		//============================================================
		//
		// Read Calendar file
		//
		
		Global.display.writeAtPosition(2, 0, " Calendar");
		@SuppressWarnings("unused")
		Calendars 		calendars 					= new Calendars();
		Global.display.writeAtPosition(2, 18, "Ok");
		
		//
		//============================================================


		//============================================================
		//
		// Initialise Global (This ought to be in constructor) xxxx
		//
		
		Global.thermoBoiler 						= Global.thermometers.fetchThermometer("Boiler");
		Global.thermoBoilerOld						= Global.thermometers.fetchThermometer("Boiler_Old");
		Global.thermoBoilerOut						= Global.thermometers.fetchThermometer("Boiler_Out");
		Global.thermoBoilerIn						= Global.thermometers.fetchThermometer("Boiler_In");
		
		Global.thermoFloorOut						= Global.thermometers.fetchThermometer("Floor_Out");
		Global.thermoFloorIn						= Global.thermometers.fetchThermometer("Floor_In");
		
		Global.thermoRadiatorOut					= Global.thermometers.fetchThermometer("Radiator_Out");
		Global.thermoRadiatorIn						= Global.thermometers.fetchThermometer("Radiator_In");
		
		Global.thermoOutside						= Global.thermometers.fetchThermometer("Outside");
		Global.thermoLivingRoom						= Global.thermometers.fetchThermometer("Living_Room");
		Global.thermoHotWater						= Global.thermometers.fetchThermometer("Hot_Water");

		Global.burnerPower	 						= Global.relays.fetchRelay("Burner");

		Global.circuitFloor							= (Circuit_Mixer) 		Global.circuits.fetchcircuit("Floor");
		Global.circuitGradient						= (Circuit_Radiator) 	Global.circuits.fetchcircuit("Radiator");
		Global.circuitHotWater						= (Circuit_HotWater) 	Global.circuits.fetchcircuit("Hot_Water");

//		Global.mixer								= Global.circuitFloor.mixer;
		
		//
		//============================================================
		
		
		//============================================================
		//
		// Start thread to continuously read the thermometers
		//
		
		Global.display.writeAtPosition(3, 0, " Thermometers");
		Thread 			thread_thermometers 		= new Thread(new Thread_Thermometers(), "Thread_Thermometers");
		thread_thermometers.start();
		Global.display.writeAtPosition(3, 18, "Ok");
		Global.waitSeconds(15);														// Must wait 15 secs for all thermometers to be read and have values + allow for retries
		
		//
		//============================================================
		
		
		//============================================================
		//
		// Start thread to handle UserInterface
		//
		
		Thread 			thread_userInterface 		= new Thread(new Thread_UserInterface(), "Thread_UserInteface");
		thread_userInterface.start();
		
		//
		//============================================================

		
		//============================================================
		//
		// Start thread to handle Mixer
		//
		
		for (Circuit_Abstract circuit : Global.circuits.circuitList)
		{
			if (circuit.mixer != null)
			{
				Thread 			thread_mixer			 		= new Thread(new Thread_Mixer((Circuit_Mixer) circuit), "Thread_Mixer_" + circuit.name);
				thread_mixer.start();
			}
		}
		
		
		//
		//============================================================
		

		
		//============================================================
		//
		// Start thread to handle UserInterface via Android etc
		//
		
		Thread 			thread_tcpListen	 		= new Thread(new Thread_TCPListen(), "Thread_TCPListen");
		thread_tcpListen.start();
		
		//
		//============================================================

		//============================================================
		//
		// Start thread to run background work
		//
		
		Thread 			thread_background 			= new Thread(new Thread_BackgroundTasks(), "Thread_BackgroundTasks");
		thread_background.start();
		
		//
		//============================================================
		
		//=============================================================
		//
		// Main Code
		//
		
		HeatRequired 	globalHeatRequired						= new HeatRequired();
		
		while (!Global.stopNow)
		{
			Global.waitSeconds(5);
		
			Global.boiler.sequencer();
			
			globalHeatRequired.tempMaximum 						= -1;
			globalHeatRequired.tempMinimum 						= -1;

			for (Circuit_Abstract circuit : Global.circuits.circuitList)
			{
				circuit.scheduleTask();
				circuit.sequencer();
				if (circuit.heatRequired != null)
				{
					if (circuit.heatRequired.tempMinimum > globalHeatRequired.tempMinimum)
					{
						globalHeatRequired.tempMinimum			= circuit.heatRequired.tempMinimum;
					}
					
					if (circuit.heatRequired.tempMaximum > globalHeatRequired.tempMaximum)
					{
						globalHeatRequired.tempMaximum			= circuit.heatRequired.tempMaximum;
					}
				}
			}
			Global.boiler.requestHeat(globalHeatRequired);
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
		case Ctrl_Actions_Stop.EXIT_Stop:											// Value 0 : Stop App
 			LogIt.info("Thread_Main", "main", "Stopping", true);
 			System.exit(Ctrl_Actions_Stop.EXIT_Stop);
 			break;
		case Ctrl_Actions_Stop.EXIT_Restart:											// Value 1 : Restart App
	 		LogIt.info("Thread_Main", "main", "Stopping and restarting application", true); 
 			System.exit(Ctrl_Actions_Stop.EXIT_Restart);
 			break;		
 		case Ctrl_Actions_Stop.EXIT_Reboot:											// Value 2 : Reboot Pi
	 		LogIt.info("Thread_Main", "main", "Stopping and rebooting", true); 
 			System.exit(Ctrl_Actions_Stop.EXIT_Reboot);
 			break;
 		}
	}
 }
