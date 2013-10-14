package eRegulation;

public class Thermometer_Stabiliser
{
	private String	 		name;
	private Integer[] 		readings;
	private Integer			readingIndex;
	private Integer			depth;
	private Integer			tolerance;
	private Integer			count;

	public Thermometer_Stabiliser(String name, Integer depth, Integer tolerance)
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
   		if (this.name.equalsIgnoreCase("Boiler")) {	System.out.println("10 Reading_Stabiliser/add WE ARE IN reading/count/index : " + newReading + "/" + count + "/" + readingIndex); }

   		if (count == 0)
		{
			readings[readingIndex] 		= newReading;
			readingIndex++;
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
				readings[readingIndex] 		= newReading;				//	Add it to the chain, otherwise we cannot change the average
				readingIndex					= (readingIndex + 1) % depth;
				if (count < depth)
				{
					count++;
				}
   	    		if (this.name.equalsIgnoreCase("Boiler")) {	System.out.println("12 Reading_Stabiliser/add ======== Returning average " + avgReading);}
				return avgReading;
			}
			else
			{
		   														if (this.name.equalsIgnoreCase("Boiler")) {	System.out.println("11 ++ Reading_Stabiliser/add Within Tolerance & index is " + this.readingIndex); }
				this.readings[readingIndex] 	= newReading;
		   														if (this.name.equalsIgnoreCase("Boiler")) {	System.out.println("11 ++ Reading_Stabiliser/add reading(index) added    index is " + readingIndex); }
		   		this.readingIndex				= (this.readingIndex + 1) % depth;
		   														if (this.name.equalsIgnoreCase("Boiler")) {	System.out.println("11 ++ Reading_Stabiliser/add reading(index) adjusted index is " + readingIndex); }
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