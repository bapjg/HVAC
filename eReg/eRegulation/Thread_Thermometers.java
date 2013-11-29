package eRegulation;

public class Thread_Thermometers implements Runnable
{
	public void run()
	{
		LogIt.info("Thread_Thermometers", "Run", "Starting", true);		

		// Set readings to any value to avoid rampup times falling on unitialised variabbles
		// See if this works
//		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		
		Integer i = 0;
		
		while (!Global.stopNow)
		{
			Global.thermoFloorOut.readUnCached();
			Global.circuitFloor.mixer.pidControler.add(Global.thermoFloorOut.reading);
			Global.waitMilliSeconds(5);
			if (Global.thermoFloorOut.reading > 450)
			{
				LogIt.display("Thread_Thermometer", "mainLoop", "FloorOut is over temperature : " + Global.thermoFloorOut.reading);
			}
			
			if (i == 0)
			{
				Global.thermoBoiler.readUnCached();
				if (Global.thermoBoiler.reading != null)		//This can happen early as burner.pidControler is initialised faily late
				{
					Global.boiler.pidControler.add(Global.thermoBoiler.reading);
				}
				Global.waitMilliSeconds(5);
				
				Global.thermoBoilerIn.read();
				Global.waitMilliSeconds(5);
				
				
				Global.thermoFloorCold.read();
				Global.waitMilliSeconds(5);
				
				Global.thermoFloorHot.read();
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
			}
			
			i = i++ % 4;

			Global.waitMilliSeconds(5000);
		}
		LogIt.info("Thread_Thermometers", "Run", "Stopping", true);			
	}
}
