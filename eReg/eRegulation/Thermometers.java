package eRegulation;

import java.util.ArrayList;

public class Thermometers
{
	public ArrayList<Thermometer> thermometerList = new ArrayList<Thermometer>();
	
	public Thermometers()
	{
	}
	public void add(String name, String address, String friendlyName, Boolean pid)
	{
		Thermometer thermometerItem 	= new Thermometer(name, address, friendlyName, pid);
		thermometerList.add(thermometerItem);
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
