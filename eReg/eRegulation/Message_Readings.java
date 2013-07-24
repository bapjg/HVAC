package eRegulation;
public class Message_Readings extends Message_Abstract
{
	Long		dateTime;
	Integer 	tempHotWater;
	Integer 	tempBoiler;
	Integer 	tempBoilerIn;
	Integer 	tempFloorOut;
	Integer 	tempFloorCold;
	Integer 	tempFloorHot;
	Integer 	tempRadiatorOut;
	Integer 	tempRadiatorIn;
	Integer 	tempOutside;
	Integer 	tempLivingRoom;
	Long		fuelConsumed;
	
	public Message_Readings()
	{
		
	}
}
