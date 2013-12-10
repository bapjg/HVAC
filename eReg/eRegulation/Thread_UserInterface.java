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
		UserControl			userControl;
        
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
    			
            	userControl												= new UserControl("Hot Water");
            	userControl.line1Text									= "Temperature";
            	userControl.line1Value									= 35;
            	userControl.display();
            	
				Long	now												= Global.getTimeNowSinceMidnight();

				// Need to change this
				// Just create a taskItem. No need to put it on taskList as it wont be repeated
				// Once performed, sequencer will set taskActive to null which will delete the object

				System.out.println("HW requested");
				
				if (Global.circuitHotWater.taskActive == null)
				{
					System.out.println("Setting up HW circuit task");
					Global.circuitHotWater.taskActive					= new CircuitTask(	now, 					// Time Start
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
		Integer x = 0;
		x++;
		
		Date date                                               		= new Date();
		SimpleDateFormat    dateFormat                          		= new SimpleDateFormat("dd.MM HH:mm:ss");
		
		Global.display.writeAtPosition(0, 0, dateFormat.format(date));

		Global.display.writeAtPosition(0, 16, Global.thermoOutside.toDisplay());
		Global.display.writeAtPosition(1, 0,  "Blr  MixH  MixO MixR");
		Global.display.writeAtPosition(2, 4,  " ");								
		Global.display.writeAtPosition(2, 0,  Global.thermoBoiler.toDisplay() + " "); // add an extra char to Blank out possible remenicence of previous info
		Global.display.writeAtPosition(2, 5,  Global.thermoBoilerOut.toDisplay());
		Global.display.writeAtPosition(2, 11, Global.thermoFloorOut.toDisplay());
		Global.display.writeAtPosition(2, 16, Global.thermoFloorIn.toDisplay());
		Global.display.writeAtPosition(3, 0,  "H2O  ");
		Global.display.writeAtPosition(3, 5,  Global.thermoHotWater.toDisplay());

		Global.display.writeAtPosition(3, 11, "LivR ");
		Global.display.writeAtPosition(3, 16, Global.thermoLivingRoom.toDisplay());
	}
	private class UserControl
	{
		String 	title;
		String 	line1Text;
		Integer line1Value;
		String 	line2Text;
		Integer line2Value;
		String 	line3Text;
		Integer line3Value;
		Integer lineActive;
		
		public UserControl (String title)
		{
			this.title													= title;
			Global.display.clear();
			Global.display.writeAtPosition(0, 0, title);
		}
		public void display()
		{
			Buttons             buttons                                 = Global.buttons;
			Global.display.writeAtPosition(0, 0, title);
			
			if (line1Text != null)
			{
				Global.display.writeAtPosition(1, 1, line1Text);
				Global.display.writeAtPositionBlink(1, 18, line1Value.toString());
				Global.waitSeconds(5);
			}
			if (line2Text != null)
			{
				Global.display.writeAtPosition(1, 2, line2Text);
			}
			if (line3Text != null)
			{
				Global.display.writeAtPosition(1, 2, line3Text);
			}
			
			buttons.read();
			
			while (!buttons.button0)
			{
				Global.waitSeconds(1);
				buttons.read();
			}
		}
	}
}
 