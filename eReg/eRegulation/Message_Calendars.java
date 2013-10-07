package eRegulation;

@SuppressWarnings("serial")
public class Message_Calendars extends Message_Abstract
{
	public String					dateTime;
	public String					calendars;
	
	public Message_Calendars()
	{
	}
	public class Request extends Message_Abstract
	{
	}
	public class Data extends Message_Calendars
	{
	}
}
