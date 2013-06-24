package eRegulation;

public class Burner
{

	public ADC	   			burnerFault;
	public Relay	   		burnerPower;
	public Integer			fuelConsumed;
	public Integer			fuelFlow;
	
	public Burner()
	{
		burnerPower				= Global.burnerPower;
		burnerFault 			= new ADC();						// ADC measure fuel flow and burner fault

		burnerPower.off();
	}
	public void powerOn()
	{
		burnerPower.on();
		// Start counting fuel flow
	}
	public void powerOff()
	{
		burnerPower.off();
		// Stop counting Fuel flow
	}
	public void sequencer()
	{
		if (checkFault())
		{
			powerOff();
		}
		// Increment fuel flow
	}
	public Boolean checkFault()
	{
		if (burnerFault.readAverage() > 4)													// more than 4V means Burner has tripped
		{
			LogIt.error("Burner", "checkFault", "Over 4 volts indicates trip");
			return false;
			//return true;
		}
		else
		{
			return false;
		}
	}
	public void updateFuelFlow()
	{
		if ((burnerFault.readAverage() > 3) && 	(burnerFault.readAverage() < 4))					// more than 3V means fuel is flowing
		{
			fuelConsumed++;
		}
	}
}
