package eRegulation;

public class Buttons
{
	private native int Read();
	
	public Boolean button0;
	public Boolean button1;
	public Boolean button2;
	public Boolean button3;
	public Boolean button4;
	public Boolean button5;

	public Buttons()
	{

	}

	public int read()
	{
		Global.semaphore.lock();
		Integer result = Read();
		Integer result1 = result;
		Global.semaphore.unlock();
		
		button0 = false;
		button1 = false;
		button2 = false;
		button3 = false;
		button4 = false;
		button5 = false;
				
		if (result1 >= 32)
		{
			button0 = true;
			result1 = result1 - 32;
		}
		if (result1 >= 16)
		{
			button1 = true;
			result1 = result1 - 16;
		}
		if (result1 >= 8)
		{
			button2 = true;
			result1 = result1 - 8;
		}
		if (result1 >= 4)
		{
			button3 = true;
			result1 = result1 - 4;
		}
		if (result1 >= 2)
		{
			button4 = true;
			result1 = result1 - 2;
		}
		if (result1 == 1)
		{
			button5 = true;
		}
		return result;
	}
	public void cancel()
	{
		Integer result = Read();
	}

}
