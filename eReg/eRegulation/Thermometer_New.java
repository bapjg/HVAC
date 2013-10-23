package eRegulation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Thermometer_New
{
	public String 					name;
	public String 					friendlyName;
	public String 					address;
	public String 					thermoFile;
 	public Integer 					reading;
 	public Integer 					readingTrue;
	public Reading_Stabiliser 		readings; 
	
	public Thermometer_New(String name, String address, String friendlyName)
	{
		this.name 		    									= name;
		this.friendlyName  										= friendlyName;
		this.address  											= address;
		
		String prefix											= "/mnt/1wire/";
		String suffix											= "/";

		this.thermoFile 										= prefix + address.toUpperCase().replace(" ", "").replace("-", ".") + suffix; // remove spaces from address like '28-0000 49ec xxxx'
//		if (this.name.equalsIgnoreCase("Floor_Out"))
//		{
//			this.readings										= new Reading_Stabiliser(name, 3, 200); // Depth 10 entries// Tolerence = 20 degrees
//		}
//		else
//		{
//			this.readings										= new Reading_Stabiliser(name, 10, 100); // Depth 10 entries// Tolerence = 10 degrees
//		}
//
//		int i;
//		for (i = 0; i < 5; i++)
//		{
//			read();
//		}
	}
    public Integer read(String resolution, Boolean all)
	{
     	/*
     	 *  Read times are :
     	 *  	When in cache : 10 ms
     	 *  	Not in cache  : 9 bit  - 300 ms
     	 *  	Not in cache  : 10 bit - 400 ms
     	 *  	Not in cache  : 11 bit - 500 ms
     	 *  	Not in cache  : 12 bit - 900 ms
     	 *  
     	 *  	A change of resolution (either up or down) seems to be uncached
     	 */
    	
    	
    	try
		{
  			if (all)
  			{
  				System.out.println("simu start");
  				FileOutputStream 	ThermoFile_OutputStream = new FileOutputStream("/mnt/1wire/simultaneous/temperature");
   				DataOutputStream 	ThermoFile_OutputData 	= new DataOutputStream(ThermoFile_OutputStream);
  				ThermoFile_OutputData.write(1);
  				System.out.println("simu end");
  				ThermoFile_OutputData.close();
  				ThermoFile_OutputStream.close();
			}
    		FileInputStream 	ThermoFile_InputStream 		= new FileInputStream(thermoFile + "temperature" + resolution);
			DataInputStream 	ThermoFile_InputData 		= new DataInputStream(ThermoFile_InputStream);
			BufferedReader 		ThermoFile_InputBuffer 		= new BufferedReader(new InputStreamReader(ThermoFile_InputData));

			String 				ThermoFile_InputLine 		= ThermoFile_InputBuffer.readLine();

			ThermoFile_InputBuffer.close();
			ThermoFile_InputData.close();
			ThermoFile_InputStream.close();


			String	 		tempString	 					= ThermoFile_InputLine.replace(" ", "");
			Float	 		tempFloat	 					= Float.parseFloat(tempString);

			System.out.println("tempString : " + tempString);
			System.out.println("tempFloat  : " + tempFloat);
			//			this.readingTrue								= (tempReading + 50)/100;
//			this.reading									= this.readings.add((tempReading + 50)/100);
//			return this.reading;
		}
		catch (Exception err)
		{
//			LogIt.error("Thermometer", "read", "Error message was : " + err.getMessage() + ", continuing iteration", false);
			System.out.println("Thermometer read Error message was : " + err.getMessage());
		}		

		return this.reading; //Last known good reading;
	}
    public String toDisplay()
    {
    	// Converts temperature in decidegrees into displayable format							// Either keep true or throw it out from display
    	Integer degrees 									= this.readingTrue/10;
    	Integer decimals 									= this.readingTrue - degrees * 10;
    	return degrees.toString() + "." + decimals.toString();
    }
    public class Reading_Stabiliser
    {
    	private String	 					name;
    	private Integer[] 					readings;
    	private Integer[] 					averages;
    	private Double[] 					standardDeviations;
    	private Integer						readingIndex;
    	private Integer						depth;
    	private Integer						tolerance;
    	private Integer						count;

    	public Reading_Stabiliser(String name, Integer depth, Integer tolerance)
    	{
    		this.name 		  	  							= name;
    		this.depth 				  	  					= depth;
    		this.readingIndex 				   				= 0;									// index is for next entry.
    		this.count										= 0;
    		this.tolerance									= tolerance;
    		this.readings									= new Integer[depth];
    		this.averages									= new Integer[depth];
    		this.standardDeviations							= new Double[depth];
    		int i;
    		for (i = 0; i < depth; i++)
    		{
    			readings[readingIndex]						= 0;
    			averages[readingIndex]						= 0;
    			standardDeviations[readingIndex]			= 0D;
    		}
    	}
    	public Integer add(Integer newReading)
    	{
   		    Integer result									= 0;
    		if (count == 0)
    		{
    			readings[readingIndex] 						= newReading;
    			averages[readingIndex] 						= newReading;
    			standardDeviations[readingIndex]			= 0D;
    			readingIndex++;
    			count++;
    			result 										= newReading;
    		}
    		else
    		{
    			Integer 	avgReading						= average();
    			Double 		standardDeviation				= Math.sqrt(averageSquared()  - avgReading * avgReading);
    			
    			if (Math.abs(avgReading - newReading) < tolerance)
    			{
    				result 									= newReading;				// Within tolerance, return the reading
    			}
    			else
    			{
    				result 									= avgReading;				// Outside tolerance, return the reading
    			}
    			readings[readingIndex] 						= newReading;				// Add reading to the chain, even if out of tolerance, otherwise we cannot change the average
    			averages[readingIndex]						= avgReading;				// Add reading to the chain, even if out of tolerance, otherwise we cannot change the average
    			standardDeviations[readingIndex]			= standardDeviation;
       			
    			readingIndex								= (readingIndex + 1) % depth;
    			if (count < depth)
    			{
    				count++;
    			}
    		}
    		return result;
     	}
    	public Integer average()
    	{
    		Integer i;
    		Integer sum										= 0;
    		for (i = 0; i < count; i++)
    		{
    			sum											= sum + readings[i];
    		}
    		return sum / count;
    	}
    	public Double averageSquared()
    	{
    		Integer i;
    		Double sumSquared								= 0D;
    		for (i = 0; i < count; i++)
    		{
    			sumSquared									= sumSquared + readings[i] * readings[i];
    		}
    		return sumSquared / count;
    	}
     }
}
