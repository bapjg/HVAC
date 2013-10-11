package eRegulation;

@SuppressWarnings("serial")
public class Message_Temperatures extends Message_Abstract
{
	private static final long 		temporary 			= 1L;
	public Long						dateTime;
	public Integer 					tempHotWater;
	public Integer 					tempBoiler;
	public Integer 					tempBoilerIn;
	public Integer 					tempFloorOut;
	public Integer 					tempFloorCold;
	public Integer 					tempFloorHot;
	public Integer 					tempRadiatorOut;
	public Integer 					tempRadiatorIn;
	public Integer 					tempOutside;
	public Integer 					tempLivingRoom;
}
