package eRegulation;

public class CIRCUIT
{
	public enum STATE
	{
		Off,
		Idle,
		Starting,
		RampingUp,
		Running,
		Stopping,
		Optimising,
		Error,
								
		Suspended,
		Resuming,
		AwaitingHeat,
								
		Start_Requested,
		Stop_Requested,
		
		NothingToDo
	}
	public enum TYPE
	{
		HotWater,
		Gradient,
		Mixer
	}	
}