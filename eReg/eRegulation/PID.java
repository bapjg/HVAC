package eRegulation;

import java.util.Arrays;
import java.util.Calendar;


public class PID 
{

    private Integer 	enqueueIndex;// Separate index to ensure enqueue happens at the end
    private Integer[] 	items;
    private Integer[] 	deltas;
    private Long[] 		integrals;
    private Integer 	count;
    public 	Integer 	target;
    private Long[] 		timeStamps;
    
    public PID(Integer target, Integer size) 
    {
        enqueueIndex 					= 0;
        items 							= new Integer[size];
        deltas 							= new Integer[size];
        integrals 						= new Long[size];
        timeStamps 						= new Long[size];
        this.target 					= target;
        count							= 0;
    }
    public void setTarget(Integer target)
    {
        this.target 					= target;
    }
    public void add(Integer newNumber) 
    {
    	// Units are temp in decimal degrees (1/10 degree)
    	//           time in milliseconds
    	//           inegration decidegrees * seconds
    	//           differential decidegrees per second
    	
    	items[enqueueIndex] 			= newNumber;
    	timeStamps[enqueueIndex] 		= Calendar.getInstance().getTimeInMillis();
    	
    	if (count == 0)
    	{
    		deltas[enqueueIndex] = 0;
    	}
    	else
    	{
    		deltas[enqueueIndex] 		= newNumber - items[(items.length + enqueueIndex - 1) % items.length];			// This is dT
    	}

    	if (count == 0)
    	{
    		integrals[enqueueIndex] = 0L;
    	}
    	else
    	{
    		Long deltaTimeStamps 		= timeStamps[enqueueIndex] - timeStamps[(timeStamps.length + enqueueIndex - 1) % timeStamps.length];
    		Long integratedTime  		= newNumber.longValue() * deltaTimeStamps/1000L;
    		
    		integrals[enqueueIndex] 	= integratedTime + integrals[(integrals.length + enqueueIndex - 1) % integrals.length];			// This is items x dT
    	}

        enqueueIndex = (enqueueIndex + 1) % items.length;
           	
    	if (count < items.length)
    	{
    		count++;
    	}
    }
    public int average() 
    {
    	int i;
    	int sum = 0;
    	for (i = 0; i < count; i++)
    	{
    		sum = sum + items[i];
    	}
    	return sum/count;
    }
    public Float getGain(Float kP, Float kD, Float kI) 
    {
    	// enqueueIndex is the place for the next item to be added.
    	// hence most recently added item is enqueueIndex-1
    	// Note that Java modulo defines that result carries same sign as numurator
    	// So to get the index of index-1 (or -2) we add the modulo base to ensure a positive outcome
    	
    	Integer		index 				= (items.length + enqueueIndex - 1) % items.length;
    	Integer 	currentError 		= items[index] - target;
    	Float 		proportional 		= currentError.floatValue();
		Float 		differential 		= 0F;
		Float 		integral 			= 0F;
		Float 		result 				= 0F;
		
    	// Differential component will be average of last 2 readings to avoid pbs with misreadings
    	// Rather than calc de/dt (which can have transients due to square wave targets
    	// we go for dnewNumber/dt which is smoother. All times are saved in ms.
    	// The integration function gives a value in seconds
    	// The differential work is done here and so is multiplied by 1000 to go from ms -> s
		
    	if (count == 0)
    	{
    		differential 				= 0F;
    	}
    	else if (count == 1)
    	{
    		differential 				= 0F;
    	}
    	else if (count == 2)
    	{
    		//Units of differential are decidegrees/millisecond
    		Long 	deltaTimeStamps 	= timeStamps[index] - timeStamps[(timeStamps.length + index - 1) % timeStamps.length];
    		differential				= deltas[index].floatValue() / deltaTimeStamps;
    	}
    	else 
    	{
    		Long 	deltaTimeStamps 	= timeStamps[index] - timeStamps[(timeStamps.length + index - 2) % timeStamps.length];
    		differential				= (deltas[index] + deltas[(deltas.length + index - 1) % deltas.length].floatValue()) / deltaTimeStamps;
    	}

		integral 						= integrals[index].floatValue();
		result 							=kP * proportional + kD * differential * 1000F + kI * integral;
		
		LogIt.pidData(target, proportional, differential, integral, kP, kD, kI, result, items[index], Global.thermoBoiler.reading);

    	return result;
    }
    @Override
    public String toString() 
    {
        return Arrays.toString(items);
    }
}
