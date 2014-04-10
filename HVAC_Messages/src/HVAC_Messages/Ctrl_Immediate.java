package HVAC_Messages;

@SuppressWarnings("serial")
public class Ctrl_Immediate extends Ctrl_Abstract
{
	private static final long 			serialVersionUID 			= 1L;
	public 	static final int				ACTION_Start 				= 1;
	public 	static final int				ACTION_Stop 				= 2;

	public Ctrl_Immediate()
	{
	}	
	public class Request extends Ctrl_Immediate
	{
		public String 					circuitName;
	}
	public class Data extends Ctrl_Immediate
	{
		private static final long 		serialVersionUID 			= 1L;
		public String 					circuitName;
		public Boolean					executionPlanned;
		public Boolean					executionActive;
		public Integer 					tempObjective;
		public Long 					timeStart;
		public Long 					timeEnd;
		public Boolean 					stopOnObjective;
	}
	public class Execute extends Ctrl_Immediate
	{
		public String 					circuitName;
		public Integer					action;
		public Boolean					executionPlanned;
		public Boolean					executionActive;
		public Integer 					tempObjective;
		public Long 					timeStart;
		public Long 					timeEnd;
		public Boolean 					stopOnObjective;
	}
	public class Ack extends Ctrl_Immediate
	{
		private static final long 		serialVersionUID 			= 1L;
	}
}