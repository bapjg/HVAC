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
		thermo.read();
		
		
		
		
		System.out.println("Done");
	}
}
