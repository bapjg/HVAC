package eRegulation;

public abstract class Message_Abstract implements java.io.Serializable
{
	private static final long 		serialVersionUID 			= 1L;
	
	public Message_Abstract()
	{
	}
	@SuppressWarnings("serial")
	public class Ack extends Message_Abstract
	{
	}
	@SuppressWarnings("serial")
	public class Nack extends Message_Abstract
	{
	}
	@SuppressWarnings("serial")
	public class Ping extends Message_Abstract
	{
	}
}
