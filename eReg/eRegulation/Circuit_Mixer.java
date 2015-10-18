package eRegulation;

import HVAC_Common.CIRCUIT;
import HVAC_Common.Ctrl_Configuration;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Circuit_Mixer extends Circuit_Abstract
{
	public Circuit_Mixer(Ctrl_Configuration.Circuit paramCircuit)
	{
		super(paramCircuit);

		this.temperatureGradient															= new TemperatureGradient(paramCircuit.tempGradient);
		this.mixer																			= new Mixer(paramCircuit.mixer);
	}
	public Long getRampUpTime(Integer tempObjective)
	{
		Integer													tempNow						= Global.thermoLivingRoom.reading;
		if (tempNow != null)
		{
			Integer													tempDifference			= tempObjective - tempNow;
	
			// Work on basis of 		:	6               hours   per degree
			// or						:   6 x 60 x 60     seconds per degree
			// or                       :   6 x 3600 x 1000 milliSeconds per degree
			// or                       :   6 x 3600        milliSeconds per milliDegree
			
			if (tempDifference > 0)
			{
				Long												rampUpMilliSeconds		= 6L * 3600 * tempDifference;		// 6 hours per degree
				return rampUpMilliSeconds;
			}
			return 0L;
		}
		else
		{
			this.state 																		= HVAC_STATES.Circuit.Error;
			return 0L;
		}
	}
	@Override
	public void sequencer()		// Called from main loop
	{
		if (taskActive == null)
		{
			//Nothing to do
		}
		else
		{
			if (	(Global.thermoBoiler.reading 		== null) 
			||		(Global.thermoLivingRoom.reading 	== null)	)
			{
				shutDown();											// This bypasses stopRequested
				// TODO Should we not close the mixer
				circuitPump.off();
				state												= HVAC_STATES.Circuit.Error;
				Global.eMailMessage("Circuit_Mixer/sequencer", "A Thermometer cannont be read");
			}

			switch (state)
			{
			case Off:
				//Nothing to do
				break;
			case Start_Requested:
				LogIt.info("Circuit_" + this.name, "sequencer", "Start Requested");
				
				if (Global.thermoLivingRoom.reading < this.taskActive.tempObjective)
				{
					circuitPump.on();														// CircuitPump must be on in order to obtain correct temperature readings
					state																	= HVAC_STATES.Circuit.Starting;
				}
				else
				{
					LogIt.info("Circuit_" + this.name, "sequencer", "Already at temperature. Just idle");
					state																	= HVAC_STATES.Circuit.Idle;
				}
				break;
			case Idle:
				if (Global.thermoLivingRoom.reading < this.taskActive.tempObjective)
				{
					circuitPump.on();														// CircuitPump must be on in order to obtain correct temperature readings
					LogIt.info("Circuit_" + this.name, "sequencer", "Idle ended");
					state																	= HVAC_STATES.Circuit.Starting;
				}
				break;
			case Starting:
				if (temperatureGradient == null)
				{
					LogIt.error("Circuit_" + this.name, "sequencer", "temperatureGradient is null");
					state																	= HVAC_STATES.Circuit.Error;
				}
				else
				{
					this.heatRequired.tempMinimum											= 60000;
					this.heatRequired.tempMaximum											= 80000;
					state																	= HVAC_STATES.Circuit.AwaitingHeat;
				}
				break;
			case AwaitingHeat:
				if (Global.thermoBoiler.reading > this.heatRequired.tempMinimum)
				{
					state																	= HVAC_STATES.Circuit.RampingUp;
				}
				break;
			case RampingUp:
				this.heatRequired.tempMinimum												= 60000;
				this.heatRequired.tempMaximum												= 80000;
				break;
			case Running:
				if (Global.thermoLivingRoom.reading > this.taskActive.tempObjective)
				{
					this.heatRequired														= null;
					state																	= HVAC_STATES.Circuit.Idle;
				}
				else
				{
					this.heatRequired.tempMinimum											= 60000;
					this.heatRequired.tempMaximum											= 80000;
				}
				break;
			case Stop_Requested:
				LogIt.info("Circuit_" + this.name, "sequencer", "Stop Requested : Now Optimise");
				// Now fall through State will be changed below
			case Optimising:
				// TODO Mixer position is at zero
//				LogIt.display("Circuit_Mixer", "sequencer/Optimising", "isSingleActiveCircuit : " 	+ Global.circuits.isSingleActiveCircuit());
//				LogIt.display("Circuit_Mixer", "sequencer/Optimising", "thermoBoiler : " 			+ Global.thermoBoiler.reading);
//				LogIt.display("Circuit_Mixer", "sequencer/Optimising", "thermoFloorIn : " 			+ Global.thermoFloorIn.reading);
//				LogIt.display("Circuit_Mixer", "sequencer/Optimising", "mixer.positionTracked : " 	+ mixer.positionTracked);
				if 	(	(Global.circuits.isSingleActiveCircuit()							)
				&& 		(Global.thermoBoiler.reading > Global.thermoFloorIn.reading + 3000	)   	//  Continue while boilerTemp more than 3 degrees than return temp
					)
// Changed 06/10/2015. Mixer gets stuck in the off position				
//				&& 		(mixer.positionTracked > 0											)   )	//  If no warm water is flowing, no point continuing
				{
					if (state != HVAC_STATES.Circuit.Optimising)
					{
						LogIt.info("Circuit_" + this.name, "sequencer", "Optimising");			// Done this way to get only one message (no repeats)
						this.heatRequired													= null;
						state																= HVAC_STATES.Circuit.Optimising;
					}
				}
				else
				{
					state																	= HVAC_STATES.Circuit.Stopping;
				}
				break;
			case Stopping:
				circuitPump.off();
				this.shutDown();					// shutDown sets state to off. Thread_mixer looks at this as signal to stop
				break;
			case Error:
				break;
			default:
				LogIt.error("Circuit_" + this.name, "sequencer", "unknown state detected : " + state);	
			}
		}
	}
/**
 * Starts the circuit in optimisation mode:
 * Supplied circuitTask becomes taskActive
 * State set to Start_Requested
 * heatRequired set to zero valued object
 */
	public void optimiseFloor()
	{
		LogIt.action(this.name, "----------------------------------optimiseFloor called");
		Long											now									= Global.Time.now();
		Integer											targetTemperature					= Global.thermoLivingRoom.reading + 2000;	// Go for 2 degrees above current temperature
		CircuitTask										task								= new CircuitTask(	
																												now, 	// Time Start
																												now + 5L * 60L * 1000L, 	// TimeEnd in 5 mins
																												targetTemperature,	// TempObjective in millidesrees
																												false,	// StopOnObjective
																												"1, 2, 3, 4, 5, 6, 7",					// Days
																												HVAC_TYPES.CircuitTask.DontKnow
				);
		this.taskActive																		= task;
		this.circuitPump.on();
		this.state																			= HVAC_STATES.Circuit.Optimising;
		this.heatRequired																	= null;
	}
}
