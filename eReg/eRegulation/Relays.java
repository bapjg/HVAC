package eRegulation;

import java.util.ArrayList;
import java.util.Iterator;


public class Relays
{
	public ArrayList<Relay> relayList = new ArrayList<Relay>();
	
	public native void OffAll(int Relay_Bank);
	public native void ScanAndSet(int Relay_Bank);

	public void addFromXML(String name, String address, String friendlyName)
	{
		Relay relayItem 				= new Relay(name, address, friendlyName);
		relayList.add(relayItem);
	}
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
		OffAll(0);		// Bank 0 : Burner, pumps, mixer
		Global.interfaceSemaphore.semaphoreUnLock();
	}
	public void scanAndSet()
	{
		Global.interfaceSemaphore.semaphoreLock("Relays.scanAndSet");
		ScanAndSet(0);		// Bank 0 : Burner
		Global.interfaceSemaphore.semaphoreUnLock();
	}
}
