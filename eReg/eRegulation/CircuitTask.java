package eRegulation;

public class CircuitTask
{
	public Long					timeStart;
	public Long					timeEnd;
	public Integer 				tempObjective;
	public Boolean 				stopOnObjective;
	public String				days;

	public Integer				state;
	
	public static final int 	TASK_STATE_WaitingToStart		= 0;
	public static final int 	TASK_STATE_Started 				= 1;
	public static final int 	TASK_STATE_RunningFree 			= 2;
	public static final int 	TASK_STATE_Completed			= 3;
	public static final int 	TASK_STATE_Error	 			= -1;
	public static final int 	TASK_STATE_NotToday 			= -2;

	public static final int 	TASK_STATE_RampUp   			= -2;
	public static final int 	TASK_STATE_RampDown   			= -2;

	
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
		this.timeStart											= Global.parseTime(timeStart);
		this.timeEnd											= Global.parseTime(timeEnd);

		this.tempObjective										= Integer.parseInt(tempObjective);
		this.days												= days;
		
		if (stopOnObjective.equalsIgnoreCase("1"))
		{
			this.stopOnObjective								= true;
		}
		else
		{
			this.stopOnObjective								= false;			
		}
	}
	public CircuitTask
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
		this.timeEnd											= timeEnd;
		this.tempObjective										= tempObjective;
		this.stopOnObjective									= stopOnObjective;
		this.days												= days;
	}
}
