package eRegulation;

import java.util.concurrent.locks.ReentrantLock;


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
//		Long now 				= System.currentTimeMillis();
		Global.semaphore.lock();
//		if (timeActioned != 0L)
//		{
//			if (now - timeActioned < 10L)
//			{
//				try
//		        {
//					System.out.println("Relay : Thread collision (on )  " + name + "/" + Thread.currentThread().getName());
//					Thread.sleep(10L - now + timeActioned);
//		        }
//		        catch (InterruptedException e)
//		        {
//			        e.printStackTrace();
//		        }
//			}
//			else
//			{
//				System.out.println("Relay : Thread Ok        (on ) "  + name + "/" + Thread.currentThread().getName());
//			}
//		}
//		timeActioned = now;
		On(relayBank, relayNumber);
		Global.semaphore.unlock();
	}
	public void off()
	{
//		Long now 				= System.currentTimeMillis();
		Global.semaphore.lock();		
//		if (timeActioned != 0L)
//		{
//			if (now - timeActioned < 10L)
//			{
//				try
//		        {
//					System.out.println("Relay : Thread collision (off) "  + name + "/" + Thread.currentThread().getName());
//					Thread.sleep(10L - now + timeActioned);
//		        }
//		        catch (InterruptedException e)
//		        {
//			        e.printStackTrace();
//		        }
//			}
//			else
//			{
//				System.out.println("Relay : Thread Ok        (off) "  + name + "/" + Thread.currentThread().getName());
//			}
//		}
//		timeActioned = now;
		Off(relayBank, relayNumber);
		Global.semaphore.unlock();
	}
	public void onM()
	{
		Global.semaphore.lock();
		Long now 				= System.currentTimeMillis();
//		timeActioned = now;
		On(relayBank, relayNumber);
		Global.semaphore.unlock();
	}
	public void offM()
	{
		Global.semaphore.lock();
		Long now 				= System.currentTimeMillis();
//		timeActioned = now;
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
