package eRegulation;

import HVAC_Common.Ctrl_Configuration;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
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
	
	public Integer	   											tempMax;
	public Integer	   											tempMin;
	public Integer												tempNeverExceed				= 95000;
	public Integer												tempOvershoot				= 18000;
	public Integer												tempCondensationAvoidance	= 55000;
	public HVAC_STATES.Boiler									state;
	public PID													pidControler;

	public Boiler(Ctrl_Configuration.Data.Boiler boilerparams)
	{
		this.thermoBoiler 																	= Global.thermometers.fetchThermometer(boilerparams.thermometer);
		this.burner																			= Global.burner;
		this.tempMax 																		= -1;
		this.tempMin 																		= -1;
		this.tempNeverExceed																= boilerparams.tempNeverExceed.milliDegrees;
		this.tempOvershoot																	= boilerparams.tempOverShoot.milliDegrees;
		if (boilerparams.tempCondensationAvoidance == null)	this.tempCondensationAvoidance	= 55000;
		else this.tempCondensationAvoidance													= boilerparams.tempCondensationAvoidance.milliDegrees;
		state																				= HVAC_STATES.Boiler.Off;
	}
	public void requestHeat(Heat_Required eR)
	{
		tempMax 																			= eR.tempMaximum;
		tempMin 																			= eR.tempMinimum;
		if (tempMax > tempNeverExceed - tempOvershoot)			tempMax 					= tempNeverExceed - tempOvershoot;
		if (state == HVAC_STATES.Boiler.Off)					state						= HVAC_STATES.Boiler.PowerUp;
	}
	public void requestIdle()
	{
		burner.powerOff();
		tempMax 																			= -1000;
		tempMin 																			= -1000;
		state 																				= HVAC_STATES.Boiler.Off;
	}
	public Boolean checkOverHeat()
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
		try
		{
			Integer												tempNow						= Global.thermoBoiler.readUnCached();
			
			if (checkOverHeat())		// This is just a temperature check
			{
				if (state != HVAC_STATES.Boiler.On_CoolingAfterOverheat)
				{
					burner.powerOff();
					LogIt.error("Boiler", "sequencer", "boiler overheat at : " + Global.thermoBoiler.reading + " , state set to STATE_OnCoolingAfterOverheat", false);
					state																	= HVAC_STATES.Boiler.On_CoolingAfterOverheat;
				}
				return;
			}
			if (burner.burnerFault())	//This reads GPIO
			{
				state																		= HVAC_STATES.Boiler.Error;
				burner.powerOff();
				LogIt.error("Boiler", "sequencer", "burner has tripped");
				return;
			}

			switch (state)
			{
			case Error:
				// do nothing
				// Dont close other relays because we must evacuate the heat
				break;
			case Off:
				// do nothing
				// Should we not close down all relays
				break;
			case On_Heating:
				if (Global.thermoBoiler.reading > tempMax)
				{
					burner.powerOff();
					state																	= HVAC_STATES.Boiler.On_Cooling;
					return;
				}
				break;
			case On_CoolingAfterOverheat:
				if (!checkOverHeat())
				{
					LogIt.error("Boiler", "sequencer", "boiler overheat, normal operating temperature : " + Global.thermoBoiler.reading + " , state set to STATE_OnCooling", false);
					state																	= HVAC_STATES.Boiler.On_Cooling; 		//Normal operating temp has returned
					return;
				}
				break;
			case On_Cooling:
				if (Global.thermoBoiler.reading < tempMin)
				{
					burner.powerOn();
					state 																	= HVAC_STATES.Boiler.On_Heating;
					return;
				}
				break;
			case PowerUp:
				if (Global.thermoBoiler.reading < tempMax)
				{
					burner.powerOn();
					state 																	= HVAC_STATES.Boiler.On_Heating;
					return;
				}
				break;
			default:
				LogIt.error("Boiler", "sequencer", "unknown state detected : " + state);	
			}
		}
		catch (Thermometer_ReadException exTR)
		{
			burner.powerOff();
			Global.eMailMessage("Boiler/sequencer", "Thermometer_ReadException on Read Boiler Thermometer");
			state																			 = HVAC_STATES.Boiler.Error;
			return;
		}
		catch (Thermometer_SpreadException exTS)
		{
			Global.eMailMessage("Boiler/sequencer", "Thermometer_SpreadException on Read Boiler Thermometer");
			// Just carry on to see if it gets worse
			return;
		}
		catch (Exception ex)
		{
			burner.powerOff();
			Global.eMailMessage("Boiler/sequencer", "An Error on Read Boiler Thermometer");
			state																			 = HVAC_STATES.Boiler.Error;
			return;
		}
	}
}
