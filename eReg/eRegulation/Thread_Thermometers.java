package eRegulation;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Thread_Thermometers implements Runnable
{
	public void run()
	{
		LogIt.info("Thread_Thermometers", "Run", "Starting", true);		

		while (!Global.stopNow)										// Read each thermometer and add to PID if appropriate
		{
			for (Thermometer thisThermometer : Global.thermometers.thermometerList)
			{
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
			
			LogIt.tempData();
			Global.waitSeconds(10);
		}
		LogIt.info("Thread_Thermometers", "Run", "Stopping", true);			
	}
}
