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
//	public Relay fetchRelay(int index)
//	{
//		Relay element 					= null;
//		int i;
//		Iterator<Relay> itr = relayList.iterator();
//		
//		for (i = 0; i <= index; i++)
//		{
//			element 					= (Relay) itr.next();
//		}
//		return element;
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
//	public Relay fetchRelay(String name)
//	{
//		Relay element = null;
//		Iterator<Relay> itr 			= relayList.iterator();
//
//		while (itr.hasNext())
//		{
//			element 					= (Relay) itr.next();
//			if (element.name.equalsIgnoreCase(name))
//			{
//				return element;
//			}
//		}
//		return element;
//	}
	public void offAll()
	{
		Global.semaphore.lock();
		OffAll(0);		// Bank 0 : Burner
		Global.semaphore.unlock();
	}
	public void scanAndSet()
	{
		Global.semaphore.lock();
		ScanAndSet(0);		// Bank 0 : Burner
		Global.semaphore.unlock();
	}
}
