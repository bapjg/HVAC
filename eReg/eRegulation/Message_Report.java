package eRegulation;

@SuppressWarnings("serial")
public class Message_Report extends Message_Abstract
{
	public Long						dateTime;
	private static final long 		temporary 			= 1L;
	public String					reportType;
	public String					className;
	public String					methodName;
	public String					reportText;
}
