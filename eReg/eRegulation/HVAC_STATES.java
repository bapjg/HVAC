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
		Off,					// Internal                          	// Inactive circuit. taskActive should be null
		StartRequested,			// External via requestStart        	// Setup up heatRequired and sets state to AwaitingHeat		
		RampingUp,				// Internal                          	// Floor : FloorOut is at max temp to shorten rampUp Time. When close to target normal tempControl used.
		Running,				// Internal                          	// Keeps on running until some sort of event occurs
		StopRequested,			// External via requestStop			 	// Decides whether to optimise or shut down
		OptimisationRequested,	// External via requestOptimisation 	// 
		AwaitingMixer,			// 	Special Case  						// 
		MixerInitialising,		// 	Special Case  						// Period where Thread_Mixer starts and positions mixer to zero
		MixerReady,				// 	Special Case  						// 
		Optimising,				// Internal 						 	// Unclear : is this an inTask initiative or a Background task initiative
		ShutDownRequested,		// External via requestShutDown     	// Switches off the circuitPump and calls shutDown (sets heatRequired to null; and sets state to Off)

		Suspended,				// Internal 						 	// Hot_Water : if not stop on objective, suspends all activity but surveys hwTemp
		 															 	// resume is called to set the state to Resuming
		Resuming,				// Internal 						 	// Hot_Water : hwTemp is below minimum, so reactivates heatRequired
		IdleRequested,			// Internal 						 	// State for pump on but no heatRequired. Used for for floor circuit inlineOptimise
		Idle,					// Internal 						 	// State for pump on but no heatRequired. Used for for floor circuit inlineOptimise

		Error					// Internal    						 	// Some sort of error has occured

	}
}
