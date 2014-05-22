package HVAC_Messages;

//This class was intended as abstract. But to be able to instanciate/reference inner classes
//abstract has been removed
public class Rpt_Abstract implements java.io.Serializable
{
	private static final long 		serialVersionUID 	= 1L;
//	private static final long 		temporary 			= 1L;
	
	@SuppressWarnings("serial")
	public class Ack extends Rpt_Abstract
	{
	}
	@SuppressWarnings("serial")
	public class Nack extends Rpt_Abstract
	{
	}
	@SuppressWarnings("serial")
	public class Ping extends Rpt_Abstract
	{
	}
}