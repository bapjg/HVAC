package eRegulation;

import HVAC_Common.Ctrl_Configuration;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Boiler
{
	// ===========================================================================================
	//
	// iState Significance Waiting for TimeToLive NextState Action
	// ====== ================= ================ ========== ========= ========
	// 0 All Off Energy Request -1 0 ComingUp
	// 1 Fan On Time 10 2 ComingUp
	// 2 Fuel Flowing Flame 20 3 ComingUp
	// 3 Flame detected Ok NoEnergyReq/TempHigh -1 3 ComingUp
	// 6 ShuttingDown Nothing 0 ShuttingDown
	//
	// Note FaultDetected overides every thing
	// ===========================================================================================

	public Integer	   											burnerState;

	public Integer	   											burnerStateNext;
	public Integer	   											timeToLive;

	public Thermometers 										thermometers;
	public Thermometer 											thermoBoiler;
	public Burner												burner;

	public Heat_Required										heatRequired;
	public Integer												tempNeverExceed				= 95000;
	public Integer												tempOvershoot				= 18000;
	public Integer												tempCondensationAvoidance	= 55000;
	public HVAC_STATES.Boiler									state;
	public PID													pidControler;

	public Boiler(Ctrl_Configuration.Data.Boiler boilerparams)
	{
// Example of new field added until the Android has set/initialised it
// Remember to restart TomCat with new version of software
// Android sends/receives data in JSON format, but eReg only gets it in Fava Object Format

		this.thermoBoiler 																	= Global.thermometers.fetchThermometer(boilerparams.thermometer);
		this.burner																			= Global.burner;
		this.tempNeverExceed																= boilerparams.tempNeverExceed.milliDegrees;
		this.tempOvershoot																	= boilerparams.tempOverShoot.milliDegrees;
		this.tempCondensationAvoidance														= boilerparams.tempCondensationAvoidance.milliDegrees;
		state																				= HVAC_STATES.Boiler.Off;
	}
	public void requestIdle()		// Called by Control prior to closing down the system
	{
		burner.powerOff();
		this.heatRequired.setZero();
		state 																				= HVAC_STATES.Boiler.Off;
	}
	public Boolean overHeatDetected()
	{
		try
		{
			Integer												tempNow						= Global.thermoBoiler.readUnCached();
			if (tempNow > tempNeverExceed)						return true;
			else												return false;
		}
		catch (Exception ex)
		{
			Global.eMailMessage("Boiler/checkOverHeat", "Error on Read Boiler Thermometer");
			return	true;
		}
	}
	public void sequencer()
	{
		if (this.heatRequired.tempMaximum < this.tempCondensationAvoidance)
		{
			this.heatRequired.setZero();
		}
		
		if (	(this.heatRequired.isZero()) 
		&& 		(this.state != HVAC_STATES.Boiler.Off)
		&&		(this.state != HVAC_STATES.Boiler.Error)		)							// No heat required and Off
		{
			this.state 																		= HVAC_STATES.Boiler.PowerDown;
		}
		else
		{
			this.heatRequired.tempMaximum	= (this.heatRequired.tempMaximum > tempNeverExceed - tempOvershoot) ? tempNeverExceed - tempOvershoot 	: this.heatRequired.tempMaximum;
			this.heatRequired.tempMinimum	= (this.heatRequired.tempMinimum < tempCondensationAvoidance) 		? tempCondensationAvoidance 		: this.heatRequired.tempMinimum;
		}
		try
		{
			Integer												tempNow						= Global.thermoBoiler.readUnCached();
			
			if (overHeatDetected())		// This is just a temperature check. Returns true in an overheat situation
			{							// In the event of an over heat
				switch (state)
				{
				case On_CoolingAfterOverheat:	break;
				case On_Cooling:				break;	// Nothing to do except wait
				case Error:						break;
				case Off:						break;	// Normally cannot happen
				case PowerDown:					break;	// Normally cannot happen, ------------------------------ditto--------------------------------------
				// 28/04/2019 : Order changed so that only PowerUp and On_Heating generate message below
				case PowerUp:					// DONT BREAK	// Normally cannot    happen, as PowerUp happens only when min temperature reached
				case On_Heating:				// DONT BREAK	// Normally shouldn't happen, as should be switched off at lower temperature (current Temp - overShoot)
				default:
					burner.powerOff();
					LogIt.error("Boiler", "sequencer", "boiler overheat at : " + Global.thermoBoiler.reading + ", state set to STATE_OnCoolingAfterOverheat, Current state : " + state);
					state																	= HVAC_STATES.Boiler.On_CoolingAfterOverheat;
					break;
				}
			}
			if (burner.burnerFault())	//This reads GPIO
			{
				state																		= HVAC_STATES.Boiler.Error;
				burner.powerOff();
				LogIt.error("Boiler", "sequencer", "burner has tripped");
			}

			switch (state)
			{
			case Error:	// do nothing Dont close other relays because we must evacuate the heat
				break;
			case Off:	// do nothing Dont close relays as optimisation may be in progress
				if (! heatRequired.isZero()) 					state						= HVAC_STATES.Boiler.PowerUp;
				break;
			case On_Heating:
				if (Global.thermoBoiler.reading > this.heatRequired.tempMaximum)
				{
					burner.powerOff();
					state																	= HVAC_STATES.Boiler.On_Cooling;
				}
				break;
			case On_CoolingAfterOverheat:
				if (! overHeatDetected())
				{
					LogIt.error("Boiler", "sequencer", "boiler overheat, normal operating temperature : " + Global.thermoBoiler.reading + " , state set to STATE_OnCooling");
					state																	= HVAC_STATES.Boiler.On_Cooling; 		//Normal operating temp has returned
				}
				break;
			case On_Cooling:
				if (	(Global.thermoBoiler.reading < this.heatRequired.tempMinimum) 
				&& 		(! this.heatRequired.isZero())								)
				{
					burner.powerOn();
					state 																	= HVAC_STATES.Boiler.On_Heating;
				}
				break;
			case PowerUp:
				if (Global.thermoBoiler.reading < this.heatRequired.tempMaximum)
				{
					burner.powerOn();
					state 																	= HVAC_STATES.Boiler.On_Heating;
				}
				break;
			case PowerDown:
				burner.powerOff();
				state 																		= HVAC_STATES.Boiler.Off;
				break;
			default:
				LogIt.error("Boiler", "sequencer", "unknown state detected : " + state);	
			}
		}
		catch (Thermometer_ReadException exTR)
		{
			burner.powerOff();
			Global.eMailMessage("Boiler/sequencer", "Thermometer_ReadException on Read Boiler Thermometer " + exTR.thermoName + " at " + exTR.thermoAddress);
			state																			 = HVAC_STATES.Boiler.Error;
		}
		catch (Thermometer_SpreadException exTS)
		{
			Global.eMailMessage("Boiler/sequencer", "Thermometer_SpreadException on Read Boiler Thermometer");
			// Just carry on to see if it gets worse
		}
		catch (Exception ex)
		{
			burner.powerOff();
			Global.eMailMessage("Boiler/sequencer", "An Error on Read Boiler Thermometer");
			state																			 = HVAC_STATES.Boiler.Error;
		}
	}
}
