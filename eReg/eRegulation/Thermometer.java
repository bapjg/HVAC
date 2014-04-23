package eRegulation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Thermometer
{
	public String 					name;
	public String 					friendlyName;
	public String 					address;
	public String 					thermoFile_Normal;
	public String 					thermoFile_UnCached;
 	public Integer 					reading;
	public PID						pidControler;
	
	public Thermometer(String name, String address, String friendlyName, Boolean pid)
	{
		this.name 		    									= name;
		this.friendlyName  										= friendlyName;
		this.address  											= address;
		
		String prefix											= "/mnt/1wire/";
		String suffix											= "/";

		this.thermoFile_Normal									= prefix               + address.toUpperCase().replace(" ", "") + suffix; // remove spaces from address like '28-0000 49ec xxxx'
		this.thermoFile_UnCached								= prefix + "uncached/" + address.toUpperCase().replace(" ", "") + suffix; // remove spaces from address like '28-0000 49ec xxxx'
		
		if (pid)
		{
			pidControler										= new PID(10);
		}
	}
	public Thermometer(String name, String address, String friendlyName, String pid)
	{
		this.name 		    									= name;
		this.friendlyName  										= friendlyName;
		this.address  											= address;
		
		String prefix											= "/mnt/1wire/";
		String suffix											= "/";

		this.thermoFile_Normal									= prefix               + address.toUpperCase().replace(" ", "") + suffix; // remove spaces from address like '28-0000 49ec xxxx'
		this.thermoFile_UnCached								= prefix + "uncached/" + address.toUpperCase().replace(" ", "") + suffix; // remove spaces from address like '28-0000 49ec xxxx'
		
		if (! pid.equalsIgnoreCase(""))
		{
			PID thisPID											= Global.pids.fetchPID(pid);
			pidControler										= thisPID;
		}
		else
		{
			pidControler										= null;
		}
	}
	public Thermometer(String name, String address)
	{
		this.name 		    									= name;
		this.friendlyName  										= "No Name";
		this.address  											= address;
		
		String prefix											= "/mnt/1wire/";
		String suffix											= "/";

		this.thermoFile_Normal									= prefix               + address.toUpperCase().replace(" ", "") + suffix; // remove spaces from address like '28-0000 49ec xxxx'
		this.thermoFile_UnCached								= prefix + "uncached/" + address.toUpperCase().replace(" ", "") + suffix; // remove spaces from address like '28-0000 49ec xxxx'
		
		pidControler										= null;
	}
    public void readAll()
	{
		try
		{
			FileOutputStream 	ThermoFile_OutputStream = new FileOutputStream("/mnt/1wire/simultaneous/temperature");
			DataOutputStream 	ThermoFile_OutputData 	= new DataOutputStream(ThermoFile_OutputStream);
			int x = 1;
			ThermoFile_OutputStream.write(x);
			ThermoFile_OutputData.close();
			ThermoFile_OutputStream.close();
		}
		catch (Exception e)
		{
			System.out.println("Simu write Error message was : " + e.getMessage());
		}		
	}
    public Integer read()
	{
     	// Returns temperature in millidegrees
    	return read(10, false);
	}
    public Integer readUnCached()
 	{
     	// Returns temperature in millidegrees
    	return read(10, true);
 	}
    public Integer read(Integer resolution, Boolean unCached)
	{
     	// Returns temperature in millidegrees
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
    	
		String	 		tempString;
		float	 		tempFloat;
    	
    	try
		{
    		FileInputStream 	ThermoFile_InputStream 		= null;
    		if (unCached)
    		{
        		ThermoFile_InputStream 						= new FileInputStream(thermoFile_UnCached + "temperature" + resolution.toString());
    		}
    		else
    		{
        		ThermoFile_InputStream 						= new FileInputStream(thermoFile_Normal   + "temperature" + resolution.toString());
    		}
			DataInputStream 	ThermoFile_InputData 		= new DataInputStream(ThermoFile_InputStream);
			BufferedReader 		ThermoFile_InputBuffer 		= new BufferedReader(new InputStreamReader(ThermoFile_InputData));
			String 				ThermoFile_InputLine 		= ThermoFile_InputBuffer.readLine();

			ThermoFile_InputBuffer.close();
			ThermoFile_InputData.close();
			ThermoFile_InputStream.close();

			tempString	 									= ThermoFile_InputLine.replace(" ", "");
			tempFloat	 									= Float.parseFloat(tempString);
			this.reading									= Math.round(tempFloat * 1000); // Round to milli-degree
		}
		catch (Exception err)
		{
			if (!this.name.equalsIgnoreCase("Boiler_In"))
			{
				System.out.println("Thermometer read Error on " + this.name + " message was : " + err.getMessage());
			}
			this.reading									= -273000; // Round to milli-degree
		}		
		return this.reading; //Last known good reading;
	}
    public String toDisplay()
    {
    	// Converts temperature in millidegrees into displayable format							// Either keep true or throw it out from display
    	DecimalFormat temperatureFormat 					= new DecimalFormat("#.0");
    	return  temperatureFormat.format((float) (this.reading)/1000F);
    }
}
