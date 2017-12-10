package HVAC_Common;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
@SuppressWarnings("serial")
public class Rpt_Temperatures extends Rpt_Abstract
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
