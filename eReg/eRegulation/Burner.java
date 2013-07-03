package eRegulation;

public class Burner
{
	public 	Relay	   		burnerPower;
	private FuelFlow		fuelflow;
	
	public Burner()
	{
		burnerPower									= Global.burnerPower;
		Global.burnerVoltages 						= new ADC();						// ADC measure fuel flow and burner fault
		System.out.println("new FuelFlow before");
		fuelflow									= new FuelFlow();
		System.out.println("new FuelFlow after");
		burnerPower.off();
	}
	public void powerOn()
	{
		burnerPower.on();
		
		Integer i;
		System.out.println("Powered burner");
		
		for (i = 0; ((i < 30) && (fuelflow.timeLastStart > -1L)); i++)
		{
			// Fuel is not yet flowing
			System.out.println("Waiting for fuel flow, iteration : " + i);
			fuelflow.update();
			Global.waitSeconds(1);
		}
		if (fuelflow.isFuelFlowing())
		{
			// All is well
		}
		else
		{
			System.out.println("Burner/powerOn no fuel flow detected : burner has tripped");
		}
	}
	public void powerOff()
	{
		burnerPower.off();
		fuelflow.update();
		
		Global.waitMilliSeconds(10);								// Need to wait a bit for relays to work and ADC to get a proper average (without voltage spikes)
		fuelflow.update();											// This should detect fuelflow off and perhaps force save
		
		if (fuelflow.isFuelFlowing())
		{
			System.out.println("Burner/powerOff and fuel flow still detected : burner has tripped");
			// What should we do here
			// This would be a big big problem
			// Perhaps close all relays
		}
		else
		{
			// All is well
		}
	}
	public void sequencer()
	{
		fuelflow.update();
		
		if (checkFault())
		{
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
