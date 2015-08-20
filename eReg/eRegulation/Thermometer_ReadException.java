package eRegulation;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------

public class Thermometer_ReadException							extends						Exception
{
	Thermometer_ReadException(String thermometerName)
	{
		super(thermometerName);
	}
}
