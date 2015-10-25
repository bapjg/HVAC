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
	public Pump													circuitPump;
	public Thermometer											circuitThermo;
								
	public HVAC_STATES.Circuit									state;
	
	public Mixer												mixer						= null;
	public TemperatureGradient 									temperatureGradient			= null;				//This will be overridden
								
	public CircuitTask											taskActive					= null;
								
	public ArrayList <CircuitTask> 								circuitTaskList 			= new ArrayList <CircuitTask>();
	public Heat_Required										heatRequired;
								
	public Boolean												willBeSingleCircuit			= false;

	public Circuit_Abstract(Ctrl_Configuration.Circuit 			paramCircuit)
	{
		this.name																			= paramCircuit.name;
		this.type																			= paramCircuit.type;
		this.circuitType																	= paramCircuit.type;
		this.tempMax																		= paramCircuit.tempMax;
		this.circuitPump																	= Global.pumps.fetchPump(paramCircuit.pump);
		this.circuitThermo																	= Global.thermometers.fetchThermometer(paramCircuit.thermometer);

		if (this.circuitPump == null)			LogIt.error("Circuit_Abstract", "Constructor : ", this.name + " invalid pump " + paramCircuit.pump);
		if (this.circuitThermo == null)			LogIt.error("Circuit_Abstract", "Constructor : ", this.name + " invalid thermometer " +  paramCircuit.thermometer);;
		
		this.state																			= HVAC_STATES.Circuit.Off;
		this.heatRequired																	= new Heat_Required();
	}
	public void addCircuitTask(Ctrl_Calendars.Calendar 				paramCalendar)
	{
		CircuitTask 	circuitTaskItem 													= new CircuitTask(paramCalendar);
		circuitTaskList.add(circuitTaskItem);
	}
	//===========================================================================================================================================================
	//
	// Performance methods
	//
	public Long getRampUpTime(Integer tempObjective) 				{  /* OverRidden in Circuit_XXX classes */	return 0L; 	}
/**
 * Starts the circuit in optimisation mode:
 * Supplied circuitTask becomes taskActive
 * State = Optimising
 * CircuitPump = ON
 * heatRequired = ZERO
 */
	public void startOptimisation()
	{
		LogIt.action(this.name, "----------------------------------startOptimisation called");
		Long											now									= Global.Time.now();
		Integer											targetTemperature					= this.circuitThermo.reading  + 2000;				// Go for 2 degrees above current temperature
		CircuitTask										task								= new CircuitTask(	
																												now, 								// Time Start
																												now + 5L * 60L * 1000L, 			// TimeEnd in 5 mins
																												targetTemperature,					// TempObjective in millidesrees
																												false,								// StopOnObjective
																												"1234567",							// Days
																												HVAC_TYPES.CircuitTask.Optimisation
				);
		this.circuitPump.on();
		this.state 																			= HVAC_STATES.Circuit.Optimising;
		this.heatRequired.setZero();
		this.taskActivate(task);
	}
	//
	//===========================================================================================================================================================

	//===========================================================================================================================================================
	//
	// Activity/State Change methods
	//
/**
 * Starts the circuit :
 * State = Starting
 * circuitPump = UNCHANGED
 * heatRequired.max/min = UNCHANGED, The Starting State should/will set it
 */	
	public  void start()
	{
		LogIt.action(this.name, "Start called");
		state																				= HVAC_STATES.Circuit.Starting;
	}
/**
 * Initiates stopping of the circuit :
 * State = Stopping.
 * circuitPump = UNCHANGED.
 * heatRequired.max/min = ZERO.
 * NB : State Stopping can lean on to Optimising. To end the task with certainty, use shutDown
 */	
	public void stop()
	{
		// Called on one of the following conditions
		//   1. Time is up : 					Detected/Called by Circuit_Abstract.scheduleTask
		//   2. Temperature objective reached : Detected/Called by Circuit_XXX.sequencer (thermometer surveillance)
		// Depending on the situation, the circuit will either optimise or stopdown completely
		LogIt.action(this.name, "Stop called");
		this.heatRequired.setZero();
		state 																				= HVAC_STATES.Circuit.Stopping;
	}
/**
 * Sets the circuit to normal operating :
 * State set to Running.
 * circuitPump = UNCHANGED.
 * heatRequired.max/min = UNCHANGED, The Running State should/will set it.
 */	
	public void nowRunning()
	{
		LogIt.action(this.name, "NowRunning called");
		state 																				= HVAC_STATES.Circuit.Running;
	}
/**
 * Idles the circuit :
 * State = Idle.
 * circuitPump = UNCHANGED.
 * heatRequired.max/min = 0.
 */	
	public void idle()
	{
		LogIt.action(this.name, "Idle called");
		this.heatRequired.setZero();
		state 																				= HVAC_STATES.Circuit.Idle;
	}
/**
 * Shuts down the circuit :
 * State = Off.
 * circuitPump = OFF.
 * heatRequired.max/min = ZERO.
 * task will be deactivated by scheduler.
 */
	public void shutDown()
	{
		LogIt.action(this.name, "Closing down completely");
		circuitPump.off();
		this.heatRequired.setZero();
		state 																				= HVAC_STATES.Circuit.Off;
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
 * State = Suspended.
 * circuitPump = OFF.
 * heatRequired.max/min = ZERO.
 */	
	public void suspend()
	{
		LogIt.action(this.name, "Suspend called");
		this.heatRequired.setZero();
		this.circuitPump.off();
		state 																				= HVAC_STATES.Circuit.Suspended;
	}						
/**
 * Resumes the circuit :
 * State = Resuming.
 * circuitPump = UNCHANGED.
 * heatRequired.max/min = UNCHANGED, The Resuming State should/will set it.
 */	
	public void resume()						
	{						
		LogIt.action(this.name, "Resume called");						
		state 																				= HVAC_STATES.Circuit.Resuming;
	}
/**
 * Optimises the circuit :
 * State = Optimising.
 * circuitPump = ON.
 * heatRequired.max/min = ZERO.
 */	
	public void optimise()						
	{						
		LogIt.debug(this.name + "Optimising called");
		LogIt.action(this.name, "Optimising called");
		this.heatRequired.setZero();
		this.circuitPump.on();																// This checks to see if on to avoid uneccessary relay activity	
		state 																				= HVAC_STATES.Circuit.Optimising;
	}
	//
	//===========================================================================================================================================================

	//===========================================================================================================================================================
	//
	// Sequencer
	//
	public abstract void sequencer();
	public abstract Boolean canOptimise();
	//
	//===========================================================================================================================================================

	//===========================================================================================================================================================
	//
	// Scheduling
	//


/**
 * - taskActive.stop() called for current task is time up
 * - Searches tasks to be scheduled and if one found
 * - activateTask called with taskFound unless
 *   . We are in summer
 *   . We are away
 */	
	public void taskScheduler()
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
		
		if ((taskActive != null) && (this.state == HVAC_STATES.Circuit.Off))
		{
			taskActive 																		= null;		// Task has recently been deactivated
		}
		
		// Deschedule activeTask if time is up
		if (taskActive != null)
		{
			// Avoid midnight perturbations
			if (Global.Time.now() > Global.Time.parseTime("23:58"))
			{
				if (this.state == HVAC_STATES.Circuit.Optimising)					this.shutDown();
				else 																taskDeactivate(taskActive);
				return; 															// Go no further
			}
			// Carry on with the real work
			if (	(now > taskActive.timeEnd						) 							// taskActive : Time up
			&&		(this.state != HVAC_STATES.Circuit.Stopping		)   
			&&		(this.state != HVAC_STATES.Circuit.Optimising	)   )
			{
				// Time is up for this task and it hasn't yet been asked to stop
				taskDeactivate(taskActive);		// Sets state to Stopping (which can go to Optimising) and end up Off
				// TODO Let it move to optimising or Off
				return;
			}
			
			if (	(taskActive.stopOnObjective								)
			&&		(this.circuitThermo.reading > taskActive.tempObjective  )	)
			{
				taskDeactivate(taskActive);		// Sets state to Stopping (which can go to Optimising) and end up Off
				// TODO Let it move to optimising or Off
				return;
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
				// Find the latest task elligible to run
				
				if (		(  circuitTask.timeStart - this.getRampUpTime(circuitTask.tempObjective) > now	)						// This task has yet to be performed (timeStart future
				&& 			(  circuitTask.timeEnd > now													)  		)				// Or time End future
				{
					// This task has yet to run : both start and end are in the future
					// Nothing todo
				}
				else if (	(  circuitTask.timeStart - this.getRampUpTime(circuitTask.tempObjective) < now	)						// This task has yet to be performed (timeStart is past
				&& 			(  circuitTask.timeEnd > now													)		)				// and time End is future
				{
					// This task should be run : start is past and end is the future and has not yet run
					// We can swap this task in
					if 	(	(taskFound == null								)														// circuitTask is first candidate
					||		(circuitTask.timeStart > taskFound.timeStart	)		)												// circuitTask is later candidate
					{
						taskFound															= circuitTask;
					}
				}
			}
		}
		if (Global.Time.now() > Global.Time.parseTime("23:55"))								return;			// Avoid scheduling tasks just before midnight
		
		if (! Global.isAway())				// We are not away so get going
		{
			if (taskFound != null)
			{
				if	(taskFound.dateLastRun.equals(today))									return;	// Task has already run or is already running
				if (type == CIRCUIT_TYPE_HotWater)
				{
					taskActivate(taskFound);
				}
				else		// Floor or Radiator
				{
					if (Global.thermoOutside.reading == null)								return;
					
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
	}	// taskScheduler
/**
 * Activates task supplied in parameter :
 * task.start() called
 */	
	public void taskActivate(CircuitTask 							thisTask)
	{
		LogIt.display("Circuit_Abstract", "taskActivate", this.name + " Task activated " + thisTask.days + " " + thisTask.timeStartDisplay + " - " + thisTask.timeEndDisplay + " " + thisTask.taskType.toString());

		if (this.taskActive == null)														// Normal operation
		{
			LogIt.display("Circuit_Abstract", "taskActivate", "Called task Scheduled");
			this.taskActive																	= thisTask;
			this.start();
			this.taskActive.dateLastRun														= Global.Date.now();
		}
		else if (this.state == HVAC_STATES.Circuit.Stopping)									// Its just been set, hasn't had time to move to optimising or Off
		{
			LogIt.debug("taskActive is stopping          = " + taskActive.days + " +++ " + taskActive.taskType.toString());
			return;
		}
		else if (taskActive != thisTask)														// Could arise (???) during rampUp
		{
			if (taskActive.taskType == HVAC_TYPES.CircuitTask.Optimisation)
			{
				// This can happen. Just switch it in
				LogIt.display("Circuit_Abstract", "taskActivate", "Called task Swapped in as optimisation in progress");
				this.taskActive																	= thisTask;
				this.start();
				this.taskActive.dateLastRun														= Global.Date.now();
			}
			else if (thisTask.taskType == HVAC_TYPES.CircuitTask.Optimisation)
			{
				// taskActive is not Optimisation but thisTask is. taskActive is more important
				LogIt.display("Circuit_Abstract", "taskActivate", "Called task ignored as it is for optimisation while currentTask is doing real work");
			}
			else
			{
				LogIt.error("Circuit_Abstract", "taskActivate", "A task is active when it shouldn't be");
				LogIt.info("Circuit_Abstract", "taskActivate", "Task to activate is occupied... Replaced");
				LogIt.info("Circuit_Abstract", "taskActivate", "Task to activate is already active");
				LogIt.display("Circuit_Abstract", "taskActivate", "taskActive          = " + taskActive.days + " +++ " + taskActive.taskType);
				LogIt.display("Circuit_Abstract", "taskActivate", "thisTask(candidate) = " + thisTask.days + " +++ " + thisTask.taskType);
			}
		}
		else	// Dont know how Should never happen, we are scheduling a currently active task. Just let it run
		{
			LogIt.error("Circuit_Abstract", "taskActivate", "WOULD HAVE SAID : A task is active when it shouldn't be");
			LogIt.info("Circuit_Abstract", "taskActivate", "Task to activate is already active");
			LogIt.display("Circuit_Abstract", "taskActivate", "taskActive          = " + System.identityHashCode(taskActive));
			LogIt.display("Circuit_Abstract", "taskActivate", "thisTask(candidate) = " + System.identityHashCode(thisTask));
		}

//			this.taskActive																	= thisTask;
//			this.start();						
//			this.taskActive.dateLastRun														= Global.Date.now();
	}	// taskActivate
/**
 * Deactivates current task :
 * taskActive set to null
 */	
	public void taskDeactivate(CircuitTask thisTask)			// After deactivation, all tasks should be inactive
	{
		LogIt.display("Circuit_Abstract", "taskDeactivate", this.name + " Task Deactivated " + thisTask.days + " " + thisTask.timeStartDisplay + " - " + thisTask.timeEndDisplay);
		if (thisTask != this.taskActive)
		{
			LogIt.display("Circuit_Abstract", "taskDeactivate", "something has gone wrong, deActivated Task isn't the running task");
		}
		this.taskActive.dateLastRun															= Global.Date.now();
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
		// taskActive is not set to null so that Circuit_Mixer & Thread_Mixer keeps a handle onto the task
		// It will be set to null by the sequencer once it has really stopped
		this.stop();
		LogIt.display("Circuit_Abstract", "taskDeactivate", "==============================================================");
	}	// taskDeactivate
	//
	//===========================================================================================================================================================
}
