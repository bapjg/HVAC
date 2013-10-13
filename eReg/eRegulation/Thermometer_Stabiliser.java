package eRegulation;

public class Thermometer_Stabiliser
{
	private String	 		name;
	private Integer[] 		readings;
	private Integer			index;
	private Integer			depth;
	private Integer			tolerance;
	private Integer			count;

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
		   		if (this.name.equalsIgnoreCase("Boiler")) {	System.out.println("11 -- Reading_Stabiliser/add Outside Tolerance "); }
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
		   		if (this.name.equalsIgnoreCase("Boiler")) {	System.out.println("11 ++ Reading_Stabiliser/add Within Tolerance "); }
				this.readings[index] 	= newReading;
		   		if (this.name.equalsIgnoreCase("Boiler")) {	System.out.println("11 ++ Reading_Stabiliser/add reading(index) added    index is " + index); }
		   		this.index				= index + 1 % depth;
		   		if (this.name.equalsIgnoreCase("Boiler")) {	System.out.println("11 ++ Reading_Stabiliser/add reading(index) adjusted index is " + index); }
				if (count < depth)
				{
			   		if (this.name.equalsIgnoreCase("Boiler")) {	System.out.println("11 ++ Reading_Stabiliser/add count < depth " + count); }
					count++;
			   		if (this.name.equalsIgnoreCase("Boiler")) {	System.out.println("11 ++ Reading_Stabiliser/add count  is now " + count); }
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