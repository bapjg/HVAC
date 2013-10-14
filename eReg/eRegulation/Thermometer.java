package eRegulation;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Thermometer
{
	public String 					name;
	public String 					friendlyName;
	public String 					address;
	public String 					thermoFile;
 	public Integer 					reading;
	public Reading_Stabiliser 		readings; 
	
	public Thermometer(String name, String address, String friendlyName)
	{
		this.name 		    									= name;
		this.friendlyName  										= friendlyName;
		this.address  											= address;
		this.thermoFile 										= "/sys/bus/w1/devices/" + address.toLowerCase().replace(" ", "") + "/w1_slave"; // remove spaces from address like '28-0000 49ec xxxx'
		if (this.name.equalsIgnoreCase("Floor_Out"))
		{
			this.readings										= new Reading_Stabiliser(name, 3, 200); // Depth 10 entries// Tolerence = 20 degrees
		}
		else
		{
			this.readings										= new Reading_Stabiliser(name, 10, 100); // Depth 10 entries// Tolerence = 10 degrees
		}
		int i;
		for (i = 0; i < 5; i++)
		{
			read();
		}
	}
    public Integer read()
	{
    	// This is for Dallas DS18B20
    	//             ==============
    	// File contains Line 1 : Hex Data CRC YES (or CRC NO if failed)
		//               Line 2 : Hex Data t=nnnnn (where n is in millidegrees celcius)
		// We will do 5 retries in the event of failure. -99999 returned if all bad
		
    	Boolean thisIsSpecial = false;
    	if (this.name.equalsIgnoreCase("Boiler"))
		{
    		thisIsSpecial = true;
 		}
		
		
    	int i;
		for (i = 0; i < 5; i++)
		{
	    	try
			{
				FileInputStream 	ThermoFile_InputStream 		= new FileInputStream(thermoFile);
				DataInputStream 	ThermoFile_InputData 		= new DataInputStream(ThermoFile_InputStream);
				BufferedReader 		ThermoFile_InputBuffer 		= new BufferedReader(new InputStreamReader(ThermoFile_InputData));

				String 				ThermoFile_InputLine1 		= ThermoFile_InputBuffer.readLine();
				String 				ThermoFile_InputLine2 		= ThermoFile_InputBuffer.readLine();

				ThermoFile_InputBuffer.close();
				ThermoFile_InputData.close();
				ThermoFile_InputStream.close();


				if (ThermoFile_InputLine1.contains("YES")) //CRC is Ok
				{
					Integer 		tempPosition 				= ThermoFile_InputLine2.indexOf("t=");
					Integer 		tempReading 				= Integer.parseInt(ThermoFile_InputLine2.substring(tempPosition + 2));

					//this.reading								= (tempReading + 50)/100;
//																		if (thisIsBoiler) {	System.out.println("6. >>>>>>>>>>>>>>> Thermometer/read : IntoAdd"); }
					this.reading								= this.readings.add((tempReading + 50)/100);
//																		if (thisIsBoiler) {	System.out.println("6. <<<<<<<<<<<<<<< Thermometer/read : OutoffAdd"); }
//																		if (thisIsBoiler) {	System.out.println("4. =============== Thermometer/read : reading/acceptedReading " + ((tempReading + 50)/100) + "/" + this.reading); }
					return this.reading;
				}
				else
				{
					Global.waitMilliSeconds(5);
				}
			}
			catch (Exception err)
			{
				LogIt.error("Thermometer", "read", "Error message was : " + err.getMessage() + ", continuing iteration", false);
				Global.waitMilliSeconds(5);
			}		
    	}
		LogIt.error("Thermometer", "read", "5 reads in a row returned CRC error on: " + name, false);
		return this.reading; //Last known good reading;
	}
    public String toDisplay()
    {
    	// Converts temperature in decidegrees into displayable format
    	Integer Degrees 									= this.reading/10;
    	Integer Decimals 									= this.reading - Degrees * 10;
    	return Degrees.toString() + "." + Decimals.toString();
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
