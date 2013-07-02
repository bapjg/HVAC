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
		// Fuel flow will be detected in sequencer, as there is an approx 10s delay before fuel starts flowing
	}
	public void powerOff()
	{
		burnerPower.off();
		fuelflow.update();
		
		Global.waitMilliSeconds(10);								// Need to wait a bit for relays to work and ADC to get an average
		fuelflow.update();											// This should force save
		
//		if (checkFuelFlow())		// This updates Fuel Consumption
//		{
//			System.out.println("Burner.powerOff and fuel is still flowing");
//		}
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
