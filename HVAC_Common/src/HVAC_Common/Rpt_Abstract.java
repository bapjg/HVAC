package HVAC_Common;

//This class was intended as abstract. But to be able to instanciate/reference inner classes
//abstract has been removed
public class Rpt_Abstract implements java.io.Serializable
{
	private static final long 		serialVersionUID 	= 10L;
	
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