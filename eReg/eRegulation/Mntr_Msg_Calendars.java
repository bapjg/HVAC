package eRegulation;

@SuppressWarnings("serial")
public class Mntr_Msg_Calendars extends Message_Abstract
{
	public String					dateTime;
	public String					calendars;
	
	public Mntr_Msg_Calendars()
	{
	}
	public class Request extends Message_Abstract
	{
	}
	public class Data extends Mntr_Msg_Calendars
	{
	}
}
