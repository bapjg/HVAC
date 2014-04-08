package HVAC_Messages;

@SuppressWarnings("serial")
public class Ctrl_Actions_HotWater extends Ctrl_Abstract
{
	private static final long 			serialVersionUID 			= 1L;

	public Ctrl_Actions_HotWater()
	{
	}	
	public class Request extends Ctrl_Actions_HotWater
	{
	}
	public class Data extends Ctrl_Actions_HotWater
	{
		private static final long 		serialVersionUID 			= 1L;
		public Boolean					executionPlanned;
		public Boolean					executionActive;
		public Integer 					tempObjective;
		public Long 					timeStart;
	}
	public class Execute extends Ctrl_Actions_HotWater
	{
		private static final long 		serialVersionUID 			= 1L;
		public Integer 					tempObjective;
	}
	public class Ack extends Ctrl_Actions_HotWater
	{
		private static final long 		serialVersionUID 			= 1L;
	}
}