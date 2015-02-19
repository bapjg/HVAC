package eRegulation;

import HVAC_Common.Ctrl_Configuration;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class TemperatureGradient
{
	public Integer 	outsideLow;
	public Integer 	tempLow;
	public Integer 	outsideHigh;
	public Integer 	tempHigh;


	// Coefficients in the form tempOut = a x tempOutside + b

	public Float 	a;
	public Float 	b;
	
	public TemperatureGradient(Ctrl_Configuration.TempGradient 		paramGradient)
	{
		this.outsideLow					= paramGradient.outsideLow.milliDegrees;
		this.tempLow					= paramGradient.tempLow.milliDegrees;
		this.outsideHigh				= paramGradient.outsideHigh.milliDegrees;
		this.tempHigh					= paramGradient.tempHigh.milliDegrees;
		
		Float gradient					= (this.tempHigh.floatValue() - this.tempLow.floatValue()) / (this.outsideHigh.floatValue() - this.outsideLow.floatValue());
		
		this.a							= gradient;
		
		// as y = gradient.x + b
		//    b = y - grandient.x
		
		Float intercept					= this.tempHigh.floatValue() - this.outsideHigh.floatValue() * gradient;			

		this.b							= intercept;
	}
	
	
	public Integer getTempToTarget()
	{
		Integer outsideTemp				= Global.thermoOutside.reading;
		Float	tempToTarget			= this.a * (float) outsideTemp + this.b;

		return (Integer) Math.round(tempToTarget);
	}
}
