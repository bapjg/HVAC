package eRegulation;

public class Thread_Mixer implements Runnable
{
	public Mixer				mixer;
	public Circuit_Mixer		circuitMixer;
	
	public Thread_Mixer(Mixer mixer, Circuit_Mixer circuitMixer)
	{
		this.mixer 								= mixer;		// object Global.mixer
		this.circuitMixer 						= circuitMixer; // object Circuit_Mixer
	}
	public void run()
	{
		LogIt.info("Thread_Mixer", "Run", "Floor Starting", true);		

		Long timeStart							= Global.now();
		mixer.positionAbsolute(mixer.swingUsableMin);
		LogIt.mixerData(timeStart, 0, Global.now(), mixer.positionTracked);
		
		Global.waitSeconds(1);
		circuitMixer.circuitPump.on();
		Global.waitSeconds(1);
		Integer 				i							= 0; 	// Used for loop waiting 20 s
		Integer 				targetTemp;
		Integer 				timeProjectInSeconds		= mixer.timeProject/1000;		// Time over which to project temperature change : Convert ms -> s
		Integer 				timeDelayInSeconds			= mixer.timeDelay/1000;			// Time to wait before doing any calculations : Convert ms -> s
		
		Integer indexProject								= timeProjectInSeconds/5;				// Used during 5sec delay loop
		Integer indexDelay									= timeDelayInSeconds/5;

		
		
		
		
		while ((!Global.stopNow) && (circuitMixer.state != circuitMixer.CIRCUIT_STATE_Off))
		{
			// Note that Mixer calls go to sleep when positionning the mixer.
			
			// Notes
			// Try introducing PID (especially Differential
			// If dT/dt is of wrong sign then we are overshooting
			// We could try Koeff = K/tempMixHot (higher Koeff for lower temps)
			// MixCold > 25 degrees indicates trip, could try sitching on radiators to induce water flow, but would need to put Mix to Hot for the duration
			// perhaps not that feasible
			
			if (Global.thermoOutside.reading > 17000)
			{
				// Outside temp is high : no need to heat
				targetTemp									= 10 * 1000;		// Dont put at zero to avoid freezing
				targetTemp									= circuitMixer.temperatureGradient.getTempToTarget();
			}
			else if (Global.thermoLivingRoom.reading > this.circuitMixer.taskActive.tempObjective - 1000)
			{
				// Must replace by PID
				// Inside temp is high : no need to heat (within 1 degree
				targetTemp									= 10 * 1000;		// Dont put at zero to avoid freezing
				targetTemp									= circuitMixer.temperatureGradient.getTempToTarget();
			}
			else if (circuitMixer.state == circuitMixer.CIRCUIT_STATE_RampingUp) // This is to accelerate rampup
			{
				targetTemp									= 43000;						// Trip avoidance kicks in at 450
			}
			else
			{
				targetTemp									= circuitMixer.temperatureGradient.getTempToTarget();
			}
			this.mixer.sequencer(targetTemp);

			Integer temperatureProjected					= 0;
			Integer tempNow;

			// Idea is to upto temeProject in 5s intervals.
			// The first intervals upto timeDelay, no decision is made
			// Thereafter, if projected temperature is out of bound, the loop stops and the PID reactivated for recalculation
			for (i = 0; (i < indexProject) && (! Global.stopNow); i++)
			{
				Global.waitSeconds(5);									// indexWait loops of 5s
				
				tempNow										= Global.thermoFloorOut.readUnCached();
				
				if (i >= indexDelay)									// We have waited for dTdt to settle a bit
				{
					temperatureProjected					= tempNow + ((Float) (Global.thermoFloorOut.pidControler.dTdt() * timeProjectInSeconds)).intValue();
					
					if (Math.abs(temperatureProjected - targetTemp) > 2000)		// More than 2 degrees difference (either over or under)
					{
						LogIt.display("Thread_Mixer", "mainLoop", "Interrupting the " + timeProjectInSeconds + "s wait after " + (i * 5) +"s, temperatureProjected : " + temperatureProjected + ", tempTarget : " + targetTemp); //in millidegreese
						break;
					}
				}
			}
		}
		// Optimise if singlecircuit
		circuitMixer.circuitPump.off();
		LogIt.info("Thread_Mixer", "Run", "Floor Thread ending", true);	
	}
}
