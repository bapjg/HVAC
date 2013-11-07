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
	
//	public Integer			state					= 0;
	
//	public final int 		STATE_Stopped 			= 0;
//	public final int 		STATE_Moving	 		= 1;
//	public final int 		STATE_AwaitingTemp 		= 2;
       
 	
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
	}
	public Float getSwingProportion(Integer temperature)
	{
		// Returns proportion of swingTime starting from zero
		// required to achieve a target temperature
		// tempDelta = difference between requested temp and cold input to the mixer
		// tempSpan  = difference between hot temp and cold input to the mixer
		Integer tempMixerInCold						= Global.thermoFloorCold.reading;
		Integer tempMixerInHot						= Global.thermoFloorHot.reading;
		Integer tempMixerInVeryHot					= Global.thermoBoiler.reading;
		
//		LogIt.info("Mixer","getSwingProportion", "Floor tempMixerInCold    : " + tempMixerInCold);
//		LogIt.info("Mixer","getSwingProportion", "Floor tempMixerInHot     : " + tempMixerInHot);
//		LogIt.info("Mixer","getSwingProportion", "Floor tempMixerInVeryHot : " + tempMixerInVeryHot);
//		LogIt.info("Mixer","getSwingProportion", "Floor target temperature : " + temperature);
		
		LogIt.tempData();
		
		Integer tempDelta 							= temperature    - tempMixerInCold;
		Integer tempSpan 							= tempMixerInHot - tempMixerInCold;
		//if (tempSpan < 30 )
		//{
	        // Temp difference is less than 3 degrees : there's no knowing where it is
			// Use the boiler temp as a better indication
			// Not a good idea, as need to openup the valve and get water flowing
			
			// tempSpan 								= tempVeryHot - Global.thermoFloorIn.reading;
        //}
		
		LogIt.info("Mixer","getSwingProportion", "Floor tempDelta : " + temperature    + " - " + tempMixerInCold + " = " + tempDelta);
		LogIt.info("Mixer","getSwingProportion", "Floor tempSpan  : " + tempMixerInHot + " - " + tempMixerInCold + " = " + tempSpan);
		
		if (tempSpan < 0 )
		{
			LogIt.info("-------====---------Mixer","getSwingProportion", "Floor tempSpan negative " + tempSpan);
	        // Temp out is colder than tempCold : we cannot tell where we are
			// Wait for water to flow to get things changing
			return  -1F;
        }

		if (tempDelta > tempSpan )
		{
			LogIt.info("Mixer","getSwingProportion", "Floor tempDelta > tempSpan " + tempSpan);
	        // Temp out is colder than tempCold : we cannot tell where we are
			// Wait for water to flow to get things changing
			return  1F;
        }

		LogIt.info("Mixer","getSwingProportion", "Floor getSwingProportion " + (Float) tempDelta.floatValue()/tempSpan.floatValue());
		
		return (Float) tempDelta.floatValue()/tempSpan.floatValue();
	}
	public void positionAtTemp(Integer targetTemp)
	{
		allOff();
		Float swingProportion 						= getSwingProportion(targetTemp);

		if (swingProportion == -1F)
		{
			// Things need to stablise This means that cold is warmer than hot
			// Just wait untill the pump does its work
			// LogIt.tempInfo("targetTemp : " + targetTemp + "swingProportion : " + swingProportion +  "positionTracked : " + 0);
			return;
		}
		if (swingProportion >= 1F)
		{
			// LogIt.info("Mixer","positionAtTemp", "Floor swingProportion >= 1 " + swingProportion);
			if (positionTracked != swingTime * 1000 )			//No need to spend a swingTime to get it where it already is
			{
				positionFull();
			}
			// LogIt.tempInfo("targetTemp : " + targetTemp + "swingProportion : " + swingProportion +  "positionTracked : " + 0);
			return;
		}

		// LogIt.tempInfo("targetTemp : " + targetTemp + "swingProportion : " + swingProportion +  "positionTracked : " + 0);

		
		swingProportion								= 1000.0F * swingProportion * swingTime.floatValue();	// Value is in milliseconds
 		Integer positionFuture						= swingProportion.intValue();							// Value is in milliseconds
 		Integer positionDiff						= 0;

		
		
		if (Math.abs(positionFuture - positionTracked) < 2000)					// A 2 percent adjustment is not worth while
		{
			// Avoid doing to many little movements. 
			// Should tempDontMove not be a time or proportion ??? xxxxx
			LogIt.info("Mixer","positionAtTemp", "Floor Small movement not actionned ");
			return;
		}
		if (positionFuture > positionTracked)
		{
			Global.mixerUp.on();
			positionDiff   							= waitAWhile(Math.abs(positionFuture - positionTracked));
		}
		else
		{
			Global.mixerDown.on();
			positionDiff   							= - waitAWhile(Math.abs(positionFuture - positionTracked));
		}
		positionTracked								= positionTracked + positionDiff;
//		LogIt.tempInfo("targetTemp : " + targetTemp + "swingProportion : " + swingProportion +  "positionTracked : " + 0);
	}
//	============================================================
//
//
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
		
		
		pidControler.target								= targetTemp;		// targetTemp is either gradient or some maxTemp for rampup
		
		// kP at 62F was too sluggish. Pushed it up to 100
		// gainI										= 0F;			// If dtI <> then = gainP/dtI;
		
		Double swingTimeRequired						= Math.floor(pidControler.getGain(gainP, gainD, gainI));
		Integer swingTimePerformed						= 0;

		if (Global.thermoFloorOut.readUnCached() > 450)
		{
			// We need to do trip avoidance
			LogIt.action("Mixer/sequencer", "Avoiding trip situation. Calculated swingTimeRequired : " + swingTimeRequired + " Forced/override to -20000 milliseconds");
			swingTimeRequired							= (double) -20000;
		}
		if (Global.thermoFloorOut.read() > 500)
		{
			// We need to do trip avoidance
			LogIt.action("Mixer/sequencer", "Have definately tripped. Temp MixerOut : " + Global.thermoFloorOut.reading);
		}
		
		if (Math.abs(swingTimeRequired) < 500)												// Less than half a second
		{
			// Do nothing to avoid contact bounce and relay problems
			swingTimePerformed							= 0;
		}
		else if (swingTimeRequired > 0)														// Moving hotter
		{
	 		if (positionTracked < 90000)													// No point going over max
	 		{
				if (positionTracked + swingTimeRequired > 90000)
		 		{
		 			swingTimeRequired 					= 90000F - positionTracked.doubleValue();		//No point waiting over maximum add an extra second to be sure of end point
		 		}
				
				LogIt.action("Mixer/sequencer", "Moving Hotter swingTimeRequired : " + swingTimeRequired + " positionTracked : " + positionTracked);
		 		Global.mixerUp.on();
		 		swingTimePerformed   					= waitAWhile(swingTimeRequired);
		 		Global.mixerUp.off();
	 		}
		}
		else																			// Moving colder
		{
			if (positionTracked > 0)													// No point going under min
	 		{
				if (positionTracked + swingTimeRequired < 0)
		 		{
		 			swingTimeRequired 					= positionTracked.doubleValue() + 1000f;		//No point waiting under minimum add an extra second to be sure of end point
		 		}
				LogIt.action("Mixer/sequencer", "Moving Colder swingTimeRequired : " + swingTimeRequired + "positionTracked : " + positionTracked);
				Global.mixerDown.on();
				swingTimePerformed   					= - waitAWhile(Math.abs(swingTimeRequired));
				Global.mixerDown.off();
	 		}
		}
		positionTracked									= positionTracked + swingTimePerformed;
		if (positionTracked > 90000)
		{
			positionTracked			 					= 90000;
		}
		else if (positionTracked < 0)
		{
			positionTracked 							= 0;
		}
		LogIt.action("Mixer/sequencer", "Moving ended positionTracked : " + positionTracked );
	}
//
//	============================================================


	public void adjustTemperature(Integer tempMixerOutTarget)
	{
		// Using similar traiangles, Formula is
		//
		// (mixerTempHot - mixerTempCold)       (mixerTempTarget - mixerTempOut)
		// ------------------------------   =   -------------------------------- 
		//           fullSwingTime                    requiredSwingTime
		//
		// or
		//
		//                                      (mixerTempTarget - mixerTempOut)
		//       requiredSwingTime          =   -------------------------------- x  fullSwingTime
		//                                       (mixerTempHot - mixerTempCold) 
		//
		// or
		//
		//                                      tempSpanRequired
		//       requiredSwingTime          =   ---------------- x  fullSwingTime
		//                                        tempSpanFull 
		//
		//
		
		allOff();
		
		Integer tempMixerInCold							= Global.thermoFloorCold.reading;
		Integer tempMixerInHot							= Global.thermoFloorHot.reading;
		Integer tempMixerInVeryHot						= Global.thermoBoiler.reading;
		Integer tempMixerOut							= Global.thermoFloorOut.reading;

		Integer tempSpanRequired						= tempMixerOutTarget   - tempMixerOut;
		Integer tempSpanFull							= tempMixerInHot       - tempMixerInCold;
		
		Float   swingProportion							= 0F;
		Float   swingTimeRequired						= 0F;
 		Integer positionMovement						= 0;
 		Integer positionMovementReal					= 0;
		
		if (tempSpanFull < 0)
		{
			// We will probably have to let things settle down
			// LogIt
			return;
		}
		
		if (tempMixerOutTarget > tempMixerInHot)
		{
			// This can happen if the circuit has been off for a long time
			// Could look at boiler temp and see if boiler is running to ensure 
			// against circuit pump tripping
			
			//Just log the fact and pump up the mixer to get hot water flowing
			// LogIt.tempInfo("Moving Hot due to target over inputHot positionAbsolute : 0.80 positionTracked : " + positionTracked );
	 		positionAbsolute(0.80F);
	 	// LogIt.tempInfo("Moving Hot due to target over inputHot ended positionAbsolute : 0.80 positionTracked : " + positionTracked );
		}
		else if (tempMixerOutTarget < tempMixerInCold)
		{
			System.out.println("tempMixerOutTarget < tempMixerInCold");
			// This can happen if the circuit has tripped 
			// The whole thing warms up and no temp is representative of anything
			// ToDo
			//  - Set the mixer to cold for 1 min
			//  - Set the mixer to 10% for 1 min
		}
		else
		{
			swingProportion								= tempSpanRequired.floatValue()/tempSpanFull.floatValue();
			
			swingTimeRequired							= 1000.0F * swingProportion * swingTime.floatValue();	// Value is in milliseconds
	 		positionMovement							= swingTimeRequired.intValue();							// Value is in milliseconds
	 		positionMovementReal						= 0;
			
	 	// LogIt.tempInfo("tempMixerOutTarget : " + tempMixerOutTarget + " swingProportion: " + swingProportion + " positionTracked : " + positionTracked );

			if (Math.abs(positionMovement) < 2000)					// A 2 percent adjustment is not worth while
			{
				// Avoid doing to many little movements. 
				// Should tempDontMove not be a time or proportion ??? xxxxx
				LogIt.info("Mixer","positionAtTemp", "Floor Small movement not actionned ");
				// LogIt.tempInfo("Not Moving" + " swingProportion : " + swingProportion + " positionTracked : " + positionTracked );
				return;
			}
			if (positionMovement > 0)
			{
				// LogIt.tempInfo("Moving Hotter" + " swingProportion : " + swingProportion + " positionTracked : " + positionTracked );
		 		Global.mixerUp.on();
				positionMovementReal   					= waitAWhile(Math.abs(positionMovement));
		 		Global.mixerUp.off();
		 	// LogIt.tempInfo("Moving Hotter ended" + " swingProportion : " + swingProportion + " positionTracked : " + positionTracked );
			}
			else
			{
				// LogIt.tempInfo("Moving Colder" + " swingProportion : " + swingProportion + " positionTracked : " + positionTracked );
				Global.mixerDown.on();
				positionMovementReal   					= - waitAWhile(Math.abs(positionMovement));
				Global.mixerDown.off();
				// LogIt.tempInfo("Moving Colder ended" + " swingProportion : " + swingProportion + " positionTracked : " + positionTracked );
			}
			positionTracked								= positionTracked + positionMovementReal;
			if (positionTracked > 90000)
			{
				positionTracked = 90000;
			}
			else if (positionTracked < 0)
			{
				positionTracked = 0;
			}
			// LogIt.tempInfo("tempMixerOutTarget : " + tempMixerOutTarget + " swingProportion : " + swingProportion + " positionTracked : " + positionTracked );
		}
	}
	public void positionZero()
	{
		/*
			Routine to send mixer to "Cold" position
			Parameters :
			Input   : none
			Returns : none
			Static  : positionTracked - Set to zero
		*/
		allOff();
		Global.mixerDown.on();
		waitAWhile(1000 * swingTime);
		Global.mixerDown.off();
		waitAWhile(400);
		positionTracked									= 0;
//		LogIt.mixerData("positionZero / Floor positionTracked " + positionTracked);
	}
	public void positionFull()
	{
		/*
			Routine to send mixer to "Cold" position
			Parameters :
			Input   : none
			Returns : none
			Static  : positionTracked - Set to zero
		*/
		allOff();
		Global.mixerUp.on();
		waitAWhile(1000 * swingTime);
		positionTracked									= swingTime * 1000;
//		LogIt.mixerData("positionFull / Floor positionTracked " + positionTracked);
	}
	public void positionAbsolute(float proportion)
	{
		positionZero();
		Global.mixerUp.on();
		Float timeToWait							= 1000 * swingTime * proportion;
	    Integer positionDiff   						= waitAWhile(timeToWait.intValue());
		Global.mixerUp.off();
 		positionTracked								= positionDiff;		
//		LogIt.mixerData("positionAbsolute / Floor positionTracked " + positionTracked);
	}
	public void allOff()
	{
		waitAWhile(0.1F);
		Global.mixerDown.off();
		waitAWhile(0.1F);
		Global.mixerUp.off();
		waitAWhile(0.1F);
	}
	public Integer waitAWhile(float timeToWait)
	{
		/*
			Routine to wait a number of seconds
			Parameters :
			Input   : Integer timeToWait - Number of seconds to wait
			Returns : Integer            - Number of milliseconds waited
		*/
		Long timeStart 								= 0L;
		Long timeEnd 								= 0L;
		Long timeWaited								= 0L;
		try
        {
	        timeStart 								= System.currentTimeMillis();
			Thread.sleep((long) timeToWait);
			timeEnd	 								= System.currentTimeMillis();
        }
        catch (InterruptedException e)
        {
	        e.printStackTrace();
        }
		timeWaited									= timeEnd - timeStart;
		return timeWaited.intValue();
	}
	public Integer waitAWhile(double timeToWait)
	{
		/*
			Routine to wait a number of seconds
			Parameters :
			Input   : Integer timeToWait - Number of seconds to wait
			Returns : Integer            - Number of milliseconds waited
		*/
		Long timeStart 								= 0L;
		Long timeEnd 								= 0L;
		Long timeWaited								= 0L;
		try
        {
	        timeStart 								= System.currentTimeMillis();
			Thread.sleep((long) timeToWait);
			timeEnd	 								= System.currentTimeMillis();
        }
        catch (InterruptedException e)
        {
	        e.printStackTrace();
        }
		timeWaited									= timeEnd - timeStart;
		return timeWaited.intValue();
	}
}
