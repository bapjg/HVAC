package HVAC_Common;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public abstract class Msg__Abstract_Test 							implements 					java.io.Serializable
{
	private static final long 									serialVersionUID 			= 1L;

	public enum Status
	{
		Ack,
		Nack,
		TimeOut,
		NotConnected,
		Else
	}	
	public  Status												status;																					 
	public  String												errorMessage;
	
	public Msg__Abstract_Test()
	{
	}
	public void setAck()
	{
		this.status																			= Status.Ack;
	}
	public void setNack(String errorMessage)
	{
		this.status																			= Status.Nack;
		this.errorMessage																	= errorMessage;
	}
}
