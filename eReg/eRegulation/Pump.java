package eRegulation;

public class Pump
{
	public String 			name;
	public Relay			relay;
	
	public Pump(String name, String relayName)
	{
		this.name 		    		= name;
		this.relay					= Global.relays.fetchRelay(relayName);
	}
	public void on()
	{
		if (!isOn())
		{
			LogIt.action(this.name, "On");
			relay.on();
		}
	}
	public void off()
	{
		if (isOn())
		{
			LogIt.action(this.name, "Off");
			relay.off();
		}
	}
	public Boolean isOn()
	{
		return relay.isOn();
	}
}