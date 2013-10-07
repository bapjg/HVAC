package eRegulation;

@SuppressWarnings("serial")
public class Message_Report extends Message_Abstract
{
	public Long						dateTime;
	public String					reportType;
	public String					className;
	public String					methodName;
	public String					reportText;
}
