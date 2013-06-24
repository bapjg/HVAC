package Calibration;

import java.io.FileNotFoundException;
import java.io.PrintWriter;



public class DS18B20
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
		int j;
	
		Thermometers thermometers = new Thermometers();
		
		long timeStart = System.currentTimeMillis();
		long timeIncrement = 0;
		
		
		PrintWriter csv = null;
        try
        {
	        csv = new PrintWriter("readings.csv");
        }
        catch (FileNotFoundException e1)
        {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
        }
		
		Integer[] reading = new Integer[10];
		
	
		for (i = 0; i < 60; i++)
		{
			String textLine = "";

			for (j = 0; j < 10; j++)
			{
				try
				{
					reading[j] = thermometers.fetchThermometer(j).readTemperature();
					textLine += reading[j] + ";";
				}
				catch (Exception e)
				{
					System.out.println(e.getMessage());
				}
			}
			timeIncrement = System.currentTimeMillis() - timeStart;
			textLine = timeIncrement +";" + textLine;
			csv.println(textLine);
			try
            {
	            Thread.sleep(1000 * 6);
            }
            catch (InterruptedException e)
            {
	            e.printStackTrace();
            }
		}
		csv.close();
	}
}
