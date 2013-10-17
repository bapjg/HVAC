package eRegulation;

import java.util.ArrayList;

abstract class Circuit_Abstract
{
	public String 					name;
	public String 					friendlyName;
	public String 					circuitType;
	public Integer 					tempMax;

	public Long 					rampUpTime					= 0L;
	public Integer					tempToTrack;						// Used to recalculate ax+b;
	public Thermometer				thermoToMonitor;					//Used with above

	public Integer					state;
	
	public static final int			CIRCUIT_STATE_Off 				= 0;
	public static final int			CIRCUIT_STATE_Starting 			= 1;
	public static final int			CIRCUIT_STATE_Running 			= 2;
	public static final int			CIRCUIT_STATE_Stopping	 		= 3;
	public static final int			CIRCUIT_STATE_Optimising 		= 4;
	public static final int			CIRCUIT_STATE_Error	 			= -1;

	public static final int			CIRCUIT_STATE_Suspended			= 5;
	public static final int			CIRCUIT_STATE_Resuming 			= 6;
	public static final int			CIRCUIT_STATE_AwaitingHeat		= 7;

	public static final int			CIRCUIT_STATE_Start_Requested	= 10;
	public static final int			CIRCUIT_STATE_Stop_Requested	= 11;

	public Mixer					mixer							= null;
	public TemperatureGradient 		temperatureGradient				= null;				//This will be overridden
	
	public CircuitTask				taskActive						= null;
	public CircuitTask				taskNext						= null;
	
	public ArrayList <CircuitTask> 	circuitTaskList 				= new ArrayList <CircuitTask>();
	public HeatRequired				heatRequired					= null;
	
	public Boolean					willBeSingleCircuit				= false;

//	public Circuit_Abstract()
//	{	
//	}
	public Circuit_Abstract(String name, String friendlyName, String circuitType, String tempMax, String rampUpTime)
	{	
		this.name													= name;
		this.friendlyName											= friendlyName;
		this.circuitType											= circuitType;
		this.tempMax												= Integer.parseInt(tempMax);
		this.rampUpTime												= Long.parseLong(rampUpTime);
		this.state													= CIRCUIT_STATE_Off;
		this.heatRequired											= null;
	}
	public void addCircuitTask
		(
		String 			timeStart, 
		String 			timeEnd,  
		String 			tempObjective, 
		String			stopOnObjective,
		String			days
		)
	{
		CircuitTask 	circuitTaskItem 							= new CircuitTask(timeStart, timeEnd, tempObjective, stopOnObjective, days);
		circuitTaskList.add(circuitTaskItem);
	}
	public Long getRampUpTime()
	{
		return 0L; // Overriddent method
	}
	public Long calculatePerformance()
	{
		return 0L; // Overriddent method
	}
	public void start()
	{
		LogIt.action(this.name, "Start called");
		this.state													= CIRCUIT_STATE_Start_Requested;
		this.heatRequired											= new HeatRequired();
	}
	public void stop()
	{
		LogIt.action(this.name, "Stop called");
		this.state													= CIRCUIT_STATE_Stop_Requested;
		this.heatRequired											= null;
		// Depending on the situation, the circuit will either optimise or stopdown completely
	}
//	public void optimise()
//	{
//		LogIt.action(this.name, "Optimising");
//		this.state													= CIRCUIT_STATE_Optimising;
//		this.heatRequired											= null;
//		this.taskActive.state										= this.taskActive.TASK_STATE_Optimising;
//	}
	public void shutDown()
	{
		LogIt.action(this.name, "Closing down completely");
		this.state													= CIRCUIT_STATE_Off;
		this.heatRequired											= null;
		this.taskActive.active										= false; // What happens if the task has been switched to a new one
		taskDeactivate(this.taskActive);
	}
	public void interupt()
	{
//		LogIt.action(this.name, "closing down");
//		this.state													= CIRCUIT_STATE_Off;
//		this.heatRequired											= null;
//		this.taskActive.state										= this.taskActive.TASK_STATE_Completed; // What happens if the task has been switched to a new one
//		this.taskActive												= null;
	}
	public void suspend()
	{
		LogIt.action(this.name, "Suspend called");
		this.state													= CIRCUIT_STATE_Suspended;
	}
	public void resume()
	{
		LogIt.action(this.name, "Resume called");
		this.state													= CIRCUIT_STATE_Resuming;
	}
	public void sequencer()										// Task overridden in sub classes
	{
	}
	public void taskActivate(CircuitTask thisTask)
	{
		for (CircuitTask aTask : this.circuitTaskList)			// Check to ensure there are no active tasks
		{
			if (aTask.active)
			{
				LogIt.error("Circuit_Abstract", "taskActivate", "A task is active when it shouldn't be");
				aTask.active										= false;
			}
		}
		thisTask.active												= true;
		this.taskActive												= thisTask;
		this.start();
		this.taskActive.dateLastRun									= Global.getTimeAtMidnight();
	}
	public void taskDeactivate(CircuitTask thisTask)			// After deactivation, all tasks should be inactive
	{
		thisTask.active												= false;
		for (CircuitTask aTask : this.circuitTaskList)
		{
			if (aTask.active)
			{
				LogIt.error("Circuit_Abstract", "taskDeactivate", "A task is active when it shouldn't be");
				aTask.active										= false;
			}
		}
		this.taskActive.dateLastRun									= Global.getTimeAtMidnight();
		this.taskActive												= null;
	}
	public void scheduleTask()
	{
		/* ============= Must adjust these comments
		 * 	Objective : Determine which task should run next : this.taskNext
		 * 
		 *  Loop through each circuitTask and see if :
		 *   - It is for today
		 *   - It has yet to run
		 *   - It has not already run (with stopOnObjective)
		 *   - If there are several which have yet to run, take the earliest 
		 *   
		 *  3 runtime situations can occur
		 *   - We have passed midnight, a new schedule must be implemented
		 *   - A new, unplanned, task may have been added (through user decision)
		 *   - Normal running, the nextTask has been scheduled to run, and the next one must be found
		 */
		String 					day 								= Global.getDayOfWeek(0);				// day = 1 Monday ... day = 7 Sunday// Sunday = 7, Monday = 1, Tues = 2 ... Sat = 6
		Long 					now									= Global.getTimeNowSinceMidnight();
		Long 					today								= Global.getTimeAtMidnight();
		CircuitTask				taskFound							= null;
		
		if (taskActive != null)
		{
			if (	(now > taskActive.timeEnd) 
			&& 		(this.state != CIRCUIT_STATE_Stop_Requested) 
			&&		(this.state != CIRCUIT_STATE_Stopping)   
			&&		(this.state != CIRCUIT_STATE_Optimising)   )
			{
				// Time is up for this task and it hasn't yet been asked to stop
				this.stop();
			}
		}
		
		for (CircuitTask circuitTask : circuitTaskList) 												// Go through all tasks
		{	
			if (	(circuitTask.days.contains(day)) 
			&& 		(! circuitTask.active)	          )
			{
				// This circuitTask must run today and is not active
				// - It can already have run and finished
				// - It can already have run and not finished but been pre-emted
				// - It can be in the state "should have started, and not yet finished" but we have just booted the RaspPi
				// - It can be running (Not possible in this branch of code)
				// - It can be yet to run
				
				if (		(circuitTask.timeStart - this.getRampUpTime() > now)							// This task has yet to be performed (timeStart future
				&& 			(circuitTask.timeEnd > now)        )											// Or time End future
				{
					// This task has yet to run : both start and end are in the future
					// Nothing todo
				}
				else if (	(circuitTask.timeStart - this.getRampUpTime() < now)							// This task has yet to be performed (timeStart is past
				&& 			(circuitTask.timeEnd > now)       												// and time End is future
				&&			(circuitTask.dateLastRun != today)						)						// and the last run wasn't today					
				{
System.out.println("Task is candidate to start. dateLastRun = " + circuitTask.dateLastRun + " today = " + today);
					// This task should be run : start is past and end is the future
					// We can swap this task in
					if (taskFound == null)
					{
						taskFound								= circuitTask;
					}
					else if (circuitTask.timeStart > taskFound.timeStart)
					{
						// taskFound contains a task which should start
						// circuitTask should start later
						// This can happen either by error (overlapping calendars)
						// or due to rampUp : circuitTask planned to run later but we will start now to ramp up
						taskFound								= circuitTask;
					}
				}
			}
		}
		if (taskFound != null)
		{
			taskActivate(taskFound);
		}
	}
}
