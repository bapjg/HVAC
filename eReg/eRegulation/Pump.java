package eRegulation;

import HVAC_Common.Ctrl_Configuration;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Pump
{
	public String 												name;
	public Relay												relay;
	public Long													dateTimeLastOperated;
	
	public Pump(Ctrl_Configuration.Data.Pump 					paramPump)
	{
		this.name 		    																= paramPump.name;
		this.relay																			= Global.relays.fetchRelay(paramPump.relay);
		this.dateTimeLastOperated															= Global.DateTime.now();
		if (this.relay == null)
		{
			LogIt.error("Pump", "constructor", this.name + ", invalid relayName : " + paramPump.relay);
		}
	}
	public void on()
	{
		if (!isOn())
		{
			LogIt.action(this.name, "On");
			relay.on();
			this.dateTimeLastOperated														= Global.DateTime.now();				
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
		return relay.isOn;
	}
}