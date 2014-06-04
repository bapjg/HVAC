package eRegulation;

import java.io.*;
import java.io.FileWriter;
import java.io.File;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class GPIO
{
	String 														pin;
	static final String 										prefix 						= "/sys/class/gpio/";
	FileWriter 													exportFile;
	FileWriter 													unexportFile;
	
	public GPIO(Integer pin)
	{
		this.pin																			= pin.toString();
        try											
        {											
        	exportFile 																		= new FileWriter(prefix + "export");
        	unexportFile 																	= new FileWriter(prefix + "unexport");
            File exportFileCheck 															= new File(prefix + "gpio"+ this.pin);
            if (exportFileCheck.exists()) 
            {
                unexportFile.write(this.pin);
                unexportFile.flush();
                unexportFile.close();
            }
        
            // Set the port for use
            exportFile.write(this.pin);   
            exportFile.flush();
            exportFile.close();
        }
        catch (Exception e)
        {
        	System.out.println("Constructor Exception : " + e);
        }
	}
	public void setInput()
	{
        try
        {
        	FileWriter 											directionFile 				= new FileWriter(prefix + "gpio" + this.pin + "/direction");
        	directionFile.write("in");
        	directionFile.flush();
        	directionFile.close();
        }
        catch (IOException e)
        {
           	System.out.println("setInput Exception : " + e);
        }
	}
	public void setOutput()
	{
        try
        {
        	FileWriter 											directionFile 				= new FileWriter(prefix + "gpio" + this.pin + "/direction");
        	directionFile.write("out");
        	directionFile.flush();
        	directionFile.close();
        }
        catch (IOException e)
        {
           	System.out.println("setOutput Exception : " + e);
        }
	}
	public void setHigh()
	{
		setOutput();
		try
        {
            BufferedWriter 										valueFile 					= new BufferedWriter(new FileWriter(prefix + "gpio" + this.pin + "/value"));
            valueFile.write("1");
            valueFile.close();
         }
        catch (IOException e)
        {
           	System.out.println("setHigh Exception : " + e);
        }
    }
	public void setLow()
	{
		setOutput();
		try
        {
            BufferedWriter 										valueFile 					= new BufferedWriter(new FileWriter(prefix + "gpio" + this.pin + "/value"));
            valueFile.write("0");
            valueFile.close();
         }
        catch (IOException e)
        {
           	System.out.println("setLow Exception : " + e);
        }
    }
	public Boolean isHigh()
	{
		try
        {
            BufferedReader  									valueFile 					= new BufferedReader(new FileReader(prefix + "gpio" + this.pin + "/value"));
            String												valueReturned				= valueFile.readLine();
            valueFile.close();
            if (valueReturned.equals("1"))
            {
            	return true;
            }
            else
            {
            	return false;
            }
        }
        catch (IOException e)
        {
           	System.out.println("isHigh Exception : " + e);
           	return false;
        }
	}
	public void finalize()
	{
       	System.out.println("Finalising");
        try
        {
        	exportFile 																		= new FileWriter(prefix + "export");
        	unexportFile 																	= new FileWriter(prefix + "unexport");
            File 												exportFileCheck 			= new File(prefix + "gpio"+ this.pin);
            if (exportFileCheck.exists()) 
            {
                unexportFile.write(this.pin);
                unexportFile.flush();
                unexportFile.close();
            }
        }
        catch (Exception e)
        {
        	System.out.println("finalize Exception : " + e);
        }
	}

}
