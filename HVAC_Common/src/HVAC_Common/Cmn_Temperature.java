package HVAC_Common;

import java.text.DecimalFormat;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Cmn_Temperature									implements					java.io.Serializable
{
	private static final long 									serialVersionUID 			= 1L;
	public Integer												milliDegrees;
	
	public Cmn_Temperature(Integer milliDegrees)
	{
		this.milliDegrees																	= milliDegrees;
	}
	public Cmn_Temperature(String degrees)
	{
		this.milliDegrees																	= ((Float) (Float.parseFloat(degrees) * 1000F)).intValue();
	}
	public String displayInteger()
	{
		return new DecimalFormat("#").format(milliDegrees/1000.0) + " °C";
	}
	public String displayDecimal()
	{
		return new DecimalFormat("#.#").format(milliDegrees/1000.0) + " °C";
	}
	public void setDegrees(String decimal)
	{
		milliDegrees																		= (int) (Float.parseFloat(decimal)	* 1000F);
	}
}
