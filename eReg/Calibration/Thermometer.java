package Calibration;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Thermometer
{
	public String thermoFile;
	public String thermoRadical;
	public String name;
	public String friendlyName;
	public String filePath;
	
	public Thermometer()
	{
	}

	public Integer readTemperature()
	{
		try
		{
			// File contains Line 1 : Hex Data CRC YES (or CRC NO if failed)
			//               Line 2 : Hex Data t=nnnnn (where n is in millidegrees celcius)
			// We will do 5 retries in the event of failure. -99999 returned if all bad
			
			int i;
			for (i = 0; i < 5; i++)
			{
				FileInputStream ThermoFile_InputStream = new FileInputStream(filePath);
				DataInputStream ThermoFile_InputData = new DataInputStream(ThermoFile_InputStream);
				BufferedReader ThermoFile_InputBuffer = new BufferedReader(new InputStreamReader(ThermoFile_InputData));

				String ThermoFile_InputLine1 = ThermoFile_InputBuffer.readLine();
				String ThermoFile_InputLine2 = ThermoFile_InputBuffer.readLine();
				
				ThermoFile_InputBuffer.close();
				ThermoFile_InputData.close();
				ThermoFile_InputStream.close();
				
				if (ThermoFile_InputLine1.contains("YES")) //CRC is Ok
				{
					Integer tempPosition = ThermoFile_InputLine2.indexOf("t=");
					Integer tempReading = Integer.parseInt(ThermoFile_InputLine2.substring(tempPosition + 2));
					
					return tempReading;
				}
				else
				{
					// try again or return -999999
					try
					{
						Thread.sleep(5);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
			return -99999;
		}
		catch (Exception err)
		{
			System.out.println("Error: " + err.getMessage());
			return -99999;
		}
	}
}
