package eRegulation;

import java.io.*;
import java.io.FileWriter;
import java.io.File;

//===============================================================================================================================================================
//
//	GPIO Virtual File Structure
//	===========================
//
//	Directory	: /sys/class/gpio/
//  File		: 		export				(used to activate a gpio connection)
//  File		: 		unexport			(used to de-activate a gpio connection)
//  Directory	: 		gpio4				(if gpio pin 4 has been activated, this directory will be created)
//  File    	: 			active_low		(writing 1 inverts logic : High = 0, Low = 1)
//  File    	: 			direction		(either write "in" or "out")
//  File    	: 			edge			(used for poll(2) call to generate interupt on signal rising/falling etc)
//  Directory   :	 		power			(???)
//  Directory   : 			subsystem		(???)
//  File    	: 			uevent			(???)
//  File    	: 			value			(High = "1", Low = "0", either read or write depending on direction, above)
//
//  Example
//  =======
//  To use gpio in 4
//  write "4" 	to  file "/sys/class/gpio/"
//  write "in" 	to 	file "/sys/class/gpio/gpio4/direction"		for reading, or
//  write "out" to 	file "/sys/class/gpio/gpio4/direction"		for writing
//  read  		    file "/sys/class/gpio/gpio4/value"			for reading (returns "0" or "1"), or
//  write "0"	    file "/sys/class/gpio/gpio4/value"		 	for writing (low = "0" or high = "1")
//
//===============================================================================================================================================================

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class GPIO
{
	String 														pin;
	String														pinDirectory;
	static final String 										prefix 						= "/sys/class/gpio/";
	FileWriter 													exportFile;
	FileWriter 													unexportFile;
	
	public GPIO(Integer pin)
	{
		this.pin																			= pin.toString();
		this.pinDirectory																	= prefix + "gpio" + this.pin;
        try											
        {											
        	exportFile 																		= new FileWriter(prefix + "export");	// to activate GPIO pin
        	unexportFile 																	= new FileWriter(prefix + "unexport");	// to deactivate GPIO pin
            File gpioPinDirectory 															= new File(pinDirectory);				// if it exists, it is a directory
            if (gpioPinDirectory.exists()) 
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
           	LogIt.error("GPIO", "constructor", "Constructor Exception : " + e);
//        	System.out.println("Constructor Exception : " + e);
        }
	}
	public void setInput()
	{
        try
        {
        	FileWriter 											directionFile 				= new FileWriter(pinDirectory + "/direction");
        	directionFile.write("in");
        	directionFile.flush();
        	directionFile.close();
        }
        catch (IOException e)
        {
           	LogIt.error("GPIO", "setInput", "setInput Exception : " + e);
//           	System.out.println("setInput Exception : " + e);
        }
	}
	public void setOutput()
	{
        try
        {
        	FileWriter 											directionFile 				= new FileWriter(pinDirectory + "/direction");
        	directionFile.write("out");
        	directionFile.flush();
        	directionFile.close();
        }
        catch (IOException e)
        {
           	LogIt.error("GPIO", "setOutput", "setOutput Exception : " + e);
//           	System.out.println("setOutput Exception : " + e);
        }
	}
	public void setHigh()
	{
		setOutput();
		try
        {
            BufferedWriter 										valueFile 					= new BufferedWriter(new FileWriter(pinDirectory + "/value"));
            valueFile.write("1");
            valueFile.close();
         }
        catch (IOException e)
        {
           	LogIt.error("GPIO", "setHigh", "setHigh Exception : " + e);
//           	System.out.println("setHigh Exception : " + e);
        }
    }
	public void setLow()
	{
		setOutput();
		try
        {
            BufferedWriter 										valueFile 					= new BufferedWriter(new FileWriter(pinDirectory + "/value"));
            valueFile.write("0");
            valueFile.close();
         }
        catch (IOException e)
        {
           	LogIt.error("GPIO", "setLow", "setLow Exception : " + e);
//           	System.out.println("setLow Exception : " + e);
        }
    }
	public Boolean isHigh()
	{
		try
        {
            BufferedReader  									valueFile 					= new BufferedReader(new FileReader(pinDirectory + "/value"));
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
           	LogIt.error("GPIO", "isLow", "isHigh Exception : " + e);
//           	System.out.println("isHigh Exception : " + e);
           	return false;
        }
	}
	public Boolean isLow()
	{
		try
        {
            BufferedReader  									valueFile 					= new BufferedReader(new FileReader(pinDirectory+ "/value"));
            String												valueReturned				= valueFile.readLine();
            valueFile.close();
            if (valueReturned.equals("1"))
            {
            	return false;
            }
            else
            {
            	return true;
            }
        }
        catch (IOException e)
        {
           	LogIt.error("GPIO", "isLow", "isLow Exception : " + e);
           	return false;
        }
	}
	public void finalize()
	{
       	LogIt.error("GPIO", "finalize", "Finalising");
//       	System.out.println("Finalising");
        try
        {
        	exportFile 																		= new FileWriter(prefix + "export");
        	unexportFile 																	= new FileWriter(prefix + "unexport");
            File gpioPinDirectory 															= new File(pinDirectory);				// if it exists, it is a directory
            if (gpioPinDirectory.exists()) 
            {
                unexportFile.write(this.pin);
                unexportFile.flush();
                unexportFile.close();
            }
        }
        catch (Exception e)
        {
           	LogIt.error("GPIO", "finalize", "finalize Exception : " + e);
//        	System.out.println("finalize Exception : " + e);
        }
	}
}
