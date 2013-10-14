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
		if (this.name.equalsIgnoreCase("Floor_Out"))
		{
			System.out.println("== Creating readingStabiliser for name : " + name +" depth = 3  and tolerance = 200 ");
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
		
    	Boolean thisIsBoiler = false;
    	if (this.name.equalsIgnoreCase("Boiler"))
		{
    		thisIsBoiler = true;
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

				ThermoFile_InputBuffer.close();
				ThermoFile_InputData.close();
				ThermoFile_InputStream.close();


				if (ThermoFile_InputLine1.contains("YES")) //CRC is Ok
				{
					Integer 		tempPosition 				= ThermoFile_InputLine2.indexOf("t=");
					Integer 		tempReading 				= Integer.parseInt(ThermoFile_InputLine2.substring(tempPosition + 2));

					//this.reading								= (tempReading + 50)/100;
																		if (thisIsBoiler) {	System.out.println("6. >>>>>>>>>>>>>>> Thermometer/read : IntoAdd"); }
					this.reading								= this.readings.add((tempReading + 50)/100);
																		if (thisIsBoiler) {	System.out.println("6. <<<<<<<<<<<<<<< Thermometer/read : OutoffAdd"); }
																		if (thisIsBoiler) {	System.out.println("4. =============== Thermometer/read : reading/acceptedReading " + ((tempReading + 50)/100) + "/" + this.reading); }
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
    	Integer Degrees 										= this.reading/10;
    	Integer Decimals 										= this.reading - Degrees * 10;
    	return Degrees.toString() + "." + Decimals.toString();
    }
    public class Reading_Stabiliser
    {
    	private String	 		name;
//    	private Integer[] 		readings;
    	private Reading[] 		readings;
    	private Integer			readingIndex;
    	private Integer			depth;
    	private Integer			tolerance;
    	private Integer			count;

    	public Reading_Stabiliser(String name, Integer depth, Integer tolerance)
    	{
    		this.name 		  	  							= name;
    		this.depth 				  	  					= depth;
    		this.readingIndex 				   				= 0;									// index is for next entry.
    		this.count										= 0;
    		this.tolerance									= tolerance;
  //  		this.readings									= new Integer[depth];
    		this.readings									= new Reading[depth];
    	}
    	public Integer add(Integer newReading)
    	{
   			System.out.println("Position 96");
   		    Integer result									= 0;
    		if (count == 0)
    		{
       			System.out.println("Position 97");
    			readings[readingIndex].reading 				= newReading;
    			readingIndex++;
    			count++;
    			result 										= newReading;
    		}
    		else
    		{
    			System.out.println("Position 45");
    			Integer avgReading							= average();
    			System.out.println("Position 46");
    			Integer varianceReading						= averageSquared() - avgReading;
    			System.out.println("Position 47");
    			
    			if (Math.abs(avgReading - newReading) < tolerance)
    			{
    				result 									= newReading;				// Within tolerance, return the reading
    			}
    			else
    			{
    				result 									= avgReading;				// Outside tolerance, return the reading
    			}
    			System.out.println("Position 1");
    			readings[readingIndex].reading 				= newReading;				// Add reading to the chain, even if out of tolerance, otherwise we cannot change the average
    			System.out.println("Position 2");
    			readings[readingIndex].average 				= avgReading;				// Add reading to the chain, even if out of tolerance, otherwise we cannot change the average
    			System.out.println("Position 3");
       			readings[readingIndex].standardDeviation	= Math.sqrt(varianceReading);
    			System.out.println("Position 4");
       			
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
    			sum											= sum + readings[i].reading;
    		}
    		return sum / count;
    	}
    	public Integer averageSquared()
    	{
    		Integer i;
    		Integer sumSquared								= 0;
    		for (i = 0; i < count; i++)
    		{
    			sumSquared									= sumSquared + readings[i].reading * readings[i].reading;
    		}
    		return sumSquared / count;
    	}
    	public class Reading
    	{
    		public 	Integer reading;
    		public 	Integer average;
    		public 	Double standardDeviation;
    		public 	Integer status;
    		public  Reading()
    		{
    		}
    	}
    }
}
