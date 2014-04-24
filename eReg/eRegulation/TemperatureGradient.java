package eRegulation;

import HVAC_Messages.Ctrl_Configuration;

public class TemperatureGradient
{
	public Integer 	outsideLow;
	public Integer 	tempLow;
	public Integer 	outsideHigh;
	public Integer 	tempHigh;


	// Coefficients in the form tempOut = a x tempOutside + b

	public Float 	a;
	public Float 	b;
	
//	public TemperatureGradient(String outsideLow, String tempLow, String outsideHigh, String tempHigh)
//	{
//		this.outsideLow					= Integer.parseInt(outsideLow);
//		this.tempLow					= Integer.parseInt(tempLow);
//		this.outsideHigh				= Integer.parseInt(outsideHigh);
//		this.tempHigh					= Integer.parseInt(tempHigh);
//		
//		Float gradient					= (this.tempHigh.floatValue() - this.tempLow.floatValue()) / (this.outsideHigh.floatValue() - this.outsideLow.floatValue());
//		
//		this.a							= gradient;
//		
//		// as y = gradient.x + b
//		//    b = y - grandient.x
//		
//		Float intercept					= this.tempHigh.floatValue() - this.outsideHigh.floatValue() * gradient;			
//
//		this.b							= intercept;
//	}
//	public TemperatureGradient(Integer outsideLow, Integer tempLow, Integer outsideHigh, Integer tempHigh)
//	{
//		this.outsideLow					= outsideLow;
//		this.tempLow					= tempLow;
//		this.outsideHigh				= outsideHigh;
//		this.tempHigh					= tempHigh;
//		
//		Float gradient					= (this.tempHigh.floatValue() - this.tempLow.floatValue()) / (this.outsideHigh.floatValue() - this.outsideLow.floatValue());
//		
//		this.a							= gradient;
//		
//		// as y = gradient.x + b
//		//    b = y - grandient.x
//		
//		Float intercept					= this.tempHigh.floatValue() - this.outsideHigh.floatValue() * gradient;			
//
//		this.b							= intercept;
//	}
	public TemperatureGradient(Ctrl_Configuration.TempGradient 		paramGradient)
	{
		this.outsideLow					= paramGradient.outsideLow;
		this.tempLow					= paramGradient.tempLow;
		this.outsideHigh				= paramGradient.outsideHigh;
		this.tempHigh					= paramGradient.tempHigh;
		
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
