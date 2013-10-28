package eRegulation;

import java.io.*;
import java.io.FileWriter;
import java.io.File;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.*;
import java.util.logging.*;

public class Test
{

	public static void main(String[] args)
	{
		Control 		Me 							= new Control();
		
		Thermometer thermo1							= new Thermometer("Test1", "28.1C01 8F04 0000", "Test friend");
		Thermometer thermo2							= new Thermometer("Test2", "28.629F 8E04 0000", "Test friend");
		Thermometer thermo3							= new Thermometer("Test3", "28.4492 8E04 0000", "Test friend");

		thermo1.readAll();

		Global.waitSeconds(3);
		
//		Integer i;
//		Integer temp1, temp2, temp3;
//		for (i = 0; i < 10; i++)
//		{
//			Long now									= Global.getTimeNowSinceMidnight();
//			temp1										= thermo1.read();
//			temp2										= thermo2.read();
//			temp3										= thermo3.read();
//			Long later									= Global.getTimeNowSinceMidnight();
//			System.out.println(i.toString() + " temp : " + temp1 + ", time required Norm : " + (later - now));
//			System.out.println(i.toString() + " temp : " + temp2 + ", time required Norm : " + (later - now));
//			System.out.println(i.toString() + " temp : " + temp3 + ", time required Norm : " + (later - now));
//		}

//		for (i = 0; i < 10; i++)
//		{
//			Long now									= Global.getTimeNowSinceMidnight();
//			temp1										= thermo1.readUnCached();
//			temp2										= thermo2.readUnCached();
//			temp3										= thermo3.readUnCached();
//			Long later									= Global.getTimeNowSinceMidnight();
//			System.out.println(i.toString() + " temp : " + temp1 + ", time required UnCa : " + (later - now));
//			System.out.println(i.toString() + " temp : " + temp2 + ", time required UnCa : " + (later - now));
//			System.out.println(i.toString() + " temp : " + temp3 + ", time required UnCa : " + (later - now));
//		}
		
		String[] GpioChannels							= { "25" };
		String GPIO_IN 									= "in";
        try 
        {
            
            /*** Init GPIO port(s) for input ***/
            
            // Open file handles to GPIO port unexport and export controls
            FileWriter unexportFile 					= new FileWriter("/sys/class/gpio/unexport");
            FileWriter exportFile 						= new FileWriter("/sys/class/gpio/export");

            for (String gpioChannel : GpioChannels) 
            {
                System.out.println(gpioChannel);
    
                // Reset the port
                File exportFileCheck 					= new File("/sys/class/gpio/gpio"+ gpioChannel);
                if (exportFileCheck.exists()) 
                {
                    unexportFile.write(gpioChannel);
                    unexportFile.flush();
                }
            
            
                // Set the port for use
                exportFile.write(gpioChannel);   
                exportFile.flush();

                // Open file handle to input/output direction control of port
                FileWriter directionFile 				= new FileWriter("/sys/class/gpio/gpio" + gpioChannel + "/direction");
            
                // Set port for input
                directionFile.write(GPIO_IN);
                directionFile.flush();
            }   
			
            /*** Read data from each GPIO port ***/
            RandomAccessFile[] raf 						= new RandomAccessFile[GpioChannels.length];
            
            int sleepPeriod 							= 10;
            final int MAXBUF 							= 256;
            
            byte[] inBytes 								= new byte[MAXBUF]; 
            String inLine;
            
            int zeroCounter 							= 0;
            
            // Get current timestamp with Calendar()
            // Open RandomAccessFile handle to each GPIO port
            for (int channum=0; channum < raf.length; channum++) 
            {
                raf[channum] 							= new RandomAccessFile("/sys/class/gpio/gpio" + GpioChannels[channum] + "/value", "r");
            }

	        // Loop forever
            while (true) 
            {
                // Get current timestamp for latest event
        
                // Use RandomAccessFile handle to read in GPIO port value
                for (int channum=0; channum < raf.length; channum++) 
                {
                   // Reset file seek pointer to read latest value of GPIO port
                    raf[channum].seek(0);
                    raf[channum].read(inBytes);
                    inLine 								= new String(inBytes);
                    
                    // Check if any value was read
                    if (inLine != null) 
                    {
                        // Compress 0 values so we don't see too many 
                        //   unimportant lines
                    	System.out.print( "inBytes : " + inBytes);
                        if (inLine.startsWith("0")) 
                        {
                            if (zeroCounter < 1000) 
                            {
                                zeroCounter++;
                            } 
                            else
                            {
                                System.out.print( "Some thing : " + inLine);
                                zeroCounter = 0;
                            }
                        } 
                        else 
                        {
                            // Else, specially mark value non-zero value
                            System.out.print( "Some thing else : " + inLine);
                            zeroCounter = 0;
                        }
                    }
                    // Wait for a while
                    java.lang.Thread.sleep(sleepPeriod);
        
                }
            }
        } 
        catch (Exception exception) 
        {
            exception.printStackTrace();
        }
    }
}
