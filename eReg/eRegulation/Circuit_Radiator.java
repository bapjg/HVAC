package eRegulation;

import HVAC_Common.CIRCUIT;
import HVAC_Common.Ctrl_Configuration;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Circuit_Radiator extends Circuit_Abstract
{
	public Circuit_Radiator(Ctrl_Configuration.Circuit				paramCircuit)			// New
	{	
		super(paramCircuit);

		this.temperatureGradient															= new TemperatureGradient(paramCircuit.tempGradient);
	}
	
	//===========================================================================================================================================================
	//
	// Performance methods
	//
	@Override 
	public Long getRampUpTime(Integer tempObjective)
	{
		Long 													rampUpMilliSeconds			= 30 * 60 * 1000L;		// 30 mins
		return rampUpMilliSeconds;
	}
	//
	//===========================================================================================================================================================

	//===========================================================================================================================================================
	//
	// Activity/State Change methods
	//
	@Override 
	public void start()
	{
		super.start();
		Integer 									temp									= temperatureGradient.getTempToTarget();
		this.heatRequired.set(temp - 7500, temp + 7500);
	}
	//
	//===========================================================================================================================================================

	//===========================================================================================================================================================
	//
	// Sequencer
	//
	@Override
	public void sequencer()
	{
		// Check for work
		if (taskActive == null)									return;						//Nothing to do				

		// Check for security / errors
		if 	(Global.thermoBoiler.reading 		== null) 
		{
			shutDown();																		// This bypasses stopRequested
			circuitPump.off();
			state												= HVAC_STATES.Circuit.Error;
			Global.eMailMessage("Circuit_Radiator/sequencer", "A Thermometer cannont be read");
		}

		// Do normal activity
	
		switch (state)
		{
		case Off:
			//Nothing to do
			break;
		case Starting:
			if (Global.thermoBoiler.reading > Global.thermoHotWater.reading) 				// We can start pumping heat (was : > this.heatRequired.tempMinimum)
			{
				LogIt.action("PumpRadiator", "On");
				circuitPump.on();
				state																		= HVAC_STATES.Circuit.Running;
			}
			break;
		case Running:
			Integer 											temp						= temperatureGradient.getTempToTarget();	// Outside temp may change
			this.heatRequired.tempMinimum													= temp - 7500;
			this.heatRequired.tempMaximum													= temp + 7500;
			break;
		case Stopping:
			if 	(	(Global.circuits.isSingleActiveCircuit())
			&& 		(Global.thermoBoiler.reading > Global.thermoHotWater.reading + 3000) ) 	//Might as well get as much heat out of it as possible
			{
				// We are alone, so as long as there is heat to get out of the system
				// carry on
			}
			else
			{
				LogIt.action(this.name, "Closing down completely");
				LogIt.action("PumpRadiator", "Off");
				circuitPump.off();
				this.heatRequired.setZero();
				this.state																	= HVAC_STATES.Circuit.Off;
				break;
			}
			break;
		case Error:
			break;
		default:
			LogIt.error("Circuit_" + this.name, "sequencer", "unknown state detected : " + state);	
		}
	}	// sequencer
	//
	//===========================================================================================================================================================
}
