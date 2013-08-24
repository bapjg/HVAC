package eRegulation;

public class Pump
{
	public String 			name;
	public String 			friendlyName;
	public int 				relayBank;
	public int 				relayNumber;
	
	public Pump(String name, String address, String friendlyName)
	{
		relayBank = 0;
		
		this.name 		    		= name;
		this.friendlyName   		= friendlyName;
		this.relayNumber			= Integer.parseInt(address);
	}
	public void on()
	{
		// On(relayBank, relayNumber);
	}
	public void off()
	{
		// Off(relayBank, relayNumber);
	}
}