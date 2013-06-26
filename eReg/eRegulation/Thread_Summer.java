package eRegulation;

public class Thread_Summer implements Runnable
{
	public Thread_Summer()
	{

	}
	public void run()
	{
		Integer i;
		
		LogIt.info("Thread_Summer", "Run", "Starting");		
		
		Global.pumpFloor.on();
		
		for (i = 0; (i < Global.summerPumpDuration) && (!Global.stopNow); i++)
		{
			Global.waitSeconds(1);
		}
		
		Global.pumpFloor.off();

		Global.pumpRadiator.on();
		
		for (i = 0; (i < Global.summerPumpDuration) && (!Global.stopNow); i++)
		{
			Global.waitSeconds(1);
		}
		
		Global.pumpRadiator.off();
		
		LogIt.info("Thread_Summer", "Run", "Stopping");		
	}
}
