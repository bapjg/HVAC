package eRegulation;

import HVAC_Messages.Ctrl_Configuration;

public class Circuit_Mixer extends Circuit_Abstract
{

//	public Circuit_Mixer(String name, String friendlyName, Integer circuitType, String tempMax, String rampUpTime)
//	{	
//		super(name, friendlyName, circuitType, tempMax, rampUpTime);
//	}
//	public Circuit_Mixer(String name, Integer circuitType, String pumpName, String thermometerName, Integer tempMax)			// New
//	{	
//		super(name, circuitType, pumpName, thermometerName, tempMax);
//	}
	public Circuit_Mixer(Ctrl_Configuration.Circuit paramCircuit)
	{
		super(paramCircuit);

		this.temperatureGradient				= new TemperatureGradient(paramCircuit.tempGradient);
		this.mixer								= new Mixer(paramCircuit.mixer);

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
		Integer temp;							// Probably not usefull;
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
					temp											= temperatureGradient.getTempToTarget();
					this.heatRequired.tempMinimum					= 60000;
					this.heatRequired.tempMaximum					= 80000;
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
					circuitPump.on();
					state											= CIRCUIT_STATE_RampingUp;
				}
				break;
			case CIRCUIT_STATE_RampingUp:
				if (Global.thermoLivingRoom.reading > (20000 - 2000))
				{
					LogIt.display("Circuit_Mixer", "sequencer", "RampUpFinished as livingroom temp is " + Global.thermoLivingRoom.reading);
					state											= CIRCUIT_STATE_Running;
				}
				temp												= 43000;
				this.heatRequired.tempMinimum						= 60000;
				this.heatRequired.tempMaximum						= 80000;
				break;
			case CIRCUIT_STATE_Running:
				temp												= temperatureGradient.getTempToTarget();
				this.heatRequired.tempMinimum						= 60000;
				this.heatRequired.tempMaximum						= 80000;
//				if (Global.thermoLivingRoom.reading > this.taskActive.tempObjective - 1000)		//Within 1 degree
//				{
//					this.heatRequired.tempMinimum					= 20000;
//					this.heatRequired.tempMaximum					= 20000;
//				}
				
				break;
			case CIRCUIT_STATE_Stop_Requested:
				LogIt.info("Circuit_" + this.name, "sequencer", "Stop Requested");
				state												= CIRCUIT_STATE_Stopping;
				//Now fall through
			case CIRCUIT_STATE_Stopping:
				if 	(	(Global.circuits.isSingleActiveCircuit())
//				&& 		(Global.thermoBoiler.reading > taskActive.tempObjective + 30)   )		// Care, we can be above objective while pumpting heat out !!!
				&& 		(Global.thermoBoiler.reading > Global.thermoFloorIn.reading + 3000)   )	// Solution : Continue while more than 3 degrees than return temp
				{
					// We are alone, so as long as there is heat to get out of the system
					// carry on
				}
				else
				{
					circuitPump.off();
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
