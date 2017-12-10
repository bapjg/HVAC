package eRegulation;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Thermometer_ReadException							extends						Exception
{
	String thermoName;
	String thermoAddress;
	
	Thermometer_ReadException(String thermometerName, String thermometerAddress)
	{
		super(thermometerName);
		this.thermoName = thermometerName;
		this.thermoAddress = thermometerAddress;
	}
}
