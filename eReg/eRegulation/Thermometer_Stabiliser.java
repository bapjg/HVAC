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
   		if (this.name.equalsIgnoreCase("Boiler")) {	System.out.println("10 Reading_Stabiliser/add WE ARE IN reading/count : " + newReading + "/" + count); }

   		if (count == 0)
		{
			readings[index] 			= newReading;
			index++;
			count++;
    		if (this.name.equalsIgnoreCase("Boiler")) {	System.out.println("10 Reading_Stabiliser/add Returning reading " + newReading); }
    		return newReading;
		}
		else
		{
	   		if (this.name.equalsIgnoreCase("Boiler")) {	System.out.println("10 Reading_Stabiliser/add calling average"); }
			Integer avgReading			= average();
	   		if (this.name.equalsIgnoreCase("Boiler")) {	System.out.println("10 Reading_Stabiliser/add called average : " +avgReading); }
			
			if (Math.abs(avgReading - newReading) > tolerance)
			{
		   		if (this.name.equalsIgnoreCase("Boiler")) {	System.out.println("11 ++ Reading_Stabiliser/add Within Tolerance "); }
				readings[index] 		= newReading;				//	Add it to the chain, otherwise we cannot change the average
				index					= index + 1 % depth;
				if (count < depth)
				{
					count++;
				}
   	    		if (this.name.equalsIgnoreCase("Boiler")) {	System.out.println("12 Reading_Stabiliser/add ======== Returning average " + avgReading);}
				return avgReading;
			}
			else
			{
		   		if (this.name.equalsIgnoreCase("Boiler")) {	System.out.println("11 -- Reading_Stabiliser/add Outside Tolerance "); }
				readings[index] 		= newReading;
				index					= index + 1 % depth;
				if (count < depth)
				{
					count++;
				}
	    		if (this.name.equalsIgnoreCase("Boiler")) { System.out.println("13 Reading_Stabiliser/add ======== Returning reading " + newReading); }
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