package eRegulation;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Buttons
{
	private native int Read();
	
	public Boolean button0;
	public Boolean buttonCancel;
	
	public Boolean button1;
	public Boolean button2;
	
	public Boolean button3;
	public Boolean buttonDown;
	
	public Boolean button4;
	public Boolean buttonUp;
	
	public Boolean button5;
	public Boolean buttonOk;

	public Buttons()
	{

	}

	public int read()
	{
		Global.interfaceSemaphore.semaphoreLock("Buttons.read");
//		Integer result = Read();
//		Integer result1 = result;
		Global.interfaceSemaphore.semaphoreUnLock();
		
		button0 			= false;
		buttonCancel 		= false;
		
		button1 			= false;
		button2 			= false;
		
		button3 			= false;
		buttonDown 			= false;
		
		button4 			= false;
		buttonUp 			= false;
		
		button5 			= false;
		buttonOk 			= false;

		return 0;	// No button pressed

//		if (result1 >= 32)
//		{
//			button0 		= true;
//			buttonCancel 	= true;
//			result1 		= result1 - 32;
//		}
//		if (result1 >= 16)
//		{
//			button1 		= true;
//			result1 		= result1 - 16;
//		}
//		if (result1 >= 8)
//		{
//			button2 		= true;
//			result1 		= result1 - 8;
//		}
//		if (result1 >= 4)
//		{
//			button3 		= true;
//			buttonDown		= true;
//			result1 		= result1 - 4;
//		}
//		if (result1 >= 2)
//		{
//			button4 		= true;
//			buttonUp		= true;
//			result1 		= result1 - 2;
//		}
//		if (result1 == 1)
//		{
//			button5 		= true;
//			buttonOk		= true;
//		}
//		return result;
	}
	public void cancel()
	{
		return;
//		Global.interfaceSemaphore.semaphoreLock("Buttons.cancel");
//		@SuppressWarnings("unused")
//		Integer result = Read();
//		Global.interfaceSemaphore.semaphoreUnLock();
	}
}
