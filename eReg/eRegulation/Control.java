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
		
		String 													osName 						= System.getProperty("os.name").toLowerCase();
		
		if (osName.contains("windows"))							System.exit(Ctrl_Actions_Stop.ACTION_Stop);
		
		System.loadLibrary("Interfaces_SPI");
		System.loadLibrary("Interfaces_I2C");

		if (false)
		{

		}
		
		
		//
		//============================================================

		//============================================================
		//
		// Initialising : Note that "Initialising" message on LCD is handled
		// in the Global constructor when the LCD display has been created
		//

		Global 													global 						= new Global();
		Global.stopNow																		= false;
		LogIt.info("Control", "main", "Starting/BootSequence");

		//
		//============================================================

		//============================================================
		//
		// Debug Code
		//
	
		//
		//============================================================
		
		//============================================================
		//
		// Read Calendar file
		//
		
		@SuppressWarnings("unused")
		Calendars												calendars 					= new Calendars();
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

		new Thread(new Thread_UserInterface(), 								"Thread_UserInteface").start();
		new Thread(new Thread_TCPListen(), 									"Thread_TCPListen").start();

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


// Would need to run git pull first, and check finished before restarting	would need to test under debugger with "SLEEP 5".
		// Could have problems as git pull will be overwriting .class files
		// Doc says that should be ok (just before restating)
		
//		  public static void main(String[] args) {
//			    try {
//			      ProcessBuilder pb = new ProcessBuilder("/home/sam/myscript.sh");
//			      Process p = pb.start();     // Start the process.
//			      p.waitFor();                // Wait for the process to finish.
//			      System.out.println("Script executed successfully");
//			    } catch (Exception e) {
//			      e.printStackTrace();
//			      }
//
//			  }	
		
		
		
//		public void restartApplication()
//		{
//		  final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
//		  final File currentJar = new File(MyClassInTheJar.class.getProtectionDomain().getCodeSource().getLocation().toURI());
//
//		  /* is it a jar file? */
//		  if(!currentJar.getName().endsWith(".jar"))
//		    return;
//
//		  /* Build command: java -jar application.jar */
//		  final ArrayList<String> command = new ArrayList<String>();
//		  command.add(javaBin);
//		  command.add("-jar");
//		  command.add(currentJar.getPath());
//
//		  final ProcessBuilder builder = new ProcessBuilder(command);
//		  builder.start();
//		  System.exit(0);
//		}

//		import java.io.File;
//		import java.io.IOException;
//		import java.lang.management.ManagementFactory;
//
//		public class Main {
//		    public static void main(String[] args) throws IOException, InterruptedException {
//		        StringBuilder cmd = new StringBuilder();
//		        cmd.append(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java ");
//		        for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
//		            cmd.append(jvmArg + " ");
//		        }
//		        cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
//		        cmd.append(Main.class.getName()).append(" ");
//		        for (String arg : args) {
//		            cmd.append(arg).append(" ");
//		        }
//		        Runtime.getRuntime().exec(cmd.toString());
//		        System.exit(0);
//		    }
//		}
		
		Runtime 												runtime 					= Runtime.getRuntime();
 		ProcessBuilder 											processBuilder;
 		Process 												process;
		
		
		switch (Global.exitStatus)
		{
		case Ctrl_Actions_Stop.ACTION_Stop:													// Value 0 : Stop App
//			Global.display.clear();
//			Global.display.writeAtPosition(0, 0, "Stopping controler");
 			LogIt.info("Thread_Main", "main", "Stopping", true);
	 		Global.waitSeconds(5);
 			System.exit(Ctrl_Actions_Stop.ACTION_Stop);
 			break;
		case Ctrl_Actions_Stop.ACTION_Restart:												// Value 1 : Restart App
//			Global.display.clear();
//			Global.display.writeAtPosition(0, 0, "Restarting controler");
	 		LogIt.info("Thread_Main", "main", "Stopping and restarting application", true);
	 		Global.waitSeconds(5);
	 		processBuilder 																	= new ProcessBuilder("/home/pi/HVAC/eReg/eRegulation/HVAC_Run.sh");
	 		process 																		= processBuilder.start();     // Start the process.
//		    process.waitFor();                // Wait for the process to finish.

 			System.exit(Ctrl_Actions_Stop.ACTION_Restart);
 			break;		
 		case Ctrl_Actions_Stop.ACTION_Reboot:												// Value 2 : Reboot Pi
//			Global.display.clear();
//			Global.display.writeAtPosition(0, 0, "Rebooting controler");
//	 		LogIt.info("Thread_Main", "main", "Stopping and rebooting", true); 
// 			System.exit(Ctrl_Actions_Stop.ACTION_Reboot);
 			
	 		LogIt.info("Thread_Main", "main", "Stopping and rebooting", true);
	 		Global.waitSeconds(5);
 		    process 																		= runtime.exec("sudo shutdown -r now");
 		    System.exit(0);
 			
 			
 			
 			break;
 		case Ctrl_Actions_Stop.ACTION_ShutDown:												// Value 5 : Reboot Pi
//			Global.display.clear();
//			Global.display.writeAtPosition(0, 0, "Shutting down");
//			Global.display.writeAtPosition(1, 2, "HVAC controler");
//			Global.display.writeAtPosition(2, 2, "completely");
//	 		LogIt.info("Thread_Main", "main", "Stopping and shutting down", true); 
// 			System.exit(Ctrl_Actions_Stop.ACTION_ShutDown);

	 		LogIt.info("Thread_Main", "main", "Stopping and shutting down", true); 
	 		Global.waitSeconds(5);
	 		process												 							= runtime.exec("sudo shutdown -h now");
 		    System.exit(0);
 			
 			
 			
 			break;
 		case Ctrl_Actions_Stop.ACTION_Debug_Wait:											// Value 6 : Reboot Pi
//			Global.display.clear();
//			Global.display.writeAtPosition(0, 0, "Restarting controler for Debug Wait");
	 		LogIt.info("Thread_Main", "main", "Stopping and restarting application with Debug Wait", true); 
	 		Global.waitSeconds(5);
	 		processBuilder 																	= new ProcessBuilder("/home/pi/HVAC/eReg/eRegulation/HVAC_Debug_Wait.sh");
	 		process 																		= processBuilder.start();     // Start the process.
 			System.exit(Ctrl_Actions_Stop.ACTION_Debug_Wait);
 			break;
 		case Ctrl_Actions_Stop.ACTION_Debug_NoWait:											// Value 7 : Reboot Pi
//			Global.display.clear();
//			Global.display.writeAtPosition(0, 0, "Restarting controler");
//			Global.display.writeAtPosition(1, 1, "Debug : No Wait");
	 		LogIt.info("Thread_Main", "main", "Stopping and restarting application with Debug NoWait", true); 
	 		Global.waitSeconds(5);
	 		processBuilder 																	= new ProcessBuilder("/home/pi/HVAC/eReg/eRegulation/HVAC_Debug_Wait.sh");
	 		process 																		= processBuilder.start();     // Start the process.
 			System.exit(Ctrl_Actions_Stop.ACTION_Debug_NoWait);
 			break;
		}
	}
}
