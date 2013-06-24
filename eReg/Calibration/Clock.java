package Calibration;
import java.util.*;

public class Clock
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

		try
		{
			long start = System.currentTimeMillis();
			System.out.println(new Date() + "\n");
			Thread.sleep(60000);
			System.out.println(new Date() + "\n");
			long end = System.currentTimeMillis();
			long diff = end - start;
			System.out.println("Difference is" + diff + "\n");
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
