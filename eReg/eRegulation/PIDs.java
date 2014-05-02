package eRegulation;

import java.util.ArrayList;
import java.util.Iterator;

import HVAC_Messages.Ctrl_Configuration;


public class PIDs
{int x = 1;
	public ArrayList<PID> pidList = new ArrayList<PID>();
	
//	public void addFromXML(String name, String depth, String sampleIncrement)
//	{
//		PID pidItem 					= new PID(name, depth, sampleIncrement);
//		pidList.add(pidItem);
//	}
//	public void addFromObject(String name, Integer depth, Integer sampleIncrement)
//	{
//		PID pidItem 					= new PID(name, depth, sampleIncrement);
//		pidList.add(pidItem);
//	}
	public void configure(ArrayList <Ctrl_Configuration.PID_Data> paramPIDs)
	{
		for (Ctrl_Configuration.PID_Data 		paramPID : paramPIDs)
		{
			PID pidItem 				= new PID(paramPID);
			pidList.add(pidItem);
		}
	}
	public PID fetchPID(String name)
	{
		for (PID element : pidList) 
		{
			if (element.name.equalsIgnoreCase(name))
			{
					return element;
			}
		}
		return null;
	}
}

