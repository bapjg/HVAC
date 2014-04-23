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
		mixer.positionAbsolute(0.30F);
		LogIt.info("Thread_Mixer", "Run", "Floor Initialised (30%)");		
		LogIt.mixerData(timeStart, 0, Global.now(), mixer.swingTime * 300);
		
		Global.waitSeconds(1);
		circuitMixer.circuitPump.on();
//		Global.pumpFloor.on();								// Need to get this from circuit
		Global.waitSeconds(1);
		Integer i								= 0; // Used for loop waiting 20 s
		
		while ((!Global.stopNow) && (circuitMixer.state != circuitMixer.CIRCUIT_STATE_Off))
		{
			// Note that Mixer calls go to sleep when positionning the mixer.
			
			// Notes
			// Try introducing PID (especially Differential
			// If dT/dt is of wrong sign then we are overshooting
			// We could try Koeff = K/tempMixHot (higher Koeff for lower temps)
			// MixCold > 25 degrees indicates trip, could try sitching on radiators to induce water flow, but would need to put Mix to Hot for the duration
			// perhaps not that feasible
			
			Integer targetTemp;
			if (Global.thermoOutside.reading > 17000)
			{
				// Outside temp is high : no need to heat
				targetTemp									= 10 * 1000;		// Dont put at zero to avoid freezing
				targetTemp									= circuitMixer.temperatureGradient.getTempToTarget();
			}
			else if (Global.thermoLivingRoom.reading > this.circuitMixer.taskActive.tempObjective - 1000)
			{
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

			Integer timeWait								= 50;
			Integer timeInterupt							= 30;
			
			Integer indexWait								= timeWait/5;
			Integer indexInterupt							= timeInterupt/5;
			Integer temperatureProjected					= 0;
			Integer tempNow;
			Integer tempPrevious							= Global.thermoFloorOut.readUnCached();

			for (i = 0; (i < indexWait) && (! Global.stopNow); i++)
			{
				Global.waitSeconds(5);									// indexWait loops of 5s
				
				tempNow										= Global.thermoFloorOut.readUnCached();
				
				if (i >= indexInterupt)									// We have waited for dTdt to settle a bit
				{
					temperatureProjected					= tempNow + ((Float) (Global.thermoFloorOut.pidControler.dTdt() * timeWait)).intValue();
					
					if (Math.abs(temperatureProjected - targetTemp) > 2000)		// More than 2 degrees difference (either over or under)
					{
						LogIt.display("Thread_Mixer", "mainLoop", "Interrupting the " + timeWait + "s wait after " + (i * 5) +"s");
						LogIt.display("Thread_Mixer", "mainLoop", "temperatureProjected : " + temperatureProjected + ", tempTarget : " + targetTemp); //in millidegreese
						break;
					}
				}
				tempPrevious								= tempNow;
			}
		}
		// Optimise if singlecircuit
		circuitMixer.circuitPump.off();
//		Global.pumpFloor.off();
		LogIt.info("Thread_Mixer", "Run", "Floor Thread ending", true);	
	}
}
