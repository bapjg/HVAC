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
    			

				// Need to change this
				// Just create a taskItem. No need to put it on taskList as it wont be repeated
				// Once performed, sequencer will set taskActive to null which will delete the object

				System.out.println("HW requested");
				
				if (Global.circuitHotWater.taskActive == null)
				{
	            	userControl											= new UserControl("Hot Water");
	            	userControl.line1Text								= "Temperature";
	            	userControl.line1Value								= 35;
	            	userControl.display();
	            	
					Long	now											= Global.getTimeNowSinceMidnight();
					
					Global.circuitHotWater.taskActive					= new CircuitTask(	now, 								// Time Start
																							now + 30 * 60 * 1000, 				// TimeEnd
																							userControl.line1Value * 10,		// TempObjective
																							true,								// StopOnObjective
																							"1, 2, 3, 4, 5, 6, 7");				// Days
					Global.circuitHotWater.start();
				}
				else
				{
					Global.circuitHotWater.stop();
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
           if (buttons.button5)
           {
                // Start HotWater
            	
    			buttons.read(); // In case of button bounce
    			
				System.out.println("Menu requested");
				UserMenu userMenu										= new UserMenu("Menu");
				userMenu.addLine("Line 1", "");
				userMenu.addLine("Line 2", "");
				userMenu.addLine("Line 3", "");
				userMenu.addLine("Line 4", "");
				userMenu.addLine("Line 5", "");
				userMenu.addLine("Line 6", "");
				Integer result											= userMenu.display();
				if (result == -1)
				{
					LogIt.display("Menu", "menu", "canceled");
				}
				else
				{
					LogIt.display("Menu", "menu", "ietm selected : " + result);
				}
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
			lineActive													= 1;
		}
		public void display()
		{
			Buttons             buttons                                 = Global.buttons;
			Global.display.writeAtPosition(0, 0, title);
			
			if (line1Text != null)
			{
				Global.display.writeAtPosition(1, 1, line1Text);
				Global.display.writeAtPosition(1, 17, line1Value.toString());
				Global.display.blinkAtPosition(1, 19);
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
			
			while ((!buttons.button0) && (!buttons.button5))
			{
				if (buttons.button3)
				{
					if (lineActive == 1)
					{
						line1Value										= line1Value - 1;
						Global.display.writeAtPosition(1, 17, line1Value.toString());
						Global.display.blinkAtPosition(1, 19);
					}
				}
				if (buttons.button4)
				{
					if (lineActive == 1)
					{
						line1Value										= line1Value + 1;
						Global.display.writeAtPosition(1, 17, line1Value.toString());
						Global.display.blinkAtPosition(1, 19);
					}
					
				}
				Global.waitMilliSeconds(200);
				buttons.read();
			}
			if (buttons.button5)
			{
				LogIt.display("UserControl", "display", "Ok pressed");
			}
			Global.display.blinkOff();
		}
	}
	private class UserMenu
	{
		String 		title;
		String[]	text;
		String[]	value;
		Integer		lineCount;
		Integer		lineActive;
		Integer		page;
		public UserMenu (String title)
		{
			this.title													= title;
			Global.display.clear();
			Global.display.writeAtPosition(0, 0, title);
			this.lineActive												= 0;
			this.lineCount												= 0;
			this.page													= 0;
			text 														= new String[20];
			value 														= new String[20];
		}
		public Integer addLine (String text, String value)
		{
			this.text[lineCount]										= "xxx"; // text;
			this.value[lineCount]										= value;
			lineCount++;
			return lineCount - 1;
		}
		public Integer display()
		{
			Buttons             buttons                                 = Global.buttons;
			Boolean				buttonCancel							= buttons.button0;
			Boolean				buttonY									= buttons.button1;
			Boolean				buttonX									= buttons.button2;
			Boolean				buttonDown								= buttons.button3;
			Boolean				buttonUp								= buttons.button4;
			Boolean				buttonOk								= buttons.button5;
			
			Integer				i										= 0;
			show();
			Global.display.blinkAtPosition(1, 19);
			
			buttons.read();
			
			while ((!buttonCancel) && (!buttonOk))
			{
				LogIt.display("UI", "disp", "readloop");
				if (buttons.button3) // (buttonDown)
				{
					LogIt.display("UI", "disp", "down");
					if (lineActive < lineCount)
					{
						lineActive++;
					}
					if (lineActive > page * 3)
					{
						page++;
						show();
					}
					showActiveLine();
				}
				if (buttonUp)
				{
					LogIt.display("UI", "disp", "up");
					if (lineActive > 1)
					{
						lineActive--;
						if (lineActive < page * 3)
						{
							page--;
							show();
						}
					}
					showActiveLine();
				}
				Global.waitMilliSeconds(2000);
				buttons.read();
			}
			
			Global.display.blinkOff();
			if (buttonCancel)
			{
				i = -1;
			}
			return i;
		}
		public void show ()
		{
			Integer	i;
			Integer pageLineTop											= page * 3;
			
			Global.display.clear();
			Global.display.writeAtPosition(0, 0, title);
			
			for (i = 0; (i < 3); i++)
			{
				Global.display.writeAtPosition(i + 1, 1,   text[pageLineTop + i]);
				Global.display.writeAtPosition(i + 1, 17, value[pageLineTop + i]);
			}
		}
		public void showActiveLine()
		{
			Integer topLine = page * 3;
			Integer lineFromTop = lineActive - topLine;
			
			Global.display.blinkAtPosition(lineFromTop + 1, 19);
		}
	}
}
 