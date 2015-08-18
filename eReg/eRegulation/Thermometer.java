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
import java.util.List;

import HVAC_Common.Ctrl_Configuration;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Thermometer
{
	public String 												name;
//	public String 												friendlyName;
	public String 												address;
	public String 												thermoFile_Normal;
	public String 												thermoFile_UnCached;
 	public Integer 												reading;
	public PID													pidControler;
	public ArrayList <Probe>									probes						= new ArrayList <Probe> ();
	
	public Thermometer(Ctrl_Configuration.Thermometer 			paramThermometer)
	{
		this.name 		    																= paramThermometer.name;
//		this.friendlyName  																	= "";
		this.address  																		= paramThermometer.address;
		this.pidControler																	= null;
		
		String prefix																		= "/mnt/1wire/";
		String suffix																		= "/";

		this.thermoFile_Normal																= prefix               + address.toUpperCase().replace(" ", "") + suffix; // remove spaces from address like '28-0000 49ec xxxx'
		this.thermoFile_UnCached															= prefix + "uncached/" + address.toUpperCase().replace(" ", "") + suffix; // remove spaces from address like '28-0000 49ec xxxx'
		
		if (paramThermometer.pidName != null)
		{
			PID 												thisPID						= Global.pids.fetchPID(paramThermometer.pidName);
			pidControler																	= thisPID;
		}
		else
		{
			pidControler																	= null;
		}
		addProbe(paramThermometer);
	}
	public void addProbe(Ctrl_Configuration.Thermometer 		paramThermometer)
	{
		probes.add(new Probe(paramThermometer));
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
    	
//		String	 		tempString;
//		float	 		tempFloat;
//    	
//    	try
//		{
//    		FileInputStream 								ThermoFile_InputStream 		= null;
//    		if (unCached)
//    		{
//        		ThermoFile_InputStream 													= new FileInputStream(thermoFile_UnCached + "temperature" + resolution.toString());
//    		}
//    		else
//    		{
//        		ThermoFile_InputStream 													= new FileInputStream(thermoFile_Normal   + "temperature" + resolution.toString());
//    		}
//			DataInputStream 								ThermoFile_InputData 		= new DataInputStream(ThermoFile_InputStream);
//			BufferedReader 									ThermoFile_InputBuffer 		= new BufferedReader(new InputStreamReader(ThermoFile_InputData));
//			String 											ThermoFile_InputLine 		= ThermoFile_InputBuffer.readLine();
//
//			ThermoFile_InputBuffer.close();
//			ThermoFile_InputData.close();
//			ThermoFile_InputStream.close();
//
//			tempString	 																= ThermoFile_InputLine.replace(" ", "");
//			tempFloat	 																= Float.parseFloat(tempString);
//			this.reading																= Math.round(tempFloat * 1000); // Round to milli-degree
//		}
//		catch (Exception err)
//		{
//			if (!this.name.equalsIgnoreCase("Boiler_In"))
//			{
//				System.out.println("Thermometer read Error on " + this.name + " message was : " + err.getMessage());
//			}
//			this.reading																= -273000; // Round to milli-degree
//		}		
//		return this.reading; //Last known good reading;
		
    	Boolean printIt = (this.name.equalsIgnoreCase("Boiler"));
    	System.out.println("Boiler/Name" + this.name + " printit : " + printIt);
		Integer												readings					= 0;
		Integer												count						= 0;
		for (Probe probe : probes)
		{
			Integer											aReading					= probe.read(resolution, unCached);
			if (aReading != null)
			{
				if (printIt) System.out.println("Boiler/" + this.address + " : " + aReading);
				readings																+= aReading;
				count++;
			}
		}
		this.reading																	= readings / count;
		if (printIt) System.out.println("Boiler/Average" + this.reading);
		return this.reading;
	}
    public String toDisplay()
    {
    	// Converts temperature in millidegrees into displayable format							// Either keep true or throw it out from display
    	DecimalFormat 										temperatureFormat 			= new DecimalFormat("0.0");
    	return  temperatureFormat.format((float) (this.reading)/1000F);
    }
    public class Probe
    {
    	public String 										name;
    	public String 										address;
    	public String 										thermoFile_Normal;
    	public String 										thermoFile_UnCached;
    	public Boolean										probeOk;
    	
    	public Probe(Ctrl_Configuration.Thermometer 		paramThermometer)
    	{
    		this.name																	= paramThermometer.name;
    		this.address																= paramThermometer.address;
    		this.probeOk																= true;
    		String prefix																= "/mnt/1wire/";
    		String suffix																= "/";

    		this.thermoFile_Normal														= prefix               + address.toUpperCase().replace(" ", "") + suffix; // remove spaces from address like '28-0000 49ec xxxx'
    		this.thermoFile_UnCached													= prefix + "uncached/" + address.toUpperCase().replace(" ", "") + suffix; // remove spaces from address like '28-0000 49ec xxxx'
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
	    		FileInputStream 								ThermoFile_InputStream 		= null;
	    		if (unCached)	        						ThermoFile_InputStream 		= new FileInputStream(thermoFile_UnCached + "temperature" + resolution.toString());
	    		else							        		ThermoFile_InputStream 		= new FileInputStream(thermoFile_Normal   + "temperature" + resolution.toString());

				DataInputStream 								ThermoFile_InputData 		= new DataInputStream(ThermoFile_InputStream);
				BufferedReader 									ThermoFile_InputBuffer 		= new BufferedReader(new InputStreamReader(ThermoFile_InputData));
				String 											ThermoFile_InputLine 		= ThermoFile_InputBuffer.readLine();

				ThermoFile_InputBuffer.close();
				ThermoFile_InputData.close();
				ThermoFile_InputStream.close();

				tempString	 																= ThermoFile_InputLine.replace(" ", "");
				tempFloat	 																= Float.parseFloat(tempString);
				if (! probeOk)									LogIt.display("Probe", "read", "Thermometer now reading Ok on " + this.name + "-" + this.address);
				probeOk																		= true;
				return	Math.round(tempFloat * 1000); // Round to milli-degree
			}
			catch (Exception err)
			{
				if (probeOk)									LogIt.display("Probe", "read", "Thermometer read Error on " + this.name + "-" + this.address + " message was : " + err.getMessage());
				probeOk																		= false;
				return null;
			}		
		}
    }
}
