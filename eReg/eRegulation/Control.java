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
		Global.pumpWater 							= Global.relays.fetchRelay("Pump_Water");
		Global.pumpFloor	 						= Global.relays.fetchRelay("Pump_Floor");
		Global.pumpRadiator 						= Global.relays.fetchRelay("Pump_Radiator");

		Global.mixerUp		 						= Global.relays.fetchRelay("Mixer_Up");
		Global.mixerDown	 						= Global.relays.fetchRelay("Mixer_Down");

		Global.circuitFloor							= (Circuit_Mixer) 		Global.circuits.fetchcircuit("Floor");
		Global.circuitGradient						= (Circuit_Gradient) 	Global.circuits.fetchcircuit("Radiator");
		Global.circuitHotWater						= (Circuit_HotWater) 	Global.circuits.fetchcircuit("Hot_Water");

		Global.mixer								= Global.circuitFloor.mixer;
		Global.mixer.pidControler					= new PID(5,3);					// PID Controler is updated every 10 seconds by Thread_Thermometers
		
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
		Global.waitSeconds(10);														// Must wait 10 secs for all thermometers to be read and have values
		
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

		//@SuppressWarnings("unused")
        //Burner 			burner 						= new Burner();

		Boiler 			boiler						= new Boiler();

		//
		//============================================================
		

		//=============================================================
		//
		// Section for debugging
		//
//
//		
//		System.out.println("Control Starting test");
//		System.out.println("Control burner on");
//		boiler.burner.powerOn();
//		
//		Global.waitSeconds(20);
//		
//		int i;
//		FuelFlow ff = boiler.burner.fuelflow;
//		System.out.println("burner off");
//		boiler.burner.powerOff();
//		
//		for (i =0; i < 15; i++)
//		{
//			System.out.println("iteration : " + Global.burnerVoltages.readAverage());
//		}
//
//		Global.stopNow = true;
//		
//		
//		Long timeStart = Global.now();
//		
//		for (i = 0; i < 100; i++)
//		{
//			//burner.sequencer();
//			Global.burnerPower.off();
//		}
//
//		Long timeEnd = Global.now();
//		
//		System.out.println("100 relay throws : " + (timeEnd - timeStart));
//		Global.stopNow = true;
//
//		
//		burner.powerOff();
//		System.out.println("Fuel Consumed : " + burner.fuelFlowTimeCumulated);
//		
//		
//		Global.waitSeconds(5);	
//
//		burner.powerOn();
//
//		for (i = 0; i < 20; i++)
//		{
//			burner.sequencer();
//			voltage = burner.burnerVoltages.read();
//			System.out.println("Iteration : " + i + " voltage : " + voltage);
//			Global.waitSeconds(1);
//		}
//		
//		
//		burner.powerOff();
//		
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
				circuit.scheduleTasks();

				circuit.sequencer();

		
				if (circuit.heatRequired.tempMinimum > globalHeatRequired.tempMinimum)
				{
					globalHeatRequired.tempMinimum				= circuit.heatRequired.tempMinimum;
				}
				
				if (circuit.heatRequired.tempMaximum > globalHeatRequired.tempMaximum)
				{
					globalHeatRequired.tempMaximum				= circuit.heatRequired.tempMaximum;
				}
			}
			
			//------------------------------------
			// This is done in Thread_Thermometers
			//LogIt.tempData();
			//------------------------------------
			
			boiler.requestHeat(globalHeatRequired);
			
			// We should only do this if no circuit active otherwise we will be heating the house in mid summer
			
			if (Global.thermoOutside.reading > Global.summerTemp)
			{
				if ((Global.getTimeNowSinceMidnight() > Global.summerPumpTime) && (Global.getTimeNowSinceMidnight() < Global.summerPumpTime + 30 * 60 * 1000L)) // 30 mins
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
