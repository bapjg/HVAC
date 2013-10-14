package eRegulation;

public class Thermometer_Stabiliser_ThrowAway
{
	private String	 		name;
	private Integer[] 		readings;
	private Integer			readingIndex;
	private Integer			depth;
	private Integer			tolerance;
	private Integer			count;

	public Thermometer_Stabiliser_ThrowAway(String name, Integer depth, Integer tolerance)
	{
		this.name 		  	  			= name;
		this.depth 		  	  			= depth;
		this.readingIndex 		   		= 0;									// index is for next entry.
		this.count						= 0;
		this.tolerance					= tolerance;
		this.readings					= new Integer[depth];
	}
	public Integer add(Integer newReading)
	{
   		Integer result					= 0;
		if (count == 0)
		{
			readings[readingIndex] 		= newReading;
			readingIndex++;
			count++;
			result 						= newReading;
		}
		else
		{
			Integer avgReading			= average();
			
			if (Math.abs(avgReading - newReading) < tolerance)
			{
				result 					= newReading;				// Within tolerance, return the reading
			}
			else
			{
				result 					= avgReading;				// Outside tolerance, return the reading
			}
			readings[readingIndex] 		= newReading;				// Add reading to the chain, even if out of tolerance, otherwise we cannot change the average
			readingIndex				= (readingIndex + 1) % depth;
			if (count < depth)
			{
				count++;
			}
		}
		return result;
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