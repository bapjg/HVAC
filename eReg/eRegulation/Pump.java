package eRegulation;

import HVAC_Messages.Ctrl_Configuration;

public class Pump
{
	public String 			name;
	public Relay			relay;
	public Long				dateLastOperated;
	
//	public Pump(String name, String relayName)
//	{
//		this.name 		    		= name;
//		this.relay					= Global.relays.fetchRelay(relayName);
//		if (this.relay == null)
//		{
//			System.out.println("Relay.Constructor Pump : " + this.name + ", invalid relayName : " +relayName);
//		}
//	}
	public Pump(Ctrl_Configuration.Data.Pump 						paramPump)
	{
		this.name 		    		= paramPump.name;
		this.relay					= Global.relays.fetchRelay(paramPump.relay);
		this.dateLastOperated		= 0L;
		if (this.relay == null)
		{
			System.out.println("Relay.Constructor Pump : " + this.name + ", invalid relayName : " + paramPump.relay);
		}
	}
	public void on()
	{
		if (!isOn())
		{
			LogIt.action(this.name, "On");
			relay.on();
			this.dateLastOperated	= Global.today();				
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