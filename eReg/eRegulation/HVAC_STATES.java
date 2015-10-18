package eRegulation;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class HVAC_STATES
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
/**
* CIRCUIT.STATE describes the state of the circuit
*/
	public enum Circuit
	{
		Off,				// Inactive circuit. taskActive should be null
		Start_Requested,	// start has been requested. Just logs the fact and sets state to starting
		Starting,			// Setup up heatRequired and sets state to AwaitingHeat		
		AwaitingHeat,		// AwaitingHeat is where the boilerTemp is lower than the requiredTemp to pump In (rather than out)
		RampingUp,			// Floor : FloorOut is at max temp to shorten rampUp Time. When close to target normal tempControl used.
		Running,			// Kepps on running until some sort of event occurs

		Suspended,			// Hot_Water : if not stop on objective, suspends all activity but surveys hwTemp
		Resuming,			// Hot_Water : hwTemp is below minimum, so reactivates heatRequired

		Optimising,			// Unclear : is this an inTask initiative or a Background task initiative
		Stop_Requested,		// Sets state to Stopping 
		Stopping,			// Switches off the circuitPump and calls shutDown (sets heatRequired to null; and sets state to Off)

		Error				// Some sort of error has occured
								
//		Idle,
	}
}
