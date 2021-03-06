package eRegulation;

import java.text.SimpleDateFormat;

import HVAC_Common.Ctrl_Calendars;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class CircuitTask
{
	public Long													timeStart;
	public Long													timeEnd;
	public String												timeStartDisplay;
	public String												timeEndDisplay;
	public Integer 												tempObjective;
	public Boolean 												stopOnObjective;
	public String												days;
								
	public Long													dateLastRun;
	public HVAC_TYPES.CircuitTask								taskType;
	
	public CircuitTask
		(
			Ctrl_Calendars.Calendar 							paramCalendar
		)
	{
		this.timeStart																		= paramCalendar.timeStart.milliSeconds;
		this.timeStartDisplay																= paramCalendar.timeStart.displayShort();
		this.tempObjective																	= paramCalendar.tempObjective.milliDegrees;
		this.days																			= paramCalendar.days;
		this.dateLastRun																	= 0L;
		this.stopOnObjective																= (paramCalendar.stopOnObjective);
		this.timeEnd																		= paramCalendar.timeEnd.milliSeconds;
		this.timeEndDisplay																	= paramCalendar.timeEnd.displayShort();
		this.taskType																		= HVAC_TYPES.CircuitTask.Calendar;
	}
	public CircuitTask												// Used to create dynamically (ie not on calendars) an immediatetask
		(
			Long 												timeStart, 
			Long 												timeEnd,  
			Integer												tempObjective, 
			Boolean												stopOnObjective,
			HVAC_TYPES.CircuitTask								taskType
		)
	{
		this.timeStart																		= timeStart;
		this.timeStartDisplay																= Global.Time.display(timeStart);
		this.timeEnd																		= timeEnd;
		this.timeEndDisplay																	= Global.Time.display(timeEnd);
		this.tempObjective																	= tempObjective;
		this.stopOnObjective																= stopOnObjective;
		this.days																			= "12345, 67";
		this.dateLastRun																	= 0L;
		this.taskType																		= taskType;
	}
}
