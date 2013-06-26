package eRegulation;

public class CircuitTask
{
	public Long				timeStart;
	public Long				timeEnd;
	public Integer 			tempObjective;
	public Boolean 			stopOnObjective;
	public Boolean			temporary;

	public Integer			state;
	
	public final int 		STATE_WaitingToStart	= 0;
	public final int 		STATE_Started 			= 1;
	public final int 		STATE_Completed			= 2;
	public final int 		STATE_Error	 			= -1;

	
	public CircuitTask
		(
		String 			timeStart, 
		String 			timeEnd,  
		String 			tempObjective, 
		String			stopOnObjective,
		Boolean			temporary
		)
	{
		this.state							= STATE_WaitingToStart;
		this.timeStart						= Global.parseTime(timeStart);
		this.timeEnd						= Global.parseTime(timeEnd);

		this.tempObjective					= Integer.parseInt(tempObjective);
		
		if (stopOnObjective.equalsIgnoreCase("1"))
		{
			this.stopOnObjective			= true;
		}
		else
		{
			this.stopOnObjective			= false;			
		}
		
		this.temporary 						= temporary;
	}
	public CircuitTask
		(
		Long 			timeStart, 
		Long 			timeEnd,  
		Integer			tempObjective, 
		Boolean			stopOnObjective,
		Boolean			temporary
		)
	{
		this.state							= STATE_WaitingToStart;
		this.timeStart						= timeStart;
		this.timeEnd						= timeEnd;
	
		this.tempObjective					= tempObjective;
		this.stopOnObjective				= stopOnObjective;
		
		this.temporary 						= temporary;
	}
}
