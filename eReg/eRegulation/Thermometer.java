package eRegulation;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

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
		this.readings											= new Reading_Stabiliser(10, 100); // Depth 10 entries// Tolerence = 10 degrees
		
		int i;
		for (i = 0; i < 10; i++)
		{
			this.reading										= read();
		}
	}
    public Integer read()
	{
    	// This is for Dallas DS18B20
    	//             ==============
    	// File contains Line 1 : Hex Data CRC YES (or CRC NO if failed)
		//               Line 2 : Hex Data t=nnnnn (where n is in millidegrees celcius)
		// We will do 5 retries in the event of failure. -99999 returned if all bad
		if (this.name.equalsIgnoreCase("Boiler"))
		{
			System.out.println("1. ===============Thermometer/read : Entered for " + this.name);
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
				if (this.name.equalsIgnoreCase("Boiler"))
				{
					System.out.println("2. ===============Thermometer/read : Line1 " + ThermoFile_InputLine1);
					System.out.println("3. ===============Thermometer/read : Line2 " + ThermoFile_InputLine2);
				}

				ThermoFile_InputData.close();
				ThermoFile_InputStream.close();
				ThermoFile_InputBuffer.close();

				if (ThermoFile_InputLine1.contains("YES")) //CRC is Ok
				{
					System.out.println("5. ===============Thermometer/read : YES");
					Integer 		tempPosition 				= ThermoFile_InputLine2.indexOf("t=");
					Integer 		tempReading 				= Integer.parseInt(ThermoFile_InputLine2.substring(tempPosition + 2));

					//this.reading								= (tempReading + 50)/100;
					this.reading								= this.readings.add((tempReading + 50)/100);

					if (this.name.equalsIgnoreCase("Boiler"))
					{
						System.out.println("4. ===============Thermometer/read : tempReading/reading " + ((tempReading + 50)/100) + "/" + this.reading);
					}
					return this.reading;
				}
				else
				{
					System.out.println("6. ===============Thermometer/read : NO");
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
    	Integer Degrees 										= this.reading/10;
    	Integer Decimals 										= this.reading - Degrees * 10;
    	return Degrees.toString() + "." + Decimals.toString();
    }
    public class Reading_Stabiliser
    {
    	public Integer[] 		readings;
    	public Integer			index;
    	public Integer			depth;
    	public Integer			tolerance;
    	public Integer			count;

    	public Reading_Stabiliser(Integer depth, Integer tolerance)
    	{
    		this.depth 		  	  			= depth;
    		this.index 		   			 	= 0;									// index is for next entry.
    		this.count						= 0;
    		this.tolerance					= tolerance;
    		this.readings					= new Integer[depth];
    	}
    	public Integer add(Integer newReading)
    	{
    		if (count == 0)
    		{
    			readings[index] 			= newReading;
    			index++;
    			count++;
	    		if (Thermometer.this.name.equalsIgnoreCase("Boiler"))
	    		{
	    			System.out.println("Returning reading " + newReading); //uSEFULL
	    		}
	    		return newReading;
    		}
    		else
    		{
    			Integer avgReading			= average();
    			
    			if (Math.abs(avgReading - newReading) > tolerance)
    			{
    				readings[index] 		= newReading;				//	Add it to the chain, otherwise we cannot change the average
    				index					= index + 1 % depth;
    				if (count < depth)
    				{
    					count++;
    				}
    				System.out.println("========Returning add average, Ecart : " + (avgReading - newReading) + " avg : " + avgReading + " rdg : " +  newReading+ " tol : " +  tolerance);
       	    		if (Thermometer.this.name.equalsIgnoreCase("Boiler"))
    	    		{
    	    			System.out.println("Returning average " + avgReading); //uSEFULL
    	    		}
    				return avgReading;
    			}
    			else
    			{
    				readings[index] 		= newReading;
    				index					= index + 1 % depth;
    				if (count < depth)
    				{
    					count++;
    				}
    	    		if (Thermometer.this.name.equalsIgnoreCase("Boiler"))
    	    		{
    	    			System.out.println("Returning reading " + newReading); //uSEFULL
    	    		}
    				return newReading;
    			}
    		}
     	}
    	public Integer average()
    	{
    		Integer i;
    		Integer sum						= 0;
    		for (i = 0; i < count; i++)
    		{
    			sum							= sum + readings[i];
    		}
    		return sum / count;
    	}
    }
}
