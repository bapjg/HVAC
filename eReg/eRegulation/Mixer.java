package eRegulation;

// Notes
// =====
// For small mixer valve openings (ie swing proportion close to zero (ie cold)
// the circuit pump works a lot in closed loop
// Hence, time required for BoilerOut (or MixerHot) to increase upto Boiler Temp
// is much longer than with much larger valve openings

public class Mixer
{
	public String 			name;
	public Integer 			swingTime 				= 90;
	public Integer 			lagTime 				= 30;

	public Integer 			tempMax 				= 480;	
	public Integer 			tempDontMove			= 20;
	public Integer 			positionTracked			= 0;			//This is the position expressed in milliseconds swinging from cold towards hot

	public Float			gainP					= 0F;
	public Float			gainD					= 0F;
	public Float			gainI					= 0F;
	public Float			timeD					= 0F;
	public Float			timeI					= 0F;
	
	public PID				pidControler;
	public Integer			state					= 0;
	public static final int	MIXER_STATE_Off 		= 0;
	public static final int	MIXER_STATE_Moving_Up	= 1;
	public static final int	MIXER_STATE_Moving_Down	= -1;
	public Long				timeToStop;

 	public Mixer
		(
			String name, 
			String swingTime, 
			String lagTime, 
			String gainP, 
			String timeD, 
			String timeI, 
			String gainI
		)
    {
		this.name 									= name;
		this.swingTime								= Integer.parseInt(swingTime);
		this.lagTime								= Integer.parseInt(lagTime);
		this.pidControler							= new PID(10);
		this.gainP									= Float.parseFloat(gainP);
		this.timeD									= Float.parseFloat(timeD);
		this.gainD									= this.gainP * this.timeD;
		this.timeI									= Float.parseFloat(timeI);
		this.gainI									= Float.parseFloat(gainI);
		this.state									= MIXER_STATE_Off;
	}
	public void sequencer(Integer targetTemp)
	{
		// Keep it simple :
		//
		// Full span is approx 60 degrees - 20 degrees = 40 degrees
		// Full span takes approx                      = 100 seconds 
		// 
		// ie            40 degrees = 400 (on reading) = 100s
		// or                           4 off .reading = 1 second
		// or                           1 off .reading = 250 ms
		// 
		// Simply measure the difference between wanted temperature and mixerOut
		// Multiply by a coefficient (250ms/decimal degree to start with) and see how it goes
		//
		allOff();
		
		
		// Koeff at 250 ok for low boiler temp circa 30
		// when over 45, had a lot of overshoot
		// Koeff at 200 still lots of overshoot
		// Integer Koeff								= 100;									// ms per decimal degree
		
		// An intial workout of K, Kd & Ki gave following results :
		// K	= 6.2 for a tempchange of 50 degrees = 62 decidegrees
		// Kd   = 1.2 degrees/min                    = 0.2 decidegrees/s
		// Ki   = 0
		// PID controler give the result in seconds
		// These params gave oscilations
		// Changed Kd = 0.02
		
		
		pidControler.target								= targetTemp;		// targetTemp is either tempGradient or some maxTemp for rampup

		Integer tempFloorOut							= Global.thermoFloorOut.readUnCached();
		
		Integer swingTimeRequired						= pidControler.getGain(gainP, gainD, gainI); 					// returns a swingTime in milliseconds
		
		Integer swingTimePerformed						= 0;

		if (tempFloorOut > 450)
		{
			// We need to do trip avoidance
			if (swingTimeRequired < 0)
			{
				swingTimeRequired						= swingTimeRequired * 2;
			}
			LogIt.display("Mixer", "sequencer", "Trip situation detected. Calculated swingTimeRequired : " + swingTimeRequired);
		}
		if (tempFloorOut > 500)
		{
			// We need to do trip avoidance
			LogIt.display("Mixer", "sequencer", "Have definately tripped. Temp MixerOut : " + Global.thermoFloorOut.reading);
		}
		
		if (Math.abs(swingTimeRequired) < 500)												// Less than half a second
		{
			// Do nothing to avoid contact bounce and relay problems
			swingTimePerformed							= 0;
		}
		else if (swingTimeRequired > 0)														// Moving hotter
		{
			if (positionTracked < this.swingTime * 1000)													// No point going over max
	 		{
				if (positionTracked + swingTimeRequired > this.swingTime * 1000)
		 		{
		 			swingTimeRequired 					= this.swingTime * 1000 - positionTracked + 2000;	//No point waiting over maximum add extra 2 seconds to be sure of end point
		 		}
		 		Global.mixerUp.on();
				Long timeStart							= Global.now();
				Global.waitMilliSeconds(swingTimeRequired.intValue());
				Global.mixerUp.off();
				Long timeEnd							= Global.now();
				Long positionDiff						= timeEnd - timeStart;
		 		swingTimePerformed   					= positionDiff.intValue();
	 		}
		}
		else																			// Moving colder
		{
			if (positionTracked > 0)													// No point going under min
	 		{
				if (positionTracked + swingTimeRequired < 0)
		 		{
		 			swingTimeRequired 					= - (positionTracked + 2000);		//No point waiting under minimum add extra 2 seconds to be sure of end point
		 		}
				if (positionTracked > swingTime * 900)										// We are over 90%, last 10% swing gives no change
		 		{
		 			swingTimeRequired 					= swingTimeRequired - (positionTracked - swingTime * 900);	//Bring it down to 10% and then start the motion
		 		}
				Global.mixerDown.on();
				Long timeStart							= Global.now();
				Global.waitMilliSeconds(Math.abs(swingTimeRequired));
				Global.mixerDown.off();
				Long timeEnd							= Global.now();
				Long positionDiff						= timeEnd - timeStart;
		 		swingTimePerformed   					= - positionDiff.intValue();
	 		}
		}
		positionTracked									= positionTracked + swingTimePerformed;
		if (positionTracked > this.swingTime * 1000)
		{
			positionTracked			 					= this.swingTime * 1000;
		}
		else if (positionTracked < 0)
		{
			positionTracked 							= 0;
		}
	}
	public void positionZero()
	{
		allOff();
		Global.mixerDown.on();
		Global.waitMilliSeconds(1000 * swingTime);
		Global.mixerDown.off();
		positionTracked									= 0;
	}
	public void positionFull()
	{
		allOff();
		Global.mixerUp.on();
		Global.waitMilliSeconds(1000 * swingTime);
		Global.mixerUp.off();
		positionTracked									= swingTime * 1000;
	}
	public void positionAbsolute(float proportion)
	{
		Long 											positionDiff;
		Float 											timeToWait;
		Long											timeStart;
		Long											timeEnd;
		if (proportion > 0.5F)
		{
			positionFull();
			Global.mixerDown.on();
			timeToWait									= 1000 * swingTime * (1F - proportion);
			timeStart									= Global.now();
			Global.waitMilliSeconds(timeToWait.intValue());
			Global.mixerDown.off();
			timeEnd										= Global.now();
			positionDiff   								= timeEnd - timeStart;
	 		positionTracked								= 1000 * swingTime - positionDiff.intValue();		
		}
		else
		{
			positionZero();
			Global.mixerUp.on();
			timeToWait									= 1000 * swingTime * proportion;
			timeStart									= Global.now();
			Global.waitMilliSeconds(timeToWait.intValue());
			Global.mixerUp.off();
			timeEnd										= Global.now();
			positionDiff   								= timeEnd - timeStart;
	 		positionTracked								= positionDiff.intValue();		
		}
	}
	public void allOff()
	{
		Global.waitMilliSeconds(100);
		Global.mixerDown.off();
		Global.waitMilliSeconds(100);
		Global.mixerUp.off();
		Global.waitMilliSeconds(100);
	}
	public Boolean wait5secs()
	{
		try
        {
			Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
	        e.printStackTrace();
        }
		if (Global.thermoFloorOut.readUnCached() > 450)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
