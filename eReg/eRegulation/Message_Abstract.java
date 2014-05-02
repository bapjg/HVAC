package eRegulation;

//This class was intended as abstract. But to be able to instanciate/reference inner classes
//abstract has been removed
public class Message_Abstract implements java.io.Serializable
{
	private static final long 		serialVersionUID 	= 1L;
//	private static final long 		temporary 			= 1L;
	
	@SuppressWarnings("serial")
	public class Ack extends Message_Abstract
	{
			int x = 1;
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