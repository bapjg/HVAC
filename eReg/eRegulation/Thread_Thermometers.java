package eRegulation;

public class Thread_Thermometers implements Runnable
{
	public void run()
	{
		LogIt.info("Thread_Thermometers", "Run", "Starting", true);		

		while (!Global.stopNow)
		{
			Global.thermoFloorOut.readUnCached();
			Global.thermoFloorOut.pidControler.add(Global.thermoFloorOut.reading);
			Global.circuitFloor.mixer.pidControler.add(Global.thermoFloorOut.reading);

			LogIt.display("Global", "run", "floorout " + Global.thermoFloorOut.pidControler.dTdt());
			
			Global.thermoBoiler.readUnCached();

			Global.thermoBoilerOut.readUnCached();
			Global.thermoBoilerOut.pidControler.add(Global.thermoBoilerOut.reading);
			LogIt.display("Global", "run", "BoilerOut " + Global.thermoBoilerOut.pidControler.dTdt());

			Global.thermoBoilerIn.read();
			Global.thermoFloorIn.read();
			Global.thermoRadiatorOut.read();
			Global.thermoRadiatorIn.read();
			Global.thermoOutside.read();
			Global.thermoLivingRoom.read();
			Global.thermoHotWater.read();

			LogIt.tempData();

			Global.waitSeconds(10);
		}
		
		LogIt.info("Thread_Thermometers", "Run", "Stopping", true);			
	}
}
