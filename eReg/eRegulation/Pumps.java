package eRegulation;

import java.util.ArrayList;
import java.util.Iterator;


public class Pumps
{
	public ArrayList<Pump> pumpList = new ArrayList<Pump>();
	
	public void addFromXML(String name, String address)
	{
		Pump pumpItem 				= new Pump(name, address);
		pumpList.add(pumpItem);
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
