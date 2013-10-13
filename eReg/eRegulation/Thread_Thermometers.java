package eRegulation;

public class Thread_Thermometers implements Runnable
{
	public void run()
	{
		LogIt.info("----------------------Thread_Thermometers", "Run", "Starting", true);		

		// Set readings to any value to avoid rampup times falling on unitialised variabbles
		
		while (!Global.stopNow)
		{
			Integer result = 0;
			Long timeStart 						= Global.now();
			
			LogIt.info("1. ---------------------Thread_Thermometers", "Run", "Reading all temperature", true);
			
			result = Global.thermoBoiler.read();
			System.out.println("2. ---------------------Thread_Thermometer/run : reading " + Global.thermoBoiler.reading);

			Global.waitMilliSeconds(5);
			result = Global.thermoBoilerIn.read();
			Global.waitMilliSeconds(5);
			result = Global.thermoFloorOut.read();
			result = Global.thermoFloorOut.reading;
			
			Global.circuitFloor.mixer.pidControler.add(result);
			Global.waitMilliSeconds(5);
			result = Global.thermoFloorCold.read();
			Global.waitMilliSeconds(5);
			result = Global.thermoFloorHot.read();
			Global.waitMilliSeconds(5);
			result = Global.thermoRadiatorOut.read();
			Global.waitMilliSeconds(5);
			result = Global.thermoRadiatorIn.read();
			Global.waitMilliSeconds(5);
			result = Global.thermoOutside.read();
			Global.waitMilliSeconds(5);
			result = Global.thermoLivingRoom.read();
			Global.waitMilliSeconds(5);
			result = Global.thermoHotWater.read();

			LogIt.tempData();
			
			Long timeEnd						= Global.now();
			Long timeElapsed					= timeEnd - timeStart;
			Long timeIncrement 					= 15000L - timeElapsed;
			
			if (timeIncrement <= 0L)
			{
				LogIt.error("Thread_Thermometers", "Run", "Temp reads over 15 seconds", false);
				timeIncrement					= 1500L;
			}

			Global.waitMilliSeconds(timeIncrement);
		}
		LogIt.info("Thread_Thermometers", "Run", "Stopping", true);			
	}
}
