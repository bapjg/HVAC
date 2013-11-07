package eRegulation;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

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
		Global.thermoBoilerIn						= Global.thermometers.fetchThermometer("Boiler_In");
		
		Global.thermoFloorOut						= Global.thermometers.fetchThermometer("Floor_Out");
		Global.thermoFloorCold						= Global.thermometers.fetchThermometer("Floor_Cold");
		Global.thermoFloorHot 						= Global.thermometers.fetchThermometer("Floor_Hot");
		
		Global.thermoRadiatorOut					= Global.thermometers.fetchThermometer("Radiator_Out");
		Global.thermoRadiatorIn						= Global.thermometers.fetchThermometer("Radiator_In");
		
		Global.thermoOutside						= Global.thermometers.fetchThermometer("Outside");
		Global.thermoLivingRoom						= Global.thermometers.fetchThermometer("Living_Room");
		Global.thermoHotWater						= Global.thermometers.fetchThermometer("Hot_Water");

		Global.burnerPower	 						= Global.relays.fetchRelay("Burner");
		Global.pumpWater 							= Global.pumps.fetchPump("Pump_Water");
		Global.pumpFloor	 						= Global.pumps.fetchPump("Pump_Floor");
		Global.pumpRadiator 						= Global.pumps.fetchPump("Pump_Radiator");

		Global.mixerUp		 						= Global.relays.fetchRelay("Mixer_Up");
		Global.mixerDown	 						= Global.relays.fetchRelay("Mixer_Down");

		Global.circuitFloor							= (Circuit_Mixer) 		Global.circuits.fetchcircuit("Floor");
		Global.circuitGradient						= (Circuit_Radiator) 	Global.circuits.fetchcircuit("Radiator");
		Global.circuitHotWater						= (Circuit_HotWater) 	Global.circuits.fetchcircuit("Hot_Water");

		Global.mixer								= Global.circuitFloor.mixer;
		Global.mixer.pidControler					= new PID(10);					// PID Controler is updated every 10 seconds by Thread_Thermometers
		
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
		// To be transferred elsewhere
		//

		Boiler 			boiler						= new Boiler();

		//
		//============================================================
		

		//=============================================================
		//
		// Section for debugging
		//
//		Global.waitSeconds(100);
//
//		boiler.burner.powerOn();
//		
//		while (!Global.stopNow)
//		{
//			Global.thermoBoiler.read();
//			Integer thisReading = Global.thermoBoiler.reading;
//			System.out.println("55 And the reading is " +thisReading);
//			Global.waitSeconds(10);
//		}
//		boiler.burner.powerOff();
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
		
			boiler.sequencer();
			
			globalHeatRequired.tempMaximum 						= -1;
			globalHeatRequired.tempMinimum 						= -1;

			for (Circuit_Abstract circuit : Global.circuits.circuitList)
			{
//				circuit.scheduleTaskNext();
//				circuit.scheduleTaskActive();
				circuit.scheduleTask();
				circuit.sequencer();
				if (circuit.heatRequired != null)
				{
					if (circuit.heatRequired.tempMinimum > globalHeatRequired.tempMinimum)
					{
						globalHeatRequired.tempMinimum				= circuit.heatRequired.tempMinimum;
					}
					
					if (circuit.heatRequired.tempMaximum > globalHeatRequired.tempMaximum)
					{
						globalHeatRequired.tempMaximum				= circuit.heatRequired.tempMaximum;
					}
				}
			}
			
			boiler.requestHeat(globalHeatRequired);
			
			// We should only do this if no circuit active otherwise we will be heating the house in mid summer
			
			if (Global.thermoOutside.reading > Global.summerTemp)
			{
				if ((Global.getTimeNowSinceMidnight() > Global.summerPumpTime) 
				&& (Global.getTimeNowSinceMidnight() < Global.summerPumpTime + 30 * 60 * 1000L)) // 30 mins
				{
					if (!Global.summerWorkDone)
					{
						Global.summerWorkDone					= true;
						
						Thread 			thread_summer 			= new Thread(new Thread_Summer(), "Thread_Summer");
						thread_summer.start();
					}
				}
			}
		}
		
		//
		// End of Main Code
		//
		//=============================================================

 		LogIt.info("Thread_Main", "main", "Stopping", true); 
 		
		boiler.requestIdle();
		Global.relays.offAll();
	}
 }
