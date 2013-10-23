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
		for (i = 9; i < 13; i++)
		{
			Long now									= Global.getTimeNowSinceMidnight();
			String iString								= i.toString();
			thermo.read(iString);
			Long later									= Global.getTimeNowSinceMidnight();
			System.out.println("time required : " + (later - now));
		
		}
		System.out.println("Done");
	}
}
