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
//	public enum													burnerStates 
//	{
//		STATE_Off, 
//		STATE_On_Heating,
//		STATE_On_Cooling,
//		STATE_On_CoolingAfterOverheat,
//		STATE_On_PowerUp,
//		STATE_Error
//	};
	public Integer	   											burnerStateNext;
	public Integer	   											timeToLive;

	public Thermometers 										thermometers;
	public Thermometer 											thermoBoiler;
	public Burner												burner;
	
	public Integer	   											tempMax;
	public Integer	   											tempMin;
	public Integer												tempNeverExceed				= 95000;
	public Integer												tempOvershoot				= 18000;
	public Integer												state;
	public PID													pidControler;
	
	public final int 											STATE_Off 						= 0;
	public final int 											STATE_On_Heating 				= 1;
	public final int 											STATE_On_Cooling 				= 2;
	public final int 											STATE_On_CoolingAfterOverheat 	= 3;
	public final int 											STATE_On_PowerUp 				= 4;
	public final int 											STATE_Error	 					= -1;
	

	public Boiler(Ctrl_Configuration.Data.Boiler boilerparams)
	{
		this.thermoBoiler 																	= Global.thermometers.fetchThermometer(boilerparams.thermometer);
		
		burner																				= Global.burner;

		this.tempMax 																		= -1;
		this.tempMin 																		= -1;
		this.tempNeverExceed																= boilerparams.tempNeverExceed.milliDegrees;
		this.tempOvershoot																	= boilerparams.tempOverShoot.milliDegrees;
		state																				= STATE_Off;
	}
	public void requestHeat(HeatRequired eR)
	{
		tempMax 																			= eR.tempMaximum;
		tempMin 																			= eR.tempMinimum;
		if (tempMax > tempNeverExceed - tempOvershoot)
		{
			tempMax 																		= tempNeverExceed - tempOvershoot;
		}
		//LogIt.display("Boiler", "requestHeat", "tempMin/tempMax are " + tempMin + "/" + tempMax);
		
		// Only change the state if it is STATE_Off
		// There could be an error (STATE_Error)
		// The sequencer will do the rest
		if (state == STATE_Off)
		{
			state																			= STATE_On_PowerUp;
		}
	}
	public void requestIdle()
	{
		burner.powerOff();
		tempMax 																			= -1000;
		tempMin 																			= -1000;
		state 																				= STATE_Off;
	}
	public Boolean checkOverHeat()
	{
		if (Global.thermoBoiler.readUnCached() > (tempNeverExceed - tempOvershoot))
		{
			return true;
		}
		else
		{
			return false;
		}
	}	
	public void sequencer()
	{
		// Take unCached temperature. The value is then cached
		
		Integer	tempNow =  Global.thermoBoiler.readUnCached();
		System.out.println("State : " + state);
		
		if (checkOverHeat())		// This is just a temperature check
		{
			burner.powerOff();
			if (state != STATE_On_CoolingAfterOverheat)
			{
				LogIt.error("Boiler", "sequencer", "boiler overheat at : " + Global.thermoBoiler.reading + " , state set to STATE_OnCoolingAfterOverheat", false);
			}
			state																			 = STATE_On_CoolingAfterOverheat;
			return;
		}
		if (burner.burnerFault())	//This reads GPIO
		{
			state																			 = STATE_Error;
			burner.powerOff();
			LogIt.error("Boiler", "sequencer", "burner has tripped");
			return;
		}

		switch (state)
		{
		case STATE_Error:
			// do nothing
			// Dont close other relays because we must evacuate the heat
			break;
		case STATE_Off:
			// do nothing
			// Should we not close down all relays
			break;
		case STATE_On_Heating:
			if (Global.thermoBoiler.reading > tempMax)
			{
				burner.powerOff();
				state																		 = STATE_On_Cooling;
				return;
			}
			break;
		case STATE_On_CoolingAfterOverheat:
			burner.powerOff();													//Ensure burner is powered off (normally, already done
			if (!checkOverHeat())
			{
				LogIt.error("Boiler", "sequencer", "boiler overheat, normal operating temperature : " + Global.thermoBoiler.reading + " , state set to STATE_OnCooling", false);
				state																		 = STATE_On_Cooling; 		//Normal operating temp has returned
				return;
			}
			break;
		case STATE_On_Cooling:
			if (Global.thermoBoiler.reading < tempMin)
			{
				burner.powerOn();
				state 																		= STATE_On_Heating;
				return;
			}
			break;
		case STATE_On_PowerUp:
			if (Global.thermoBoiler.reading < tempMax)
			{
				burner.powerOn();
				state 																		= STATE_On_Heating;
				return;
			}
			break;
		default:
			LogIt.error("Boiler", "sequencer", "unknown state detected : " + state);	
		}
	}
}
