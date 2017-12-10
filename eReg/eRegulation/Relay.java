package eRegulation;

import HVAC_Common.Ctrl_Configuration;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Relay
{
	private native void 										On(int Relay_Bank, int Relay_Number);
	private native void 										Off(int Relay_Bank, int Relay_Number);
	private native void 										AllOff(int Relay_Bank);
	private native boolean 										IsOn(int Relay_Bank, int Relay_Number);

	public String 												name;
	public String 												friendlyName;
	public int 													relayBank;
	public int 													relayNumber;
	public Boolean												isOn;
	
	public Relay(Ctrl_Configuration.Relay 				relayParam)
	{
		relayBank 																			= 0;
		
		this.name 		    																= relayParam.name;
		this.friendlyName   																= "";
		this.relayNumber																	= relayParam.relayNumber;
		this.isOn																			= false;
	}
	public void on()
	{
		Global.interfaceSemaphore.semaphoreLock("Relay.on");
		On(relayBank, relayNumber);
		isOn																				= true;
		Global.interfaceSemaphore.semaphoreUnLock();
	}
	public void off()
	{
		// Call takes approx 12 ms (100 call to off = 1225ms)
		Global.interfaceSemaphore.semaphoreLock("Relay.off");
		Off(relayBank, relayNumber);
		isOn																				= false;
		Global.interfaceSemaphore.semaphoreUnLock();
	}
// this seems to be failing, returning constantly false
//	public Boolean isOn()
//	{
//		Global.interfaceSemaphore.semaphoreLock("Relay.isOn");
//		Boolean result																		= IsOn(relayBank, relayNumber);
//		Global.interfaceSemaphore.semaphoreUnLock();
//		return result;
//	}
}
