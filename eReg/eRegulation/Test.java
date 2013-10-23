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

		thermo.read(12, false);

		Integer i;
		for (i = 0; i < 10; i++)
		{
			Long now									= Global.getTimeNowSinceMidnight();
			thermo.read(12, false);
			Long later									= Global.getTimeNowSinceMidnight();
			System.out.println(i.toString() + "/time required : " + (later - now));
		
		}
		System.out.println("Done");
	}
}
