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
           if (buttons.buttonOk)
           {
                // Start Menu
            	
    			buttons.read(); // In case of button bounce
    			
				UserMenu menuLevel_0										= new UserMenu("Menu");
				UserMenu menuLevel_1;
				UserMenu menuLevel_2;
				
				Integer result_0;
				Integer result_1;
				Integer result_2;
				
				menuLevel_0.addLine("Circuits", "");
				menuLevel_0.addLine("Pumps",    "");
				menuLevel_0.addLine("Mixer",    "");
				menuLevel_0.addLine("Burner",   "");

				result_0													= menuLevel_0.display();
				
				switch (result_0)
				{
				case -1:
					break;
				case 0:
					menuLevel_1												= new UserMenu("Circuits");
					menuLevel_1.addLine("Hot Water", "");
					menuLevel_1.addLine("Floor",     "");
					menuLevel_1.addLine("Radiator",  "");

					result_1												= menuLevel_1.display();
					
					break;
				case 1:
					menuLevel_1												= new UserMenu("Pumps");
					menuLevel_1.addLine("Hot Water", "");
					menuLevel_1.addLine("Floor",     "");
					menuLevel_1.addLine("Radiator",  "");
					
					result_1												= menuLevel_1.display();
					
					break;
				case 2:
					menuLevel_1												= new UserMenu("Mixer");
					menuLevel_1.addLine("Up",       "");
					menuLevel_1.addLine("Down",     "");
					menuLevel_1.addLine("Off",      "");
					
					result_1												= menuLevel_1.display();
					
					break;
				case 3:
					menuLevel_1												= new UserMenu("Burner");
					menuLevel_1.addLine("On",       "");
					menuLevel_1.addLine("Off",      "");
					
					result_1												= menuLevel_1.display();
					
					break;
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
		Global.display.writeAtPosition(2, 0,  Global.thermoBoiler.toDisplay() + "  "); // add 2 extra chars to Blank out possible remenicence of previous info
		Global.display.writeAtPosition(2, 5,  Global.thermoBoilerOut.toDisplay() + "  ");
		Global.display.writeAtPosition(2, 11, Global.thermoFloorOut.toDisplay() + "  ");
		Global.display.writeAtPosition(2, 16, Global.thermoFloorIn.toDisplay());
		Global.display.writeAtPosition(3, 0,  "H2O  ");
		Global.display.writeAtPosition(3, 5,  Global.thermoHotWater.toDisplay() + "  ");

		Global.display.writeAtPosition(3, 11, "LivR     ");
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
	private class MenuItem <DataType>
	{
		public static final int			DATA_TYPE_Menu 					= 0;
		public static final int			DATA_TYPE_String				= 1;
		public static final int			DATA_TYPE_Integer				= 2;
		public static final int			DATA_TYPE_Time					= 3;
		public static final int			DATA_TYPE_Boolean				= 4;

		String							text;
		DataType						value;
		int								type;
		
		public MenuItem (String text, DataType value,  int type)
		{
			this.text													= text;
			this.value													= value;
			this.type													= type;
		}
		public String getValue()
		{
			String response = "";
			
			if (this.value instanceof Integer)
			{
				response = ((Integer) this.value).toString();
			}
			else if (this.value instanceof Boolean)
			{
				if ((Boolean) this.value)
				{
					response = " On";
				}
				else
				{
					response = "Off";
				}
			}
			else if (this.value instanceof String)
			{
				response = (String) this.value;
			}
//			else if (this.value instanceof Menu)
//			{
//				response = "";
//			}
			return response;
		}
		public void incrementValue()
		{
			if (this.value instanceof Integer)
			{

				Integer x = ((Integer) this.value) + 1;
				this.value =  (DataType) x;
			}
			else if (this.value instanceof Boolean)
			{
				Boolean y = !((Boolean) this.value);
				this.value =  (DataType) y;
			}
		}
		public void decrementValue()
		{
			if (this.value instanceof Integer)
			{

				Integer x = ((Integer) this.value) - 1;
				this.value =  (DataType) x;
			}
			else if (this.value instanceof Boolean)
			{
				Boolean y = !((Boolean) this.value);
				this.value =  (DataType) y;
			}
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
			this.text[lineCount]										= text;
			this.value[lineCount]										= value;
			lineCount++;
			return lineCount - 1;
		}
		public Integer display()
		{
			Buttons             buttons                                 = Global.buttons;
			
			Integer				i										= 0;
			show();
			Global.display.blinkAtPosition(1, 19);
			
			buttons.read();
			
			while ((!buttons.buttonCancel) && (!buttons.buttonOk))
			{
				if (buttons.buttonDown)
				{
					if (lineActive < lineCount)
					{
						lineActive++;
					}
					if (lineActive > page * 3 + 2)		// Last line of current page
					{
						page++;
						show();
					}
					showActiveLine();
				}
				if (buttons.buttonUp)
				{
					if (lineActive > 0)
					{
						lineActive--;
						if (lineActive < page * 3)		// First line of current page
						{
							page--;
							show();
						}
					}
					showActiveLine();
				}
				Global.waitMilliSeconds(200);
				buttons.read();
			}
			
			Global.display.blinkOff();
			if (buttons.buttonCancel)
			{
				return -1;
			}
			return lineActive;
		}
		public void show ()
		{
			Integer	i;
			Integer pageLineTop											= page * 3;
			
			Global.display.clear();
			Global.display.writeAtPosition(0, 0, title);
			
			for (i = 0; (i < 3); i++)
			{
				if (pageLineTop + i < lineCount)
				{
					Global.display.writeAtPosition(i + 1, 1,   text[pageLineTop + i]);
					Global.display.writeAtPosition(i + 1, 17, value[pageLineTop + i]);
				}
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
 