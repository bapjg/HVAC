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
	public Integer			tempNeverExceed;
	public Integer			state;
	
	public final int 		STATE_Off 				= 0;
	public final int 		STATE_OnHeating 		= 1;
	public final int 		STATE_OnCooling 		= 2;
	public final int 		STATE_OnPowerUp 		= 3;
	public final int 		STATE_Error	 			= -1;
	

	public Boiler()
	{
		thermometers 								= new Thermometers();
		thermoBoiler 								= thermometers.fetchThermometer("Boiler");
		
		burner										= new Burner();

		tempMax 									= -1;
		tempMin 									= -1;
		tempNeverExceed								= 850;
		state										= STATE_Off;
	}
	public void requestHeat(HeatRequired eR)
	{
		tempMax 									= eR.tempMaximum;
		tempMin 									= eR.tempMinimum;
		LogIt.info("Boiler", "requestHeat", "tempMin/tempMax are " + tempMin + "/" + tempMax);
		
		// Only change the state if it is STATE_Off
		// There could be an error (STATE_Error)
		// The sequencer will do the rest
		if (state == STATE_Off)
		{
			state									= STATE_OnPowerUp;
		}
	}
	public void requestIdle()
	{
		burner.powerOff();
		tempMax 									= -1000;
		tempMin 									= -1000;
		state 										= STATE_Off;
	}
	public Boolean checkFault()
	{
		if (Global.thermoBoiler.reading > tempNeverExceed)
		{
			LogIt.error("Boiler", "checkFault", "tempNeverExceed has been reached");
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
		
		if (checkFault())
		{
			state									 = STATE_Error;
			burner.powerOff();
			LogIt.error("Boiler", "sequencer", "boiler overheat");
			return;
		}
		if (burner.checkFault())
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
			// Should we not close down all relays
			break;
		case STATE_Off:
			// do nothing
			// Should we not close down all relays
			break;
		case STATE_OnHeating:
			//LogIt.info("Boiler", "sequencer",  "STATE_OnHeating : temp boiler/max " + Global.tempBoiler + "/" + tempMax);
			if (Global.thermoBoiler.reading > tempMax)
			{
				burner.powerOff();
				state								 = STATE_OnCooling;
				return;
			}
			break;
		case STATE_OnCooling:
			//LogIt.info("Boiler", "sequencer", "STATE_OnCooling : temp boiler/min " + Global.tempBoiler + "/" + tempMin);
			if (Global.thermoBoiler.reading < tempMin)
			{
				burner.powerOn();
				state 								= STATE_OnHeating;
				return;
			}
			break;
		case STATE_OnPowerUp:
			//LogIt.info("Boiler", "sequencer", "STATE_OnPowerUp : temp boiler/min " + Global.tempBoiler + "/" + tempMin);
			if (Global.thermoBoiler.reading < tempMax)
			{
				burner.powerOn();
				state 								= STATE_OnHeating;
				return;
			}
			break;
		default:
			LogIt.error("Boiler", "sequencer", "unknown state detected : " + state);	
		}
	}
}
