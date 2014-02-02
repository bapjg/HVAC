package eRegulation;

@SuppressWarnings("serial")
public class Message_Temperatures extends Message_Abstract
{
	private static final long 		serialVersionUID 			= 1L;
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
	
	public Float					pidMixerDifferential;
	public Integer					positionTracked;
	public Integer					pidMixerTarget;
	public Integer					tempLivingRoomTarget;
	public Float					pidBoilerOutDifferential;
}
