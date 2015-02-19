package eRegulation;

import HVAC_Common.Ctrl_Configuration;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Circuit_Mixer extends Circuit_Abstract
{
	public Circuit_Mixer(Ctrl_Configuration.Circuit paramCircuit)
	{
		super(paramCircuit);

		this.temperatureGradient				= new TemperatureGradient(paramCircuit.tempGradient);
		this.mixer								= new Mixer(paramCircuit.mixer);
	}
	public Long getRampUpTime(Integer tempObjective)
	{
		Integer													tempNow						= Global.thermoLivingRoom.read();
		Integer													tempDifference				= tempObjective - tempNow;

		// Work on basis of 		:	6               hours   per degree
		// or						:   6 x 60 x 60     seconds per degree
		// or                       :   6 x 3600 x 1000 milliSeconds per degree
		// or                       :   6 x 3600        milliSeconds per milliDegree
		
		if (tempDifference > 0)
		{
			Long												rampUpMilliSeconds			= 6L * 3600 * tempDifference;		// 6 hours per degree
			return rampUpMilliSeconds;
		}
		return 0L;
	}
	@Override
	public Long calculatePerformance()
	{
		return 10000L;
	}
	@Override
	public void sequencer()
	{
		if (taskActive == null)
		{
			//Nothing to do
		}
		else
		{
			switch (state)
			{
			case CIRCUIT_STATE_Off:
				//Nothing to do
				break;
			case CIRCUIT_STATE_Start_Requested:
				LogIt.info("Circuit_" + this.name, "sequencer", "Start Requested");
				circuitPump.on();														// CircuitPump must be on in order to obtain correct temperature readings
				state												= CIRCUIT_STATE_Starting;
				//Now fall through
			case CIRCUIT_STATE_Starting:
				if (temperatureGradient == null)
				{
					LogIt.error("Circuit_" + this.name, "sequencer", "temperatureGradient is null");
					state										= CIRCUIT_STATE_Error;
				}
				else
				{
					this.heatRequired.tempMinimum					= 60000;
					this.heatRequired.tempMaximum					= 80000;
					state											= CIRCUIT_STATE_AwaitingHeat;
				}
				break;
			case CIRCUIT_STATE_AwaitingHeat:
				if (Global.thermoBoiler.reading > this.heatRequired.tempMinimum)
				{
					state											= CIRCUIT_STATE_RampingUp;
				}
				break;
			case CIRCUIT_STATE_RampingUp:
				this.heatRequired.tempMinimum						= 60000;
				this.heatRequired.tempMaximum						= 80000;
				break;
			case CIRCUIT_STATE_Running:
				this.heatRequired.tempMinimum						= 60000;
				this.heatRequired.tempMaximum						= 80000;
				break;
			case CIRCUIT_STATE_Stop_Requested:
				LogIt.info("Circuit_" + this.name, "sequencer", "Stop Requested : Now Optimise");
				// Now fall through State will be changed below
			case CIRCUIT_STATE_Optimising:
				if 	(	(Global.circuits.isSingleActiveCircuit())
				&& 		(Global.thermoBoiler.reading > Global.thermoFloorIn.reading + 3000)   )	// Solution : Continue while more than 3 degrees than return temp
				{
					if (state != CIRCUIT_STATE_Optimising)
					{
						LogIt.info("Circuit_" + this.name, "sequencer", "Optimising");			// Done this way to get only one message (no repeats)
						this.heatRequired							= null;
						state										= CIRCUIT_STATE_Optimising;
					}
				}
				else
				{
					state											= CIRCUIT_STATE_Stopping;
				}
				break;
			case CIRCUIT_STATE_Stopping:
				circuitPump.off();
				this.shutDown();					// shutDown sets state to off. Thread_mixer looks at this as signal to stop
				break;
			case CIRCUIT_STATE_Error:
				break;
			default:
				LogIt.error("Circuit_" + this.name, "sequencer", "unknown state detected : " + state);	
			}
		}
	}
}
