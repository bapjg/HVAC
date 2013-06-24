package eRegulation;

public class Thread_Summer implements Runnable
{
	public Thread_Summer()
	{

	}
	public void run()
	{
		System.out.println("Thread Summer starting");
		
		Global.pumpFloor.on();
		Global.waitSeconds(Global.summerPumpDuration);
		Global.pumpFloor.off();

		Global.pumpRadiator.on();
		Global.waitSeconds(Global.summerPumpDuration);
		Global.pumpRadiator.off();
		
		System.out.println("Thread Summer ended");
	}
}
