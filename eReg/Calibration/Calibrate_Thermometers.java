package Calibration;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import eRegulation.Boiler;
import eRegulation.Burner;
import eRegulation.Buttons;
import eRegulation.Control;
import eRegulation.Global;
import eRegulation.LCD;
import eRegulation.LogIt;
import eRegulation.Thread_Thermometers;

public class Calibrate_Thermometers
{
	public static void main(String[] args)
	{
		//============================================================
		//
		// Instantiate this class (required for JNI)
		//
		@SuppressWarnings("unused")
		Control Me = new Control();
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
		
		//============================================================
		//
		// Start thread to continuously read the thermomoeters
		//
		display.writeAtPosition(3, 0, " Thermometers");
		Thread thread_thermometers 					= new Thread(new Thread_Thermometers(), "Thermometers");
		thread_thermometers.start();
		try
		{
			Thread.sleep(10000);						// Must wait 10 secs for all thermometers to be read and have values
		}
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
		//
		//============================================================
		display.writeAtPosition(3, 18, "Ok");

		//LogIt.tempInfo("Relays off ");
		Global.relays.offAll();
		Global.pumpFloor.on();
		Global.pumpWater.on();
		
		Global.mixerDown.off();
		Global.mixerUp.off();
		
		Global.mixerDown.on();
		secondsToWait(90);
		Global.mixerDown.off();
		//LogIt.tempInfo("Mixer Down ");
		
		Global.mixerUp.on();
		secondsToWait(45);
		Global.mixerUp.off();
		//LogIt.tempInfo("Mixer 50% ");

		//LogIt.tempInfo("Mixer positionned, Pumps on ");
		
		@SuppressWarnings("unused")
        Burner 			burner 						= new Burner();
		@SuppressWarnings("unused")
		Boiler 			boiler						= new Boiler();

		
		display.clear();
		
		//LogIt.tempInfo("Starting now ");
		
		while (!Global.stopNow)
		{
			secondsToWait(30);												// cycle every 30s
			LogIt.tempData();
			displayData(display);
			
			buttons.read();
			
			if (buttons.button0)
			{
				buttons.read();
				Global.stopNow 						= true;					//Exit the loop;
			}
		}

		//LogIt.tempInfo("Test Finished");
	
		Global.relays.offAll();
	}
    public static String tempToString(Integer temp)
    {
    	// Converts temperature in decidegrees into displayable format
    	Integer Degrees = temp/10;
    	Integer Decimals = temp - Degrees * 10;
    	return Degrees.toString() + "." + Decimals.toString();
    }
    public static void secondsToWait(Integer seconds)
    {
		try
        {
            Thread.sleep(seconds * 1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    public static void displayData(LCD display)
    {
		Date date 												= new Date();
		SimpleDateFormat 	dateFormat 							= new SimpleDateFormat("dd.MM HH:mm:ss");
		display.writeAtPosition(0, 0, dateFormat.format(date));

		display.writeAtPosition(0, 16, tempToString(Global.thermoOutside.reading));
		display.writeAtPosition(1, 0,  "Blr  MixH  MixO MixC");
		display.writeAtPosition(2, 0,  tempToString(Global.thermoBoiler.reading));
		display.writeAtPosition(2, 5,  tempToString(Global.thermoFloorHot.reading));
		display.writeAtPosition(2, 11, tempToString(Global.thermoFloorOut.reading));
		display.writeAtPosition(2, 16, tempToString(Global.thermoFloorCold.reading));
		display.writeAtPosition(3, 0,  "H2O  ");
		display.writeAtPosition(3, 5,  tempToString(Global.thermoHotWater.reading));

		display.writeAtPosition(3, 11, "LivR ");
		display.writeAtPosition(3, 16, tempToString(Global.thermoLivingRoom.reading));
    }
}
