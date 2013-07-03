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
		//
		@SuppressWarnings("unused")
		Control 		Me 							= new Control();
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
		
		String 			xmlParams 					= "";
		String 			xmlCalendars 				= "";

		if (args.length > 0)
		{
			xmlParams 								= args[0] + "_eRegulator.xml";			
			xmlCalendars 							= args[0] + "_eCalendars.xml";			
		}
		else
		{
			xmlParams 								= "eRegulator.xml";			
			xmlCalendars 							= "eCalendars.xml";			
		}

		@SuppressWarnings("unused")
		Global 			global 						= new Global(xmlParams);
		
		Global.stopNow								= false;

		//
		//============================================================
	
		
		//============================================================
		//
		// Read Calendar file
		//
		Global.display.writeAtPosition(2, 0, " Calendar");
		@SuppressWarnings("unused")
		Calendars 		calendars 					= new Calendars(xmlCalendars);
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

		Global.circuitFloor							= (Circuit_Mixer) Global.circuits.fetchcircuit("Floor");
		Global.circuitGradient						= (Circuit_Gradient) Global.circuits.fetchcircuit("Radiator");
		Global.circuitHotWater						= (Circuit_HotWater) Global.circuits.fetchcircuit("Hot_Water");

		Global.mixer								= Global.circuitFloor.mixer;
		Global.mixer.pidControler					= new PID(5,3);					// PID Controler is updated every 10 secondes by Thread_Thermometers
		//
		//============================================================
		
		
		//============================================================
		//
		// Start thread to continuously read the thermometers
		//
		Global.display.writeAtPosition(3, 0, " Thermometers");
		Thread 			thread_thermometers 		= new Thread(new Thread_Thermometers(), "Thermometers");
		thread_thermometers.start();
		Global.display.writeAtPosition(3, 18, "Ok");
		Global.waitSeconds(10);														// Must wait 10 secs for all thermometers to be read and have values
		//
		//============================================================
		
		
		//============================================================
		//
		// Start thread to handle UserInterface
		//
		Thread 			thread_userInterface 		= new Thread(new Thread_UserInterface(), "UserInteface");
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

		
//		System.out.println("Starting test");
//		// burner.powerOn();
//		
//		int i;
//		ADC adc = new ADC();
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
		//============================================================
		
		
		//=============================================================
		//
		// Main Code
		//
		
		HeatRequired 	globalHeatRequired						= new HeatRequired();
		
		while (!Global.stopNow)
		{
			Global.waitSeconds(5);
		
//			LogIt.info("Main", "General Loop", "Before Burner sequencer");
			boiler.sequencer();
			
			globalHeatRequired.tempMaximum 						= -1;
			globalHeatRequired.tempMinimum 						= -1;

//			LogIt.info("Main", "General Loop", "Number of circuits is : "+ Global.circuits.circuitList.size());

			for (Circuit_Abstract circuit : Global.circuits.circuitList)
			{
//				LogIt.info("Control","mainLoop", "Looking at circuit : " + circuit.name);
				circuit.scheduleTasks();
//				LogIt.info("Control","mainLoop", "scheduler called state is  "  + circuit.name + "/" + circuit.state);

//				LogIt.info("Control","mainLoop", "will call sequencer state is  "  + circuit.name + "/" + circuit.state);
				circuit.sequencer();
//				LogIt.info("Control","mainLoop", "have called sequencer state is  "  + circuit.name + "/" + circuit.state);

		
				if (circuit.heatRequired.tempMinimum > globalHeatRequired.tempMinimum)
				{
					globalHeatRequired.tempMinimum				= circuit.heatRequired.tempMinimum;
				}
				
				if (circuit.heatRequired.tempMaximum > globalHeatRequired.tempMaximum)
				{
					globalHeatRequired.tempMaximum				= circuit.heatRequired.tempMaximum;
				}
			}
				
			LogIt.tempData();
			
			//LogIt.info("Control", "EnergyRequirements", "now are min/max " + globalEnergyRequirements.tempMinimum + "/" + globalEnergyRequirements.tempMaximum);
			// We now have the global energy requirements
			
			boiler.requestHeat(globalHeatRequired);
			
//			if (Global.thermoOutside.reading > Global.summerTemp)
//			{
//				if (Global.getTimeNowSinceMidnight() > Global.summerPumpTime)
//				{
//					if (!Global.summerWorkDone)
//					{
//						Global.summerWorkDone					= true;
//						
//						Thread 			thread_summer 			= new Thread(new Thread_Summer(), "Summer");
//						thread_summer.start();
//					}
//				}
//			}
		}
		//
		// End of Main Code
		//
		//=============================================================
		
		boiler.requestIdle();
		Global.relays.offAll();
	}
 }
