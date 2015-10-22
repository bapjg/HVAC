package eRegulation;

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
	
	
//	public Thermometer get(String name) 
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
//	public Thermometer set(int index, Thermometer element) 
//	{
//		return null;
//	}
//    public int size() 
//    {
//        return thermometerList.size();
//    }
}
