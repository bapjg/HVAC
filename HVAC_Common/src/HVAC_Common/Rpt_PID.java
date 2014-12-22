package HVAC_Common;


public class Rpt_PID extends Rpt_Abstract
{
	private static final long 		serialVersionUID 			= 1L;
	
	public Rpt_PID()
	{
	}
	public class Data extends Rpt_Abstract
	{
		private static final long 		serialVersionUID 			= 1L;
		public Long						dateTime;
		public Integer					target;
		public Integer 					tempCurrent;
		public Integer 					tempCurrentError;
		public Float					proportional;
		public Float					differential;
		public Float					integral;
		public Float					kP;
		public Float					kD;
		public Float					kI;
		public Float					result;	
		public Integer					tempOut;	
		public Integer					tempBoiler;	
		public Integer					positionTracked;
		public Boolean					beforeMovement;
	}
	public class Request extends Rpt_Abstract
	{
	}
	public class Update extends Rpt_PID.Data
	{
		public Update()
		{
			this.dateTime 																	= System.currentTimeMillis();
		}
	}
}
