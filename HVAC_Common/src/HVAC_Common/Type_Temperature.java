package HVAC_Common;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Type_Temperature										implements 					java.io.Serializable
{
	private static final long 									serialVersionUID 			= 1L;
	public Integer												milliDegrees;
	
	public Type_Temperature(Integer milliDegrees)
	{
		this.milliDegrees																	= milliDegrees;
	}
	public Type_Temperature(String degrees)
	{
		this.milliDegrees																	= ((Float) (Float.parseFloat(degrees) * 1000F)).intValue();
	}
	public String displayInteger()
	{
		Integer								degrees											= (milliDegrees/100 + 5)/10;
		return degrees.toString() + " °C";
	}
	public String displayDecimal()
	{
		Integer								deciDegrees										= (milliDegrees/10 + 5)/10;
		Integer								units											= deciDegrees/10;
		Integer								decimal											= deciDegrees - units*10;
		return units.toString() + "." + decimal + " °C";
	}
	public void setMilliDegrees(String decimal)
	{
		milliDegrees																		= (int) (Float.parseFloat(decimal)	* 1000F);
	}
}

