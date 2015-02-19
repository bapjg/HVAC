package HVAC_Common;

public class CIRCUIT
{
	public enum STATE
	{
		Off,
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
		Stop_Requested
	}
	public enum TYPE
	{
		HotWater,
		Gradient,
		Mixer
	}	
}