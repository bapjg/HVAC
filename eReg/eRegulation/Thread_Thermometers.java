package eRegulation;

public class Thread_Thermometers implements Runnable
{

	public void run()
	{
		LogIt.info("Thread_Thermometers", "Run", "Starting");		

		while (!Global.stopNow)
		{
			
			Long timeStart 						= Global.now();
			
			Global.thermoBoiler.read();
			Global.waitMilliSeconds(5);
			Global.thermoBoilerIn.read();
			Global.waitMilliSeconds(5);
			Global.thermoFloorOut.read();
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
			
			Long timeElapsed					= Global.now() - timeStart;
			Integer timeIncrement 				= 10000 - timeElapsed.intValue();
			
			if (timeIncrement <= 0)
			{
				LogIt.error("Thread_Thermometers", "run", "Temp reads over 10 seconds");
				timeIncrement					= 1000;
			}

			Global.waitMilliSeconds(timeIncrement);
		}
		
		LogIt.info("Thread_Thermometers", "Run", "Stopping");			
	}
}
