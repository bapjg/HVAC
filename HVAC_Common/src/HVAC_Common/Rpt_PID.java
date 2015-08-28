package HVAC_Common;


public class Rpt_PID extends Rpt_Abstract
{
	private static final long 		serialVersionUID 			= 1L;
	
	public Rpt_PID()
	{
	}
	public class Data extends Rpt_Abstract
	{
		private static final long 		serialVersionUID 		= 1L;
		public Long						dateTime;
		
		public Integer					target;
		public Integer 					tempCurrent;
		public Integer 					tempCurrentError;
		
		public Integer					termProportional;
		public Integer					termDifferential;
		public Integer					termIntegral;

		public Integer					gainProportional;
		public Integer					gainDifferential;
		public Integer					gainIntegral;
		
		public Float					kP;
		public Float					kD;
		public Float					kI;
		
		public Integer					gainTotal;	
		
		public Integer					tempOut;	
		public Integer					tempBoiler;	
		public Integer					positionTracked;
		
		public Boolean					startMovement;
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
