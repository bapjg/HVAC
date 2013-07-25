package eRegulation;

public abstract class Message_Abstract implements java.io.Serializable
{
//	public static final int			TYPE_Ack		 			= 1;
//	public static final int			TYPE_Nack 					= 2;
//	public static final int			TYPE_Readings		 		= 3;
//	public static final int			TYPE_Return					= 4;
//	public static final int			TYPE_X					 	= 5;
//	public static final int			TYPE_Y				 		= -1;
//	
//	public Integer					messageType					= 0;
	
	private static final long serialVersionUID = 354054054054L;
	
	public Message_Abstract()
	{
	}	public Message_Abstract(int messageType)
	{
//		this.messageType										= messageType;
	}
}
