package eRegulation;

import java.util.ArrayList;
import java.util.Iterator;

import HVAC_Common.Ctrl_Configuration;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Relays
{
	public ArrayList<Relay> relayList = new ArrayList<Relay>();
	
	public native void OffAll(int Relay_Bank);
	public native void ScanAndSet(int Relay_Bank);

//	public void addFromXML(String name, String address, String friendlyName)
//	{
//		Relay relayItem 				= new Relay(name, address, friendlyName);
//		relayList.add(relayItem);
//	}
//	public void addFromObject(String name, Integer address, Integer friendlyName)
//	{
//		Relay relayItem 				= new Relay(name, address, friendlyName);
//		relayList.add(relayItem);
//	}
	public void configure(ArrayList <Ctrl_Configuration.Relay> paramRelays)
	{
		for (Ctrl_Configuration.Relay		 	paramRelay : paramRelays)
		{
			Relay relayItem 				= new Relay(paramRelay);
			relayList.add(relayItem);
		}
	}
	
//	public void addFromObject(Ctrl_Configuration.Relay relayParam)
//	{
//		Relay relayItem 				= new Relay(relayParam);
//		relayList.add(relayItem);
//	}
	public Relay fetchRelay(String name)
	{
		for (Relay element : relayList) 
		{
			if (element.name.equalsIgnoreCase(name))
			{
					return element;
			}
		}
		return null;
	}
	public void offAll()
	{
		Global.interfaceSemaphore.semaphoreLock("Relays.offAll");
		for (Relay element : relayList) 
		{
			element.off();
			Global.waitMilliSeconds(500);								// Avoid switches all relays off at the same time
		}
		Global.interfaceSemaphore.semaphoreUnLock();
	}
	public void scanAndSet()
	{
		Global.interfaceSemaphore.semaphoreLock("Relays.scanAndSet");
		ScanAndSet(0);		// Bank 0 : Burner
		Global.interfaceSemaphore.semaphoreUnLock();
	}
}
