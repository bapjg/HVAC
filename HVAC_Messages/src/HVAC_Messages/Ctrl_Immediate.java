package HVAC_Messages;

@SuppressWarnings("serial")
public class Ctrl_Immediate extends Ctrl_Abstract
{
	private static final long 			serialVersionUID 			= 1L;

	public Ctrl_Immediate()
	{
	}	
	public class Request extends Ctrl_Immediate
	{
	}
	public class Data extends Ctrl_Immediate
	{
		private static final long 		serialVersionUID 			= 1L;
		public Boolean					executionPlanned;
		public Boolean					executionActive;
		public Integer 					tempObjective;
		public Long 					timeStart;
	}
	public class Execute extends Ctrl_Immediate
	{
		private static final long 		serialVersionUID 			= 1L;
		public String 					circuitName;
		public Integer 					tempObjective;
		public Long 					timeEnd;
		public Boolean 					stopOnObjective;
		public Boolean 					start;
	}
	public class Ack extends Ctrl_Immediate
	{
		private static final long 		serialVersionUID 			= 1L;
	}
}