package eRegulation;

public class Thread_Thermometers implements Runnable
{
	public void run()
	{
		LogIt.info("Thread_Thermometers", "Run", "Starting", true);		

		while (!Global.stopNow)
		{
			Global.thermoFloorOut.readUnCached();
			Global.circuitFloor.mixer.pidControler.add(Global.thermoFloorOut.reading);
			Global.waitMilliSeconds(5);
			
			Global.thermoBoiler.readUnCached();
			Global.waitMilliSeconds(5);
			Global.thermoBoilerOut.read();
			Global.waitMilliSeconds(5);
			Global.thermoBoilerIn.read();
			Global.waitMilliSeconds(5);
			
			Global.thermoFloorIn.read();
			Global.waitMilliSeconds(5);
			
			Global.thermoRadiatorOut.read();
			Global.waitMilliSeconds(5);
			Global.thermoRadiatorIn.read();
			Global.waitMilliSeconds(5);
			
			Global.thermoOutside.read();
			Global.waitMilliSeconds(5);
			
			Global.thermoLivingRoom.read();
			Global.waitMilliSeconds(5);
			
			Global.thermoHotWater.read();

			LogIt.tempData();

			Global.waitSeconds(10);
		}
		
		LogIt.info("Thread_Thermometers", "Run", "Stopping", true);			
	}
}
