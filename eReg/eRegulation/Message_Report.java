package eRegulation;

public class Message_Report extends Message_Abstract
{
	private static final 	long 		serialVersionUID = 9L;
	public 					Long		dateTime;
	public 					String		reportType;
	public 					String		className;
	public 					String		methodName;
	public 					String		reportText;
	
	public Message_Report()
	{
	}
}
