package eRegulation;

public class Message_Temperatures extends Message_Abstract
{
	private static final 	long 		serialVersionUID = 8L;
	public 					Long		dateTime;
	public  				Integer 	tempHotWater;
	public 					Integer 	tempBoiler;
	public  				Integer 	tempBoilerIn;
	public  				Integer 	tempFloorOut;
	public 					Integer 	tempFloorCold;
	public 					Integer 	tempFloorHot;
	public  				Integer 	tempRadiatorOut;
	public  				Integer 	tempRadiatorIn;
	public  				Integer 	tempOutside;
	public  				Integer 	tempLivingRoom;
	
	public Message_Temperatures()
	{
//		super(TYPE_Readings);
	}
}
