package eRegulation;

import eReg_Forms.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.text.DecimalFormat;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import HVAC_Common.*;
import HVAC_Common.Ctrl_Configuration.Relay;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Control
{
   @SuppressWarnings("unused")
   public static void main(String[] args) 						throws 						IOException, 
																							SAXException, 
																							ParserConfigurationException
	{
    	//============================================================
		//
		// Instantiate this class (required for JNI)
    	// and Set the current thread name
		//
    	
		@SuppressWarnings("unused")
		Control 												Me 							= new Control();
		Thread.currentThread().setName("Thread_Main");
		
		//
		//============================================================
		
		//============================================================
		//
		// For debugging on a Windows machine without the necessary hardware
		//
		
		// Not used any more, as debugging is done directly on the Pi
//		if (System.getProperty("os.name").equalsIgnoreCase("windows"))	System.out.println("libraries not loaded");
//		else															System.loadLibrary("Interfaces_SPI");

		if (System.getProperty("os.name").equalsIgnoreCase("windows"))	return;
		
		System.loadLibrary("Interfaces_SPI");
		System.loadLibrary("Interfaces_I2C");

		if (true)
		{

			if (!GraphicsEnvironment.isHeadless())
			{
				GraphicsDevice[] gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices(); // Only works if NOT headLess
		        
				Form_LogIt formLogIt = new Form_LogIt();
				formLogIt.setVisible(true);
				
				Form_Temperatures formTemperatures = new Form_Temperatures();
				formTemperatures.setVisible(true);
				
				Form_Actions formActions = new Form_Actions();
				formActions.setVisible(true);

//				JFrame frame = new JFrame("FrameDemo");
//		        frame.setMinimumSize(new Dimension(800, 400));
//		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
//	
//		        JLabel myLabel = new JLabel("Hello World !!!", SwingConstants.CENTER);
//		        myLabel.setFont(new Font("Serif", Font.BOLD, 22));
//		        myLabel.setBackground(Color.blue);
//		        myLabel.setOpaque(true);
//		        myLabel.setPreferredSize(new Dimension(100, 80));
//	
//		        frame.getContentPane().add(myLabel, BorderLayout.NORTH);
//		        frame.setVisible(true);
			}
			
			
			
			System.out.println("Reader");
	        Reader reader = new InputStreamReader(System.in);
	        boolean ready1 = reader.ready();
			System.out.println("ready : " + ready1);
			int ch = reader.read();
			System.out.println("char : " + ch);
		    
			System.out.println("InputStreamReader");
			InputStreamReader reader2 = new InputStreamReader(System.in);
	        boolean ready2 = reader2.ready();
			System.out.println("ready : " + ready2);
			int ch2 = reader.read();
			System.out.println("char : " + ch2);
		
			
			System.out.println("InputStreamReader & System.in.read() Try1");
			InputStreamReader reader3 = new InputStreamReader(System.in);
            int key;
	        boolean ready3 = reader3.ready();
			System.out.println("ready : " + ready3);
            try 
            {
                key = System.in.read();
                // read a character and process it 
                System.out.println("key pressed");
             } 
            catch (java.io.IOException ioex) 
            {
                System.out.println("IO Exception");
            }

			System.out.println("InputStreamReader & System.in.read() Try2");
			System.out.println("ready : " + reader3.ready());
            try 
            {
                key = System.in.read();
                // read a character and process it 
                System.out.println("key pressed");
             } 
            catch (java.io.IOException ioex) 
            {
                System.out.println("IO Exception");
            }
			
			
			
			
//			        java.io.InputStreamReader reader = new java.io.InputStreamReader(System.in);
//			        boolean b = false;
//			        while(!b)
//			        {
//			            try 
//			            {
//			                int key = System.in.read();
//			                // read a character and process it 
//			                System.out.println("key pressed");
//			                b = true;
//			             } catch (java.io.IOException ioex) {
//			                System.out.println("IO Exception");
//			             }
//			             // edit, lets not hog any cpu time
//			             try {
//			                Thread.sleep(50);
//			                System.out.println("nop yet");
//			             } catch (InterruptedException ex) {
//			                // can't do much about it can we? Ignoring 
//			                System.out.println("Interrupted Exception");
//			             }
//			        }
		
			

//			System.out.print(String.format("%c[%d;%df",escCode,row,column));		
//			System.out.print("99999999999999999");		
		}
		
//		Below is a test to use JNA in stead of JNI... It didn't work, failing on the statement
//	    	Linux_C_lib_DirectMapping libC = new Linux_C_lib_DirectMapping();
//		the constructor loads a linux library, which fails (library not found)
//		Experimentation abandoned
		
//	    System.setProperty("jna.library.path","/home/pi/HVAC/eReg/eRegulation/");
	    //Open I2C Bus 1 file
//		int O_RDWR = 0x00000002;
//	    String fileName = "/dev/i2c-1";
//	    Linux_C_lib_DirectMapping libC = new Linux_C_lib_DirectMapping();
//	    int file = libC.open(fileName, O_RDWR);
//	    if(file < 0)
//	    {
//	        System.out.println("Error opening file");
//	        return;
//	    }
//	    else
//	    {
//	        System.out.println("File open for reading and writing");
//	    }
		
		
		
		//
		//============================================================

		//============================================================
		//
		// Initialising : Note that "Initialising" message on LCD is handled
		// in the Global constructor when the LCD display has been created
		//

		@SuppressWarnings("unused")
		Global 													global 						= new Global();
		Global.stopNow																		= false;
		LogIt.info("Control", "main", "Starting/BootSequence");
		//
		//============================================================

		//============================================================
		//
		// Debug Code
		//
//		Global.relays.scanAndSet();
//		Global.waitSeconds(2);
//		Global.relays.scanAndSet();
//		Global.waitSeconds(2);
//		Global.relays.scanAndSet();
//		Global.waitSeconds(2);
//		Global.relays.scanAndSet();
//		Global.waitSeconds(2);
//		Global.relays.scanAndSet();
//		if (true) return;		
		//
		//============================================================
		
		//============================================================
		//
		// Read Calendar file
		//
		
		Global.display.writeAtPosition(2, 0, " Calendar");
		@SuppressWarnings("unused")
		Calendars												calendars 					= new Calendars();
		Global.display.writeAtPosition(2, 18, "Ok");

		calendars																			= null;				// To avoid memory use in the event of a new calendar later
		
		//
		//============================================================

		//============================================================
		//
		// Start threads (start with mixer, as it takes a long time
		//
		
		Global.display.writeAtPosition(3, 0, " Thermometers");
		for (Circuit_Abstract circuit : Global.circuits.circuitList)
		{
			if (circuit.mixer != null)
			{
				new Thread(new Thread_Mixer((Circuit_Mixer) circuit), 		"Thread_Mixer_" + circuit.name).start();
			}
		}
		new Thread(new Thread_Thermometers(), 								"Thread_Thermometers").start();
		Global.waitSeconds(15);												// Must wait 15 secs for all thermometers to be read and have values + allow for retries
		Global.display.writeAtPosition(3, 18, "Ok");

		new Thread(new Thread_UserInterface(), 								"Thread_UserInteface").start();
		new Thread(new Thread_TCPListen(), 									"Thread_TCPListen").start();
// TODO wait for Thread_Mixer to finish setting it to zero
		Global.waitSecondsForStopNow(90);									// Must wait 90 secs for all thermometers to be read and have values + allow for retries
		new Thread(new Thread_BackgroundTasks(), 							"Thread_BackgroundTasks").start();
		
		//
		//============================================================
		
		//=============================================================
		//
		// Main Code
		//
		
		Heat_Required 											globalHeatRequired			= new Heat_Required();
		Global.boiler.heatRequired															= globalHeatRequired;	// Avoid null pointer if stopNow actionned immediately
		
		while (!Global.stopNow)
		{
			Global.waitSecondsForStopNow(5);

			// Sequence each circuit and get the heat requirements
			globalHeatRequired.setZero();

			for (Circuit_Abstract circuit : Global.circuits.circuitList)
			{
//				if (circuit.active)								// Modified 30/06/2017 as radiator/floor were running
//				{
					circuit.taskScheduler();					// In Circuit_Abstract
					circuit.sequencer();
					if (circuit.heatRequired.tempMinimum > globalHeatRequired.tempMinimum)
					{
						globalHeatRequired.tempMinimum										= circuit.heatRequired.tempMinimum;
					}
					
					if (circuit.heatRequired.tempMaximum > globalHeatRequired.tempMaximum)
					{
						globalHeatRequired.tempMaximum										= circuit.heatRequired.tempMaximum;
					}
//				}
			}
//			if (! globalHeatRequired.isZero())			Global.boiler.requestHeat(globalHeatRequired);
			
			// Give heatRequirements to boiler and then run the sequencer
			Global.boiler.heatRequired														= globalHeatRequired;
			Global.boiler.sequencer();
		}
		
		//
		// End of Main Code : while (!Global.stopNow)
		//
		//=============================================================

		Global.boiler.requestIdle();
		Global.relays.offAll();
		Global.waitThreadTermination();							// Ensure that this is the last thread to stop

//		Global.exitStatus
//		0 : Stop goto Bash
//		1 : Restart eRegulation
//		2 : Reboot
//		3 : Reload Configuration	Not Handled here
//		4 : Reload Calendars		Not Handled here
//		5 : ShutDown
//		6 : Debug and Wait
//		7 : Debug no wait
		
		switch (Global.exitStatus)
		{
		case Ctrl_Actions_Stop.ACTION_Stop:													// Value 0 : Stop App
			Global.display.clear();
			Global.display.writeAtPosition(0, 0, "Stopping controler");
 			LogIt.info("Thread_Main", "main", "Stopping", true);
 			System.exit(Ctrl_Actions_Stop.ACTION_Stop);
 			break;
		case Ctrl_Actions_Stop.ACTION_Restart:												// Value 1 : Restart App
			Global.display.clear();
			Global.display.writeAtPosition(0, 0, "Restarting controler");
	 		LogIt.info("Thread_Main", "main", "Stopping and restarting application", true); 
 			System.exit(Ctrl_Actions_Stop.ACTION_Restart);
 			break;		
 		case Ctrl_Actions_Stop.ACTION_Reboot:												// Value 2 : Reboot Pi
			Global.display.clear();
			Global.display.writeAtPosition(0, 0, "Rebooting controler");
	 		LogIt.info("Thread_Main", "main", "Stopping and rebooting", true); 
 			System.exit(Ctrl_Actions_Stop.ACTION_Reboot);
 			break;
 		case Ctrl_Actions_Stop.ACTION_ShutDown:												// Value 5 : Reboot Pi
			Global.display.clear();
			Global.display.writeAtPosition(0, 0, "Shutting down");
			Global.display.writeAtPosition(1, 2, "HVAC controler");
			Global.display.writeAtPosition(2, 2, "completely");
	 		LogIt.info("Thread_Main", "main", "Stopping and shutting down", true); 
 			System.exit(Ctrl_Actions_Stop.ACTION_ShutDown);
 			break;
 		case Ctrl_Actions_Stop.ACTION_Debug_Wait:											// Value 6 : Reboot Pi
			Global.display.clear();
			Global.display.writeAtPosition(0, 0, "Restarting controler for Debug Wait");
	 		LogIt.info("Thread_Main", "main", "Stopping and restarting application with Debug Wait", true); 
 			System.exit(Ctrl_Actions_Stop.ACTION_Debug_Wait);
 			break;
 		case Ctrl_Actions_Stop.ACTION_Debug_NoWait:											// Value 7 : Reboot Pi
			Global.display.clear();
			Global.display.writeAtPosition(0, 0, "Restarting controler");
			Global.display.writeAtPosition(1, 1, "Debug : No Wait");
	 		LogIt.info("Thread_Main", "main", "Stopping and restarting application with Debug NoWait", true); 
 			System.exit(Ctrl_Actions_Stop.ACTION_Debug_NoWait);
 			break;
		}
	}
}
