package eRegulation;

public class Thread_Mixer implements Runnable
{
	public Mixer				mixer;
	public Circuit_Mixer		circuitMixer;
	
	public Thread_Mixer(Mixer mixer, Circuit_Mixer circuitMixer)
	{
		this.mixer 				= mixer;		// object Global.mixer
		this.circuitMixer 		= circuitMixer; // object Circuit_Mixer
	}
	public void run()
	{
		LogIt.info("Thread_Mixer", "Run", "Floor Starting", true);		

		if (Global.thermoBoiler.reading > 350)
		{
			mixer.positionAbsolute(0.10F);
			LogIt.info("Thread_Mixer", "Run", "Floor Initialised (10%)");		
		}
		else
		{
			mixer.positionFull();
			LogIt.info("Thread_Mixer", "Run", "Floor Initialised Full (100%)");		
		}			
		
		Global.waitSeconds(1);
		Global.pumpFloor.on();
		Global.waitSeconds(10);
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
			if (circuitMixer.state == circuitMixer.CIRCUIT_STATE_RampingUp) // This is to accelerate rampup
			{
				targetTemp						= 430;						// Trip avoidance kicks in at 450
			}
			else
			{
				targetTemp						= circuitMixer.temperatureGradient.getTempToTarget();
			}
			this.mixer.sequencer(targetTemp);

			for (i = 0; i < 4; i++)
			{
				Global.waitSeconds(5);									// 4 loops of 5s

				if ((Global.thermoFloorOut.readUnCached() > 490) || (!Global.stopNow))
				{
					System.out.println(LogIt.dateTimeStamp() + "Thread_Mixer/mainLoop : Interrupting the 20s wait");
					break;
				}
			}
		}
		// Optimise if singlecircuit
		Global.pumpFloor.off();
		LogIt.info("Thread_Mixer", "Run", "Floor Thread ending", true);	
	}
}
