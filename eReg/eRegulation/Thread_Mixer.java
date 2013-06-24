package eRegulation;

public class Thread_Mixer implements Runnable
{
	public Mixer		mixer;
	public Circuit_Mixer	circuitMixer;
	public Boolean 		stopNow;
	
	public Thread_Mixer(Mixer mixer, Circuit_Mixer circuitMixer)
	{
		this.mixer 				= mixer;
		this.circuitMixer 		= circuitMixer;
	}
	public void run()
	{
		this.stopNow = false;
		
		LogIt.info("Thread_Mixer", "Run", "Floor Starting");		

		mixer.positionAbsolute(0.10F);
		
		Global.waitSeconds(60);
//		try
//        {
//			LogIt.info("===========================Thread_Mixer", "Run", "Floor waiting for 1min to let temps settle down");		
//	        Thread.sleep(1000 * 60);
//        }
//        catch (InterruptedException e)
//        {
//	        e.printStackTrace();
//        }
		Global.pumpFloor.on();
		
		LogIt.info("Thread_Mixer", "Run", "Floor Initialised (10%)");		
		
		while ((!Global.stopNow) && (!this.stopNow))
		{
			
			/* 			
				states can be 
					- rampUp	
					- rampStabalising
					- rampStablised
				
				if (tempTarget - tempLivingRoom > 20)
				{
					state = STATE_RampUp
				}
				else if (abs(tempTarget - tempLivingRoom) > 10)
				{
					state = STATE_rampStabalising
				}
				else if (abs(tempTarget - tempLivingRoom) < 10)
				{
					state = STATE_rampStabale
				}
				else 
				{
					state = STATE_rampError
				}
				
				Initially rampUp	
				When tempLivingRoom within 2 degrees of desired state := rampStabiling
				When tempLivingRoom within 1 degree  of desired state := rampStabile
				
				switch (state)
				{
				case rampUp:
					mixer.positionAtTemp(maxTemp);
					break;
				case rampStabalising:
					mixer.positionAtTemp(gradient);
					break;
				case tampStabale:
					Need statistics on Out/In/Ciruit temps
					mixer.positionAtTemp(gradient + correction);
					break;
				case STATE_rampError:
					LogIt.error("Thread_Mixer", "Main Loop", "STATE_rampError detected");
				default:
					LogIt.error("Thread_Mixer", "Main Loop", "Unknown state detected : " + state);
				}	
			*/			
			// Note that Mixer calls go to sleep when positionning the mixer.
			
			// Notes
			// Try introducing PID (especially Differential
			// If dT/dt is of wrong sign then we are overshooting
			// We could try Koeff = K/tempMixHot (higher Koeff for lower temps)
			// MixCold > 25 degrees indicates trip, could try sitching on radiators to induce water flow, but would need to put Mix to Hot for the duration
			// perhaps not that feasible
			
			
			Integer targetTemp						= circuitMixer.temperatureGradient.getTempToTarget();
			LogIt.tempData();		
			
			this.mixer.pidSimple(targetTemp);

			Global.waitSeconds(20);

//			try
//			{
//				Thread.sleep(1000 * 20);
//			}
//			catch (InterruptedException e)
//			{
//				e.printStackTrace();
//			}
		}
		LogIt.info("===========================Thread_Mixer", "Run", "Floor Thread ending");	
	}
}
