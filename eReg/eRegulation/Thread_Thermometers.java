package eRegulation;

public class Thread_Thermometers implements Runnable
{
	public void run()
	{
		LogIt.info("Thread_Thermometers", "Run", "Starting", true);		

		while (!Global.stopNow)										// Read each thermometer and add to PID if appropriate
		{
			for (Thermometer thisThermometer : Global.thermometers.thermometerList)
			{
				System.out.println("Reading " + thisThermometer.name);
				
				if (thisThermometer.pidControler != null)
				{
					if (thisThermometer.pidControler.sampleIncrement == 1)
					{
						thisThermometer.readUnCached();
						thisThermometer.pidControler.add(thisThermometer.reading);
					}
					else
					{
						thisThermometer.read();
						thisThermometer.pidControler.increment = ++thisThermometer.pidControler.increment % thisThermometer.pidControler.sampleIncrement;

						if (thisThermometer.pidControler.increment == 0)
						{
							thisThermometer.pidControler.add(thisThermometer.reading);
						}
					}
				}
				else
				{
					thisThermometer.readUnCached();
				}
			}
			
			Global.thermoFloorOut.readUnCached();
			Global.thermoFloorOut.pidControler.add(Global.thermoFloorOut.reading);
			
			Global.thermoBoiler.readUnCached();
			Global.thermoBoilerOut.readUnCached();
			Global.thermoBoilerOut.pidControler.add(Global.thermoBoilerOut.reading);
			
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
