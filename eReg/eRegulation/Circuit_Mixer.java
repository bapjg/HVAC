package eRegulation;

public class Circuit_Mixer extends Circuit_Abstract
{
	// Should delete below as superClass contains these
//	public final int 				CIRCUIT_STATE_Off 				= 0;
//	public final int 				CIRCUIT_STATE_Started 			= 1;
//	public final int 				CIRCUIT_STATE_Running 			= 2;
//	public final int 				CIRCUIT_STATE_Stopping	 		= 3;
//	public final int 				CIRCUIT_STATE_Optimising 		= 4;
//	public final int 				CIRCUIT_STATE_Error	 			= -1;

	public Circuit_Mixer(String name, String friendlyName, String circuitType, String tempMax, String rampUpTime)
	{	
		super(name, friendlyName, circuitType, tempMax, rampUpTime);
	}
	public Long getRampUpTime()
	{
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
					Integer temp									= temperatureGradient.getTempToTarget();
					this.heatRequired.tempMinimum					= 500;
					this.heatRequired.tempMaximum					= 800;
					state											= CIRCUIT_STATE_AwaitingHeat;

					LogIt.info("Circuit_" + this.name, "sequencer", "Thread Started");
					Global.waitSeconds(1);
					Thread thread_mixer 							= new Thread(new Thread_Mixer(mixer, this), "Mixer");
					thread_mixer.start();
				}
				break;
			case CIRCUIT_STATE_AwaitingHeat:
				if (Global.thermoBoiler.reading > this.heatRequired.tempMinimum)
				{
					LogIt.action("PumpFloor", "On");
					Global.pumpFloor.on();
					state											= CIRCUIT_STATE_Running;
				}
				break;
			case CIRCUIT_STATE_Running:
				Integer temp										= temperatureGradient.getTempToTarget();
				this.heatRequired.tempMinimum						= 500;
				this.heatRequired.tempMaximum						= 800;
				break;
			case CIRCUIT_STATE_Stop_Requested:
				LogIt.info("Circuit_" + this.name, "sequencer", "Stop Requested");
				state												= CIRCUIT_STATE_Stopping;
				//Now fall through
			case CIRCUIT_STATE_Stopping:
//				LogIt.info("Circuit", "sequencerFloor", "Stopping Stopping zzzzzzzzzz");

				if 	(	(Global.circuits.isSingleActiveCircuit())
//				&& 		(Global.thermoBoiler.reading > taskActive.tempObjective + 30)   )		// Care, we can be above objective while pumpting heat out !!!
				&& 		(Global.thermoBoiler.reading > Global.thermoFloorCold.reading + 30)   )	// Solution : Continue while more than 3 degrees than return temp
				{
					// We are alone, so as long as there is heat to get out of the system
					// carry on
				}
				else
				{
					LogIt.action("PumpFloor", "Off");
					Global.pumpFloor.off();
					this.shutDown();					// shutDown sets state to off. Threadmixer looks at this as signal to stop
				}
				break;
			case CIRCUIT_STATE_Error:
				break;
			default:
				LogIt.error("Circuit_" + this.name, "sequencer", "unknown state detected : " + state);	
			}
		}
	}
}
