package eRegulation;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class ADC
{
	// Modified after Merge Panic
	private native void 	Initialise(int ADC_Channels, int ADC_Samples, int ADC_Bits_To_Shift);
	private native int 		Read();
	private native int 		ReadAverage();
	
	public  ADC()
	{
		int ADC_Channels			= 2;		// Analog input is second channel. Need to monitor 2 channels
		int ADC_Samples				= 64;		// Number of samples in a read (Default value) (64 = 0x40 ie need to remove bits 0x3F)
		int ADC_Bits_To_Shift		= 6;		// Shifting 6 bits is dividing by 64
		
		Global.interfaceSemaphore.semaphoreLock("ADC.constructor");
		Initialise(ADC_Channels, ADC_Samples, ADC_Bits_To_Shift);
		Global.interfaceSemaphore.semaphoreUnLock();
	}
	private float read()
	{
		// Test showed that this call takes less than 1ms/call
		// Actual data : 74 ms for 100 calls
		Global.interfaceSemaphore.semaphoreLock("ADC.read");
		float voltage = Read();
		Global.interfaceSemaphore.semaphoreUnLock();
		return 						 voltage * 5 /1023;  // 1023 = 5V
	}
	private float readAverage()
	{
		// Test showed that this call takes less than 1ms/call
		// Actual data : 74 ms for 100 calls
		Global.interfaceSemaphore.semaphoreLock("ADC.readAverage");
		float voltage = ReadAverage();
		Global.interfaceSemaphore.semaphoreUnLock();
		return 						 voltage * 5 /1023;  // 1023 = 5V
	}
	public Boolean isFault()					// Connected to burner to detect trip
	{
		float reading = readAverage();
		
		if (reading > 4.0)
		{
			//return true;
			return false;
		}
		else
		{
			return false;
		}
	}
	public Boolean isFuelFlowing()				// Connected to FuelFlow 230V line
	{
		float reading = readAverage();

		if ((reading > 2.0) && (reading < 3.0))
		{
			return true;
		}
		else
		{
			//return false;
			return true;
		}
	}
}
