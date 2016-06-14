package eRegulation;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import HVAC_Common.Ctrl_Configuration;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Thermometers
{
	public ArrayList<Thermometer> 								thermometerList 			= new ArrayList<Thermometer>();
	
	public Thermometers()
	{ 
	}
	public void configure(ArrayList <Ctrl_Configuration.Thermometer> paramThermometers)
	{
		for (Ctrl_Configuration.Thermometer paramThermometer : paramThermometers)
		{
			Thermometer 										thermometerItem				= fetchThermometer(paramThermometer.name);				
			if (thermometerItem == null)
			{
				thermometerItem 															= new Thermometer(paramThermometer);
				thermometerList.add(thermometerItem);
			}
			else
			{
				thermometerItem.addProbe(paramThermometer);
			}
		}
	}
//	public Thermometer (this, string name)
//	{
//		for (Thermometer element : thermometerList) 
//		{
//			if (element.name.equalsIgnoreCase(name))
//			{
//					return element;
//			}
//		}
//		return null;
//	}
	public Thermometer fetchThermometer(String name)
	{
		for (Thermometer element : thermometerList) 
		{
			if (element.name.equalsIgnoreCase(name))
			{
					return element;
			}
		}
		return null;
	}
	public int readNewProbe(String address)
	{
		try
		{
			String 										probeFileName				= "/mnt/1wire" + address.toUpperCase().replace(" ", "") + "/temperature9";
			FileInputStream 							ThermoFile_InputStream 		= new FileInputStream(probeFileName);
			DataInputStream 							ThermoFile_InputData 		= new DataInputStream(ThermoFile_InputStream);
			BufferedReader 								ThermoFile_InputBuffer 		= new BufferedReader(new InputStreamReader(ThermoFile_InputData));
			String 										ThermoFile_InputLine 		= ThermoFile_InputBuffer.readLine();
	
			ThermoFile_InputBuffer.close();
			ThermoFile_InputData.close();
			ThermoFile_InputStream.close();
	
			String 										tempString	 				= ThermoFile_InputLine.replace(" ", "");
			float 										tempFloat	 				= Float.parseFloat(tempString);
			return	Math.round(tempFloat * 1000); // Round to milli-degree
		}
		catch (Exception ex)
		{
			return -273000;
		}
	}
}
