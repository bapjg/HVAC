package eRegulation;

public class Thread_Thermometers implements Runnable
{
	public void run()
	{
		LogIt.info("Thread_Thermometers", "Run", "Starting", true);		

		while (!Global.stopNow)
		{
			for (Thermometer thisThermometer : Global.thermometers.thermometerList)
			{
				System.out.println("thisThermo " + thisThermometer.name);

				if (thisThermometer.pidControler != null)
				{
					System.out.println("thisThermo.pid " + thisThermometer.pidControler.name);

					if (thisThermometer.pidControler.sampleIncrement == 1)
					{
						System.out.println("ThermoName YES = " + thisThermometer.name);
					}
					else
					{
						System.out.println("ThermoName NON = " + thisThermometer.name);
					}
				}
				else
				{
					// Read no pid
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
