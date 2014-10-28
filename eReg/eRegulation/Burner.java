package eRegulation;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
import HVAC_Common.Ctrl_Configuration;

/*
	Experiment 23/10/2013
	=====================
	Burner tripped at 110 degrees
	Burner de-tripped at 97 degrees
	Overshoot went to 120 degrees
		Probably limited by latent heat ofboiling
		I noted a lot of vapour in the circuit (floor, which tripped later)
 */

public class Burner
{
	public 	Relay	   		burnerPower;
	public  FuelFlow		fuelflow;
	private	GPIO			monitorBurnerFault;
	private GPIO			monitorFuelFlow;
	
	public Burner(Ctrl_Configuration.Data.Burner burnerparams)
	{
		burnerPower									= Global.relays.fetchRelay(burnerparams.relay);
//		Global.burnerVoltages 						= new ADC();						// ADC measure fuel flow and burner fault
		monitorBurnerFault							= new GPIO(4);
		monitorFuelFlow								= new GPIO(17);
		fuelflow									= new FuelFlow();
		burnerPower.off();
	}
	
	public void powerOn()
	{
		LogIt.action("Burner", "On");
		burnerPower.on();
		
		Integer i;
		fuelflow.update(isFuelFlowing());
		for (i = 0; i < 30; i++)
		{
//			if (fuelflow.isFuelFlowing())
			if	(isFuelFlowing())
			{
				// System.out.println("Burner/powerOn : fuel flow detected ");
				
				return;
			}
			else if (burnerFault())
			{
				LogIt.error("Burner", "powerOn", "checkFault has revealed a problem");
				Global.eMailMessage("Burner fault", "'checkfault()' has detected a fault");
			// When burner starts fuel flow is detected
				// a message has already been displayed
				// Don't know what to do here
			}
			else
			{
				Global.waitSeconds(1);
			}
		}
		LogIt.error("Burner", "powerOn", "no fuel flow detected : burner has tripped");
		Global.eMailMessage("Burner fault", "Burner/powerOn : no fuel flow detected, burner has tripped");
	}
	public void powerOff()
	{
		LogIt.action("Burner", "powerOff");
		burnerPower.off();

		Integer i;
		
		for (i = 0; i < 30; i++)
		{
			fuelflow.update(isFuelFlowing());
//			if (fuelflow.isFuelFlowing())
			if	(isFuelFlowing())
			{
				Global.waitMilliSeconds(10);								// Need to wait a bit for relays to work and ADC to get a proper average (without voltage spikes)
			}
			else
			{
				return;														// All is well
			}
		}
		LogIt.error("Burner", "powerOff", "fuel flow still detected after 300 ms: burner has tripped");
		Global.eMailMessage("Burner fault", "Burner/powerOff : fuel flow still detected after 300 ms: burner has tripped");
	}
	public void sequencer()
	{
		fuelflow.update(isFuelFlowing());
		
		if (burnerFault())
		{
			LogIt.error("Burner", "sequencer", "checkFault has detected a problem");
			Global.eMailMessage("Burner fault", "Burner/sequencer : 'checkFault()' has detected a problem");
			powerOff();
		}

		// Must also check max temp;
	}
	public Boolean isFuelFlowing()
	{
		return monitorFuelFlow.isHigh();
	}
	public Boolean burnerFault()
	{
		if (monitorBurnerFault.isHigh())
		{
			LogIt.error("Burner", "checkFault", "Burner has tripped");
			Global.eMailMessage("Burner fault", "Burner/checkFault : Burner has tripped");
			return true;
		}
		else
		{
			return false;
		}
	}
	
//	public Boolean checkFault()
//	{
//// TODO		return burnerFault.isHigh();
//		
//		if (Global.burnerVoltages.isFault())
//		{
//			LogIt.error("Burner", "checkFault", "Over 4 volts indicates trip");
//			Global.eMailMessage("Burner fault", "Burner/checkFault : Over 4 volts indicates trip");
//			return true;
//		}
//		else
//		{
//			return false;
//		}
//	}
}
