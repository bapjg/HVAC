package eRegulation;

public class Pump
{
	public String 			name;
	public int 				relayBank;
	public int 				relayNumber;
	public Relay			relay;
	public Boolean			isOn;
	
	public Pump(String name, String address)
	{
		this.relayBank 				= 0;
		this.name 		    		= name;
		this.relayNumber			= Integer.parseInt(address);
		this.relay					= Global.relays.fetchRelay(name);
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