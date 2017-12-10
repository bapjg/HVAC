package HVAC_Common;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public abstract class Msg__Abstract 							implements 					java.io.Serializable
{
	private static final long 									serialVersionUID 			= 1L;
	
	public class Ack 											extends 					Msg__Abstract
	{
		private static final long 								serialVersionUID 			= 1L;
	}
	public class Nack 											extends 					Msg__Abstract
	{
		private static final long 								serialVersionUID 			= 1L;
	}

}
