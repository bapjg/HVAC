package eRegulation;

// Note that this algorithm relies on the first reading being correct !!!

public class Thermometer_Stabliser
{
	public Integer[] 		readings;
	public Integer			index;
	public Integer			depth;
	public Integer			tolerance;
	public Integer			count;

	public Thermometer_Stabliser(Integer readingDepth, Integer tolerance)
	{
		this.depth 		  	  			= readingDepth;
		this.index 		   			 	= 0;
		this.count						= 0;
		this.tolerance					= tolerance;
		this.readings					= new Integer[readingDepth];
	}
	public Integer add(Integer newReading)
	{
		if (count == 0)
		{
			readings[index] 			= newReading;
			index++;
			count++;
			return newReading;
		}
		else
		{
			Integer avgReading			= average();
			
			if (Math.abs(avgReading - newReading) > tolerance)
			{
				System.out.println("========Returning add average, Ecart : " + (avgReading - newReading) + " avg : " + avgReading + " rdg : " +  newReading+ " tol : " +  tolerance);
				return avgReading;
			}
			else
			{
				readings[index] 		= newReading;
				index					= index + 1 % depth;
				if (count < depth)
				{
					count++;
				}
				return newReading;
			}
		}
 	}
	public Integer average()
	{
		Integer i;
		Integer sum						= 0;
		for (i = 0; i < count; i++)
		{
			sum							= sum + readings[i];
		}
		return sum / count;
	}
}
