package eRegulation;

public class Pump
{
	public String 			name;
	public int 				relayBank;
	public int 				relayNumber;
	public Relay			relay;
	
	public Pump(String name, String address)
	{
		this.relayBank 				= 0;
		this.name 		    		= name;
		this.relayNumber			= Integer.parseInt(address);
		this.relay					= Global.relays.fetchRelay(name);
	}
	public void on()
	{
		LogIt.action(this.name, "On");
		relay.on();
	}
	public void off()
	{
		LogIt.action(this.name, "Off");
		relay.off();
	}
}