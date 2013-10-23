package eRegulation;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class Test
{

	public static void main(String[] args)
	{
		Control 		Me 							= new Control();
		
		Thermometer_New thermo1						= new Thermometer_New("Test1", "28.72D1 8504 0000", "Test friend");
		Thermometer_New thermo2						= new Thermometer_New("Test2", "28.9ED0 8504 0000", "Test friend");
		Thermometer_New thermo3						= new Thermometer_New("Test3", "28.B491 8504 0000", "Test friend");

		Integer i;
		Integer temp1, temp2, temp3;
		for (i = 0; i < 10; i++)
		{
			Long now									= Global.getTimeNowSinceMidnight();
			temp1										= thermo1.read();
			temp2										= thermo2.read();
			temp3										= thermo3.read();
			Long later									= Global.getTimeNowSinceMidnight();
			System.out.println(i.toString() + " temp : " + temp1 + ", time required Norm : " + (later - now));
			System.out.println(i.toString() + " temp : " + temp2 + ", time required Norm : " + (later - now));
			System.out.println(i.toString() + " temp : " + temp3 + ", time required Norm : " + (later - now));
		}

		for (i = 0; i < 10; i++)
		{
			Long now									= Global.getTimeNowSinceMidnight();
			temp1										= thermo1.readUnCached();
			temp2										= thermo2.readUnCached();
			temp3										= thermo3.readUnCached();
			Long later									= Global.getTimeNowSinceMidnight();
			System.out.println(i.toString() + " temp : " + temp1 + ", time required UnCa : " + (later - now));
			System.out.println(i.toString() + " temp : " + temp2 + ", time required UnCa : " + (later - now));
			System.out.println(i.toString() + " temp : " + temp3 + ", time required UnCa : " + (later - now));
		}
System.out.println("Done");
	}
}
