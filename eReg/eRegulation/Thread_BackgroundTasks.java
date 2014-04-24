package eRegulation;

public class Thread_BackgroundTasks implements Runnable
{
	public Thread_BackgroundTasks()
	{

	}
	public void run()
	{
		Integer i;
		
		// This task must handle :
		//   Summer pump running
		//   Antifreeze
		//   Optimisation
		//   Getting expected weather predictions
		
		
		LogIt.info("Thread_Summer", "Run", "Starting", true);
		
		// Do for each circuit where type <> hotwater =>>>   circuit.circuitPump.on().
		
		LogIt.action("PumpFloor", "On");
		Global.pumps.fetchPump("Pump_Floor").on();
		
		for (i = 0; (i < Global.summerPumpDuration) && (!Global.stopNow); i++)
		{
			Global.waitSeconds(1);
		}
		
		LogIt.action("PumpFloor", "Off");
		Global.pumps.fetchPump("Pump_Floor").off();

		LogIt.action("PumpRadiator", "On");
		Global.pumps.fetchPump("Pump_Radiator").on();
		
		for (i = 0; (i < Global.summerPumpDuration) && (!Global.stopNow); i++)
		{
			Global.waitSeconds(1);
		}
		
		LogIt.action("PumpRadiator", "Off");
		Global.pumps.fetchPump("Pump_Radiator").off();
		
		LogIt.info("Thread_Summer", "Run", "Stopping", true);		
	}
}
