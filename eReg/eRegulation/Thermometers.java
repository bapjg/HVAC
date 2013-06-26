package eRegulation;

import java.util.ArrayList;
//import java.util.Iterator;

public class Thermometers
{
	public ArrayList<Thermometer> thermometerList = new ArrayList<Thermometer>();
	
	public Thermometers()
	{
	}
	public void add(String name, String address, String friendlyName)
	{
		Thermometer thermometerItem 	= new Thermometer(name, address, friendlyName);
		thermometerList.add(thermometerItem);
	}
//	public Thermometer fetchThermometer(int index)
//	{
//		Thermometer element = null;
//		int i;
//		Iterator<Thermometer> itr 		= thermometerList.iterator();
//		
//		for (i = 0; i <= index; i++)
//		{
//			element = (Thermometer)itr.next();
//		}
//		return element;
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
		//		Thermometer element = null;
//		Iterator<Thermometer> itr 		= thermometerList.iterator();
//
//		while (itr.hasNext())
//		{
//			element				 		= (Thermometer) itr.next();
//			if (element.name.equalsIgnoreCase(name))
//			{
//				return element;
//			}
//		}
//		return element;
//	}
}
