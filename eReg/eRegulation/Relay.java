package eRegulation;

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
	
	public Relay(String name, String address, String friendlyName)
	{
		relayBank = 0;
		
		this.name 		    		= name;
		this.friendlyName   		= friendlyName;
		this.relayNumber			= Integer.parseInt(address);
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
		return	relayOn;
//		The Interfaces.c code does not work : There is a fault somewhere
		// Code comparison with bw_tool : cannot find the difference
//		Global.interfaceSemaphore.semaphoreLock("Relay.isOn");
//		Boolean result			= IsOn(relayBank, relayNumber);
//		System.out.println("++++Relay " + name + ", number " + relayNumber + " is " + result);
//		Global.interfaceSemaphore.semaphoreUnLock();
//		return result;
	}
}
