package HVAC_Common;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Type_Integer										implements 					java.io.Serializable
{
	private static final long 									serialVersionUID 			= 1L;
	public Integer												value;
	public String												units;
	
	public Type_Integer(Integer value, String units)
	{
		this.value																			= value;
		this.units																			= units;
	}
	public String getValue()
	{
		return this.value + " " + this.units;
	}
}

