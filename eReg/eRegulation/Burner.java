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
	public 	Relay	   											burnerPower;
	public  FuelFlow											fuelflow;
	public	GPIO												monitorBurnerFault;
	public 	GPIO												monitorFuelFlow;
	public	Long												lastSwitchedOn				= 0L;
	public	Long												lastSwitchedOff				= 0L;
	private Boolean												isOn 						= false;
	
	public Burner(Ctrl_Configuration.Data.Burner burnerparams)
	{
		burnerPower																			= Global.relays.fetchRelay(burnerparams.relay);
		monitorBurnerFault																	= new GPIO(17);
		monitorFuelFlow																		= new GPIO(4);
		fuelflow																			= new FuelFlow();
		burnerPower.off();
	}
	
	public void powerOn()
	{
		if (isOn)
		{
			LogIt.error("Burner", "powerOn", "Burner has been powered on when already on");
			Global.eMailMessage("Burner fault", "Burner has been powered on when already on");
		}
		LogIt.action("Burner", "On");
		burnerPower.on();
		isOn																				= true;
		lastSwitchedOn																		= Global.DateTime.now();
		
		// After power on, ventilation clears fuel out of combustion chamber for 10 seconds
		// After which fuel is injected and an ignition arc ignited for 20 - 30 seconds
		// Fuel flow should be detected after about 10 seconds after burnerPower.on() call
		Integer i;
		
		for (i = 0; i < 30; i++)
		{
			if	(isFuelFlowing())
			{
				// System.out.println("Burner/powerOn : fuel flow detected ");
				fuelflow.switchedOn();
				return;
			}
			else if (burnerFault())
			{
				LogIt.error("Burner", "powerOn", "checkFault has revealed a problem");
				Global.eMailMessage("Burner fault", "'checkfault()' has detected a fault");
			}
			else
			{
				Global.waitSeconds(1);
			}
		}
		LogIt.error("Burner", "powerOn", "no fuel flow detected : burner has tripped");
		Global.eMailMessage("Burner fault", "Burner/powerOn : no fuel flow detected, burner has tripped");
		powerOff();
	}
	public void powerOff()
	{
		if (! isOn)
		{
			// Do nothing special, it's just the Boiler playing things safe
		}
		LogIt.action("Burner", "powerOff");
		burnerPower.off();
		isOn																				= false;
		lastSwitchedOff																		= Global.DateTime.now();

		Integer i;
		
		for (i = 0; i < 30; i++)
		{
			if	(isFuelFlowing())
			{
				Global.waitMilliSeconds(10);								// Need to wait a bit for relays to work and ADC to get a proper average (without voltage spikes)
			}
			else
			{
				fuelflow.switchedOff();
				return;														// All is well
			}
		}
		LogIt.error("Burner", "powerOff", "fuel flow still detected after 300 ms: burner has tripped");
		Global.eMailMessage("Burner fault", "Burner/powerOff : fuel flow still detected after 300 ms: burner has tripped");
		fuelflow.switchedOff();
	}
	public void sequencer()
	{
		if (burnerFault())
		{
			LogIt.error("Burner", "sequencer", "checkFault has detected a problem");
			Global.eMailMessage("Burner fault", "Burner/sequencer : 'checkFault()' has detected a problem");
			powerOff();
		}
		// TODO Must also check max temp;
	}
	public Boolean isFuelFlowing()
	{
		return monitorFuelFlow.isHigh();
	}
	public Boolean burnerFault()
	{
		if (monitorBurnerFault.isHigh())
		{
			LogIt.error("Burner", "burnerFault", "Burner has tripped");
			Global.eMailMessage("Burner fault", "Burner/checkFault : Burner has tripped");
			return true;
		}
		else
		{
			return false;
		}
	}
}
