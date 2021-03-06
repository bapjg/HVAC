package eRegulation;

import java.util.Arrays;
import java.util.Calendar;
import HVAC_Common.*;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class PID 
{
	public  String												name;
	public  Integer												sampleIncrement;
	public  Integer												increment;
	private Integer 											indexEnqueue;	// Separate index to ensure enqueue happens at the end
    private Integer 											count;			// Count is the number of entries in the PID Table <= pidDepth
    public 	Integer 											target;

    private	Integer												pidDepth;		// Depth is the size of the PID table
    private PID_Entry[]											entries;
    
    public PID(Ctrl_Configuration.PID_Data 		paramPID) 
    {
    	this.name		 																	= paramPID.name;
    	this.sampleIncrement		 														= paramPID.sampleIncrement;
       	this.increment				 														= 1;
        this.indexEnqueue 																	= 0;
        this.target 																		= 0;
        this.count																			= 0;
        // TODO Is this correct
    	this.sampleIncrement		 														= 1;
    	this.increment				 														= 1;
    	
        this.pidDepth																		= paramPID.depth;
        this.entries																		= new PID_Entry[pidDepth];
        
        int i;
        for (i = 0; i < pidDepth; i++)
        {
        	this.entries[i]																	= new PID_Entry();
        }
    }
    public void setTarget(Integer target)
    {
        this.target 																		= target;
    }
    public void add(Integer newNumber) 
    {
    	Integer previousIndex																= 0;
    	entries[indexEnqueue].timeStamp														= Calendar.getInstance().getTimeInMillis();
    	entries[indexEnqueue].item															= newNumber;
 
    	if (count == 0)
    	{
    		entries[indexEnqueue].delta														= 0;
    		entries[indexEnqueue].integral													= 0L;
    	}
    	else
    	{
    		// previous index is enqueueIndex -1 modulo length. We add queue length to avoid negative values 
    		previousIndex																	= (indexEnqueue  - 1 + pidDepth) % pidDepth;

    		// Calculate dTemp. Note that it is independant of the target (rate of change)
    		entries[indexEnqueue].delta 													= newNumber - entries[previousIndex].item;
    		
    		Long deltaTimeStamps 															= entries[indexEnqueue].timeStamp - entries[previousIndex].timeStamp;
    		Long millidegreeSeconds															= (newNumber.longValue() - target.longValue()) * deltaTimeStamps/1000L;		// decidegreeSeconds = offTarget x seconds
    		
    		entries[indexEnqueue].integral 													= millidegreeSeconds + entries[previousIndex].integral;			// This is items x.dt
    	}
    	
    	indexEnqueue 																		= (indexEnqueue + 1) % pidDepth;

    	if (count < pidDepth)
    	{
    		count++;					// TODO later
    	}
    }
    public int average() 
    {
    	int i;
    	int sum = 0;
    	for (i = 0; i < count; i++)
    	{
    		sum 																			= sum + entries[indexEnqueue].item;
    	}
    	return sum/count;
    }
    public Float dTdt() 
    {
		Float 		 											differential				= 0F;								// unit = millidegrees/second 
		Long 		 											deltaTimeStamps				= 0L;								// unit = milliseconds
    	Integer		 											indexCurrent				= (indexEnqueue - 1 + pidDepth) % pidDepth;
    	Integer		 											indexPrevious				= (indexEnqueue - 2 + pidDepth) % pidDepth;
    	
    	if (count <= 1)
    	{
    		differential 																	= 0F;
    	}
    	else
    	{
    		//Units of differential are decidegrees/millisecond
    		deltaTimeStamps 																= entries[indexCurrent].timeStamp - entries[indexPrevious].timeStamp;	// in ms
    		differential																	= 1000F * entries[indexCurrent].delta.floatValue() / deltaTimeStamps;	// in millidegrees per second
    	}

    	return differential;
    }
    public Integer tempCurrent() 
    {
    	Integer													indexCurrent				= (indexEnqueue - 1 + pidDepth) % pidDepth;
    	Integer													current						= entries[indexCurrent].item;
    	return current;
    }
    public Integer tempCurrentError() 
    {
    	Integer													indexCurrent				= (indexEnqueue - 1 + pidDepth) % pidDepth;
    	Integer													currentError				= entries[indexCurrent].item - target;
    	return currentError;
    }
    public Integer getGainP(Float kP) 
    {
    	Integer		indexCurrent															= (indexEnqueue - 1 + pidDepth) % pidDepth;
    	Integer		indexPrevious															= (indexEnqueue - 2 + pidDepth) % pidDepth;
    	Integer 	currentError 															= entries[indexCurrent].item - target;
       	Float 		proportional 															= currentError.floatValue();		// unit = milligrees offtarget
       	Float		getGainP																=  - (kP * proportional);
       	return 		getGainP.intValue();
    }
    public Integer getGainD(Float kD) 
    {
       	Float 													differential 				= 0F;
       	
    	if (count <= 1)
    	{
    		differential 																	= 0F;
    	}
    	else
    	{
    		//Units of differential are millidegrees/second
    		//Units of kD are seconds x milliseconds/milliDegrees
        	Integer												indexCurrent				= (indexEnqueue - 1 + pidDepth) % pidDepth;
        	Integer												indexPrevious				= (indexEnqueue - 2 + pidDepth) % pidDepth;
    		Long 												deltaTimeStamps 			= entries[indexCurrent].timeStamp - entries[indexPrevious].timeStamp;
    		differential																	= 1000F * entries[indexCurrent].delta.floatValue() / deltaTimeStamps;	// in millidegrees per second
    	}
       	// units of getGainD are ms
    	Float		getGainD																=  - (kD * differential);
       	return 		getGainD.intValue();
    }
    public Integer getGainD(Float kD, int depth) 
    {
       	Float 		differential 															= 0F;
       	
    	if (count <= depth + 1)
    	{
    		differential 																	= 0F;
    	}
    	else
    	{
    		//Units of differential are millidegrees/second
    		//Units of kD are seconds x milliseconds/milliDegrees
        	Integer												indexCurrent				= (indexEnqueue - 1         + pidDepth) % pidDepth;
        	Integer												indexPrevious				= (indexEnqueue - depth - 1 + pidDepth) % pidDepth;
    		Long 												deltaTimeStamps 			= entries[indexCurrent].timeStamp - entries[indexPrevious].timeStamp;
    		differential																	= 1000F * entries[indexCurrent].delta.floatValue() / deltaTimeStamps;	// in millidegrees per second
    	}
       	// units of getGainD are ms
    	Float		getGainD																=  - (kD * differential);
       	return 		getGainD.intValue();
    }
    public Float getdTdt(Integer previous)
    {
    	Integer		indexCurrent															= (indexEnqueue - 1 + previous + pidDepth) % pidDepth;
    	Integer		indexPrevious															= (indexEnqueue - 2 + previous + pidDepth) % pidDepth;
       	Float 		differential 															= 0F;
       	
    	if (count <= 2)
    	{
    		differential 																	= 0F;
    	}
    	else
    	{
    		//Units of differential are millidegrees/second
    		//Units of kD are seconds x milliseconds/milliDegrees
    		Long 	deltaTimeStamps 														= entries[indexCurrent].timeStamp - entries[indexPrevious].timeStamp;
    		differential																	= 1000F * entries[indexCurrent].delta.floatValue() / deltaTimeStamps;	// in millidegrees per second
    	}
       	// units of getGainD are ms
    	return differential;
    }
    public Integer getGainI(Float kI) 
    {
    	//Units must be checked
    	Integer		indexCurrent															= (indexEnqueue - 1 + pidDepth) % pidDepth;
    	Integer		indexPrevious															= (indexEnqueue - 2 + pidDepth) % pidDepth;
    	Integer 	currentError 															= entries[indexCurrent].item - target;
		Float 		integral 																= 0F;								// unit = milligrees offtarget x seconds
		integral 																			= entries[indexCurrent].integral.floatValue();							// in millidegree x seconds
       	Float		getGainI																=  - (kI * integral);
       	return 		getGainI.intValue();
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
    	
    	return	 	getGainP(kP) +  getGainD(kD) +  getGainI(kI);
    }
    public class PID_Entry
    {
        private Long 											timeStamp;		//                         stored unit = milliseconds
        private Integer 										item;			// Proportional component, stored unit = millidegrees (we store values, not differences to avoid pbs with 
        private Integer 										delta;			// Differential component, stored unit = millidegrees  sudden target changes with sudden target changes)
        private Long 											integral;		// Integral component,     stored unit = millidegree from target x seconds
               
        public PID_Entry() 
        {
        }
     }
}
