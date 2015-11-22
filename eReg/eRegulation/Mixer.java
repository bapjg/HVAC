package eRegulation;

import HVAC_Common.CIRCUIT;
import HVAC_Common.Ctrl_Configuration;
import HVAC_Common.Rpt_PID;
import HVAC_Common.Rpt_PID.Update;

// Notes
// =====
// For small mixer valve openings (ie swing proportion close to zero (ie cold)
// the circuit pump works a lot in closed loop
// Hence, time required for BoilerOut (or MixerHot) to increase upto Boiler Temp
// is much longer than with much larger valve openings

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Mixer
{
	public String 												name;
	public Integer 												swingTime 					= 90000;
	public Integer 												swingUsableMax				= 90000;
	public Integer 												swingUsableMin				= 0;
	public Integer 												timeDelay 					= 30;
	public Integer 												timeProjection				= 30;
	public Integer 												marginProjection			= 2;

	public Integer 												tempMax 					= 48000;	
	public Integer 												tempDontMove				= 20;
	public Integer 												positionTracked				= null;			//This is the position expressed in milliseconds swinging from cold towards hot
	public Integer												swingTimeRequired			= 0;
	public Integer												safeSingleCircuitPosition	= 23 * 1000;	// Was 26 * 1000
	public Integer												safeDoubleCircuitPosition	= 40 * 1000;	
	
	public Float												gainP						= 0F;
	public Float												gainD						= 0F;
	public Float												gainI						= 0F;
	public Float												timeD						= 0F;
	public Float												timeI						= 0F;
										
	public Relay												mixerUp;
	public Relay												mixerDown;
	public Long													timeToStop;
	
	public float												lastBoilerDTdt				= 0;
	public HVAC_STATES.BoilerTemperatureVariation				boilerTemperatureVariation	= HVAC_STATES.BoilerTemperatureVariation.NormalOperating;

 	public Mixer(Ctrl_Configuration.Mixer			paramMixer)
    {
		this.name 																			= paramMixer.name;
		this.swingTime																		= paramMixer.swingTime					* 1000;
		this.positionTracked 																= paramMixer.swingTime					* 1000;
		this.swingUsableMin																	= paramMixer.swingProportionMin * this.swingTime / 100;
		this.swingUsableMax																	= paramMixer.swingProportionMax * this.swingTime / 100;
		this.timeDelay																		= paramMixer.pidParams.timeDelay        * 1000;			// Convert to milliSeconds
		this.timeProjection																	= paramMixer.pidParams.timeProjection   * 1000;			// Convert to milliSeconds
		this.marginProjection																= paramMixer.pidParams.marginProjection * 1000;			// Convert to milliDegrees
		this.gainP																			= paramMixer.pidParams.gainP;
		this.timeD																			= paramMixer.pidParams.timeD;
		this.gainD																			= this.gainP * this.timeD;
		this.timeI																			= paramMixer.pidParams.timeI;
		this.positionTracked 																= paramMixer.swingTime					* 1000;
		if (this.timeI == 0)
		{
			this.gainI																		= 0F;
		}
		else
		{
			this.gainI																		= this.gainP / this.timeI / 1000F; // timeI is in seconds
		}
		this.gainI																			= paramMixer.pidParams.gainI;
												
		this.mixerUp																		= Global.relays.fetchRelay(paramMixer.relayUp);
		this.mixerDown																		= Global.relays.fetchRelay(paramMixer.relayDown);
		
		if ((this.mixerUp == null) || (this.mixerDown == null))
		{
			System.out.println("Mixer.Contructor : Unknown mixer relay");
		}
//		this.state set to Off in Thread_Mixer initialisation																			= STATES.Mixer.Off;
	}
	public void positionAtTemperatureAndWait(Integer targetTemp)
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
		if (positionTracked == null)				return;			// Mixer has not yet been positionned
		try
		{
			allOff();
			PID 												pidFloorOut					= Global.thermoFloorOut.pidControler;
			PID 												pidBurner					= Global.thermoBoiler.pidControler;
			MixerMove_Report 									report;
			
			
			// Koeff at 250 ok for low boiler temp circa 30
			// when over 45, had a lot of overshoot
			// Koeff at 200 still lots of overshoot
			// Integer Koeff								= 100;							// ms per decimal degree
			
			// An intial workout of K, Kd & Ki gave following results :
			// K	= 6.2 for a tempchange of 50 degrees = 62 decidegrees
			// Kd   = 1.2 degrees/min                    = 0.2 decidegrees/s
			// Ki   = 0
			// PID controler give the result in seconds
			// These params gave oscilations
			// Changed Kd = 0.02
			
			pidFloorOut.target																= targetTemp;		// targetTemp is either tempGradient or some maxTemp for rampup
	
			Integer 											tempFloorOut				= Global.thermoFloorOut.readUnCached();
			float												thisBoilerDTdt				= pidBurner.dTdt();
		
			if ((lastBoilerDTdt < 0) && (thisBoilerDTdt > 0))								// boiler was cooling, now heating
			{
				// Have reached minimum, boilerTemp will now increase
				
				// 50% : Too much as it takes the rest of the cycle to catch up and often overshoots
				// 10% : Try it
				// 30% : Still overshoot with use of burnerPid unadjusted for position.
				// Try 50% again with position adjusted burnerPid
//			Float												swingTimeRequiredFloat		= positionTracked.floatValue() * 0.30F;
//			swingTimeRequired																= - swingTimeRequiredFloat.intValue();
			
			
				if (Global.circuits.isSingleActiveCircuit())	swingTimeRequired			= safeSingleCircuitPosition - positionTracked;				// Gives negative number
				else											swingTimeRequired			= safeDoubleCircuitPosition - positionTracked;				// Gives negative number
				boilerTemperatureVariation													= HVAC_STATES.BoilerTemperatureVariation.MinReached;
				if (swingTimeRequired > 0)			// This can happen at startup, or if positionTracked is slightly below safeCircuitPosition
				{
					if (positionTracked != 0)
					{	
						swingTimeRequired													= 0;
					}
				}
			}
			else if ((lastBoilerDTdt > 0) && (thisBoilerDTdt < 0))							// boiler was heating, now cooling
			{
				swingTimeRequired															= 0;
				boilerTemperatureVariation													= HVAC_STATES.BoilerTemperatureVariation.MaxReached;
			}
			else
			{
				swingTimeRequired															= 0;
			}
			if (thisBoilerDTdt != 0)							lastBoilerDTdt				= thisBoilerDTdt;
			
			if (swingTimeRequired == 0)
			{
				Float											swingProportion				= positionTracked.floatValue()/swingTime.floatValue();
				
				Integer											swingTimeMixerP				= pidFloorOut.getGainP(gainP);
				Integer											swingTimeMixerD				= pidFloorOut.getGainD(gainD * 0.0F);
				Integer											swingTimeBurnerD			= pidFloorOut.getGainD(gainD * 1.0F * swingProportion, 2);		// Uses average over 4 readings rather than 2
			
				swingTimeRequired															= swingTimeMixerP + swingTimeMixerD + swingTimeBurnerD + 500;	// Add 500ms
			}

			if (tempFloorOut > 50000)
			{
			}
			else if (tempFloorOut > 45000)
			{
				if (swingTimeRequired > 0)
				{
					swingTimeRequired															= 0;
				}
			}
		
			if (Math.abs(swingTimeRequired) > 500)											// Less than half a second
			{
				Integer 										positionProjected			= positionTracked + swingTimeRequired;
				Rpt_PID.Update									messageBefore				= (new Rpt_PID()).new Update();
				
				messageBefore.target														= targetTemp;
				messageBefore.tempCurrent													= pidFloorOut.tempCurrent();
				messageBefore.tempCurrentError												= pidFloorOut.tempCurrentError();
				
				messageBefore.termProportional												= - pidFloorOut.getGainP(1F);
				messageBefore.termDifferential												= - pidFloorOut.getGainD(1F);
				messageBefore.termIntegral													= - pidFloorOut.getGainI(1F);
	
				messageBefore.gainProportional												= pidFloorOut.getGainP(gainP);
				messageBefore.gainDifferential												= pidFloorOut.getGainD(gainD);
				messageBefore.gainIntegral													= pidFloorOut.getGainI(gainI);
				
				messageBefore.kP															= gainP;
				messageBefore.kD															= gainD;
				messageBefore.kI															= gainI;
				
				messageBefore.gainTotal														= swingTimeRequired;
				messageBefore.tempOut														= Global.thermoFloorOut.reading;
				messageBefore.tempBoiler													= Global.thermoBoiler.reading;
				
				messageBefore.positionTracked												= positionTracked;
				messageBefore.startMovement													= true;
			
				switch (boilerTemperatureVariation)
				{
				case MinReached:															// This is to inhibit mixer moving hotter until warmer boiler water has filtered through
					if (pidFloorOut.dTdt() > 0F)											// BoilerWarming has reached floorOut which is now warming
					{
						boilerTemperatureVariation 											= HVAC_STATES.BoilerTemperatureVariation.NormalOperating;		
					}
					else																	// FloorOut is still cooling, hold back
					{
						if (	Global.circuits.isSingleActiveCircuit() 					// A circuit may have been switched off since
						&& 		positionTracked > safeSingleCircuitPosition	)
						{
							swingTimeRequired												= safeSingleCircuitPosition - positionTracked;				// Backdown
						}
						else if (swingTimeRequired > 0)
						{
							swingTimeRequired												= 0;
						}
						else
						{
							// swingTimeRequired is negative, so must be handled
						}
					}
					break;
				case MaxReached:
				case NormalOperating:
				default:
					break;
				}
			
				if (swingTimeRequired > 0)		// Moving hotter
				{
					if (positionTracked == this.swingTime)
					{
						// Do nothing as already at minimum
						swingTimeRequired													= 0;
					}
					else if (positionProjected > this.swingTime)
			 		{
			 			swingTimeRequired 													= this.swingTime - positionTracked + 2000;		//No point waiting over maximum add extra 2 seconds to be sure of end point
						report																= mixerMoveUp(swingTimeRequired);
						positionTracked														= this.swingTime;					
			 		}
					else																	// Normal operating
					{
						report																= mixerMoveUp(swingTimeRequired);
						positionTracked														= report.positionTracked;
					}
				}
				else	// Moving colder
				{
					if (positionTracked == 0)
					{
						// Do nothing as already at minimum
						swingTimeRequired													= 0;
					}
					else if (positionProjected < 0)																// Should never happen
			 		{
			 			swingTimeRequired 													= - (positionTracked + 2000);					//Add extra 2 seconds to be sure of end point
						report																= mixerMoveDown(swingTimeRequired);
						positionTracked														= 0;					
			 		}
					else																					// Normal operating
					{
						report																= mixerMoveDown(swingTimeRequired);
						positionTracked														= report.positionTracked;
					}
				}
				Rpt_PID.Update									messageAfter				= (new Rpt_PID()).new Update();
				messageAfter.target															= targetTemp;
				messageAfter.tempCurrent													= pidFloorOut.tempCurrent();
				messageAfter.tempCurrentError												= pidFloorOut.tempCurrentError();
				
				messageAfter.termProportional												= - pidFloorOut.getGainP(1F);
				messageAfter.termDifferential												= - pidFloorOut.getGainD(1F);
				messageAfter.termIntegral													= - pidFloorOut.getGainI(1F);
	
				messageAfter.gainProportional												= pidFloorOut.getGainP(gainP);
				messageAfter.gainDifferential												= pidFloorOut.getGainD(gainD);
				messageAfter.gainIntegral													= pidFloorOut.getGainI(gainI);
				
				messageAfter.kP																= gainP;
				messageAfter.kD																= gainD;
				messageAfter.kI																= gainI;
				
				messageAfter.gainTotal														= swingTimeRequired;
				messageAfter.tempOut														= Global.thermoFloorOut.reading;
				messageAfter.tempBoiler														= Global.thermoBoiler.reading;
				
				messageAfter.positionTracked												= positionTracked;
				messageAfter.startMovement													= false;
				
				LogIt.pidData(messageBefore);
				LogIt.pidData(messageAfter);
			}
			else
			{
				// Less that 500 ms. Do nought
			}
		}
		catch (Exception ex)
		{
			positionZero();											// This bypasses stopRequested
			Global.eMailMessage("Mixer/sequencer", "Thermometer " + Global.thermoFloorOut.name + " cannont be read");
			return;
		}
	}	// End of sequencer
	public void positionZero()
	{
		if (positionTracked == null)
		{
			allOff();
			mixerDown.on();
			Global.waitMilliSeconds(swingTime + 2000);
			mixerDown.off();
			positionTracked																	= 0;
		}
		else if (positionTracked > 0)
		{
			allOff();
			mixerDown.on();
			Global.waitMilliSeconds(positionTracked + 2000);					// Add 2 extra seconds to be certain
			mixerDown.off();
			positionTracked																	= 0;
		}
		else if (positionTracked == 0)
		{
			// Do nothing
		}
	}
	public void positionPercentage(float percentage)
	{
		if (positionTracked == null)				positionZero();
		allOff();
		positionTracked																		= positionAbsolute((int) (swingTime.floatValue() * percentage)).positionTracked;
	}
	public void positionFull()
	{
		if (positionTracked == null)
		{
			allOff();
			mixerUp.on();
			Global.waitMilliSeconds(swingTime + 2000);
			mixerUp.off();
			positionTracked																	= swingTime;
		}
		else if (positionTracked < swingTime)
		{
			allOff();
			mixerUp.on();
			Global.waitMilliSeconds(positionTracked + 2000);					// Add 2 extra seconds to be certain
			mixerUp.off();
			positionTracked																	= swingTime;
		}
		else if (positionTracked == swingTime)
		{
			// Do nothing
		}
	}
	public MixerMove_Report positionAbsolute(Integer targetPosition)
	{
		LogIt.display("Mixer", "positionAbsolute", "targetPosition : " + targetPosition.toString());
		Integer 												swingTimeToTarget			= targetPosition - positionTracked;				
		if (swingTimeToTarget > 0)
			return 	mixerMoveUp(swingTimeToTarget);
		else 
			return 	mixerMoveDown(-swingTimeToTarget);
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
		Long													positionChange				= 0L;
		MixerMove_Report										report						= new MixerMove_Report();
		
		mixerUp.on();
		report.timeStart																	= Global.DateTime.now();
		Global.waitMilliSeconds(swingTimeRequired);
		mixerUp.off();
		report.timeEnd																		= Global.DateTime.now();
		positionChange																		= report.timeEnd - report.timeStart;		// Positive number as moved up
		report.swingTimePerformed															= positionChange.intValue();
		report.positionTracked																= positionTracked + report.swingTimePerformed;
		if (report.positionTracked > swingTime)
		{
			report.positionTracked 															= swingTime;
		}
		return report;
	}
	private MixerMove_Report mixerMoveDown(Integer swingTimeRequired)
	{
		Long													positionChange				= 0L;
		MixerMove_Report										report						= new MixerMove_Report();
		
		mixerDown.on();
		report.timeStart																	= Global.DateTime.now();
		Global.waitMilliSeconds(-swingTimeRequired);									
		mixerDown.off();									
		report.timeEnd																		= Global.DateTime.now();
		positionChange																		= report.timeStart - report.timeEnd;		// Negative number as moved down
		report.swingTimePerformed															= positionChange.intValue();
		report.positionTracked																= positionTracked + report.swingTimePerformed;
		if (report.positionTracked < 0)									
		{									
			report.positionTracked 															= 0;
		}
		return report;
	}
	private class MixerMove_Report
	{
		public	Long											timeStart;
		public	Long											timeEnd;
		public	Integer											swingTimePerformed;
		public 	Integer											positionTracked;
	}
}
