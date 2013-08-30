package eRegulation;
 
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
 
public class Thread_UserInterface implements Runnable
{
	public void run()
    {
		LogIt.info("Thread_UserInterface", "Run", "Starting", true);            
 
		Global.display.clear();
		Buttons             buttons                                 = Global.buttons;   
        
		buttons.cancel();			// Removes all old button pushes from the buffer
		Global.waitSeconds(1);
		buttons.cancel();			// Removes all old button pushes from the buffer
		
		while (!Global.stopNow)
		{
                   
			showTemperatures();
 
			Global.waitSeconds(10);
			
			buttons.read();
                   
            if (buttons.button0)
            {
                   Global.stopNow                                     	= true;           //Exit the loop;
            }
            if (buttons.button1)
            {
                // Start HotWater
            	
    			buttons.read(); // In case of button bounce
    			
				Long	now												= Global.getTimeNowSinceMidnight();

				// Need to change this
				// Just create a taskItem. No need to put it on taskList as it wont be repeated
				// Once performed, sequencer will set taskActive to null which will delete the object

				if (Global.circuitHotWater.taskActive != null)
				{
					Global.circuitHotWater.taskActive					= new CircuitTask(	Global.circuitHotWater, // Circuit reference
																							now, 					// Time Start
																							now + 30 * 60 * 1000, 	// TimeEnd
																							350,					// TempObjective
																							true,					// StopOnObjective
																							"1, 2, 3, 4, 5, 6, 7");	// Days
				}
            }
            if (buttons.button2)
            {
                // Get task list
            	
    			buttons.read(); // In case of button bounce
    			
    			// This is to list active threads
				
				Set <Thread> threadSet = Thread.getAllStackTraces().keySet();
				System.out.println("---------------------------");
    			
    			Iterator<Thread> i = threadSet.iterator();
    			while(i.hasNext()) 
    			{
    				Thread j 											= i.next();
    				String threadName 									= j.getName();
    				if (threadName.substring(0,7).equals("Thread_"))
    				{
       					System.out.println(threadName);
    				}
    				else
    				{
    					System.out.println("--" + threadName);
    				}
    				
    			}
				System.out.println("---------------------------");
           }
           if (buttons.button3)
           {
                // Start HotWater
            	
    			buttons.read(); // In case of button bounce
    			Global.pumpWater.on();
           }
           if (buttons.button4)
           {
                // Start HotWater
            	
    			buttons.read(); // In case of button bounce
    			Global.pumpWater.off();
           }
		}
 		LogIt.info("Thread_UserInterface", "Run", "Stopping", true);             
	}
	public void showTemperatures()
	{
		Date date                                               		= new Date();
		SimpleDateFormat    dateFormat                          		= new SimpleDateFormat("dd.MM HH:mm:ss");
		
		Global.display.writeAtPosition(0, 0, dateFormat.format(date));

		Global.display.writeAtPosition(0, 16, Global.thermoOutside.toDisplay());
		Global.display.writeAtPosition(1, 0,  "Blr  MixH  MixO MixC");
		Global.display.writeAtPosition(2, 0,  Global.thermoBoiler.toDisplay());
		Global.display.writeAtPosition(2, 5,  Global.thermoFloorHot.toDisplay());
		Global.display.writeAtPosition(2, 11, Global.thermoFloorOut.toDisplay());
		Global.display.writeAtPosition(2, 16, Global.thermoFloorCold.toDisplay());
		Global.display.writeAtPosition(3, 0,  "H2O  ");
		Global.display.writeAtPosition(3, 5,  Global.thermoHotWater.toDisplay());

		Global.display.writeAtPosition(3, 11, "LivR ");
		Global.display.writeAtPosition(3, 16, Global.thermoLivingRoom.toDisplay());
	}
}
 