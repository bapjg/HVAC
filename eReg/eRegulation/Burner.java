package eRegulation;

public class Burner
{

	public 	ADC	   			burnerVoltages;							//circa 2.5V = fuel flowing and 4.5V = fault
	public 	Relay	   		burnerPower;
	public 	Long			fuelFlowTimeCumulated;
	private Long			fuelFlowTimeLastStart;
	private	Boolean			fuelIsFlowing;
	
	public Burner()
	{
		burnerPower				= Global.burnerPower;
		burnerVoltages 			= new ADC();						// ADC measure fuel flow and burner fault

		burnerPower.off();
		fuelFlowTimeCumulated	= 0L;
		fuelIsFlowing			= false;
	}
	public void powerOn()
	{
		burnerPower.on();
		// Fuel flow will be detected in sequencer
	}
	public void powerOff()
	{
		burnerPower.off();
		
		Global.waitMilliSeconds(10);								// Need to wait a bit for relays to work and ADC to get an average
		
		if (checkFuelFlow())		// This updates Fuel Consumption
		{
			System.out.println("Burner.powerOff and fuel is still flowing");
		}
		
		
		
		// Stop counting Fuel flow
	}
	public void sequencer()
	{
		if (checkFault())
		{
			powerOff();
			System.out.println("A Burner Fault has been detected by the burner Sequencer");
		}
		// Increment fuel flow
		checkFuelFlow();
		// Must also check max temp;
	}
	public Boolean checkFault()
	{
		if (burnerVoltages.isFault())
		{
			LogIt.error("Burner", "checkFault", "Over 4 volts indicates trip");
			return true;
		}
		else
		{
			return false;
		}
	}
	public Boolean checkFuelFlow()
	{
		Boolean fuelFlowDetected = burnerVoltages.isFuelFlowing();
		
		if (fuelFlowDetected)
		{
			if (fuelIsFlowing)
			{
				// This means that fuel flow start has been previously detected
			}
			else
			{
				// Fuel flow has just been detected
				fuelFlowTimeLastStart	= Global.now();
				
			}
			fuelIsFlowing				= true;
			return true;
		}
		else
		{
			if (fuelIsFlowing)
			{
				// This means that fuel flow start has been previously detected
				// Fuel flow has been turned off
				fuelFlowTimeCumulated	= fuelFlowTimeCumulated + Global.now() - fuelFlowTimeLastStart;
				//
				// This is the place to write to disk
				//
				//
				
			}
			else
			{
				// Fuel flow was not on and has been detected as still being not on
				// Nothing to do
			}
			fuelIsFlowing				= false;
			return false;
		}
	}
}
