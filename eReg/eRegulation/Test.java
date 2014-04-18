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
		
		TestXML x	= new Test().new TestXML();
		XML y = new XML();
		
		try
		{
			String z = y.getXML(x);
			System.out.println(z);
		}
		catch (Exception e)
		{
			
		}
		
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

		GPIO pin25										= new GPIO(25);
		pin25.setOutput();
		pin25.setHigh();
		pin25.setLow();
		pin25.setInput();
		System.out.println("isHigh : " + pin25.isHigh());
		pin25.finalize();
		pin25 = null;
		System.out.println("end of Test : =============== ");
		
		
		
		
		
		
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
                directionFile.write("out");
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
//                raf[channum] 							= new RandomAccessFile("/sys/class/gpio/gpio" + GpioChannels[channum] + "/value", "r");
                raf[channum] 							= new RandomAccessFile("/sys/class/gpio/gpio" + GpioChannels[channum] + "/value", "rw");
                raf[channum].seek(0);
//                byte[] bytes 							= new byte[2];
//                bytes[0]								= "1";
//                bytes[1]								= "\n";
//                
//                raf[channum].write(bytes);
                raf[channum].close();
                
                File mpuFile 							= new File("/sys/class/gpio/gpio" + GpioChannels[channum] + "/value");
                if(mpuFile.canWrite()) 
                {
                	System.out.println("Can write");
                		BufferedWriter bw 				= new BufferedWriter(new FileWriter("/sys/class/gpio/gpio" + GpioChannels[channum] + "/value"));
                		System.out.println("Can bw");
                		bw.write("0");
                		System.out.println("Can 1");
                		bw.flush();
                        bw.close();
                		System.out.println("Can nought");
                } else {
                		System.out.println("Cant write");
//                        Process p = Runtime.getRuntime().exec("su");
//                        
//                        DataOutputStream dos = new DataOutputStream(p.getOutputStream());
//                        dos.writeBytes("echo " + obj.getValue() + " > " + obj.getFile() + "\n");
//                        dos.writeBytes("exit");
//                        dos.flush();
//                        dos.close();
//                        
//                        if(p.waitFor() != 0)
//                                Log.i(TAG, "Could not write to " + obj.getFile());
                }

            
            
            
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
                    	System.out.print( "inBytes : " + inLine);
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
	public class TestXML
	{
		String thermo1							= "abc";
		Integer thermo2							= 2;
	}
}
