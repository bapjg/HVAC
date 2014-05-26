package eRegulation;

import HVAC_Common.Ctrl_Configuration;

// Notes
// =====
// For small mixer valve openings (ie swing proportion close to zero (ie cold)
// the circuit pump works a lot in closed loop
// Hence, time required for BoilerOut (or MixerHot) to increase upto Boiler Temp
// is much longer than with much larger valve openings

public class Mixer
{
	public String 			name;
	public Integer 			swingTime 								= 90000;
	public Integer 			swingUsableMax							= 90000;
	public Integer 			swingUsableMin							= 0;
	public Integer 			timeDelay 								= 30000;
	public Integer 			timeProjection							= 30000;
	public Integer 			marginProjection						= 2000;

	public Integer 			tempMax 								= 48000;	
	public Integer 			tempDontMove							= 20;
	public Integer 			positionTracked							= 0;			//This is the position expressed in milliseconds swinging from cold towards hot
	public Integer			swingTimeRequired						= 0;
	
	public Float			gainP									= 0F;
	public Float			gainD									= 0F;
	public Float			gainI									= 0F;
	public Float			timeD									= 0F;
	public Float			timeI									= 0F;
	
	public Relay			mixerUp;
	public Relay			mixerDown;
	
	
	public Integer			state									= 0;
	public static final int	MIXER_STATE_Off 						= 0;
	public static final int	MIXER_STATE_Normal_Operating			= 1;
	public static final int	MIXER_STATE_Moving_Up					= 2;
	public static final int	MIXER_STATE_Moving_Down					= 3;
	public static final int	MIXER_STATE_Moving_Waint				= 4;
	public static final int	MIXER_STATE_Moving_OverTemp_Recovery	= 5;
	public static final int	MIXER_STATE_Moving_Idle					= 6;

	public Long				timeToStop;

 	public Mixer(Ctrl_Configuration.Mixer			paramMixer)
    {
		this.name 									= paramMixer.name;
		this.swingTime								= paramMixer.swingTime;
		this.swingUsableMax							= this.swingTime * 90 /100;
		this.swingUsableMin							= this.swingTime * 30 /100;
		this.timeDelay								= paramMixer.pidParams.timeDelay;
		this.timeProjection							= paramMixer.pidParams.timeProjection;
		this.marginProjection						= paramMixer.pidParams.marginProjection;
		this.gainP									= paramMixer.pidParams.gainP;
		this.timeD									= paramMixer.pidParams.timeD;
		this.gainD									= this.gainP * this.timeD;
		this.timeI									= paramMixer.pidParams.timeI;
		this.gainI									= paramMixer.pidParams.gainI;
		
		this.mixerUp								= Global.relays.fetchRelay(paramMixer.relayUp);
		this.mixerDown								= Global.relays.fetchRelay(paramMixer.relayDown);
		
		if ((this.mixerUp == null) || (this.mixerDown == null))
		{
			System.out.println("Mixer.Contructor : Unknown mixer relay");
		}
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
		PID pidControler								= Global.thermoFloorOut.pidControler;
		MixerMove_Report report;
		
		
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
		
		swingTimeRequired								= pidControler.getGain(gainP, gainD, gainI); 					// returns a swingTime in milliseconds
		if (tempFloorOut > 50000)
		{
			LogIt.display("Mixer", "sequencer", "Have definately tripped. Temp MixerOut : " + Global.thermoFloorOut.reading);
		}
		else if (tempFloorOut > 45000)
		{
			// We need to do trip avoidance
			if (swingTimeRequired < 0)
			{
				swingTimeRequired						= swingTimeRequired * 2;		// i.e. double the gain factor kp
			}
			else
			{
				LogIt.display("Mixer", "sequencer", "Trip situation detected. Calculated swingTimeRequired : " + swingTimeRequired);
			}
		}
		
		if (Math.abs(swingTimeRequired) > 500)												// Less than half a second
		{
			Integer positionProjected					= positionTracked + swingTimeRequired;

			if (swingTimeRequired > 0)								// Moving hotter
			{
				if (positionProjected > this.swingTime)													// Should never happen as algorith maintains with usable range
		 		{
		 			swingTimeRequired 					= this.swingTime - positionTracked + 2000;		//No point waiting over maximum add extra 2 seconds to be sure of end point
					report								= mixerMoveUp(swingTimeRequired);
					positionTracked						= this.swingTime;					
		 		}
				else																					// Normal operating
				{
					report								= mixerMoveUp(swingTimeRequired);
					positionTracked						= report.positionTracked;
				}
			}
			else													// Moving colder
			{
				if (positionProjected < 0)																// Should never happen
		 		{
		 			swingTimeRequired 					= - (positionTracked + 2000);					//Add extra 2 seconds to be sure of end point
					report								= mixerMoveDown(swingTimeRequired);
					positionTracked						= 0;					
		 		}
				else																					// Normal operating
				{
					report								= mixerMoveDown(swingTimeRequired);
					positionTracked						= report.positionTracked;
				}
			}
		}
		else
		{
			// Less that 500 ms. Do nought
		}
	}
	public void positionZero()
	{
		if ((positionTracked != null) && (positionTracked != 0))
		{
			allOff();
			mixerDown.on();
			Global.waitMilliSeconds(positionTracked + 2000);					// Add 2 extra seconds to be certain
			mixerDown.off();
			positionTracked								= 0;
		}
		else
		{
			allOff();
			mixerDown.on();
			Global.waitMilliSeconds(swingTime + 2000);
			mixerDown.off();
			positionTracked								= 0;
		}
	}
	public void positionFull()
	{
		if ((positionTracked != null) && (positionTracked != swingTime))
		{
			allOff();
			mixerUp.on();
			Global.waitMilliSeconds(swingTime - positionTracked + 2000);		// Add 2 extra seconds to be certain
			mixerUp.off();
			positionTracked								= swingTime;
		}
		else
		{
			allOff();
			mixerUp.on();
			Global.waitMilliSeconds(swingTime + 2000);
			mixerUp.off();
			positionTracked								= 0;
		}
	}
	public void positionAbsolute(Integer position)
	{
		Long 											positionDiff;
		Integer											timeToWait;
		Long											timeStart;
		Long											timeEnd;
		if (positionTracked == null)
		{
			positionZero();
		}
		if (position > swingTime / 2)
		{
			positionFull();
			mixerDown.on();
			timeToWait									= swingTime - position;
			timeStart									= Global.now();
			Global.waitMilliSeconds(timeToWait);
			mixerDown.off();
			timeEnd										= Global.now();
			positionDiff   								= timeEnd - timeStart;
	 		positionTracked								= swingTime - positionDiff.intValue();		
		}
		else
		{
			positionZero();
			mixerUp.on();
			timeToWait									= position;
			timeStart									= Global.now();
			Global.waitMilliSeconds(timeToWait.intValue());
			mixerUp.off();
			timeEnd										= Global.now();
			positionDiff   								= timeEnd - timeStart;
	 		positionTracked								= positionDiff.intValue();		
		}
	}
	public void allOff()
	{
		Global.waitMilliSeconds(100);
		mixerDown.off();
		Global.waitMilliSeconds(100);
		mixerUp.off();
		Global.waitMilliSeconds(100);
	}
	private MixerMove_Report mixerMoveUp(Integer swingTimeRequired)
	{
		Long				positionChange				= 0L;
		MixerMove_Report	report						= new MixerMove_Report();
		
		mixerUp.on();
		report.timeStart								= Global.now();
		Global.waitMilliSeconds(swingTimeRequired);
		mixerUp.off();
		report.timeEnd									= Global.now();
		positionChange									= report.timeEnd - report.timeStart;		// Positive number as moved up
		report.swingTimePerformed						= positionChange.intValue();
		report.positionTracked							= positionTracked + report.swingTimePerformed;
		if (report.positionTracked > swingTime)
		{
			report.positionTracked 						= swingTime;
		}
		return report;
	}
	private MixerMove_Report mixerMoveDown(Integer swingTimeRequired)
	{
		Long				positionChange				= 0L;
		MixerMove_Report	report						= new MixerMove_Report();
		
		mixerDown.on();
		report.timeStart								= Global.now();
		Global.waitMilliSeconds(-swingTimeRequired);
		mixerDown.off();
		report.timeEnd									= Global.now();
		positionChange									= report.timeStart - report.timeEnd;		// Negative number as moved down
		report.swingTimePerformed						= positionChange.intValue();
		report.positionTracked							= positionTracked + report.swingTimePerformed;
		if (report.positionTracked < 0)
		{
			report.positionTracked 						= 0;
		}
		return report;
	}
	private class MixerMove_Report
	{
		public	Long		timeStart;
		public	Long		timeEnd;
		public	Integer		swingTimePerformed;
		public 	Integer		positionTracked;
	}
}
