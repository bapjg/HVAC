package eRegulation;

import HVAC_Messages.Ctrl_Configuration;

public class Relay
{
	private native void 	On(int Relay_Bank, int Relay_Number);
	private native void 	Off(int Relay_Bank, int Relay_Number);
	private native void 	AllOff(int Relay_Bank);
	private native boolean 	IsOn(int Relay_Bank, int Relay_Number);

	public String 			name;
	public String 			friendlyName;
	public int 				relayBank;
	public int 				relayNumber;
	public Boolean			relayOn;
	
//	public Relay(String name, String address, String friendlyName)
//	{
//		relayBank = 0;
//		
//		this.name 		    		= name;
//		this.friendlyName   		= friendlyName;
//		this.relayNumber			= Integer.parseInt(address);
//		this.relayOn				= false;
//	}
//	public Relay(String name, Integer relayBank, Integer relayNumber)
//	{
//		relayBank 					= relayBank;
//		
//		this.name 		    		= name;
//		this.friendlyName   		= "";
//		this.relayNumber			= relayNumber;
//		this.relayOn				= false;
//	}
	public Relay(Ctrl_Configuration.Relay relayParam)
	{
		relayBank 					= 0;
		
		this.name 		    		= relayParam.name;
		this.friendlyName   		= "";
		this.relayNumber			= relayParam.relayNumber;
		this.relayOn				= false;
	}
	public void on()
	{
		Global.interfaceSemaphore.semaphoreLock("Relay.on");
		On(relayBank, relayNumber);
		relayOn						= true;
		Global.interfaceSemaphore.semaphoreUnLock();
	}
	public void off()
	{
		// Call takes approx 12 ms (100 call to off = 1225ms)
		Global.interfaceSemaphore.semaphoreLock("Relay.off");
		Off(relayBank, relayNumber);
		relayOn						= false;
		Global.interfaceSemaphore.semaphoreUnLock();
	}
	public Boolean isOn()
	{
		Global.interfaceSemaphore.semaphoreLock("Relay.isOn");
		Boolean result			= IsOn(relayBank, relayNumber);
		Global.interfaceSemaphore.semaphoreUnLock();
		return result;
	}
}
