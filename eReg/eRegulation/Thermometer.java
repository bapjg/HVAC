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
	public Thermometer_Stabiliser 	readings; 
	
	public Thermometer(String name, String address, String friendlyName)
	{
		this.name 		    									= name;
		this.friendlyName  										= friendlyName;
		this.address  											= address;
		this.thermoFile 										= "/sys/bus/w1/devices/" + address.toLowerCase().replace(" ", "") + "/w1_slave"; // remove spaces from address like '28-0000 49ec xxxx'
		this.readings											= new Thermometer_Stabiliser(name, 10, 100); // Depth 10 entries// Tolerence = 10 degrees
		
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
				if (thisIsBoiler)
				{
					System.out.println("2. ===============Thermometer/read : Line1 " + ThermoFile_InputLine1);
					System.out.println("3. ===============Thermometer/read : Line2 " + ThermoFile_InputLine2);
				}

				ThermoFile_InputData.close();
				ThermoFile_InputStream.close();
				ThermoFile_InputBuffer.close();

				if (ThermoFile_InputLine1.contains("YES")) //CRC is Ok
				{
					if (thisIsBoiler)
					{
						System.out.println("5. ===============Thermometer/read : YES");
					}
					Integer 		tempPosition 				= ThermoFile_InputLine2.indexOf("t=");
					Integer 		tempReading 				= Integer.parseInt(ThermoFile_InputLine2.substring(tempPosition + 2));

					//this.reading								= (tempReading + 50)/100;
					if (thisIsBoiler) {	System.out.println("6. >>>>>>>>>>>>>>> Thermometer/read : IntoAdd"); }

					this.reading								= this.readings.add((tempReading + 50)/100);
					if (thisIsBoiler) {	System.out.println("6. <<<<<<<<<<<<<<< Thermometer/read : OutoffAdd"); }

					if (thisIsBoiler) {	System.out.println("4. ===============Thermometer/read : tempReading/reading " + ((tempReading + 50)/100) + "/" + this.reading); }
					return this.reading;
				}
				else
				{
					if (thisIsBoiler) {	System.out.println("6. ===============Thermometer/read : NO");}

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

}
