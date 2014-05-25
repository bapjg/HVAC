package eRegulation;

import java.util.ArrayList;

import HVAC_Common.Ctrl_Configuration;

public class Thermometers
{
	public ArrayList<Thermometer> thermometerList = new ArrayList<Thermometer>();
	
	public Thermometers()
	{ 
	}
	public void configure(ArrayList <Ctrl_Configuration.Thermometer> paramThermometers)
	{
		for (Ctrl_Configuration.Thermometer 	paramThermometer : paramThermometers)
		{
			Thermometer thermometerItem 	= new Thermometer(paramThermometer);
			thermometerList.add(thermometerItem);
		}
	}
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
}
