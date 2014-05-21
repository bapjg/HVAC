package HVAC_Types;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Temperature										implements 					java.io.Serializable
{
	private static final long 									serialVersionUID 			= 1L;
	public Integer												milliDegrees;
	
	public Temperature(Integer milliDegrees)
	{
		this.milliDegrees																	= milliDegrees;
	}
	public String displayInteger()
	{
		Integer								degrees											= (milliDegrees/100 + 5)/10;
		return degrees.toString();
	}
	public String displayDecimal()
	{
		Integer								deciDegrees										= (milliDegrees/10 + 5)/10;
		Integer								units											= deciDegrees/10;
		Integer								decimal											= deciDegrees - units*10;
		return units.toString() + "." + decimal;
	}
	public void setMilliDegrees(String decimal)
	{
		milliDegrees																		= (int) (Float.parseFloat(decimal)	* 1000F);
	}
}

