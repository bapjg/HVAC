package eRegulation;

public class Thread_Thermometers implements Runnable
{

	public void run()
	{
		LogIt.info("Thread_Thermometers", "Run", "Starting");		

		while (!Global.stopNow)
		{
			
			long timeStart 						= System.currentTimeMillis();
			
			Global.thermoBoiler.read();
			waitAWhile();
			Global.thermoBoilerIn.read();
			waitAWhile();
			Global.thermoFloorOut.read();
			Global.circuitFloor.mixer.pidControler.add(Global.thermoFloorOut.reading);
			waitAWhile();
			Global.thermoFloorCold.read();
			waitAWhile();
			Global.thermoFloorHot.read();
			waitAWhile();
			Global.thermoRadiatorOut.read();
			waitAWhile();
			Global.thermoRadiatorIn.read();
			waitAWhile();
			Global.thermoOutside.read();
			waitAWhile();
			Global.thermoLivingRoom.read();
			waitAWhile();
			Global.thermoHotWater.read();

			LogIt.tempData();
			
			long timeEnd 						= System.currentTimeMillis();
			long timeIncrement 					= 10000 - timeEnd + timeStart;
			
			if (timeIncrement <= 0)
			{
				LogIt.error("Thread_Thermometers", "run", "Temp reads over 10 seconds");
				timeIncrement					= 1000;
			}


			try
            {
                Thread.sleep(timeIncrement);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

		}
		
		LogIt.info("Thread_Thermometers", "Run", "Stopping");			
	}
	public void waitAWhile()
	{
		try
        {
            Thread.sleep(5);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
		
	}
}
