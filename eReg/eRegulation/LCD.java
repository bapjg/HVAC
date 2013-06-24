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
		Global.semaphore.lock();
		Clear();
		Global.semaphore.unlock();
	}

	public void write(String sText)
	{
		Global.semaphore.lock();
		displayText = sText;
		Write(displayText);
		Global.semaphore.unlock();
	}
	public void writeAtPosition(int iLine, int iColumn, String sText)
	{
		Global.semaphore.lock();
		Position(iLine, iColumn);
		Global.waitMilliSeconds(50);
		displayText = sText;
		Write(displayText);
		Global.semaphore.unlock();
	}

	public void position(int iLine, int iColumn)
	{
		Global.semaphore.lock();
		Position(iLine, iColumn);
		// I've found that screen gets screwed up
		//global.wait(1);
		Global.semaphore.unlock();
	}
}
