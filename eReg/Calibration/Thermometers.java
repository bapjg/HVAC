package Calibration;

import java.util.ArrayList;
import java.util.Iterator;

public class Thermometers
{
	public String[][]  thermometerDetails =	
	{	
		{"Boiler",       "048F 011C", "Chaudiere"},
		{"Boiler_Out",   "048E 062C", "Chaudiere_Depart"},
		{"Boiler_In",    "048E 9F62", "Chaudiere_Retour"},
		{"Floor_Out", 	 "048E 9244", "Plancher_Depart"},
		{"Floor_In", 	 "048E DA69", "Plancher_Retour"},
		{"Radiator_Out", "048E 94A1", "Radiateur_Depart"},
		{"Radiator_In",  "048E EF76", "Radiateur_Retour"},
		{"Outside", 	 "048E DB41", "Exterieur"},
		{"Living_Room",  "048E DB90", "Sonde_RdC"},
		{"Hot_Water",  	 "048E 7489", "ECS"},
	};
	public ArrayList<Thermometer> thermometerList = new ArrayList<Thermometer>();
	
	public Thermometers()
	{
		int i;
		for (i = 0; i < thermometerDetails.length; i++)
		{
			Thermometer thermometerItem 	= new Thermometer();
		
			thermometerItem.name 		    = thermometerDetails[i][0];
			thermometerItem.thermoFile 	    = thermometerDetails[i][1];
			thermometerItem.friendlyName    = thermometerDetails[i][2];
			
			//String thermoRadical 		    = "/sys/bus/w1/devices/28-0000";
			String thermoRadical 		    = "C:/sys/28-0000";
			String thermoFile 				= thermoRadical + thermometerItem.thermoFile.toLowerCase().replace(" ", "") + "/w1_slave";
			
			thermometerItem.filePath 		= thermoFile;
			
			thermometerList.add(thermometerItem);
		}
	}
	public Thermometer fetchThermometer(int index)
	{
		Thermometer element = null;
		int i;
		Iterator<Thermometer> itr = thermometerList.iterator();
		
		for (i = 0; i <= index; i++)
		{
			element = (Thermometer)itr.next();
		}
		return element;
	}
	public Thermometer fetchThermometer(String name)
	{
		Thermometer element = null;
		Iterator<Thermometer> itr = thermometerList.iterator();

		while (itr.hasNext())
		{
			element = (Thermometer)itr.next();
			if (element.name == name)
			{
				return element;
			}
		}
		return element;
	}
}
