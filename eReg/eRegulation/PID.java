package eRegulation;

import java.util.Arrays;
import java.util.Calendar;


public class PID 
{
    private Integer 	enqueueIndex;	// Separate index to ensure enqueue happens at the end
    private Integer[] 	items;			// Proportional component, stored unit = decidegrees (we store values, not differences to avoid pbs with 
    private Integer[] 	deltas;			// Differential component, stored unit = decidegrees  sudden target changes with sudden target changes)
    private Long[] 		integrals;		// Integral component,     stored unit = decidegree from target x seconds
    private Integer 	count;
    public 	Integer 	target;
    private Long[] 		timeStamps;		//                         stored unit = milliseconds
    
    public PID(Integer size) 
    {
        enqueueIndex 					= 0;
        items 							= new Integer[size];
        deltas 							= new Integer[size];
        integrals 						= new Long[size];
        timeStamps 						= new Long[size];
        this.target 					= 0;
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
    	//           differential decidegrees per second (this is not handled here, but is handles in getGain()
    	
    	Integer previousIndex			= 0;
    	items[enqueueIndex] 			= newNumber;
    	timeStamps[enqueueIndex] 		= Calendar.getInstance().getTimeInMillis();
    	
    	if (count == 0)
    	{
    		deltas[enqueueIndex] = 0;
    		integrals[enqueueIndex] = 0L;
    	}
    	else
    	{
    		// previous index is enqueueIndex -1 modulo length. We add queue length to avoid negative values 
    		previousIndex				= (enqueueIndex - 1 + timeStamps.length) % timeStamps.length;
    		
    		// Calculate dTemp. Note that it is independant of the target (rate of change)
    		deltas[enqueueIndex] 		= newNumber - items[previousIndex];							
    		
    		Long deltaTimeStamps 		= timeStamps[enqueueIndex] - timeStamps[previousIndex];
    		Long decidegreeSeconds		= (newNumber.longValue() - target.longValue()) * deltaTimeStamps/1000L;		// decidegreeSeconds = offTarget x seconds
    		
    		integrals[enqueueIndex] 	= decidegreeSeconds + integrals[previousIndex];			// This is items x.dT
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
    public Float dTdt() 
    {
		Float 		differential 		= 0F;								// unit = decigrees/second 
    	Integer		indexCurrent		= (enqueueIndex - 1 + items.length) % items.length;
    	Integer		indexPrevious		= (enqueueIndex - 2 + items.length) % items.length;
    	if (count <= 1)
    	{
    		differential 				= 0F;
    	}
    	else
    	{
    		//Units of differential are decidegrees/millisecond
    		Long 	deltaTimeStamps 	= timeStamps[indexCurrent] - timeStamps[indexPrevious];
    		differential				= 1000F * deltas[indexCurrent].floatValue() / deltaTimeStamps;	// in decidegrees per second
    	}

    	return differential;
    }
    public Float getGain(Float kP, Float kD, Float kI) 
    {
    	// Parameters are
    	//    kP = number of milliseconds swingTime per decidegree
    	//    kD = number of milliseconds swingTime per decidegree/second change in temperature = number of seconds over which to calculate x kP
    	//    kI = number of milliseconds swingTime per decidegree x seconds cumulated off target error
    	
    	
    	// enqueueIndex is the place for the next item to be added.
    	// hence most recently added item is enqueueIndex-1
    	// Note that Java modulo defines that result carries same sign as numurator
    	// So to get the index of index-1 (or -2) we add the modulo base to ensure a positive outcome
    	
    	Integer		indexCurrent		= (enqueueIndex - 1 + items.length) % items.length;
    	Integer		indexPrevious		= (enqueueIndex - 2 + items.length) % items.length;
    	Integer 	currentError 		= items[indexCurrent] - target;
    	Float 		proportional 		= currentError.floatValue();		// unit = decigrees offtarget
		Float 		differential 		= 0F;								// unit = decigrees/second 
		Float 		integral 			= 0F;								// unit = decigrees offtarget x seconds
		Float 		result 				= 0F;								// retruns number of milliseconds to move 3way valve
																			// Made negative as is a negative feedback system
		
    	// Rather than calc de/dt (which can have transients due to square wave targets
    	// we go for dnewNumber/dt which is smoother. All times are saved in ms.
    	// The integration function gives a value in seconds
    	// The differential work is done here and so is multiplied by 1000 to go from ms -> s
		
    	if (count <= 1)
    	{
    		differential 				= 0F;
    	}
    	else
    	{
    		//Units of differential are decidegrees/millisecond
    		Long 	deltaTimeStamps 	= timeStamps[indexCurrent] - timeStamps[indexPrevious];
    		differential				= 1000F * deltas[indexCurrent].floatValue() / deltaTimeStamps;	// in decidegrees per second
    	}

		integral 						= integrals[indexCurrent].floatValue();							// in decidegree x seconds
		result 							= - kP * proportional - kD * differential - kI * integral;
		
		LogIt.pidData(target, proportional, differential, integral, kP, kD, kI, result, items[indexCurrent], Global.thermoBoiler.reading);

    	return result;
    }
    @Override
    public String toString() 
    {
        return Arrays.toString(items);
    }
}
