package eRegulation;

@SuppressWarnings("serial")
public class Message_PID extends Message_Abstract
{
	public Message_PID()
	{
	}
	public class Data extends Message_Abstract
	{
		public Long						dateTime;
		public Integer					target;
		public Float					proportional;
		public Float					differential;
		public Float					integral;
		public Float					kP;
		public Float					kD;
		public Float					kI;
		public Float					result;	
	}
	public class Request extends Message_Abstract
	{
	}
	public class Update extends Message_PID.Data
	{
	}
}
