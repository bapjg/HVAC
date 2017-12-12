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
	
	// GPIO
	public Integer												gpioChannel;
	public GPIO													gpio;
	// GPIO END
	
	public Boolean												isOn;
	
	public Relay(Ctrl_Configuration.Relay 				relayParam)
	{
		relayBank 																			= 0;
		
		this.name 		    																= relayParam.name;
		this.friendlyName   																= "";
		this.relayNumber																	= relayParam.relayNumber;
		this.isOn																			= false;
		this.gpioChannel																	= relayParam.gpioChannel;
		
		if (this.gpioChannel == null)							return;
		if (this.gpioChannel == 0)								return;
		
		this.gpio																			= new GPIO(gpioChannel);
		this.gpio.setLow();
	}
	public void on()
	{
		Global.interfaceSemaphore.semaphoreLock("Relay.on");
		
		if (gpio == null)										On(relayBank, relayNumber);
		else													gpio.setHigh();
		isOn																				= true;
		Global.interfaceSemaphore.semaphoreUnLock();
	}
	public void off()
	{
		// Call takes approx 12 ms (100 call to off = 1225ms)
		Global.interfaceSemaphore.semaphoreLock("Relay.off");
		if (gpio == null)										Off(relayBank, relayNumber);
		else													gpio.setLow();
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
