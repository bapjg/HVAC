package HVAC_Messages;

import java.util.ArrayList;

import HVAC_Messages.Ctrl_Configuration.Boiler;
import HVAC_Messages.Ctrl_Configuration.Burner;
import HVAC_Messages.Ctrl_Configuration.Circuit;
import HVAC_Messages.Ctrl_Configuration.PID_Data;
import HVAC_Messages.Ctrl_Configuration.Pump;
import HVAC_Messages.Ctrl_Configuration.Relay;
import HVAC_Messages.Ctrl_Configuration.Thermometer;
import HVAC_Messages.Ctrl_Configuration.Update;

public class Ctrl_Calendars 					extends 					Ctrl__Abstract 
{
	public Long dateTime;
	private static final long 					serialVersionUID 			= 1L;
	
	public Ctrl_Calendars()
	{
	}
	public class Data							extends 					Ctrl_Calendars
	{
		private static final long 				serialVersionUID 			= 1L;
		public ArrayList <Word>					wordList 					= new ArrayList <Word>();
		public ArrayList <Circuit>				circuitList 				= new ArrayList <Circuit>();
		public ArrayList <Away>					awayList 					= new ArrayList <Away>();
		public TasksBackGround					tasksBackGround				= new TasksBackGround();
		
		public Circuit fetchCircuit(String name)
		{
	        for (Circuit 						circuit 					: circuitList)
	        {
	        	if (circuit.name.equalsIgnoreCase(name))
	        	{
	        		return circuit;
	        	}
	        }
	        return null;
		}
	}
	public class Update							extends 					Ctrl_Calendars
	{
		private static final long 				serialVersionUID 			= 1L;
		public ArrayList <Word>					wordList 					= new ArrayList <Word>();
		public ArrayList <Circuit>				circuitList 				= new ArrayList <Circuit>();
		public ArrayList <Away>					awayList 					= new ArrayList <Away>();
		public TasksBackGround					tasksBackGround				= new TasksBackGround();
	}
	public class Request						extends 					Ctrl_Calendars
	{
		private static final long 				serialVersionUID 			= 1L;
	}
	public class Word							extends 					Ctrl_Calendars
												implements					Cloneable
	{
		private static final long 				serialVersionUID 			= 1L;
		public String 							name;
		public String							days;
		
		public Object clone() 
		{
			Object cloneObject 												= null;
			try 
			{
				cloneObject 												= super.clone();
			} 
			catch(CloneNotSupportedException cnse) 
			{
				cnse.printStackTrace(System.err);
			}
			return cloneObject;
		}
	}
	public class Circuit						extends 					Ctrl_Calendars
	{
		private static final long 				serialVersionUID 			= 1L;
		public String 							name;
		public ArrayList<Calendar>				calendarList 				= new ArrayList<Calendar>();
	}
	public class Calendar						extends 					Ctrl_Calendars
	{
		private static final long 				serialVersionUID 			= 1L;
		public String							days;
		public String							timeStart;
		public String							timeEnd;
		public Integer							tempObjective;
		public Boolean							stopOnObjective;
	}
	public class Away							extends 					Ctrl_Calendars
	{
		private static final long 				serialVersionUID 			= 1L;
		public Long 							dateTimeStart;
		public Long 							dateTimeEnd;
	}
	public class TasksBackGround				extends 					Ctrl_Calendars
	{
		private static final long 				serialVersionUID 			= 1L;
		public Long 							pumpCleanTime;
		public Integer							pumpCleanDurationSeconds;
		public Integer							antiFreeze;
	}
	public void initialise()
	{
		Update									calendarUpdate				= (Update) this;
		calendarUpdate.dateTime												= null;			// Server is used as utlimate arbitrator for time
		
		// Vocabulary
		Ctrl_Calendars.Word						word						= new Word();
		word.name															= "EveryDay";
		word.days															= "1234567";
		calendarUpdate.wordList.add(word);

		word																= new Word();
		word.name															= "TestDay";
		word.days															= "1234567";
		calendarUpdate.wordList.add(word);

		word																= new Word();
		word.name															= "HalfDay";
		word.days															= "4";
		calendarUpdate.wordList.add(word);

		word																= new Word();
		word.name															= "Locataire";
		word.days															= "";
		calendarUpdate.wordList.add(word);

		word																= new Word();
		word.name															= "WeekEnd";
		word.days															= "67";
		calendarUpdate.wordList.add(word);

		word																= new Word();
		word.name															= "WeekDay";
		word.days															= "12345";
		calendarUpdate.wordList.add(word);

		//=========================================================================================
		//
		// Circuit : Hot Water
		//
		
		Ctrl_Calendars.Circuit					circuit						= new Circuit();
		circuit.name														= "Hot_Water";
		calendarUpdate.circuitList.add(circuit);

		Ctrl_Calendars.Calendar					calendar					= new Calendar();
		calendar.days														= "WeekDay";
		calendar.timeStart													= "06:15";
		calendar.timeEnd													= "07:00";
		calendar.stopOnObjective											= true;
		calendar.tempObjective												= 40000;
		circuit.calendarList.add(calendar);
		
		calendar															= new Calendar();
		calendar.days														= "EveryDay";
		calendar.timeStart													= "21:30";
		calendar.timeEnd													= "22:00";
		calendar.stopOnObjective											= true;
		calendar.tempObjective												= 40000;
		circuit.calendarList.add(calendar);

		calendar															= new Calendar();
		calendar.days														= "WeekEnd";
		calendar.timeStart													= "07:30";
		calendar.timeEnd													= "08:00";
		calendar.stopOnObjective											= true;
		calendar.tempObjective												= 40000;
		circuit.calendarList.add(calendar);

		calendar															= new Calendar();
		calendar.days														= "WeekEnd";
		calendar.timeStart													= "17:30";
		calendar.timeEnd													= "18:15";
		calendar.stopOnObjective											= true;
		calendar.tempObjective												= 40000;
		circuit.calendarList.add(calendar);

		calendar															= new Calendar();
		calendar.days														= "EveryDay";
		calendar.timeStart													= "22:00";
		calendar.timeEnd													= "22:60";
		calendar.stopOnObjective											= true;
		calendar.tempObjective												= 40000;
		circuit.calendarList.add(calendar);
		
		//
		//=========================================================================================

		//=========================================================================================
		//
		// Circuit : Radiator
		//

		circuit																= new Circuit();
		circuit.name														= "Radiator";
		calendarUpdate.circuitList.add(circuit);

		calendar															= new Calendar();
		calendar.days														= "WeekDay";
		calendar.timeStart													= "07:00";
		calendar.timeEnd													= "08:00";
		calendar.stopOnObjective											= false;
		calendar.tempObjective												= 50000;
		circuit.calendarList.add(calendar);

		calendar															= new Calendar();
		calendar.days														= "WeekEnd";
		calendar.timeStart													= "08:00";
		calendar.timeEnd													= "09:00";
		calendar.stopOnObjective											= false;
		calendar.tempObjective												= 50000;
		circuit.calendarList.add(calendar);

		calendar															= new Calendar();
		calendar.days														= "EveryDay";
		calendar.timeStart													= "21:30";
		calendar.timeEnd													= "22:00";
		calendar.stopOnObjective											= false;
		calendar.tempObjective												= 50000;
		circuit.calendarList.add(calendar);

		calendar															= new Calendar();
		calendar.days														= "Locataire"	;
		calendar.timeStart													= "17:00";
		calendar.timeEnd													= "22:00";
		calendar.stopOnObjective											= false;
		calendar.tempObjective												= 50000;
		circuit.calendarList.add(calendar);

		calendar															= new Calendar();
		calendar.days														= "HalfDay";
		calendar.timeStart													= "14:00";
		calendar.timeEnd													= "22:00";
		calendar.stopOnObjective											= false;
		calendar.tempObjective												= 50000;
		circuit.calendarList.add(calendar);
		
		//
		//=========================================================================================

		//=========================================================================================
		//
		// Circuit : Floor
		//
	
		circuit																= new Circuit();
		circuit.name														= "Floor";
		calendarUpdate.circuitList.add(circuit);

		calendar															= new Calendar();
		calendar.days														= "WeekDay";
		calendar.timeStart													= "05:00";
		calendar.timeEnd													= "07:30";
		calendar.stopOnObjective											= true;
		calendar.tempObjective												= 20000;
		circuit.calendarList.add(calendar);

		calendar															= new Calendar();
		calendar.days														= "WeekDay";
		calendar.timeStart													= "15:00";
		calendar.timeEnd													= "22:00";
		calendar.stopOnObjective											= false;
		calendar.tempObjective												= 20000;
		circuit.calendarList.add(calendar);

		calendar															= new Calendar();
		calendar.days														= "WeekEnd";
		calendar.timeStart													= "06:30";
		calendar.timeEnd													= "21:30";
		calendar.stopOnObjective											= false;
		calendar.tempObjective												= 20000;
		circuit.calendarList.add(calendar);

		calendar															= new Calendar();
		calendar.days														= "HalfDay";
		calendar.timeStart													= "11:45";
		calendar.timeEnd													= "14:45";
		calendar.stopOnObjective											= false;
		calendar.tempObjective												= 20000;
		circuit.calendarList.add(calendar);

		calendar															= new Calendar();
		calendar.days														= "ZZZZDay";
		calendar.timeStart													= "00:01";
		calendar.timeEnd													= "23:50";
		calendar.stopOnObjective											= false;
		calendar.tempObjective												= 20000;
		circuit.calendarList.add(calendar);

		
		//
		//=========================================================================================

		//=========================================================================================
		//
		// Away List
		//

		Away									awayItem					= new Away();
		awayItem.dateTimeStart												= System.currentTimeMillis();
		awayItem.dateTimeEnd												= awayItem.dateTimeStart + 30 * 60 * 1000L; // 30 mins
		calendarUpdate.awayList.add(awayItem);

		awayItem															= new Away();
		awayItem.dateTimeStart												= System.currentTimeMillis() + 60 * 60 * 1000L;
		awayItem.dateTimeEnd												= awayItem.dateTimeStart + 30 * 60 * 1000L; // 30 mins
		calendarUpdate.awayList.add(awayItem);

		//
		//=========================================================================================


		//=========================================================================================
		//
		// AntiFreeze
		//

		calendarUpdate.tasksBackGround.antiFreeze							= 5000;
		calendarUpdate.tasksBackGround.pumpCleanTime						= 60 * 60 * 1000L; // 1 am
		calendarUpdate.tasksBackGround.pumpCleanDurationSeconds				= 300;

		//
		//=========================================================================================


	}
}
