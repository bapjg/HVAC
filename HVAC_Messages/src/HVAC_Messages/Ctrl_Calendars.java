package HVAC_Messages;

import java.util.ArrayList;

import HVAC_Messages.Ctrl_Configuration.Boiler;
import HVAC_Messages.Ctrl_Configuration.Burner;
import HVAC_Messages.Ctrl_Configuration.Circuit;
import HVAC_Messages.Ctrl_Configuration.PID_Data;
import HVAC_Messages.Ctrl_Configuration.Pump;
import HVAC_Messages.Ctrl_Configuration.Relay;
import HVAC_Messages.Ctrl_Configuration.Thermometer;

public class Ctrl_Calendars 					extends 					Ctrl_Abstract 
{
	public Long dateTime;
	private static final long 					serialVersionUID 			= 1L;

	public String w1 = "How to deal with away";
	public String w2 = "is it inside circuits or outside (favour)";
	public String w3 = "look at away list";
	public String w4 = "anti freeze should be percircuit in eReg";
	
	
	
	
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
	public static class Word					extends 					Ctrl_Calendars
	{
		private static final long 				serialVersionUID 			= 1L;
		public String 							word;
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
		public String							timeEnd;
		public String							tempObjective;
		public String							stopOnObjective;
		public String							active;
	}
	
	
	public void initialise()
	{
		Ctrl_Calendars.Data						calendarUpdate				= new Ctrl_Calendars.Data();
		
		// PIDs
		Ctrl_Calendars.Word						word						= new Word();
		word.word															= "EveryDay";
		word.days															= "1234567";
		calendarUpdate.wordList.add(word);

		word																= new Word();
		word.word															= "TestDay";
		word.days															= "";
		calendarUpdate.wordList.add(word);

		word																= new Word();
		word.word															= "HalfDay";
		word.days															= "4";
		calendarUpdate.wordList.add(word);

		word																= new Word();
		word.word															= "Locataire";
		word.days															= "";
		calendarUpdate.wordList.add(word);

		word																= new Word();
		word.word															= "WeekEnd";
		word.days															= "67";
		calendarUpdate.wordList.add(word);

		word																= new Word();
		word.word															= "WeekDay";
		word.days															= "12345";
		calendarUpdate.wordList.add(word);

		Ctrl_Calendars.Circuit						circuit					= new Circuit();
		circuit.name														= "Hot_Water";
		calendarUpdate.circuitList.add(circuit);

		Ctrl_Calendars.Calendar						calendar				= new Calendar();
		calendar.name														="EveryDay";
		calendar.days														="WeekDay";
		calendar.timeStart													="06:15";
		calendar.timeEnd													="06:45";
		calendar.tempObjective												="35000";
		calendar.stopOnObjective											="1";
		calendar.active														="1";
		circuit.calendarList.add(calendar);
		
		calendar															= new Calendar();
		calendar.name														="EveryDay";
		calendar.days														="EveryDay";
		calendar.timeStart													="21:30";
		calendar.timeEnd													="22:00";
		calendar.tempObjective												="35000";
		calendar.stopOnObjective											="1";
		calendar.active														="1";
		circuit.calendarList.add(calendar);


		calendar															= new Calendar();
		calendar.name														="EveryDay";
		calendar.days														="WeekEnd";
		calendar.timeStart													="07:30";
		calendar.timeEnd													="08:30";
		calendar.tempObjective												="35000";
		calendar.stopOnObjective											="1";
		calendar.active														="1";
		circuit.calendarList.add(calendar);


		calendar															= new Calendar();
		calendar.name														="EveryDay";
		calendar.days														="WeekEnd";
		calendar.timeStart													="17:30";
		calendar.timeEnd													="18:30";
		calendar.tempObjective												="45000";
		calendar.stopOnObjective											="1";
		calendar.active														="1";
		circuit.calendarList.add(calendar);


		calendar															= new Calendar();
		calendar.name														="Test";
		calendar.days														="EveryDay";
		calendar.timeStart													="18:00";
		calendar.timeEnd													="18:30";
		calendar.tempObjective												="40000";
		calendar.stopOnObjective											="1";
		calendar.active														="1";
		circuit.calendarList.add(calendar);
		
		circuit																= new Circuit();
		circuit.name														= "Radiator";
		calendarUpdate.circuitList.add(circuit);

		calendar															= new Calendar();
		calendar.name														= "WeekDay";
		calendar.days														= "WeekDay";
		calendar.timeStart													= "07:00";
		calendar.timeEnd													= "08:00";
		calendar.tempObjective												= "50000";
		calendar.stopOnObjective											= "0";
		calendar.active														="1";
		circuit.calendarList.add(calendar);


		calendar															= new Calendar();
		calendar.name														= "WeekEnd";
		calendar.days														= "WeekEnd";
		calendar.timeStart													= "08:00";
		calendar.timeEnd													= "09:00";
		calendar.tempObjective												= "50000";
		calendar.stopOnObjective											= "0";
		calendar.active														="1";
		circuit.calendarList.add(calendar);


		calendar															= new Calendar();
		calendar.name														= "EveryDay";
		calendar.days														= "EveryDay";
		calendar.timeStart													= "21:30";
		calendar.timeEnd													= "22:00";
		calendar.tempObjective												= "50000";
		calendar.stopOnObjective											= "0";
		calendar.active														="1";
		circuit.calendarList.add(calendar);


		calendar															= new Calendar();
		calendar.name														= "Locataire";
		calendar.days														= "Locataire"	;
		calendar.timeStart													= "17:00";
		calendar.timeEnd													= "22:00";
		calendar.tempObjective												= "50000";
		calendar.stopOnObjective											= "0";
		calendar.active														="1";
		circuit.calendarList.add(calendar);


		calendar															= new Calendar();
		calendar.name														= "Locataire";
		calendar.days														= "HalfDay";
		calendar.timeStart													= "14:00";
		calendar.timeEnd													= "16:30";
		calendar.tempObjective												= "50000";
		calendar.stopOnObjective											= "0";
		calendar.active														="1";
		circuit.calendarList.add(calendar);
		
		
		
		
		
		circuit																= new Circuit();
		circuit.name														= "Floor";
		calendarUpdate.circuitList.add(circuit);

		calendar															= new Calendar();
		calendar.name														= "WeekDay";
		calendar.days														= "TestDay";
		calendar.timeStart													= "00:01";
		calendar.timeEnd													= "23:59";
		calendar.tempObjective												= "19000";
		calendar.stopOnObjective											= "0";
		calendar.active														="1";
		circuit.calendarList.add(calendar);



		calendar															= new Calendar();
		calendar.name														= "WeekDay";
		calendar.days														= "WeekDay";
		calendar.timeStart													= "05:00";
		calendar.timeEnd													= "07:30";
		calendar.tempObjective												= "19000";
		calendar.stopOnObjective											= "0";
		calendar.active														="1";
		circuit.calendarList.add(calendar);


		calendar															= new Calendar();
		calendar.name														= "WeekDay";
		calendar.days														= "ZeekDay";
		calendar.timeStart													= "15:00";
		calendar.timeEnd													= "23:30";
		calendar.tempObjective												= "19000";
		calendar.stopOnObjective											= "0";
		calendar.active														="1";
		circuit.calendarList.add(calendar);



		calendar															= new Calendar();
		calendar.name														= "WeekEnd";
		calendar.days														= "WeekEnd";
		calendar.timeStart													= "06:30";
		calendar.timeEnd													= "21:30";
		calendar.tempObjective												= "19000";
		calendar.stopOnObjective											= "0";
		calendar.active														="1";
		circuit.calendarList.add(calendar);



		calendar															= new Calendar();
		calendar.name														= "Locataire";
		calendar.days														= "HalfDay";
		calendar.timeStart													= "12:45";
		calendar.timeEnd													= "14:45";
		calendar.tempObjective												= "50000";
		calendar.stopOnObjective											= "0";
		calendar.active														="1";
		circuit.calendarList.add(calendar);

	
	}
}
