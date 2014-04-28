package eRegulation;

import java.util.ArrayList;
import java.util.Iterator;

import HVAC_Messages.Ctrl_Configuration;


public class Pumps
{
	public ArrayList<Pump> pumpList = new ArrayList<Pump>();
	public Long							dateTimeLastClean;
	
	public void configure(ArrayList <Ctrl_Configuration.Pump> paramPumps)
	{
		for (Ctrl_Configuration.Pump 	paramPump : paramPumps)
		{
			Pump pumpItem 				= new Pump(paramPump);
			pumpList.add(pumpItem);
		}
		dateTimeLastClean				= 0L;
	}
	public Pump fetchPump(String name)
	{
		for (Pump element : pumpList) 
		{
			if (element.name.equalsIgnoreCase(name))
			{
					return element;
			}
		}
		return null;
	}
}