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

public class Ctrl_Calendars 					extends 					Ctrl_Abstract 
{
	public Long dateTime;
	private static final long 					serialVersionUID 			= 1L;
	
	public Ctrl_Calendars()
	{
	}
	public static class Data					extends 					Ctrl_Calendars
	{
		private static final long 				serialVersionUID 			= 1L;
		public ArrayList<Word> 					wordList 					= new ArrayList<Word>();
		public ArrayList<Circuit>				circuitList 				= new ArrayList<Circuit>();
		public ArrayList<Circuit>				awayList 					= new ArrayList<Circuit>();
	}
	public class Update							extends 					Ctrl_Calendars.Data
	{
		private static final long 				serialVersionUID 			= 1L;
	}
	public static class Request					extends 					Ctrl_Calendars
	{
		private static final long 				serialVersionUID 			= 1L;
	}
	public static class Word					extends 					Ctrl_Calendars
	{
		private static final long 				serialVersionUID 			= 1L;
		public String 							name;
		public String							days;
	}
	public static class Circuit					extends 					Ctrl_Calendars
	{
		private static final long 				serialVersionUID 			= 1L;
		public String 							name;
		public ArrayList<Calendar>				calendarList 				= new ArrayList<Calendar>();
	}
	public static class Calendar				extends 					Ctrl_Calendars
	{
		private static final long 				serialVersionUID 			= 1L;
		public String 							name;
		public String							days;
		public String							timeStart;
		public Integer							tempObjective;
		public Stop_Criterion					stopCriterion;
		public String							active;
	}
	public static class Stop_Criterion			extends 					Ctrl_Calendars
	{
		private static final long 				serialVersionUID 			= 1L;
		public  static final Integer			STOP_REASON_OnObjective		= 0;
		public  static final Integer			STOP_REASON_OnEndTime		= 1;
		public  static final Integer			STOP_REASON_OnDuration		= 2;
		public Integer 							stopReason;
		public String							timeEnd;
		public String							timeDuration;
	}
	
	
	public void initialise()
	{
		Update									calendarUpdate				= (Update) this;
		calendarUpdate.dateTime												= System.currentTimeMillis();
		
		// PIDs
		Ctrl_Calendars.Word						word						= new Word();
		word.name															= "EveryDay";
		word.days															= "1234567";
		calendarUpdate.wordList.add(word);

		word																= new Word();
		word.name															= "TestDay";
		word.days															= "";
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
		
		Ctrl_Calendars.Circuit						circuit					= new Circuit();
		circuit.name														= "Hot_Water";
		calendarUpdate.circuitList.add(circuit);

		Ctrl_Calendars.Calendar						calendar				= new Calendar();
		calendar.name														= "EveryDay";
		calendar.days														= "WeekDay";
		calendar.timeStart													= "06:15";
		calendar.stopCriterion												= new Stop_Criterion();
		calendar.stopCriterion.stopReason									= Stop_Criterion.STOP_REASON_OnObjective;
		calendar.stopCriterion.timeEnd										= "07:00";
		calendar.stopCriterion.timeDuration									= "00:45";
		calendar.tempObjective												= 40000;
		calendar.active														= "1";
		circuit.calendarList.add(calendar);
		
		calendar															= new Calendar();
		calendar.name														= "EveryDay";
		calendar.days														= "EveryDay";
		calendar.timeStart													= "21:30";
		calendar.stopCriterion												= new Stop_Criterion();
		calendar.stopCriterion.stopReason									= Stop_Criterion.STOP_REASON_OnObjective;
		calendar.stopCriterion.timeEnd										= "22:00";
		calendar.stopCriterion.timeDuration									= "00:45";
		calendar.tempObjective												= 40000;
		calendar.active														= "1";
		circuit.calendarList.add(calendar);

		calendar															= new Calendar();
		calendar.name														= "EveryDay";
		calendar.days														= "WeekEnd";
		calendar.timeStart													= "07:30";
		calendar.stopCriterion												= new Stop_Criterion();
		calendar.stopCriterion.stopReason									= Stop_Criterion.STOP_REASON_OnObjective;
		calendar.stopCriterion.timeEnd										= "08:15";
		calendar.stopCriterion.timeDuration									= "00:45";
		calendar.tempObjective												= 40000;
		calendar.active														= "1";
		circuit.calendarList.add(calendar);

		calendar															= new Calendar();
		calendar.name														= "EveryDay";
		calendar.days														= "WeekEnd";
		calendar.timeStart													= "17:30";
		calendar.stopCriterion												= new Stop_Criterion();
		calendar.stopCriterion.stopReason									= Stop_Criterion.STOP_REASON_OnObjective;
		calendar.stopCriterion.timeEnd										= "19:00";
		calendar.stopCriterion.timeDuration									= "00:45";
		calendar.tempObjective												= 40000;
		calendar.active														= "1";
		circuit.calendarList.add(calendar);

		calendar															= new Calendar();
		calendar.name														= "Test";
		calendar.days														= "EveryDay";
		calendar.timeStart													= "18:00";
		calendar.stopCriterion												= new Stop_Criterion();
		calendar.stopCriterion.stopReason									= Stop_Criterion.STOP_REASON_OnObjective;
		calendar.stopCriterion.timeEnd										= "18:45";
		calendar.stopCriterion.timeDuration									= "00:45";
		calendar.tempObjective												= 40000;
		calendar.active														= "1";
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
		calendar.name														= "WeekDay";
		calendar.days														= "WeekDay";
		calendar.timeStart													= "07:00";
		calendar.stopCriterion												= new Stop_Criterion();
		calendar.stopCriterion.stopReason									= Stop_Criterion.STOP_REASON_OnEndTime;
		calendar.stopCriterion.timeEnd										= "08:00";
		calendar.stopCriterion.timeDuration									= null;
		calendar.tempObjective												= 50000;
		calendar.active														= "1";
		circuit.calendarList.add(calendar);

		calendar															= new Calendar();
		calendar.name														= "WeekEnd";
		calendar.days														= "WeekEnd";
		calendar.timeStart													= "08:00";
		calendar.stopCriterion												= new Stop_Criterion();
		calendar.stopCriterion.stopReason									= Stop_Criterion.STOP_REASON_OnEndTime;
		calendar.stopCriterion.timeEnd										= "09:00";
		calendar.stopCriterion.timeDuration									= null;
		calendar.tempObjective												= 50000;
		calendar.active														= "1";
		circuit.calendarList.add(calendar);

		calendar															= new Calendar();
		calendar.name														= "EveryDay";
		calendar.days														= "EveryDay";
		calendar.timeStart													= "21:30";
		calendar.stopCriterion												= new Stop_Criterion();
		calendar.stopCriterion.stopReason									= Stop_Criterion.STOP_REASON_OnEndTime;
		calendar.stopCriterion.timeEnd										= "22:00";
		calendar.stopCriterion.timeDuration									= null;
		calendar.tempObjective												= 50000;
		calendar.active														= "1";
		circuit.calendarList.add(calendar);

		calendar															= new Calendar();
		calendar.name														= "Locataire";
		calendar.days														= "Locataire"	;
		calendar.timeStart													= "17:00";
		calendar.stopCriterion												= new Stop_Criterion();
		calendar.stopCriterion.stopReason									= Stop_Criterion.STOP_REASON_OnEndTime;
		calendar.stopCriterion.timeEnd										= "22:00";
		calendar.stopCriterion.timeDuration									= null;
		calendar.tempObjective												= 50000;
		calendar.active														= "1";
		circuit.calendarList.add(calendar);

		calendar															= new Calendar();
		calendar.name														= "Locataire";
		calendar.days														= "HalfDay";
		calendar.timeStart													= "14:00";
		calendar.stopCriterion												= new Stop_Criterion();
		calendar.stopCriterion.stopReason									= Stop_Criterion.STOP_REASON_OnEndTime;
		calendar.stopCriterion.timeEnd										= "16:30";
		calendar.stopCriterion.timeDuration									= null;
		calendar.tempObjective												= 50000;
		calendar.active														= "1";
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
		calendar.name														= "WeekDay";
		calendar.days														= "TestDay";
		calendar.timeStart													= "00:01";
		calendar.stopCriterion												= new Stop_Criterion();
		calendar.stopCriterion.stopReason									= Stop_Criterion.STOP_REASON_OnEndTime;
		calendar.stopCriterion.timeEnd										= "23:59";
		calendar.stopCriterion.timeDuration									= null;
		calendar.tempObjective												= 20000;
		calendar.active														= "1";
		circuit.calendarList.add(calendar);

		calendar															= new Calendar();
		calendar.name														= "WeekDay";
		calendar.days														= "WeekDay";
		calendar.timeStart													= "05:00";
		calendar.stopCriterion												= new Stop_Criterion();
		calendar.stopCriterion.stopReason									= Stop_Criterion.STOP_REASON_OnEndTime;
		calendar.stopCriterion.timeEnd										= "07:30";
		calendar.stopCriterion.timeDuration									= null;
		calendar.tempObjective												= 20000;
		calendar.active														= "1";
		circuit.calendarList.add(calendar);

		calendar															= new Calendar();
		calendar.name														= "WeekDay";
		calendar.days														= "ZeekDay";
		calendar.timeStart													= "15:00";
		calendar.stopCriterion												= new Stop_Criterion();
		calendar.stopCriterion.stopReason									= Stop_Criterion.STOP_REASON_OnEndTime;
		calendar.stopCriterion.timeEnd										= "23:30";
		calendar.stopCriterion.timeDuration									= null;
		calendar.tempObjective												= 20000;
		calendar.active														= "1";
		circuit.calendarList.add(calendar);

		calendar															= new Calendar();
		calendar.name														= "WeekEnd";
		calendar.days														= "WeekEnd";
		calendar.timeStart													= "06:30";
		calendar.stopCriterion												= new Stop_Criterion();
		calendar.stopCriterion.stopReason									= Stop_Criterion.STOP_REASON_OnEndTime;
		calendar.stopCriterion.timeEnd										= "21:30";
		calendar.stopCriterion.timeDuration									= null;
		calendar.tempObjective												= 20000;
		calendar.active														= "1";
		circuit.calendarList.add(calendar);

		calendar															= new Calendar();
		calendar.name														= "Locataire";
		calendar.days														= "HalfDay";
		calendar.timeStart													= "12:45";
		calendar.stopCriterion												= new Stop_Criterion();
		calendar.stopCriterion.stopReason									= Stop_Criterion.STOP_REASON_OnEndTime;
		calendar.stopCriterion.timeEnd										= "14:45";
		calendar.stopCriterion.timeDuration									= null;
		calendar.tempObjective												= 20000;
		calendar.active														= "1";
		circuit.calendarList.add(calendar);
		
		//
		//=========================================================================================

	}
}
