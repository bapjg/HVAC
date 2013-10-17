package eRegulation;

public class CircuitTask
{
	public Long					timeStart;
	public Long					timeEnd;
	public String				timeStartDisplay;
	public String				timeEndDisplay;
	public Integer 				tempObjective;
	public Boolean 				stopOnObjective;
	public String				days;

	public Boolean				active;
	public Integer				state;
	public Long					dateLastRun;
	
	public static final int 	TASK_STATE_WaitingToStart		= 0;
	public static final int 	TASK_STATE_WillBeNext			= 1;
	public static final int 	TASK_STATE_Started 				= 2;
	public static final int 	TASK_STATE_Optimising 			= 3;
	public static final int 	TASK_STATE_Completed			= 4;
	public static final int 	TASK_STATE_Error	 			= -1;
	public static final int 	TASK_STATE_NotToday 			= -2;

	public static final int 	TASK_STATE_RampUp   			= -3;
	public static final int 	TASK_STATE_RampDown   			= -4;

	public CircuitTask
		(
			String 				timeStart, 
			String 				timeEnd,  
			String 				tempObjective, 
			String				stopOnObjective,
			String				days
		)
	{
		this.state												= TASK_STATE_WaitingToStart;
		this.active												= false;
		this.timeStart											= Global.parseTime(timeStart);
		this.timeStartDisplay									= timeStart;
		this.timeEnd											= Global.parseTime(timeEnd);
		this.timeEndDisplay										= timeEnd;
		this.tempObjective										= Integer.parseInt(tempObjective);
		this.days												= days;
		this.dateLastRun										= 0L;
		
		if (stopOnObjective.equalsIgnoreCase("1"))
		{
			this.stopOnObjective								= true;
		}
		else
		{
			this.stopOnObjective								= false;			
		}
	}
	public CircuitTask												// Used to create dynamically (ie not on calendars) an immediatetask
		(
			Long 				timeStart, 
			Long 				timeEnd,  
			Integer				tempObjective, 
			Boolean				stopOnObjective,
			String				days
		)
	{
		this.state												= TASK_STATE_WaitingToStart;
		this.timeStart											= timeStart;
		this.timeStartDisplay									= "Now";
		this.timeEnd											= timeEnd;
		this.timeEndDisplay										= "A While";
		this.tempObjective										= tempObjective;
		this.stopOnObjective									= stopOnObjective;
		this.days												= days;
		this.dateLastRun										= 0L;
	}
}
