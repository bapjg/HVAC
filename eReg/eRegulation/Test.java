package eRegulation;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class Test
{

	public static void main(String[] args)
	{
		Control 		Me 							= new Control();
		
		Thermometer thermo1							= new Thermometer("Test1", "28.1C01 8F04 0000", "Test friend");
		Thermometer thermo2							= new Thermometer("Test2", "28.629F 8E04 0000", "Test friend");
		Thermometer thermo3							= new Thermometer("Test3", "28.4492 8E04 0000", "Test friend");

		thermo1.readAll();

		Global.waitSeconds(3);
		
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
