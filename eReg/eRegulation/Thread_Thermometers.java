package eRegulation;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Thread_Thermometers 								implements 					Runnable
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
						// TODO Check reading != null
						try
						{
							thisThermometer.readUnCached();
							thisThermometer.pidControler.add(thisThermometer.reading);
						}
						catch (Exception ex)
						{
							// Nought todo as all reporting has been done
						}
					}
					else
					{
						// TODO Check reading != null
						thisThermometer.pidControler.increment = ++thisThermometer.pidControler.increment % thisThermometer.pidControler.sampleIncrement;

						if (thisThermometer.pidControler.increment == 0)
						{
							try
							{
								thisThermometer.read();
								thisThermometer.pidControler.add(thisThermometer.reading);
							}
							catch (Exception ex)
							{
								// Nought todo as all reporting has been done
							}
						}
					}
				}
				else
				{
					try
					{
						thisThermometer.readUnCached();
					}
					catch (Exception ex)
					{
						// Nought todo as all reporting has been done
					}
				}
			}
			LogIt.tempData();
			Global.waitSeconds(10);
		}
		LogIt.info("Thread_Thermometers", "Run", "Stopping", true);			
	}
}
