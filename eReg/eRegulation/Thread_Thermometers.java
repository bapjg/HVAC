package eRegulation;

public class Thread_Thermometers implements Runnable
{
	public void run()
	{
		LogIt.info("Thread_Thermometers", "Run", "Starting", true);		

		// Set readings to any value to avoid rampup times falling on unitialised variabbles
		// See if this works
//		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		
		while (!Global.stopNow)
		{
			Long timeStart 						= Global.now();
			
			Global.thermoBoiler.readUnCached();
			Global.waitMilliSeconds(5);
			
			Global.thermoBoilerIn.read();
			Global.waitMilliSeconds(5);
			
			Global.thermoFloorOut.readUnCached();
			Global.circuitFloor.mixer.pidControler.add(Global.thermoFloorOut.reading);
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
			
			Long timeEnd						= Global.now();
			Long timeElapsed					= timeEnd - timeStart;
			Long timeIncrement 					= 15000L - timeElapsed;
			
			if (timeIncrement <= 0L)
			{
//				LogIt.error("Thread_Thermometers", "Run", "Temp reads over 15 seconds", false);
				timeIncrement					= 1500L;
			}

			Global.waitMilliSeconds(timeIncrement);
		}
		LogIt.info("Thread_Thermometers", "Run", "Stopping", true);			
	}
}
