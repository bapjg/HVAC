package eRegulation;

// Note that this algorithm relies on the first reading being correct !!!

public class Thermometer_Stabliser
{
	public Integer[] 		readings;
	public Integer			index;
	public Integer			depth;
	public Integer			tolerance;

	public Thermometer_Stabliser(Integer readingDepth, Integer tolerance)
	{
		this.depth 		  	  			= readingDepth;
		this.index 		   			 	= 0;
		this.tolerance					= tolerance;
	}
	public Integer add(Integer newReading)
	{
		if (readings.length == 0)
		{
			readings[index] 			= newReading;
			index++;
			return newReading;
		}
		else
		{
			Integer avgReading			= average();
			
			if (Math.abs(avgReading - newReading) > tolerance)
			{
				return avgReading;
			}
			else
			{
				readings[index] 		= newReading;
				index					= index + 1 % depth;
				return newReading;
			}
		}
 	}
	private Integer average()
	{
		Integer i;
		Integer sum						= 0;
		for (i = 0; i < readings.length; i++)
		{
			sum							+= readings[i];
		}
		return sum / readings.length;
	}

}
