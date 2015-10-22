package eRegulation;

import HVAC_Common.CIRCUIT;


//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Thread_Mixer implements Runnable
{
	public Mixer				mixer;
	public Circuit_Mixer		circuit;
	
	public Thread_Mixer(Circuit_Mixer 						circuit)
	{
		this.circuit 																		= circuit; 				// circuit for which this thread operates
		this.mixer 																			= circuit.mixer;		// mixer on this circuit to be controlled
	}
	public void run()
	{
		LogIt.info("Thread_Mixer_" + circuit.name, "Run", "Starting", true);		
		Global.waitMilliSeconds(3000);														// Wait 3s before switching on the mixer, in case a pump has been turned on
		mixer.positionZero();
		LogIt.mixerData(Global.DateTime.now(), 0, 0L, 0);									// If timeEnd = 0, then the second part is not inserted into DataBase
		
		Integer 												i							= 0; 	// Used for loop waiting 20 s
		Integer 												targetTemp;
		Integer 												timeProjectInSeconds		= mixer.timeProjection/1000;		// Time over which to project temperature change : Convert ms -> s
		Integer 												timeDelayInSeconds			= mixer.timeDelay/1000;				// Time to wait before doing any calculations : Convert ms -> s
		
		Integer indexProject																= timeProjectInSeconds/5;			// Used during 5sec delay loop
		Integer indexDelay																	= timeDelayInSeconds/5;
		
		while (!Global.stopNow)
		{
//			Mixer states can be 
//				- starting/running
//				- Idle (i.e. room already at targetTemperature)
//				- stoping/off
//				- running cold
//			if stopping/off do nothing
//			if running cold just carry on at cold position (do we need to call sequencer ?)
//			if running/starting do below	
			
			if (	(Global.thermoOutside.reading 		== null)
			||		(Global.thermoLivingRoom.reading 	== null)	)
			{
				Global.eMailMessage("Thread_Mixer/run", "Unable to read a Thermometer");
				circuit.shutDown();
			}
			// TODO CHeck this

			switch(circuit.state)
			{
			case Off :
				if (mixer.positionTracked > 0)
				{
					mixer.positionZero();
				}
				break;
			case Starting :							// Just fall through to AwaitingHeat
				mixer.positionPercentage(0.20F);
				break;
			case RampingUp :
			case Running :
			                        
			case Suspended :
			case Resuming :
			                        
			case Optimising :
			case Stopping :
			                        
			case Error :
				break;
			}
			System.out.println("state ----------------: " + circuit.state.toString());
			
			if  ((circuit.state != HVAC_STATES.Circuit.Off) && (circuit.state != HVAC_STATES.Circuit.Suspended)  && (circuit.state != HVAC_STATES.Circuit.Error))
			{
				// Note that Mixer calls go to sleep when positionning the mixer.
				
				// Notes
				// Try introducing PID (especially Differential
				// If dT/dt is of wrong sign then we are overshooting
				// We could try Koeff = K/tempMixHot (higher Koeff for lower temps)
				// MixCold > 25 degrees indicates trip, could try sitching on radiators to induce water flow, but would need to put Mix to Hot for the duration
				// perhaps not that feasible
				
				if (Global.thermoOutside.reading > Global.tasksBackGround.summerTemp)			// > summerTemp
				{
					// Outside temp is high : no need to heat
					targetTemp																	= 10 * 1000;					// Dont put at zero to avoid freezing
				}
				else if (Global.thermoLivingRoom.reading > this.circuit.taskActive.tempObjective - 1000)
				{
					// Must replace by PID
					// Inside temp is high : no need to heat (within 1 degree
//					targetTemp																= circuit.temperatureGradient.getTempToTarget();
					
					Integer										insideTempSpan				= this.circuit.taskActive.tempObjective - Global.thermoOutside.reading;
					Float										totalTempSpan				= insideTempSpan.floatValue()/0.55F;
					
					targetTemp																= Global.thermoOutside.reading + totalTempSpan.intValue();
					
					Integer										targetFloorIn				= Global.thermoOutside.reading + ((int) (totalTempSpan * 0.17F));
					
					// If tempFloorIn > targetFloorIn => We are probably going to overtemp.
					// This uses Leaking baths method to get correct temperature in LivingRoom
					
					//-----^------FloorOut-----^---------   = targetTemp
					//     |                   |
					//    17%                  | 
					//	   |                   |
					//---^-v------FloorIn -----|---------   = targetFloorIn
					//	 |                     |
					//  27%                   100%   = totalTempSpan
					//   |                     |
					//---v-^------LivingRoom   |
					//     |                   |
					//    55%                  |     = insideTempSpan
					//     |                   |
					//-----v------Outside------v--
					
				}
				else if (circuit.state == HVAC_STATES.Circuit.RampingUp) 						// This is to accelerate rampup
				{
					targetTemp																= 41000;						// Trip avoidance kicks in at 450
				}
				else
				{
					targetTemp																= circuit.temperatureGradient.getTempToTarget();  //Loi d'eau
				}
				
				this.mixer.sequencer(targetTemp);
	
				Integer 										temperatureProjected		= 0;
				Integer 										tempNow;
	
				// Idea is to upto temeProject (timeProjection) in 5s intervals.
				// The first intervals upto timeDelay, no decision is made
				// Thereafter, if projected temperature is out of bound, the loop stops and the PID reactivated for recalculation
				for (i = 0; (i < indexProject) && (! Global.stopNow); i++)
				{
					Global.waitSeconds(5);													// indexWait loops of 5s
						
					try
					{
						tempNow																= Global.thermoFloorOut.readUnCached();
					}
					catch (Exception ex)					// Panic : a read error has occured on thermometer
					{
						circuit.shutDown();
						circuit.state														= HVAC_STATES.Circuit.Error;
						circuit.mixer.positionZero();
						break;
					}
					
					// TODO check that tempNow != null
					
					if (i >= indexDelay)													// We have waited for dTdt to settle a bit
					{
						temperatureProjected												= tempNow + ((Float) (Global.thermoFloorOut.pidControler.dTdt() * timeProjectInSeconds)).intValue();
						
						// Perhaps a better idea is to calculate time at which targetTemp will be attained
						// Are we getting closer or is it moving out, or worse will never happen.
						
						Long 									timeNow 					= Global.DateTime.now();
						Float 									timeProjected 				=  (targetTemp - tempNow)/Global.thermoFloorOut.pidControler.dTdt();
						
						if (Math.abs(temperatureProjected - targetTemp) > mixer.marginProjection)		// More than 2 degrees difference (either over or under)
						{
							break;	// Stops the loop to reanalyse the situation, i.e.GoTo "for (i = 0; (i < indexProject) && (! Global.stopNow); i++)"
						}
					}
				}
			}
			// TODO CHeck this
			else if  ((circuit.state == HVAC_STATES.Circuit.Off ) || (circuit.state == HVAC_STATES.Circuit.Suspended)) //Running Cold
			{
				// TODO should we position zero every cycle. what about optimisation. what about floor temp measurement
				// circuit.state = circuit.CIRCUIT_STATE_Shutting_down doesn't last long enough to be reliable. use positiontacked
				// as indication of whether we are stopped or not
				if (mixer.positionTracked > 0)
				{
					mixer.positionZero();
				}
			}
		}		// End while
		circuit.circuitPump.off();
		LogIt.info("Thread_Mixer", "Run", "Stopping", true);	
	}
}
