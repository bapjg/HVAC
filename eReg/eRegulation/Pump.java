package eRegulation;

public class Pump
{
	public String 			name;
	public Relay			relay;
	public Boolean			isOn;
	
	public Pump(String name)
	{
		this.name 		    		= name;
		this.relay					= Global.relays.fetchRelay(name);
		this.isOn					= false;
	}
	public void on()
	{
		if (!isOn)
		{
			LogIt.action(this.name, "On");
			relay.on();
			isOn					= true;
		}
	}
	public void off()
	{
		if (isOn)
		{
			LogIt.action(this.name, "Off");
			relay.off();
			isOn					= false;
		}
	}
}