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
	
	public Relay(String name, String address, String friendlyName)
	{
		relayBank = 0;
		
		this.name 		    		= name;
		this.friendlyName   		= friendlyName;
		this.relayNumber			= Integer.parseInt(address);
	}
	public void on()
	{
		// Thread Safety
		Global.interfaceSemaphore.lock();
		On(relayBank, relayNumber);
		Global.interfaceSemaphore.unlock();
	}
	public void off()
	{
		// Call takes approx 12 ms (100 call to off = 1225ms)
		Global.interfaceSemaphore.lock();		
		Off(relayBank, relayNumber);
		Global.interfaceSemaphore.unlock();
	}
	public void onM()
	{
		Global.interfaceSemaphore.lock();
		Long now 				= System.currentTimeMillis();
		On(relayBank, relayNumber);
		Global.interfaceSemaphore.unlock();
	}
	public void offM()
	{
		Global.interfaceSemaphore.lock();
		Long now 				= System.currentTimeMillis();
		Off(relayBank, relayNumber);
		Global.interfaceSemaphore.unlock();
	}
	public Boolean isOn()
	{
		Global.interfaceSemaphore.lock();
		Boolean result			= IsOn(relayBank, relayNumber);
		Global.interfaceSemaphore.unlock();
		return result;
	}
}
