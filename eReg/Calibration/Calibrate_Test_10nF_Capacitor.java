package Calibration;

import eRegulation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Calibrate_Test_10nF_Capacitor
{
	public static void main(String[] args)
	{
		//============================================================
		//
		// This is to test the 10 nF capacitor used on mixer relay
		// Put the mixer into half way position
		// Then simply move it up and down repeatedly
		//
		//============================================================

		
		//============================================================
		//
		// Instantiate this class (required for JNI)
		//
		@SuppressWarnings("unused")
		Calibrate_Test_10nF_Capacitor Me = new Calibrate_Test_10nF_Capacitor();
		//
		//============================================================

		if (System.getProperty("os.name").equalsIgnoreCase("windows 7"))
		{
			System.out.println("libraries not loaded");
		}
		else
		{
			System.loadLibrary("Interfaces");
		}

		LCD 			display 					= new LCD();
		@SuppressWarnings("unused")
		Buttons 		buttons 					= new Buttons();

		display.clear();
		display.writeAtPosition(0, 0, "Initialising");

		Global.stopNow								= false;

		String xmlParams 							= "";
		@SuppressWarnings("unused")
		String xmlCalendars 						= "";

		//display.writeAtPosition(1, 0, "Reading params");


	
		try
		{
			@SuppressWarnings("unused")
			Global global 								= new Global();
		} 
		catch (Exception e2) 
		{
			e2.printStackTrace();
		}
		
		display.writeAtPosition(1, 0, " Params");
		display.writeAtPosition(1, 18, "Ok");
		
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

		Global.circuitFloor							= (Circuit_Mixer) Global.circuits.fetchcircuit("Floor");
		Global.circuitGradient						= (Circuit_Gradient) Global.circuits.fetchcircuit("Radiator");
		Global.circuitHotWater						= (Circuit_HotWater) Global.circuits.fetchcircuit("Hot_Water");

		Global.mixer								= Global.circuitFloor.mixer;

		//============================================================
		//
		// Start thread to continuously read the thermomoeters
		//
		display.writeAtPosition(3, 0, " Thermometers");
		Thread thread_thermometers 					= new Thread(new Thread_Thermometers(), "Thermometers");
		thread_thermometers.start();
		
		Global.waitSeconds(10);							// Must wait 10 secs for all thermometers to be read and have values
		
		//
		//============================================================
		display.writeAtPosition(3, 18, "Ok");

		//LogIt.tempInfo("Relays off and pump on");
		Global.relays.offAll();
		Global.pumpFloor.on();
		Global.mixerDown.off();
		Global.mixerUp.off();
		
		//LogIt.tempInfo("Mixer going down and wait 90s - do this manually to save time");
		Global.mixerDown.on();
		Global.waitSeconds(30);
		Global.mixerDown.off();
		//LogIt.tempInfo("Mixer Down ");
		
		//LogIt.tempInfo("Mixer going up half way");
		Global.mixerUp.on();
		Global.waitSeconds(45);
		Global.mixerUp.off();
		//LogIt.tempInfo("Mixer 50% ");

		//LogIt.tempInfo("Mixer positionned, Pumps on ");
		
		@SuppressWarnings("unused")
        Burner 			burner 						= new Burner();
		@SuppressWarnings("unused")
		Boiler 			boiler						= new Boiler();

		int				i;
		
		display.clear();
		
		for (i = 0; i < 50; i++)
		{
			if (i % 2 == 0)
			{
				//LogIt.tempInfo("Mixer Up, sequence " + i);
				Global.mixerUp.on();
			}
			else
			{
				//LogIt.tempInfo("Mixer Down, sequence " + i);
				Global.mixerDown.on();
			}
			Global.waitSeconds(1);
			Global.mixerUp.off();
			Global.mixerDown.off();
			Global.waitSeconds(3);
		}
		
		//LogIt.tempInfo("Test Finished");
		Global.relays.offAll();
		Global.stopNow								= true;
	}
    public static String tempToString(Integer temp)
    {
    	// Converts temperature in decidegrees into displayable format
    	Integer Degrees = temp/10;
    	Integer Decimals = temp - Degrees * 10;
    	return Degrees.toString() + "." + Decimals.toString();
    }
    public static void displayData()
    {
		Date date 												= new Date();
		SimpleDateFormat 	dateFormat 							= new SimpleDateFormat("dd.MM HH:mm:ss");
		Global.display.writeAtPosition(0, 0, dateFormat.format(date));

		Global.display.writeAtPosition(0, 16, tempToString(Global.thermoOutside.reading));
		Global.display.writeAtPosition(1, 0,  "Blr  MixH  MixO MixC");
		Global.display.writeAtPosition(2, 0,  tempToString(Global.thermoBoiler.reading));
		Global.display.writeAtPosition(2, 5,  tempToString(Global.thermoFloorHot.reading));
		Global.display.writeAtPosition(2, 11, tempToString(Global.thermoFloorOut.reading));
		Global.display.writeAtPosition(2, 16, tempToString(Global.thermoFloorCold.reading));
		Global.display.writeAtPosition(3, 0,  "H2O  ");
		Global.display.writeAtPosition(3, 5,  tempToString(Global.thermoHotWater.reading));

		Global.display.writeAtPosition(3, 11, "LivR ");
		Global.display.writeAtPosition(3, 16, tempToString(Global.thermoLivingRoom.reading));
    }
    public static void waitForBoilerTemp(Integer target, LCD display)
    {
		while (Global.thermoBoiler.reading < target)
		{
			Global.waitSeconds(10);
			displayData();				
		}
    }
}
