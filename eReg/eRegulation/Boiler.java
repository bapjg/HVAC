package eRegulation;

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

	public Integer	   		burnerState;
	public Integer	   		burnerStateNext;
	public Integer	   		timeToLive;

	public Thermometers 	thermometers;
	public Thermometer 		thermoBoiler;
	public Burner			burner;
	
	public Integer	   		tempMax;
	public Integer	   		tempMin;
	public Integer			tempNeverExceed						= 950;
	public Integer			tempOvershoot						= 180;
	public Integer			state;
	
	public final int 		STATE_Off 							= 0;
	public final int 		STATE_On_Heating 					= 1;
	public final int 		STATE_On_Cooling 					= 2;
	public final int 		STATE_On_CoolingAfterOverheat 		= 3;
	public final int 		STATE_On_PowerUp 					= 4;
	public final int 		STATE_Error	 						= -1;
	

	public Boiler()
	{
		thermometers 								= new Thermometers();
		thermoBoiler 								= thermometers.fetchThermometer("Boiler");
		
		burner										= new Burner();

		tempMax 									= -1;
		tempMin 									= -1;
		tempNeverExceed								= 950;
		tempOvershoot								= 150;
		state										= STATE_Off;
	}
	public void requestHeat(HeatRequired eR)
	{
		tempMax 									= eR.tempMaximum;
		tempMin 									= eR.tempMinimum;
		if (tempMax > tempNeverExceed - tempOvershoot)
		{
			tempMax 								= tempNeverExceed - tempOvershoot;
		}
		//LogIt.info("Boiler", "requestHeat", "tempMin/tempMax are " + tempMin + "/" + tempMax);
		
		// Only change the state if it is STATE_Off
		// There could be an error (STATE_Error)
		// The sequencer will do the rest
		if (state == STATE_Off)
		{
			state									= STATE_On_PowerUp;
		}
	}
	public void requestIdle()
	{
		burner.powerOff();
		tempMax 									= -1000;
		tempMin 									= -1000;
		state 										= STATE_Off;
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
		//LogIt.info("Boiler", "sequencer", "state is : " + state);
		
		// Take unCached temperature. The value is then cached
		
		Integer	tempNow =  Global.thermoBoiler.readUnCached();
		
		if (checkOverHeat())		// This is just a temperature check
		{
			burner.powerOff();
			if (state != STATE_On_CoolingAfterOverheat)
			{
				LogIt.error("Boiler", "sequencer", "boiler overheat at : " + Global.thermoBoiler.reading + " , state set to STATE_OnCoolingAfterOverheat");
			}
			state									 = STATE_On_CoolingAfterOverheat;
			return;
		}
		if (burner.checkFault())	//This reads ADC
		{
			state									 = STATE_Error;
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
				state								 = STATE_On_Cooling;
				return;
			}
			break;
		case STATE_On_CoolingAfterOverheat:
			burner.powerOff();													//Ensure burner is powered off (normally, already done
			if (!checkOverHeat())
			{
				LogIt.error("Boiler", "sequencer", "boiler overheat, normal operating temperature : " + Global.thermoBoiler.reading + " , state set to STATE_OnCooling");
				state								 = STATE_On_Cooling; 		//Normal operating temp has returned
				return;
			}
			break;
		case STATE_On_Cooling:
			if (Global.thermoBoiler.reading < tempMin)
			{
				burner.powerOn();
				state 								= STATE_On_Heating;
				return;
			}
			break;
		case STATE_On_PowerUp:
			if (Global.thermoBoiler.reading < tempMax)
			{
				burner.powerOn();
				state 								= STATE_On_Heating;
				return;
			}
			break;
		default:
			LogIt.error("Boiler", "sequencer", "unknown state detected : " + state);	
		}
	}
}
