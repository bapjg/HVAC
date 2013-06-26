package eRegulation;

public class Thread_Summer implements Runnable
{
	public Thread_Summer()
	{

	}
	public void run()
	{
		LogIt.info("Thread_Summer", "Run", "Starting");		
		
		Global.pumpFloor.on();
		Global.waitSeconds(Global.summerPumpDuration);
		Global.pumpFloor.off();

		Global.pumpRadiator.on();
		Global.waitSeconds(Global.summerPumpDuration);
		Global.pumpRadiator.off();
		
		LogIt.info("Thread_Summer", "Run", "Stopping");		
	}
}
