package eRegulation;

public class Thermometer_Stabiliser
{
	public String	 		name;
	public Integer[] 		readings;
	public Integer			index;
	public Integer			depth;
	public Integer			tolerance;
	public Integer			count;

	public Thermometer_Stabiliser(String name, Integer depth, Integer tolerance)
	{
		this.name 		  	  			= name;
		this.depth 		  	  			= depth;
		this.index 		   			 	= 0;									// index is for next entry.
		this.count						= 0;
		this.tolerance					= tolerance;
		this.readings					= new Integer[depth];
	}
	public Integer add(Integer newReading)
	{
		if (count == 0)
		{
			readings[index] 			= newReading;
			index++;
			count++;
    		if (this.name.equalsIgnoreCase("Boiler"))
    		{
    			System.out.println("10 Reading_Stabiliser/add Returning reading " + newReading); //uSEFULL
    		}
    		return newReading;
		}
		else
		{
			Integer avgReading			= average();
			
			if (Math.abs(avgReading - newReading) > tolerance)
			{
				readings[index] 		= newReading;				//	Add it to the chain, otherwise we cannot change the average
				index					= index + 1 % depth;
				if (count < depth)
				{
					count++;
				}
				System.out.println("11 Reading_Stabiliser/add ======== Returning add average, Ecart : " + (avgReading - newReading) + " avg : " + avgReading + " rdg : " +  newReading+ " tol : " +  tolerance);
   	    		if (this.name.equalsIgnoreCase("Boiler"))
	    		{
	    			System.out.println("12 Reading_Stabiliser/add ======== Returning average " + avgReading); //uSEFULL
	    		}
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
	    		if (this.name.equalsIgnoreCase("Boiler"))
	    		{
	    			System.out.println("13 Reading_Stabiliser/add ======== Returning reading " + newReading); //uSEFULL
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