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
	public abstract Long getRampUpTime(Integer tempObjective);
	public abstract Boolean canOptimise();
	//
	//===========================================================================================================================================================

	//===========================================================================================================================================================
	//
	// Activity/State Change methods
	//
/**
 * Starts the circuit :
 * State = Starting
 */	
	public void requestStart()
	{
		LogIt.action(this.name, "Start called");
		LogIt.display("Circuit_Abstract", "requestStart", "Start called " + this.name);
		state																				= HVAC_STATES.Circuit.StartRequested;
	}
/**
 * Optimises the circuit :
 * State = OptimisationRequested.
 */
	public void requestOptimisation()
	{
		state 																				= HVAC_STATES.Circuit.OptimisationRequested;
	}
/**
 * Stops the circuit :
 * State = StopRequested.
 */
	public void requestStop()
	{
		state 																				= HVAC_STATES.Circuit.StopRequested;
	}
 
/**
* Shuts down the circuit :
* State = ShutDownRequested.
*/
	public void requestShutDown()
	{
		state 																				= HVAC_STATES.Circuit.ShutDownRequested;
//			LogIt.action(this.name, "Closing down completely");
//			LogIt.debug("Closing down completely" + this.name);
//			circuitPump.off();
//			this.heatRequired.setZero();
//			state 																			= HVAC_STATES.Circuit.Off;
	}
	//
	//===========================================================================================================================================================

	//===========================================================================================================================================================
	//
	// Sequencer
	//
	public abstract void sequencer();
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
				this.requestShutDown();
				return; 															// Go no further
			}
			// Deschedule timeup
			if (	(now > taskActive.timeEnd								) 							// taskActive : Time up
			&&		(this.state != HVAC_STATES.Circuit.StopRequested		)   	// Only called for states start, rampup, running, suspended, resuming, idle
			&&		(this.state != HVAC_STATES.Circuit.OptimisationRequested)   	// AwaitingMixer, MixerReady
			&&		(this.state != HVAC_STATES.Circuit.ShutDownRequested	)   
			&&		(this.state != HVAC_STATES.Circuit.AwaitingMixer		)   	// Do not perturb mixer initialisation which takes a long time.
			&&		(this.state != HVAC_STATES.Circuit.MixerReady			)   	//     This is part of OptimisationRequested sequence
			&&		(this.state != HVAC_STATES.Circuit.Off					)   
			&&		(this.state != HVAC_STATES.Circuit.Optimising			)   )
			{
				// Time is up for this task and it hasn't yet been asked to stop
				LogIt.display("Circuit_Abstract", "taskScheduler/Line179", "deActivating task " + this.name + "with state " + this.state.toString());
				taskDeactivate(taskActive);		// Sets state to Stopping (which can go to Optimising) and end up Off
				return;
			}	// if deactivate called, return in order for the sequencer to have time to handle before recheduling next task
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
		LogIt.display("Circuit_Abstract", "taskActivate", "Task activate requested " + this.name + " : "+ thisTask.days + " " + thisTask.timeStartDisplay + " - " + thisTask.timeEndDisplay + " " + thisTask.taskType.toString());

		// Test to see if :
		// - activeTask is null (just swap in)
		// - activeTask if NOT null, and circuit is stopping
		// - a newTask is moving in replacing an unfinished activeTask
		// - try to schedule a task which is already active (shouldn't happen)

		if (this.taskActive == thisTask)												// Should not normally occur
		{
			LogIt.display("Circuit_Abstract", "taskActivate", "The scheduled task is already active");
			LogIt.debug("Circuit_Abstract/taskActivate taskActive    = " + System.identityHashCode(taskActive));
			LogIt.debug("Circuit_Abstract/taskActivate candidateTask = " + System.identityHashCode(thisTask));
		}
		else
		{
			if (this.taskActive == null)														// Normal operation
			{
				LogIt.display("Circuit_Abstract", "taskActivate", "Activated task inserted normally " + this.name + ", type " + thisTask.taskType);
			}
			else
			{
				LogIt.display("Circuit_Abstract", "taskActivate", "Activated task replacing a task " + this.name + ", details follow :");
				LogIt.display("Circuit_Abstract", "taskActivate", "Replaced Task  " + thisTask.days + " " + thisTask.timeStartDisplay + " - " + thisTask.timeEndDisplay + " " + thisTask.taskType.toString());
			}

			this.taskActive																	= thisTask;
			this.taskActive.dateLastRun														= Global.Date.now();
		
			switch (thisTask.taskType)
			{
			case Optimisation :
				this.requestOptimisation();
				break;
			case Immediate :
			case AntiFreeze :
			case Calendar :
				this.requestStart();
				break;
			}
		}
	}	// taskActivate
/**
 * Deactivates current task :
 * taskActive set to null
 */	
	public void taskDeactivate(CircuitTask thisTask)			// After deactivation, all tasks should be inactive
	{
		LogIt.display("Circuit_Abstract", "taskDeactivate", this.name + " Task Deactivated called " + thisTask.days + " " + thisTask.timeStartDisplay + " - " + thisTask.timeEndDisplay);
		this.taskActive.dateLastRun															= Global.Date.now();
		// taskActive is not set to null so that Circuit_Mixer & Thread_Mixer keeps a handle onto the task
		// It will be set to null by the sequencer once it has really stopped
		this.requestStop();
	}	// taskDeactivate
	//
	//===========================================================================================================================================================
}
