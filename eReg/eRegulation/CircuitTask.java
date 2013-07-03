package eRegulation;

public class CircuitTask
{
	public Long				timeStart;
	public Long				timeEnd;
	public Integer 			tempObjective;
	public Boolean 			stopOnObjective;
	public Boolean			temporary;
	public String			days;

	public Integer			state;
	
	public final int 		TASK_STATE_WaitingToStart		= 0;
	public final int 		TASK_STATE_Started 				= 1;
	public final int 		TASK_STATE_RunningFree 			= 1;
	public final int 		TASK_STATE_Completed			= 2;
	public final int 		TASK_STATE_Error	 			= -1;
	public final int 		TASK_STATE_NotToday 			= -2;

	
	public CircuitTask
		(
		String 			timeStart, 
		String 			timeEnd,  
		String 			tempObjective, 
		String			stopOnObjective,
		String			days,
		Boolean			temporary
		)
	{
		this.state											= TASK_STATE_WaitingToStart;
		this.timeStart										= Global.parseTime(timeStart);
		this.timeEnd										= Global.parseTime(timeEnd);

		this.tempObjective									= Integer.parseInt(tempObjective);
		this.days											= days;
		
		if (stopOnObjective.equalsIgnoreCase("1"))
		{
			this.stopOnObjective							= true;
		}
		else
		{
			this.stopOnObjective							= false;			
		}
		
		this.temporary 										= temporary;
	}
	public CircuitTask
		(
		Long 			timeStart, 
		Long 			timeEnd,  
		Integer			tempObjective, 
		Boolean			stopOnObjective,
		String			days,
		Boolean			temporary
		)
	{
		this.state											= TASK_STATE_WaitingToStart;
		this.timeStart										= timeStart;
		this.timeEnd										= timeEnd;
	
		this.tempObjective									= tempObjective;
		this.stopOnObjective								= stopOnObjective;
		
		this.days											= days;
		this.temporary 										= temporary;
	}
}
