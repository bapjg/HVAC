package HVAC_Common;

public class CIRCUIT
{
    /**
     * CIRCUIT.STATE describes the state of the circuit
     */
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
    /**
     * CIRCUIT.TYPE defines which type of circuit
     */
	public enum TYPE
	{
		HotWater,
		Gradient,
		Mixer
	}	
}