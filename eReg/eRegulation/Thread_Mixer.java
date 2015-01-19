package eRegulation;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Thread_Mixer implements Runnable
{
	public Mixer				mixer;
	public Circuit_Mixer		circuit;
	
	public Thread_Mixer(Circuit_Mixer 						circuit)
	{
		this.circuit 							= circuit; 				// circuit for which this thread operates
		this.mixer 								= circuit.mixer;		// mixer on this circuit to be controlled
	}
	public void run()
	{
		LogIt.info("Thread_Mixer_" + circuit.name, "Run", "Starting", true);		

		mixer.positionZero();
//		LogIt.mixerData(Global.now(), 0, Global.now(), mixer.positionTracked);					// Problem with dbInsert if timeStart = timeEnd (primary Index)
		LogIt.mixerData(Global.DateTime.now(), 0, 0L, 0);												// If timeEnd = 0, then the second part is not inserted into DataBase
		
		Integer 				i							= 0; 	// Used for loop waiting 20 s
		Integer 				targetTemp;
		Integer 				timeProjectInSeconds		= mixer.timeProjection/1000;		// Time over which to project temperature change : Convert ms -> s
		Integer 				timeDelayInSeconds			= mixer.timeDelay/1000;				// Time to wait before doing any calculations : Convert ms -> s
		
		Integer indexProject								= timeProjectInSeconds/5;			// Used during 5sec delay loop
		Integer indexDelay									= timeDelayInSeconds/5;
		
		while (!Global.stopNow)
//		while ((!Global.stopNow) && (circuitMixer.state != circuitMixer.CIRCUIT_STATE_Off))
		{
//			Mixer states can be 
//				- starting/running
//				- stoping/off
//				- running cold
//			if stopping/off do nothing
//			if running cold just carry on at cold position (do we need to call sequencer ?)
//			if running/starting do below	
			
			if  (circuit.state != circuit.CIRCUIT_STATE_Off)
			{
				// Note that Mixer calls go to sleep when positionning the mixer.
				
				// Notes
				// Try introducing PID (especially Differential
				// If dT/dt is of wrong sign then we are overshooting
				// We could try Koeff = K/tempMixHot (higher Koeff for lower temps)
				// MixCold > 25 degrees indicates trip, could try sitching on radiators to induce water flow, but would need to put Mix to Hot for the duration
				// perhaps not that feasible
				
				if (Global.thermoOutside.reading > 17000)			// > summerTemp
				{
					// Outside temp is high : no need to heat
					targetTemp									= 10 * 1000;					// Dont put at zero to avoid freezing
				}
				else if (Global.thermoLivingRoom.reading > this.circuit.taskActive.tempObjective - 1000)
				{
					// Must replace by PID
					// Inside temp is high : no need to heat (within 1 degree
					targetTemp									= circuit.temperatureGradient.getTempToTarget();
				}
				else if (circuit.state == circuit.CIRCUIT_STATE_RampingUp) 						// This is to accelerate rampup
				{
					targetTemp									= 43000;						// Trip avoidance kicks in at 450
				}
				else
				{
					targetTemp									= circuit.temperatureGradient.getTempToTarget();
				}
				this.mixer.sequencer(targetTemp);
	
				Integer temperatureProjected					= 0;
				Integer tempNow;
	
				// Idea is to upto temeProject (timeProjection) in 5s intervals.
				// The first intervals upto timeDelay, no decision is made
				// Thereafter, if projected temperature is out of bound, the loop stops and the PID reactivated for recalculation
				for (i = 0; (i < indexProject) && (! Global.stopNow); i++)
				{
					Global.waitSeconds(5);														// indexWait loops of 5s
						
					tempNow										= Global.thermoFloorOut.readUnCached();
					
					if (i >= indexDelay)														// We have waited for dTdt to settle a bit
					{
						temperatureProjected					= tempNow + ((Float) (Global.thermoFloorOut.pidControler.dTdt() * timeProjectInSeconds)).intValue();
						
						// Perhaps a better idea is to calculate time at which targetTemp will be attained
						// Are we getting closer or is it moving out, or worse will never happen.
						
						Long timeNow = Global.DateTime.now();
						Float timeProjected =  (targetTemp - tempNow)/Global.thermoFloorOut.pidControler.dTdt();
						if (timeProjected < 0)
						{
							// LogIt.display("Thread_Mixer", "sequencer", "---Houston we have a problem--- the gap is widening");
						}
						else if (timeProjected > indexProject) //Rubbish
						{
							// LogIt.display("Thread_Mixer", "sequencer", "---Houston we should have saved the previous value---");
						}
						
						if (Math.abs(temperatureProjected - targetTemp) > mixer.marginProjection)		// More than 2 degrees difference (either over or under)
						{
							// This is wrong, if going up, exceed upperbound gives error
							// if going down, only lowerbound is wrong
							
							
							
							//							LogIt.display("Thread_Mixer", "mainLoop", "Interrupting the " + timeProjectInSeconds + "s wait after " + (i * 5) +"s, temperatureProjected : " + temperatureProjected + ", tempTarget : " + targetTemp); //in millidegreese
							break;
						}
					}
				}
			}
			else if  (circuit.state == circuit.CIRCUIT_STATE_Off )  //Running Cold
			{
				// TODO should we position zero evry cycle. what about optimisation. what about floor temp measurement
				// circuit.state = circuit.CIRCUIT_STATE_Shutting_down doesn't last long enough to be reliable. use positiontacked
				// as indication of whether we are stopped or not
				if (mixer.positionTracked > 0)
				{
					mixer.positionZero();
				}
			}
		}
		circuit.circuitPump.off();
		LogIt.info("Thread_Mixer", "Run", "Stopping", true);	
	}
}
