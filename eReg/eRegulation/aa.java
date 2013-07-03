package eRegulation;

import java.util.ArrayList;

public abstract class aa
{
	public String 					name;
	public String 					friendlyName;
	public String 					circuitType;
	public Integer 					tempMax;

	public Integer 					rampUp;
	public Integer 					rampDown;
	public Integer					tempToTrack;						// Used to recalculate ax+b;
	public Thermometer				thermoToMonitor;					//Used with above

	public Integer					state;
	
	public static final int 				CIRCUIT_STATE_Off 			= 0;
	public static final int 				CIRCUIT_STATE_Started 		= 1;
	public static final int 				CIRCUIT_STATE_Running 		= 2;
	public static final int 				CIRCUIT_STATE_Stopping	 	= 3;
	public static final int 				CIRCUIT_STATE_Optimising 	= 4;
	public static final int 				CIRCUIT_STATE_Error	 		= -1;


	public Mixer					mixer						= null;
	public TemperatureGradient 		temperatureGradient			= null;				//This will be overridden
	
	public CircuitTask				activeTask					= null;
	public ArrayList<CircuitTask> 	circuitTaskList 			= new ArrayList<CircuitTask>();
	public HeatRequired				heatRequired				= new HeatRequired();

	public aa(String name, String friendlyName, String circuitType, String tempMax, String rampUp, String rampDown)
	{	
		this.name												= name;
		this.friendlyName										= friendlyName;
		this.circuitType										= circuitType;
		this.tempMax											= Integer.parseInt(tempMax);
		this.rampUp												= Integer.parseInt(rampUp);
		this.rampDown											= Integer.parseInt(rampDown);
		this.state												= CIRCUIT_STATE_Off;
	}
	public void addCircuitTask
	(
	String 			timeStart, 
	String 			timeEnd,  
	String 			tempObjective, 
	String			stopOnObjective,
	String			days,
	Boolean			temporary
	)
{
	CircuitTask circuitTaskItem 							= new CircuitTask(timeStart, timeEnd, tempObjective, stopOnObjective, days, temporary);
	circuitTaskList.add(circuitTaskItem);
}
public void addCircuitTask
	(
	Long 			timeStart, 
	Long 			timeEnd,  
	Integer			tempObjective, 
	Boolean			stopOnObjective,
	String			days,
	Boolean			temporary
	)
{
	CircuitTask circuitTaskItem 							= new CircuitTask(timeStart, timeEnd, tempObjective, stopOnObjective, days, temporary);
	circuitTaskList.add(circuitTaskItem);
}
public void start()
{
	LogIt.error("Circuit","start", "Overloaded method not called");
	// This method is over ridden in subclasses
}
public void stop()
{
	LogIt.error("Circuit","stop", "Overloaded method not called");
	// This method is over ridden in subclasses
}
public void runFree()
{
	LogIt.error("Circuit","runFree", "Overloaded method not called");
	// This method is over ridden in subclasses
}
public void sequencer()
{
	LogIt.error("Circuit","sequencer", "Overloaded method not called");
	// This method is over ridden in subclasses
}
public void scheduleTasks()
/*
 *  This sets up "circuitTask" to be the task selected to run based on date/time
 *  If the circuit has no task programmed, "circuitTask" is null (not true, but should it be true)
 *  
 *  Circuit_Abstract has several implmentations (HW/Radiator/Mixer
 *  
 *  Circuit_Abstract has a list of tasks (circuitTask)
 *  
 *  
 *  
 *  
 */
{
	if (activeTask != null)
	{
		//There is an active task. No need to schedule anything until it has finished
		//Unless it is not running (ie we stopped the program mid-schedule
//		LogIt.info("Circuit","scheduleTasks", "activeTask <> null in : " + name);
		if (Global.getTimeNowSinceMidnight() > activeTask.timeEnd)
		{
			activeTask.state								= activeTask.TASK_STATE_Completed;
			// 
			// If this a temporary task, should be deleted
			//
			// LogIt.info("Circuit","scheduleTasks", "checking circuitTask for : " + name + "starting at time " + circuitTask.timeStart/3600/100 + "completed");
		}
		else
		{
			// Just carry on
		}
	}
	else
	{
		for (CircuitTask circuitTask : circuitTaskList) 
		{
			String day 										= Global.getDayOfWeek();  				// day = 1 Monday ... day = 7 Sunday// Sunday = 7, Monday = 1, Tues = 2 ... Sat = 6

			if (circuitTask.days.contains(day))
			{
				//	LogIt.info("Circuit","scheduleTasks", "checking circuitTask for : " + name + "starting at time " + circuitTask.timeStart/3600/100);
				
				// Three possibilities : Task is yet to run, task currently running, finished running
				if (circuitTask.state != circuitTask.TASK_STATE_Completed)
				{
					if (Global.getTimeNowSinceMidnight() < circuitTask.timeStart)
					{
						circuitTask.state						= circuitTask.TASK_STATE_WaitingToStart;
						// LogIt.info("Circuit","scheduleTasks", "checking circuitTask for : " + name + "starting at time " + circuitTask.timeStart/3600/100 + "waiting to start");
					}
					else if ((circuitTask.timeStart < Global.getTimeNowSinceMidnight()) && ( Global.getTimeNowSinceMidnight() < circuitTask.timeEnd))
					{
						circuitTask.state						= circuitTask.TASK_STATE_Started;	// Indication in the Task that it has been started (just informative)
						
						// All this is very fudgy/strange/needs to be sorted out
						
						state									= CIRCUIT_STATE_Started;				// State of the circuit is started. The sequencer will actually get things moving
						activeTask								= circuitTask;
						// LogIt.info("Circuit","scheduleTasks", "checking circuitTask for : " + name + "starting at time " + circuitTask.timeStart/3600/100 + "activating");
					}
					else if (Global.getTimeNowSinceMidnight() > circuitTask.timeEnd)
					{
						//circuitTask.state						= circuitTask.STATE_Completed;
						circuitTask.state						= circuitTask.TASK_STATE_Completed;
						// activeTask will be set to null in the sequencer
						// LogIt.info("Circuit","scheduleTasks", "checking circuitTask for : " + name + "starting at time " + circuitTask.timeStart/3600/100 + "completed");
					}
					else
					{
						// We have an error
						//circuitTask.state						= circuitTask.STATE_Error;
						circuitTask.state						= circuitTask.TASK_STATE_Error;
					}
				}
			}
			else
			{
				//circuitTask.state								= circuitTask.STATE_NotToday;
				circuitTask.state								= circuitTask.TASK_STATE_NotToday;
			}
		}
	}
}

}
