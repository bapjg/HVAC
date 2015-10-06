package eRegulation;

import java.util.ArrayList;

import HVAC_Common.CIRCUIT;
import HVAC_Common.Ctrl_Calendars;
import HVAC_Common.Ctrl_Configuration;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
abstract class Circuit_Abstract
{
	public static final int										CIRCUIT_TYPE_HotWater		= 0;
	public static final int										CIRCUIT_TYPE_Gradient		= 1;
	public static final int										CIRCUIT_TYPE_Mixer			= 2;

	public String 												name;
	public Integer 												type;
	public Integer 												circuitType;
	public Integer 												tempMax;

	public Long 												rampUpTime					= 0L;
	public Integer												tempToTrack;					// Used to recalculate ax+b;
	public Thermometer											thermoToMonitor;				//Used with above
	public Pump													circuitPump;
	public Thermometer											circuitThermo;
								
	public CIRCUIT.STATE										state;

	public Mixer												mixer						= null;
	public TemperatureGradient 									temperatureGradient			= null;				//This will be overridden
								
	public CircuitTask											taskActive					= null;
								
	public ArrayList <CircuitTask> 								circuitTaskList 			= new ArrayList <CircuitTask>();
	public HeatRequired											heatRequired				= null;
								
	public Boolean												willBeSingleCircuit			= false;

	public Circuit_Abstract(Ctrl_Configuration.Circuit 			paramCircuit)
	{
		this.name																			= paramCircuit.name;
		this.type																			= paramCircuit.type;
		this.circuitType																	= paramCircuit.type;
		this.tempMax																		= paramCircuit.tempMax;
		this.circuitPump																	= Global.pumps.fetchPump(paramCircuit.pump);
		this.circuitThermo																	= Global.thermometers.fetchThermometer(paramCircuit.thermometer);

		if (this.circuitPump == null)			System.out.println("Circuit.Constructor : " + name + " invalid pump " + paramCircuit.pump);
		if (this.circuitThermo == null)			System.out.println("Circuit.Constructor : " + name + " invalid thermometer " + paramCircuit.thermometer);
		
		this.state																			= CIRCUIT.STATE.Off;
		this.heatRequired																	= null;
	}
	public void addCircuitTask(Ctrl_Calendars.Calendar 				paramCalendar)
	{
		CircuitTask 	circuitTaskItem 													= new CircuitTask(paramCalendar);
		circuitTaskList.add(circuitTaskItem);
	}
	public Long getRampUpTime(Integer tempObjective) 				{  /* OverRidden in Circuit_XXX classes */	return 0L; 	}
	public void sequencer()											{  /* OverRidden in Circuit_XXX classes */	}
	public void start()
	{
		LogIt.action(this.name, "Start called");
		this.state																			= CIRCUIT.STATE.Start_Requested;
		this.heatRequired																	= new HeatRequired();
	}
/**
 * Starts the circuit :
 * Supplied circuitTask becomes taskActive
 * State set to Start_Requested
 * heatRequired set to zero valued object
 */
	public void start(CircuitTask 									thisTask)
	{
		LogIt.action(this.name, "Start called with circuitTask");
		this.taskActive																		= thisTask;
		this.state																			= CIRCUIT.STATE.Start_Requested;
		this.heatRequired																	= new HeatRequired();
	}
/**
 * Stops the circuit :
 * State set to Stop_Requested
 * heatRequired set to null
 */	
	public void stop()
	{
		// Called on one of the following conditions
		//   1. Time is up : 					Detected/Called by Circuit_Abstract.scheduleTask
		//   2. Temperature objective reached : Detected/Called by Circuit_XXX.sequencer (thermometer surveillance)
		LogIt.action(this.name, "Stop called");
		this.state																			= CIRCUIT.STATE.Stop_Requested;
		this.heatRequired																	= null;
		// Depending on the situation, the circuit will either optimise or stopdown completely
	}
/**
 * Shuts down the circuit :
 * State set to off
 * heatRequired set to null
 * task deactivated
 */
	public void shutDown()
	{
		LogIt.action(this.name, "Closing down completely");
		this.state																			= CIRCUIT.STATE.Off;
		this.heatRequired																	= null;
		taskDeactivate(this.taskActive);
	}
/**
 * Not implemented
 */
	public void interupt()
	{
//		LogIt.action(this.name, "closing down");
//		this.state																			= CIRCUIT_STATE_Off;
//		this.heatRequired																	= null;
//		this.taskActive.state																= this.taskActive.TASK_STATE_Completed; // What happens if the task has been switched to a new one
//		this.taskActive																		= null;
	}
/**
 * Suspends the circuit :
 * This is used for Hot_Water, if target temperature is reached, the circuitPump is switched off
 * If the temperature falls below target - 5 degrees, resume will be called to trun it onn again
 * State set to Suspended
 * switches OFF circuitPump
 * heatRequired.max/min set to 0
 */	
	public void suspend()
	{
		LogIt.action(this.name, "Suspend called");
		this.heatRequired.tempMinimum														= 0;
		this.heatRequired.tempMaximum														= 0;
		this.circuitPump.off();
		this.state																			= CIRCUIT.STATE.Suspended;
	}						
/**
 * Resumes the circuit :
 * State set to Resumed
 * Called to allow circuitPump to be switched on again (after suspend)
 * switches ON circuitPump
 * heatRequired.max/min must be set by caller
 */	
	public void resume()						
	{						
		LogIt.action(this.name, "Resume called");						
		this.circuitPump.on();
		this.state																			= CIRCUIT.STATE.Resuming;
	}
/**
 * Optimises the circuit :
 * State set to Optimising
 * heatRequired.max/min set to 0
 */	
	public void optimise()						
	{						
		LogIt.action(this.name, "Optimising called");
		// Modified to be the same as this.stop() which may call optimise()
//		this.heatRequired.tempMinimum														= 0;
//		this.heatRequired.tempMaximum														= 0;
		this.heatRequired																	= null;
		this.state																			= CIRCUIT.STATE.Optimising;
	}
/**
 * Activates task supplied in parameter :
 * task.start() called
 */	
	public void taskActivate(CircuitTask 							thisTask)
	{
		LogIt.display("Circuit_Abstract", "taskActivate", this.name + " Task activated ");

		if (this.taskActive == null)														// Normal operation
		{
			this.taskActive																	= thisTask;
			this.start();
			this.taskActive.dateLastRun														= Global.Date.now();
		}
		else
		{
			if (taskActive == thisTask)														// Could arise (???) during rampUp
			{
				LogIt.error("Circuit_Abstract", "taskActivate", "WOULD HAVE SAID : A task is active when it shouldn't be");
				LogIt.info("Circuit_Abstract", "taskActivate", "Task to activate is already active");
				LogIt.display("Circuit_Abstract", "taskActivate", "taskActive          = " + System.identityHashCode(taskActive));
				LogIt.display("Circuit_Abstract", "taskActivate", "thisTask(candidate) = " + System.identityHashCode(thisTask));
			}
			else																			// Dont know how
			{
				LogIt.error("Circuit_Abstract", "taskActivate", "A task is active when it shouldn't be");
				LogIt.info("Circuit_Abstract", "taskActivate", "Task to activate is occupied... Replaced");
				LogIt.info("Circuit_Abstract", "taskActivate", "Task to activate is already active");
				LogIt.display("Circuit_Abstract", "taskActivate", "taskActive          = " + System.identityHashCode(taskActive));
				LogIt.display("Circuit_Abstract", "taskActivate", "thisTask(candidate) = " + System.identityHashCode(thisTask));
			}
			this.taskActive																	= thisTask;
			this.start();						
			this.taskActive.dateLastRun														= Global.Date.now();
		}
	}
/**
 * Deactivates current task :
 * taskActive set to null
 */	
	public void taskDeactivate(CircuitTask thisTask)			// After deactivation, all tasks should be inactive
	{
		LogIt.display("Circuit_Abstract", "taskDeactivate", this.name + " Task Deactivated ");
		this.taskActive.dateLastRun															= Global.Date.now();
		this.taskActive																		= null;
		StackTraceElement[] 									stackTraceElements 			= Thread.currentThread().getStackTrace();
		int i;
		for (i = 1; i < stackTraceElements.length - 1; i++)
		{
			StackTraceElement 									stackTraceElement			= stackTraceElements[i];
			LogIt.display("Circuit_Abstract", "taskDeactivate", "index		: " + i);
			LogIt.display("Circuit_Abstract", "taskDeactivate", "ClassName 	: " + stackTraceElement.getClassName());
			LogIt.display("Circuit_Abstract", "taskDeactivate", "MethodName	: " + stackTraceElement.getMethodName());
			LogIt.display("Circuit_Abstract", "taskDeactivate", "FileName 	: " + stackTraceElement.getFileName());
			LogIt.display("Circuit_Abstract", "taskDeactivate", "Line number 	: " + stackTraceElement.getLineNumber());
			LogIt.display("Circuit_Abstract", "taskDeactivate", "----------------------------------------------");
		}
		LogIt.display("Circuit_Abstract", "taskDeactivate", "==============================================================");
	}
/**
 * - taskActive.stop() called for current task is time up
 * - Searches tasks to be scheduled and if one found
 * - activateTask called with taskFound unless
 *   . We are in summer
 *   . We are away
 */	
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
		String 												day 							= Global.Date.getDayOfWeek(0);				// day = 1 Monday ... day = 7 Sunday// Sunday = 7, Monday = 1, Tues = 2 ... Sat = 6
		Long 												now								= Global.Time.now();
		Long 												today							= Global.Date.now();
		CircuitTask											taskFound						= null;
		
		// Stop activeTask
		
		if (taskActive != null)
		{
			if (	(now > taskActive.timeEnd) 							// taskActive : Time up
			&& 		(this.state != CIRCUIT.STATE.Stop_Requested	) 
			&&		(this.state != CIRCUIT.STATE.Stopping		)   
			&&		(this.state != CIRCUIT.STATE.Optimising		)   )
			{
				// Time is up for this task and it hasn't yet been asked to stop
				this.stop();
			}
		}

		// Go through all task entries for this circuit
		
		for (CircuitTask circuitTask : circuitTaskList) 													// Go through all tasks
		{	
			if (circuitTask.days.contains(day)) 
			{
				// This circuitTask must run today and is not active
				// - It can already have run and finished
				// - It can already have run and not finished but been pre-emted
				// - It can be in the state "should have started, and not yet finished" but we have just booted the RaspPi
				// - It can be running (Not possible in this branch of code)
				// - It can be yet to run
				
				if (		(  circuitTask.timeStart - this.getRampUpTime(circuitTask.tempObjective) > now	)						// This task has yet to be performed (timeStart future
				&& 			(  circuitTask.timeEnd > now													)  		)						// Or time End future
				{
					// This task has yet to run : both start and end are in the future
					// Nothing todo
				}
				else if (	(  circuitTask.timeStart - this.getRampUpTime(circuitTask.tempObjective) < now	)						// This task has yet to be performed (timeStart is past
				&& 			(  circuitTask.timeEnd > now													)   					// and time End is future
				&&			(! circuitTask.dateLastRun.equals(today)										)		)				// and the last run wasn't today					
				{
					// This task should be run : start is past and end is the future
					// We can swap this task in
					if (taskFound == null)
					{
						taskFound															= circuitTask;
					}
					else if (circuitTask.timeStart > taskFound.timeStart)
					{
						// taskFound contains a task which should start
						// circuitTask should start later
						// This can happen either by error (overlapping calendars)
						// or due to rampUp : circuitTask planned to run later but we will start now to ramp up
						taskFound															= circuitTask;
					}
					else
					{
						// taskFound is already the correct task to run
					}
				}
			}
		}
		
		if (! Global.isAway())				// We are not away so get going
		{
			if (taskFound != null)
			{
				if (type == CIRCUIT_TYPE_HotWater)
				{
					taskActivate(taskFound);
				}
				else		// Floor or Radiator
				{
					if (Global.thermoOutside.reading == null)							return;
					
					if (Global.thermoOutside.reading > Global.tasksBackGround.summerTemp)
					{
						// TODO do nothing or marked as finished
					}
					else
					{
						/*
						 * am = means before maxTempPredicted (around 14h00)
						 * pm = means after maxTempPredicted (around 14h00)
						 * 
						 * if startTime = am and endTime = am
						 * 		if currentTemp > summer dropIt for today
						 * 		if maxPredicted > summer dropit for today
						 * 		if maxPredicted > winter schedule for idle (idle must survey infoor temp to see if heat required) 
						 * 
						 * elseif startTime = pm and endTime = pm
						 * 		if currentTemp > summer dropIt for today
						 * 		if currentTemp > winter schedule for idle (idle must survey infoor temp to see if heat required) 
						 * 		if currentTemp < winter schedule for start 
						 * 
						 * 
						 * 
						 * elseif startTime = am and endTime = pm
						 * 		if currentTemp > summer dropIt for today
						 * 		if maxPredicted > summer dropit for today
						 * 
						 * 
						 * 
						 * 
						 * else
						 * 		Is this an error
						 * fi
						 * 
						 * 
						 * 
						 * 
						 * Task may start/Stop before maxTempPredicted		Dont need to start
						 * Task may start/Stop after maxTempPredicted		Need to start
						 * Task may start      before and end after maxTempPredicted		Need to start (perhaps)
						 * 
						 * 
						 */
//						System.out.println("Activating task " + this.name);
						taskActivate(taskFound);
					}
				}
			}
		}
	}
}
