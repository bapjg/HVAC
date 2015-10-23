package eRegulation;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Heat_Required
{
	Integer														tempMinimum;
	Integer														tempMaximum;
	
	public Heat_Required()
	{
		this.setZero();
	}
	public void setZero()
	{
		this.tempMinimum 																	= 0;
		this.tempMaximum 																	= 0;
	}
	public void set(Integer tempMinimum, Integer tempMaximum)
	{
		this.tempMinimum 																	= tempMinimum;
		this.tempMaximum 																	= tempMaximum;
	}
	public void set(Integer tempMaximum)
	{
		this.tempMaximum 																	= tempMaximum;
	}
}
