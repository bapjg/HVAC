package eRegulation;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Thermometer
{
	public String 					thermoFile;
	public String 					thermoRadical;
	public String 					name;
	public String 					friendlyName;
	public String 					filePath;
 	public Integer 					reading;
//	Go for a depth of 5 readings and a tolerance of 2 degrees
	public Thermometer_Stabliser 	readings		= new Thermometer_Stabliser(5, 20); 
	
	
	public Thermometer(String name, String address, String friendlyName)
	{
		this.name 		    									= name;
		this.thermoFile 			    						= address;
		this.friendlyName  										= friendlyName;
		this.reading											= 0;
		
		String  					thermoRadical 				= "/sys/bus/w1/devices/";
		String 						thermoFile 					= thermoRadical + this.thermoFile.toLowerCase().replace(" ", "") + "/w1_slave"; // remove spaces from address like '28-0000 49ec xxxx'
		
		this.filePath 											= thermoFile;
	}
    public Integer read()
	{
		// File contains Line 1 : Hex Data CRC YES (or CRC NO if failed)
		//               Line 2 : Hex Data t=nnnnn (where n is in millidegrees celcius)
		// We will do 5 retries in the event of failure. -99999 returned if all bad
		
    	int i;
		for (i = 0; i < 5; i++)
		{
	    	try
			{
				FileInputStream 	ThermoFile_InputStream 		= new FileInputStream(filePath);
				DataInputStream 	ThermoFile_InputData 		= new DataInputStream(ThermoFile_InputStream);
				BufferedReader 		ThermoFile_InputBuffer 		= new BufferedReader(new InputStreamReader(ThermoFile_InputData));

				String 				ThermoFile_InputLine1 		= ThermoFile_InputBuffer.readLine();
				String 				ThermoFile_InputLine2 		= ThermoFile_InputBuffer.readLine();

				ThermoFile_InputData.close();
				ThermoFile_InputStream.close();
				ThermoFile_InputBuffer.close();

				if (ThermoFile_InputLine1.contains("YES")) //CRC is Ok
				{
					Integer 		tempPosition 				= ThermoFile_InputLine2.indexOf("t=");
					Integer 		tempReading 				= Integer.parseInt(ThermoFile_InputLine2.substring(tempPosition + 2));

					this.reading								= this.readings.add((tempReading + 50)/100);
					return this.reading;
				}
				else
				{
					// try again or return -999999
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
		return reading; //Last known good reading;
	}
    public String toDisplay()
    {
    	// Converts temperature in decidegrees into displayable format
    	Integer Degrees 										= this.reading/10;
    	Integer Decimals 										= this.reading - Degrees * 10;
    	return Degrees.toString() + "." + Decimals.toString();
    }
}
