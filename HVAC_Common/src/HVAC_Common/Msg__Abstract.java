package HVAC_Common;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Msg__Abstract 										implements 					java.io.Serializable
{
	@SuppressWarnings("serial")	public class Ack 				extends Msg__Abstract		{}
	@SuppressWarnings("serial")	public class Nack 				extends Msg__Abstract		
	{
		public String											errorMessage;
		public Nack ()							{	this.errorMessage		= "";				}
		public Nack (String errorMessage)		{	this.errorMessage		= errorMessage;		}
	}
	@SuppressWarnings("serial")	public class Ping 				extends Msg__Abstract		{}
	@SuppressWarnings("serial")	public class TimeOut 			extends Msg__Abstract		{}
	@SuppressWarnings("serial")	public class NoData 			extends Msg__Abstract		{}
	@SuppressWarnings("serial")	public class NoConnection 		extends Msg__Abstract		{}
}
