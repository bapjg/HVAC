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
		System.out.println("============In add");
		
		if (count == 0)
		{
			System.out.println("In add == 0 : reading is " + newReading);
			readings[index] 			= newReading;
			index++;
			count++;
			System.out.println("Return add read 0");
			return newReading;
		}
		else
		{
			System.out.println("In add else");
			Integer avgReading			= average();
			
			if (Math.abs(avgReading - newReading) > tolerance)
			{
				System.out.println("Return add average : " + avgReading);
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
		for (i = 0; i < count; i++)
		{
			sum							= sum + readings[i];
		}
		System.out.println("sum/count : " + sum + " / " + count);
		return sum / count;
	}
}
