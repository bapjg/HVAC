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
		PowerDown,
		Error
	}
/**
* CIRCUIT.STATE describes the state of the circuit
*/
	public enum Circuit
	{
		Off,				// Inactive circuit. taskActive should be null
		Starting,			// Setup up heatRequired and sets state to AwaitingHeat		
		Resuming,			// Hot_Water : hwTemp is below minimum, so reactivates heatRequired
		RampingUp,			// Floor : FloorOut is at max temp to shorten rampUp Time. When close to target normal tempControl used.
		Running,			// Kepps on running until some sort of event occurs
		Optimising,			// Unclear : is this an inTask initiative or a Background task initiative
		Stopping,			// Switches off the circuitPump and calls shutDown (sets heatRequired to null; and sets state to Off)

		Idle,				// State for pump on but no heatRequired. Used for for floor circuit inlineOptimise
		Suspended,			// Hot_Water : if not stop on objective, suspends all activity but surveys hwTemp
							// resume is called to set the state to Resuming

		Error				// Some sort of error has occured
								

	}
}
