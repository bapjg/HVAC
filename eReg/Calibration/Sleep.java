package Calibration;

public class Sleep
{
	public static void main(String[] args)
	{
		//============================================================
		//
		// Instantiate this class (required for JNI)
		//
		// Calibrate_Thermometers Me = new Calibrate_Thermometers();
		//
		//============================================================

		int i;
	
		long timeStart = System.currentTimeMillis();
		long timeIncrement = 0;
		long timeAverage = 0;
		
		
		for (i = 0; i < 10; i++)
		{
			timeStart = System.currentTimeMillis();
			try
            {
	            Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
	            e.printStackTrace();
            }
			timeIncrement = System.currentTimeMillis() - timeStart;
			timeAverage += timeIncrement;
			System.out.println("Iteration " + i + " Suspendtime : " + timeIncrement);

		}
		System.out.println("Average Suspendtime : " + timeAverage/10);

	}
}
