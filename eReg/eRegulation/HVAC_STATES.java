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
* Outside the main thread, statechanges can be operated by
* start : sets state to starting
* stop  : sets state to stopping
* shutDown : sets state to ShuttingDown
* optimise : sets state to BeginningOptimisation
*/
	public enum Circuit
	{
		Off,					// Internal                          // Inactive circuit. taskActive should be null
		Starting,				// External via initiateStart        // Setup up heatRequired and sets state to AwaitingHeat		
		RampingUp,				// Internal                          // Floor : FloorOut is at max temp to shorten rampUp Time. When close to target normal tempControl used.
		Running,				// Internal                          // Keeps on running until some sort of event occurs
		Stopping,				// Internal 						 // Decides whether to optimise or shut down
		BeginningOptimisation,	// External via initiateOptimisation // 
		Optimising,				// Internal 						 // Unclear : is this an inTask initiative or a Background task initiative
		ShuttingDown,			// Internal via initiateShutDown     // Switches off the circuitPump and calls shutDown (sets heatRequired to null; and sets state to Off)

		Suspended,				// Internal 						 // Hot_Water : if not stop on objective, suspends all activity but surveys hwTemp
		 															 // resume is called to set the state to Resuming
		Resuming,				// Internal 						 // Hot_Water : hwTemp is below minimum, so reactivates heatRequired
		Idle,					// Internal 						 // State for pump on but no heatRequired. Used for for floor circuit inlineOptimise

		Error					// Internal    						 // Some sort of error has occured

	}
}
