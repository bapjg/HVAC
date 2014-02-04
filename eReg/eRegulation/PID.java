package eRegulation;

import java.util.Arrays;
import java.util.Calendar;


public class PID 
{
    private Integer 	indexEnqueue;	// Separate index to ensure enqueue happens at the end
    private Integer 	count;			// Count is the number of entries in the PID Table <= pidDepth
    public 	Integer 	target;

    private	Integer		pidDepth;		// Depth is the size of the PID table
    private PID_Entry[]	entries;
    
    public PID(Integer pidDepth) 
    {
    	this.indexEnqueue 						= 0;
        this.target 							= 0;
        this.count								= 0;
        
        this.pidDepth							= pidDepth;
        this.entries							= new PID_Entry[pidDepth];
        
        int i;
        for (i = 0; i < pidDepth; i++)
        {
        	this.entries[i]						= new PID_Entry();
        }
    }
    public void setTarget(Integer target)
    {
        this.target 							= target;
    }
    public void add(Integer newNumber) 
    {
    	// This is new code
    	Integer previousIndex					= 0;
    	entries[indexEnqueue].timeStamp			= Calendar.getInstance().getTimeInMillis();
    	entries[indexEnqueue].item				= newNumber;
 
    	if (count == 0)
    	{
    		entries[indexEnqueue].delta			= 0;
    		entries[indexEnqueue].integral		= 0L;
    	}
    	else
    	{
    		// previous index is enqueueIndex -1 modulo length. We add queue length to avoid negative values 
    		previousIndex						= (indexEnqueue  - 1 + pidDepth) % pidDepth;

    		// Calculate dTemp. Note that it is independant of the target (rate of change)
    		entries[indexEnqueue].delta 		= newNumber - entries[previousIndex].item;
    		
    		Long deltaTimeStamps 				= entries[indexEnqueue].timeStamp - entries[previousIndex].timeStamp;
    		Long millidegreeSeconds				= (newNumber.longValue() - target.longValue()) * deltaTimeStamps/1000L;		// decidegreeSeconds = offTarget x seconds
    		
    		entries[indexEnqueue].integral 		= millidegreeSeconds + entries[previousIndex].integral;			// This is items x.dt
    	}
    	
    	indexEnqueue 							= (indexEnqueue + 1) % pidDepth;

    	if (count < pidDepth)
    	{
    		count++;					// Do later
    	}
    }
    public int average() 
    {
    	int i;
    	int sum = 0;
    	for (i = 0; i < count; i++)
    	{
    		sum 								= sum + entries[indexEnqueue].item;
    	}
    	return sum/count;
    }
    public Float dTdt() 
    {
		Float 		differential 				= 0F;								// unit = decigrees/second 
		Long 		deltaTimeStamps				= 0L;								// unit = millisconds
    	Integer		indexCurrent				= (indexEnqueue - 1 + pidDepth) % pidDepth;
    	Integer		indexPrevious				= (indexEnqueue - 2 + pidDepth) % pidDepth;
    	
    	if (count <= 1)
    	{
    		differential 						= 0F;
    	}
    	else
    	{
    		//Units of differential are decidegrees/millisecond
    		deltaTimeStamps 					= entries[indexCurrent].timeStamp - entries[indexPrevious].timeStamp;
    		differential						= 1000F * entries[indexCurrent].delta.floatValue() / deltaTimeStamps;	// in decidegrees per second
    	}

    	return differential;
    }
    public Integer getGain(Float kP, Float kD, Float kI) 
    {
    	// Parameters are
    	//    kP = number of milliseconds swingTime per decidegree
    	//    kD = number of milliseconds swingTime per decidegree/second change in temperature = number of seconds over which to calculate x kP
    	//    kI = number of milliseconds swingTime per decidegree x seconds cumulated off target error
    	
    	
    	// enqueueIndex is the place for the next item to be added.
    	// hence most recently added item is enqueueIndex-1
    	// Note that Java modulo defines that result carries same sign as numurator
    	// So to get the index of index-1 (or -2) we add the modulo base to ensure a positive outcome
    	
    	Integer		indexCurrent				= (indexEnqueue - 1 + pidDepth) % pidDepth;
    	Integer		indexPrevious				= (indexEnqueue - 2 + pidDepth) % pidDepth;
    	Integer 	currentError 				= entries[indexCurrent].item - target;
    	Float 		proportional 				= currentError.floatValue();		// unit = decigrees offtarget
		Float 		differential 				= 0F;								// unit = decigrees/second 
		Float 		integral 					= 0F;								// unit = decigrees offtarget x seconds
		Float 		result 						= 0F;								// retruns number of milliseconds to move 3way valve
																			// Made negative as is a negative feedback system
		
    	// Rather than calc de/dt (which can have transients due to square wave targets
    	// we go for dnewNumber/dt which is smoother. All times are saved in ms.
    	// The integration function gives a value in seconds
    	// The differential work is done here and so is multiplied by 1000 to go from ms -> s
		
    	if (count <= 1)
    	{
    		differential 						= 0F;
    	}
    	else
    	{
    		//Units of differential are decidegrees/millisecond
    		Long 	deltaTimeStamps 			= entries[indexCurrent].timeStamp - entries[indexPrevious].timeStamp;
    		differential						= 1000F * entries[indexCurrent].delta.floatValue() / deltaTimeStamps;	// in decidegrees per second
    	}

		integral 								= entries[indexCurrent].integral.floatValue();							// in decidegree x seconds
		result 									= - kP * proportional - kD * differential - kI * integral;
		
		return result.intValue();
    }
    public class PID_Entry
    {
        private Long 		timeStamp;		//                         stored unit = milliseconds
        private Integer 	item;			// Proportional component, stored unit = millidegrees (we store values, not differences to avoid pbs with 
        private Integer 	delta;			// Differential component, stored unit = millidegrees  sudden target changes with sudden target changes)
        private Long 		integral;		// Integral component,     stored unit = millidegree from target x seconds
        private Integer 	delta2;			// Second order Differential component, stored unit = millidegrees per ????  sudden target changes with sudden target changes)
               
        public PID_Entry() 
        {
        }
     }
}
