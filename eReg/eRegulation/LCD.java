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
		Global.interfaceSemaphore.lock();
		Clear();
		Global.interfaceSemaphore.unlock();
	}

	public void write(String sText)
	{
		Global.interfaceSemaphore.lock();
		displayText = sText;
		Write(displayText);
		Global.interfaceSemaphore.unlock();
	}
	public void writeAtPosition(int iLine, int iColumn, String sText)
	{
		Global.interfaceSemaphore.lock();
		Position(iLine, iColumn);
		Global.waitMilliSeconds(50);
		displayText = sText;
		Write(displayText);
		Global.interfaceSemaphore.unlock();
	}

	public void position(int iLine, int iColumn)
	{
		Global.interfaceSemaphore.lock();
		Position(iLine, iColumn);
		// I've found that screen gets screwed up
		//global.wait(1);
		Global.interfaceSemaphore.unlock();
	}
}
