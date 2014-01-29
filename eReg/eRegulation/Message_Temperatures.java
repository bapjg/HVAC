package eRegulation;

@SuppressWarnings("serial")
public class Message_Temperatures extends Message_Abstract
{
	private static final long 		temporary 			= 1L;
	public Long						dateTime;
	
	public Integer 					tempBoiler;
	public Integer 					tempBoilerIn;
	public Integer 					tempBoilerOut;
	
	public Integer 					tempFloorIn;
	public Integer 					tempFloorOut;
	
	public Integer 					tempRadiatorOut;
	public Integer 					tempRadiatorIn;

	public Integer 					tempHotWater;
	public Integer 					tempOutside;
	public Integer 					tempLivingRoom;
	
	public Float					pid_dTdt;
}
