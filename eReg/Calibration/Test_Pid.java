package Calibration;
import eRegulation.*;

import java.util.Calendar;

public class Test_Pid
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Long now = Calendar.getInstance().getTimeInMillis();
		
		
		PID pid	= new PID(5,3);
		pid.add(3);
		
		try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
		pid.add(4);
		try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }		pid.add(5);
		System.out.println(pid.getGain(0f, 0f, 1f));

	}

}
