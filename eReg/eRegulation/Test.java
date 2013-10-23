package eRegulation;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class Test
{

	public static void main(String[] args)
	{
		Control 		Me 							= new Control();
		
		Thermometer_New thermo						= new Thermometer_New("Test", "28.72D1 8504 0000", "Test friend");

		Integer i;
		Integer temp;
		for (i = 0; i < 10; i++)
		{
			Long now									= Global.getTimeNowSinceMidnight();
			temp										= thermo.read();
			Long later									= Global.getTimeNowSinceMidnight();
			System.out.println(i.toString() + " temp : " + temp + ", time required Norm : " + (later - now));
		}

		for (i = 0; i < 10; i++)
		{
			Long now									= Global.getTimeNowSinceMidnight();
			temp										= thermo.readUnCached();
			Long later									= Global.getTimeNowSinceMidnight();
			System.out.println(i.toString() + " temp : " + temp + ", time required UnCa : " + (later - now));
		}
System.out.println("Done");
	}
}
