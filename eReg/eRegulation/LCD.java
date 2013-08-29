package eRegulation;

public class LCD
{
	private native void Clear();
	private native void Write(String displayText);
	private native void Position(int iLine, int iColumn);

	private String displayText;
	
	public LCD()
	{
	}

	public void clear()
	{
		Global.interfaceSemaphore.semaphoreLock("LDC.clear");
		Clear();
		Global.interfaceSemaphore.semaphoreUnLock();
	}

	public void write(String sText)
	{
		Global.interfaceSemaphore.semaphoreLock("LDC.write");
		displayText = sText;
		Write(displayText);
		Global.interfaceSemaphore.semaphoreUnLock();
	}
	public void writeAtPosition(int iLine, int iColumn, String sText)
	{
		Global.interfaceSemaphore.semaphoreLock("LDC.writeAtPosition");
		Position(iLine, iColumn);
		Global.waitMilliSeconds(50);
		displayText = sText;
		Write(displayText);
		Global.interfaceSemaphore.semaphoreUnLock();
	}

	public void position(int iLine, int iColumn)
	{
		Global.interfaceSemaphore.semaphoreLock("LDC.position");
		Position(iLine, iColumn);
		// I've found that screen gets screwed up
		//global.wait(1);
		Global.interfaceSemaphore.semaphoreUnLock();
	}
}
