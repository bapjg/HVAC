package _Depricated;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class LCD
{
	private native void Clear();
	private native void Write(String displayText);
	private native void Position(int iLine, int iColumn);
	private native void BlinkOn();
	private native void BlinkOff();

	private String displayText;
	
	public LCD()
	{
	}

	public void clear()
	{
		return;
//		Global.interfaceSemaphore.semaphoreLock("LDC.clear");
//		Clear();
//		Global.interfaceSemaphore.semaphoreUnLock();
	}
	public void write(String sText)
	{
		return;
//		Global.interfaceSemaphore.semaphoreLock("LDC.write");
//		displayText = sText;
//		Write(displayText);
//		Global.interfaceSemaphore.semaphoreUnLock();
	}
	public void writeAtPosition(int iLine, int iColumn, String sText)
	{
		return;
//		Global.interfaceSemaphore.semaphoreLock("LDC.writeAtPosition");
//		Position(iLine, iColumn);
//		Global.waitMilliSeconds(50);
//		displayText = sText;
//		Write(displayText);
//		Global.interfaceSemaphore.semaphoreUnLock();
	}
	public void blinkAtPosition(int iLine, int iColumn)
	{
		return;
//		Global.interfaceSemaphore.semaphoreLock("LDC.blinkAtPosition");
//		Position(iLine, iColumn);
//		Global.waitMilliSeconds(50);
//		BlinkOn();
//		Global.interfaceSemaphore.semaphoreUnLock();
	}
	public void position(int iLine, int iColumn)
	{
		return;
//		Global.interfaceSemaphore.semaphoreLock("LDC.position");
//		Position(iLine, iColumn);
//		// I've found that screen gets screwed up
//		//global.wait(1);
//		Global.interfaceSemaphore.semaphoreUnLock();
	}
	public void blinkOn()
	{
		return;
//		Global.interfaceSemaphore.semaphoreLock("LDC.blinkOn");
//		BlinkOn();
//		Global.interfaceSemaphore.semaphoreUnLock();
	}
	public void blinkOff()
	{
		return;
//		Global.interfaceSemaphore.semaphoreLock("LDC.blinkOff");
//		BlinkOff();
//		Global.interfaceSemaphore.semaphoreUnLock();
	}
}
