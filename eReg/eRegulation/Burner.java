package eRegulation;

public class Burner
{
	public 	Relay	   		burnerPower;
	public  FuelFlow		fuelflow;
	
	public Burner()
	{
		burnerPower									= Global.burnerPower;
		Global.burnerVoltages 						= new ADC();						// ADC measure fuel flow and burner fault
		fuelflow									= new FuelFlow();
		burnerPower.off();
	}
	public void powerOn()
	{
		LogIt.action("Burner", "On");
		burnerPower.on();
		
		Integer i;
		// System.out.println("===================Powered burner");
		
		for (i = 0; i < 30; i++)
		{
			fuelflow.update();
			if (fuelflow.isFuelFlowing())
			{
				// System.out.println("Burner/powerOn : fuel flow detected ");
				return;
			}
			else if (checkFault())
			{
				LogIt.error("Burner", "powerOn", "checkFault has revealed a problem");
			// When burner starts fuel flow is detected
				// a message has already been displayed
				// Don't know what to do here
			}
			else
			{
				Global.waitSeconds(1);
			}
		}
		System.out.println("Burner/powerOn no fuel flow detected : burner has tripped");
	}
	public void powerOff()
	{
		LogIt.action("Burner", "powerOff");
		burnerPower.off();

		Integer i;
		
		for (i = 0; i < 30; i++)
		{
			fuelflow.update();
			if (fuelflow.isFuelFlowing())
			{
				Global.waitMilliSeconds(10);								// Need to wait a bit for relays to work and ADC to get a proper average (without voltage spikes)
			}
			else
			{
				return;														// All is well
			}
		}
		LogIt.error("Burner", "powerOff", "fuel flow still detected after 300 ms: burner has tripped");
		System.out.println("Burner/powerOff and fuel flow still detected after 300 ms: burner has tripped");
	}
	public void sequencer()
	{
		fuelflow.update();
		
		if (checkFault())
		{
			LogIt.error("Burner", "sequencer", "checkFault has detected a problem");
			powerOff();
			System.out.println("A Burner Fault has been detected by the burner Sequencer");
		}

		// Must also check max temp;
	}
	public Boolean checkFault()
	{
		if (Global.burnerVoltages.isFault())
		{
			LogIt.error("Burner", "checkFault", "Over 4 volts indicates trip");
			return true;
		}
		else
		{
			return false;
		}
	}
}
