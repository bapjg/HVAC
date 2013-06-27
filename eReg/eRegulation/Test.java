package eRegulation;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class Test
{

	public static void main(String[] args)
	{
		Control 		Me 							= new Control();
		
		Long			fuelConsumed				= 45927L;

		try
		{
			InputStream  	file 					= new FileInputStream("FuelConsumed.txt");
			DataInputStream	input  					= new DataInputStream (file);
		    try
		    {
		    	fuelConsumed 						= input.readLong();
		    }
		    finally
		    {
		    	input.close();
		    }
		}  
		catch(IOException ex)
		{
			System.out.println("I/O error");
		}	

		
		fuelConsumed++;
		
		
		
		try
		{
			OutputStream 		file 				= new FileOutputStream("FuelConsumed.txt");
		    DataOutputStream 	output 				= new DataOutputStream(file);
		    try
		    {
		    	output.writeLong(fuelConsumed);
		    }
		    finally
		    {
		        output.close();
		    }
		}  
		catch(IOException ex)
		{
			System.out.println("I/O error");
		}	
		
		System.out.println("Done");
	}
}
