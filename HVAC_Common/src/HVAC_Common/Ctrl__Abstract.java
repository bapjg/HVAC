package HVAC_Common;


//This class was intended as abstract. But to be able to instanciate/reference inner classes
//abstract has been removed

// TODO

public class Ctrl__Abstract implements java.io.Serializable
{
	private int x = 3;
	private static final long 		serialVersionUID 	= 10L;
	
	@SuppressWarnings("serial")
	public class Ack extends Ctrl__Abstract
	{
	}
	@SuppressWarnings("serial")
	public class Nack extends Ctrl__Abstract
	{
	}
	@SuppressWarnings("serial")
	public class Ping extends Ctrl__Abstract
	{
	}
	@SuppressWarnings("serial")
	public class TimeOut extends Ctrl__Abstract
	{
	}
	@SuppressWarnings("serial")
	public class NoData extends Ctrl__Abstract
	{
	}
	@SuppressWarnings("serial")
	public class NoConnection extends Ctrl__Abstract
	{
	}
}