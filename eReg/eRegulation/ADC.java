package eRegulation;

public class ADC
{
	private native void 	Initialise(int ADC_Channels, int ADC_Samples, int ADC_Bits_To_Shift);
	private native int 		Read();
	private native int 		ReadAverage();	
	
	public  ADC()
	{
		int ADC_Channels			= 2;		// Analog input is second channel. Need to monitor 2 channels
		int ADC_Samples				= 64;		// Number of samples in a read (Default value) (64 = 0x40 ie need to remove bits 0x3F)
		int ADC_Bits_To_Shift		= 6;		// Shifting 6 bits is dividing by 64
		
		//Initialise(ADC_Channels, ADC_Samples, ADC_Bits_To_Shift);
	}
	public float read()
	{
		Global.semaphore.lock();
		float voltage = Read();
		Global.semaphore.unlock();
		voltage 					= voltage * 5 /1023;  // 1023 = 5V
		return voltage;
	}
	public float readAverage()
	{
		Global.semaphore.lock();
		float voltage = Read();
		//float voltage = ReadAverage();
		Global.semaphore.unlock();
		voltage 					= voltage * 5 /1023;  // 1023 = 5V
		return voltage;
	}
}
