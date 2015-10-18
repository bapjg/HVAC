package eRegulation;

import HVAC_Common.Ctrl_Calendars;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class CircuitTask
{
	public Long													timeStart;
	public Long													timeEnd;
	public String												timeStartDisplay;
	public String												timeEndDisplay;
	public Integer 												tempObjective;
	public Boolean 												stopOnObjective;
	public String												days;
								
//	public Integer												state;
	public Long													dateLastRun;
	public HVAC_TYPES.CircuitTask									taskType;
	
//	public static final int 								TASK_STATE_WaitingToStart		= 0;
//	public static final int 								TASK_STATE_WillBeNext			= 1;
//	public static final int 								TASK_STATE_Started 				= 2;
//	public static final int 								TASK_STATE_Optimising 			= 3;
//	public static final int 								TASK_STATE_Completed			= 4;
//	public static final int 								TASK_STATE_Error	 			= -1;
//	public static final int 								TASK_STATE_NotToday 			= -2;
//							
//	public static final int 								TASK_STATE_RampUp   			= -3;
//	public static final int 								TASK_STATE_RampDown   			= -4;

	public CircuitTask(Ctrl_Calendars.Calendar 					paramCalendar)
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
			String												days,
			HVAC_TYPES.CircuitTask									taskType
		)
	{
		this.timeStart																		= timeStart;
		this.timeStartDisplay																= "Now";
		this.timeEnd																		= timeEnd;
		this.timeEndDisplay																	= "A While";
		this.tempObjective																	= tempObjective;
		this.stopOnObjective																= stopOnObjective;
		this.days																			= days;
		this.dateLastRun																	= 0L;
		this.taskType																		= taskType;
	}
}
