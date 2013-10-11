package eRegulation;

public class Thread_Mixer implements Runnable
{
	public Mixer				mixer;
	public Circuit_Mixer		circuitMixer;
//	public Boolean 				stopNow;
	
	public Thread_Mixer(Mixer mixer, Circuit_Mixer circuitMixer)
	{
		this.mixer 				= mixer;
		this.circuitMixer 		= circuitMixer;
	}
	public void run()
	{
//		this.stopNow = false;
		
		LogIt.info("Thread_Mixer", "Run", "Floor Starting", true);		

		mixer.positionAbsolute(0.10F);
		
		Global.waitSeconds(60);
		LogIt.action("PumpFloor", "On");
		Global.pumpFloor.on();
		
		LogIt.info("Thread_Mixer", "Run", "Floor Initialised (10%)");		
		
//		while ((!Global.stopNow) && (!this.stopNow))
		while (!Global.stopNow)
		{
			// Note that Mixer calls go to sleep when positionning the mixer.
			
			// Notes
			// Try introducing PID (especially Differential
			// If dT/dt is of wrong sign then we are overshooting
			// We could try Koeff = K/tempMixHot (higher Koeff for lower temps)
			// MixCold > 25 degrees indicates trip, could try sitching on radiators to induce water flow, but would need to put Mix to Hot for the duration
			// perhaps not that feasible
			
			
			Integer targetTemp						= circuitMixer.temperatureGradient.getTempToTarget();
			//LogIt.tempData();		not needed as done in Thread thermometers
			
			this.mixer.pidSimple(targetTemp);

			Global.waitSeconds(20);
		}
		LogIt.info("Thread_Mixer", "Run", "Floor Thread ending", true);	
	}
}
