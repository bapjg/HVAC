package eRegulation;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class STATES
{
	public enum BoilerTemperatureVariation
	{
		NormalOperating, 
		MaxReached, 
		MinReached
	}
	public enum Boiler
	{
		Off,
		On_Heating,
		On_Cooling,
		On_CoolingAfterOverheat,
		PowerUp,
		Error
	}
	// IS THIS REQUIRED
//	public enum Mixer
//	{
//		Off,
//		Normal_Operating,
//		Moving_Up,
//		Moving_Down,
//		Moving_Waint,
//		Moving_OverTemp_Recovery,
//		Moving_Idle
//	}
	/**
	* CIRCUIT.STATE describes the state of the circuit
	*/
	public enum Circuit
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
}
