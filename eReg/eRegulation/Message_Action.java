package eRegulation;

@SuppressWarnings("serial")
public class Message_Action extends Message_Abstract
{
	private static final long 		temporary 			= 1L;
	public Long						dateTime;
	public String					device;
	public String					action;
}
