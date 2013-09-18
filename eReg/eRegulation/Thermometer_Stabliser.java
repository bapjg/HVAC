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
		System.out.println("In add");
		System.out.println(readings.length);
		System.out.println("In add again");
		
		if (readings.length == 0)
		{
			System.out.println("In add == 0");
			readings[index] 			= newReading;
			index++;
			System.out.println("Return add read 0");
			return newReading;
		}
		else
		{
			System.out.println("In add else");
			Integer avgReading			= average();
			
			if (Math.abs(avgReading - newReading) > tolerance)
			{
				System.out.println("Return add average");
				return avgReading;
			}
			else
			{
				readings[index] 		= newReading;
				index					= index + 1 % depth;
				System.out.println("Return add index : " + index);
				System.out.println("Return add result : " + newReading);
				return newReading;
			}
		}
 	}
	public Integer average()
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
