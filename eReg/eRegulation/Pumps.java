package eRegulation;

import java.util.ArrayList;
import java.util.Iterator;

import HVAC_Messages.Ctrl_Configuration;


public class Pumps
{
	public ArrayList<Pump> pumpList = new ArrayList<Pump>();
	
//	public void addFromXML(String name, String relayName)
//	{
//		Pump pumpItem 				= new Pump(name, relayName);
//		pumpList.add(pumpItem);
//	}
//	public void addFromObject(String name, String relayName)
//	{
//		Pump pumpItem 				= new Pump(name, relayName);
//		pumpList.add(pumpItem);
//	}
	public void addFromObject(Ctrl_Configuration.Data.Pump pumpParam)
	{
		Pump pumpItem 				= new Pump(pumpParam);
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