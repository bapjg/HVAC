package eRegulation;

import HVAC_Common.CIRCUIT;


//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Thread_Mixer implements Runnable
{
	public Mixer				mixer;
	public Circuit_Mixer		circuit;
	
	public Thread_Mixer(Circuit_Mixer 							circuit)
	{
		this.circuit 																		= circuit; 				// circuit for which this thread operates
		this.mixer 																			= circuit.mixer;		// mixer on this circuit to be controlled
	}
	public void run()
	{
		LogIt.info("Thread_Mixer_" + circuit.name, "Run", "Starting", true);
		circuit.state																		= HVAC_STATES.Circuit.MixerInitialising;
		Global.waitSeconds(1);																// Wait 1s before switching on the mixer, in case a pump has been turned on/off
		
		mixer.positionZero();
		circuit.state																		= HVAC_STATES.Circuit.Off;
		LogIt.mixerData(Global.DateTime.now(), 0, 0L, 0);									// If timeEnd = 0, then the second part is not inserted into DataBase
		
		Integer 												i							= 0; 	// Used for loop waiting 20 s
		Integer 												targetTemp					= 10 * 1000;						// Dont put at zero to avoid freezing
		Integer													targetFloorIn;													// Could be useful
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
				circuit.requestShutDown();
			}
			
			// Note that Mixer calls go to sleep when positionning the mixer.
			// Notes
			// Try introducing PID (especially Differential
			// If dT/dt is of wrong sign then we are overshooting
			// We could try Koeff = K/tempMixHot (higher Koeff for lower temps)
			// MixCold > 25 degrees indicates trip, could try sitching on radiators to induce water flow, but would need to put Mix to Hot for the duration
			// perhaps not that feasible

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
			
			Integer												insideTempSpan;
			Float												totalTempSpan;

			switch(circuit.state)
			{
			case Off :											// if positionTracked is null, Thread_Mixer will set it to zero
				mixer.positionZero();															// positionZero() checks for null value														
				break;
			case StartRequested :
				mixer.positionPercentage(0.20F);				// TODO Is there a bug here
				break;
			case RampingUp :
				controlMixerAndWait(41000);
				break;
			case Running :
				insideTempSpan																	= this.circuit.taskActive.tempObjective - Global.thermoOutside.reading;
				totalTempSpan																	= insideTempSpan.floatValue()/0.55F;
				targetTemp																		= Global.thermoOutside.reading + totalTempSpan.intValue();
				targetFloorIn																	= Global.thermoOutside.reading + ((int) (totalTempSpan * 0.17F));
				controlMixerAndWait(targetTemp);
				break;
			case StopRequested :								// Note Circuit.Sequencer goes to optimising if any heat left in the system, or shuts down
				break;							// TODO Is there a bug here
			case OptimisationRequested :
//				mixer.positionPercentage(0.2F);													// Can take upto 90 seconds
//				circuit.state																	= HVAC_STATES.Circuit.MixerReady;
				break;
			case AwaitingMixer :
				mixer.positionPercentage(0.2F);													// Can take upto 90 seconds, but can also take 0 seconds
				circuit.state																	= HVAC_STATES.Circuit.MixerReady;
				break;
			case MixerReady :
				break;
			case Optimising :
				controlMixerAndWait(41000);
				break;
			case ShutDownRequested :							// Note Circuit.Sequencer goes to optimising if any heat left in the system, or shuts down
				mixer.positionZero();				// TODO Is there a bug here
				break;
			case Suspended :									// Floor pump is off / NOT A NORMAL situation (except perhaps in summer
//				mixer.positionZero();
				break;
			case Resuming :
//				insideTempSpan																	= this.circuit.taskActive.tempObjective - Global.thermoOutside.reading;
//				totalTempSpan																	= insideTempSpan.floatValue()/0.55F;
//				targetTemp																		= Global.thermoOutside.reading + totalTempSpan.intValue();
//				targetFloorIn																	= Global.thermoOutside.reading + ((int) (totalTempSpan * 0.17F));
//				controlMixer(targetTemp);
				break;
			case IdleRequested:									// Idle means pump is on, with room over temperature (fire in Chimeny)
				// This is a transient state, so do nothing until State = Idle
				insideTempSpan																	= this.circuit.taskActive.tempObjective - Global.thermoOutside.reading;
				totalTempSpan																	= insideTempSpan.floatValue()/0.55F;
				targetTemp																		= Global.thermoOutside.reading + totalTempSpan.intValue();
				targetFloorIn																	= Global.thermoOutside.reading + ((int) (totalTempSpan * 0.17F));
				LogIt.display("Circuit_Floor", "sequencer", "IdleRequested, targetTemp would have been : " + targetTemp.toString() + ",floorReturn is at : " + Global.thermoFloorIn.reading);
				LogIt.display("Circuit_Floor", "sequencer", "IdleRequested, will set target (Floor Out) to : " + ((Integer) this.circuit.taskActive.tempObjective + 2000));
//				controlMixerAndWait(targetTemp);	// TODO ajust
				break;
			case Idle:											// Idle means pump is on, with room over temperature (fire in Chimeny)
//				if (Global.circuits.isSingleActiveCircuit()) 	mixer.positionPercentage(0.2F);		// Get any residual heat into the room
//				else 											mixer.positionZero();
				controlMixerAndWait(this.circuit.taskActive.tempObjective + 2000);				// Just keep some warth in floor
				break;																
			case Error :
				break;
			}
			switch(circuit.state)
			{
			case IdleRequested:									// Explicit wait required
			case Idle:											
			case StartRequested :
			case StopRequested :								
			case OptimisationRequested :
			case Optimising :									// No mixer movement so wait 10 secs
			case ShutDownRequested :							
			case MixerReady :									// We have just waited to move mixer to 0.2F, wait no longer
			case Off :
			case Suspended :									
			case Resuming :
			case Error :
				Global.waitSeconds(10);
				break;

			
			case AwaitingMixer :								// in this state, cycle immediately to get state = MixerReady
			case RampingUp :									// MixerControl Called with implicit wait
			case Running :										// ------------- " ditto " --------------
				break;											
			}
		}		// End while
		circuit.circuitPump.off();
		LogIt.info("Thread_Mixer", "Run", "Stopping", true);	
	}
	public void controlMixerAndWait(Integer targetTemp)
	{
		//=====================================================================
		//
		// position mixer for the arget Temperature
		//
		this.mixer.positionAtTemperatureAndWait(targetTemp);											// Get the mixer moving, then survey the results	
		//
		//=====================================================================

		Integer 												temperatureProjected		= 0;
		Integer 												tempNow;

		Integer 												i							= 0; 	// Used for loop waiting 20 s
		Integer													targetFloorIn;													// Could be useful
		Integer 												timeProjectInSeconds		= mixer.timeProjection/1000;		// Time over which to project temperature change : Convert ms -> s
		Integer 												timeDelayInSeconds			= mixer.timeDelay/1000;				// Time to wait before doing any calculations : Convert ms -> s
		
		Integer indexProject																= timeProjectInSeconds/5;			// Used during 5sec delay loop
		Integer indexDelay																	= timeDelayInSeconds/5;

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
				LogIt.display("Thread_Mixer","Run", "Exception on thermoFloorOut");
				circuit.requestShutDown();
				circuit.mixer.positionZero();
				return;
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
}
