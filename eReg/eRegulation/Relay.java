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
		Global.semaphore.lock();
		On(relayBank, relayNumber);
		Global.semaphore.unlock();
	}
	public void off()
	{
		// Call takes approx 12 ms (100 call to off = 1225ms)
		Global.semaphore.lock();		
		Off(relayBank, relayNumber);
		Global.semaphore.unlock();
	}
	public void onM()
	{
		Global.semaphore.lock();
		Long now 				= System.currentTimeMillis();
		On(relayBank, relayNumber);
		Global.semaphore.unlock();
	}
	public void offM()
	{
		Global.semaphore.lock();
		Long now 				= System.currentTimeMillis();
		Off(relayBank, relayNumber);
		Global.semaphore.unlock();
	}
	public Boolean isOn()
	{
		Global.semaphore.lock();
		Boolean result			= IsOn(relayBank, relayNumber);
		Global.semaphore.unlock();
		return result;
	}
}
