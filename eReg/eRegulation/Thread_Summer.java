package eRegulation;

public class Thread_Summer implements Runnable
{
	public Thread_Summer()
	{

	}
	public void run()
	{
		Integer i;
		
		LogIt.info("Thread_Summer", "Run", "Starting", true);
		
		// Do for each circuit where type <> hotwater =>>>   circuit.circuitPump.on().
		
		LogIt.action("PumpFloor", "On");
		Global.pumpFloor.on();
		
		for (i = 0; (i < Global.summerPumpDuration) && (!Global.stopNow); i++)
		{
			Global.waitSeconds(1);
		}
		
		LogIt.action("PumpFloor", "Off");
		Global.pumpFloor.off();

		LogIt.action("PumpRadiator", "On");
		Global.pumpRadiator.on();
		
		for (i = 0; (i < Global.summerPumpDuration) && (!Global.stopNow); i++)
		{
			Global.waitSeconds(1);
		}
		
		LogIt.action("PumpRadiator", "Off");
		Global.pumpRadiator.off();
		
		LogIt.info("Thread_Summer", "Run", "Stopping", true);		
	}
}
