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
								
	public Integer												state;
	public CIRCUIT.STATE										stateNew;
								
	public static final int										CIRCUIT_STATE_Off 			= 0;
	public static final int										CIRCUIT_STATE_Starting 		= 1;
	public static final int										CIRCUIT_STATE_RampingUp		= 2;
	public static final int										CIRCUIT_STATE_Running 		= 3;
	public static final int										CIRCUIT_STATE_Stopping	 	= 4;
	public static final int										CIRCUIT_STATE_Optimising 	= 5;
	public static final int										CIRCUIT_STATE_Error	 		= -1;
							
	public static final int										CIRCUIT_STATE_Suspended		= 5;
	public static final int										CIRCUIT_STATE_Resuming 		= 6;
	public static final int										CIRCUIT_STATE_AwaitingHeat	= 7;
							
	public static final int										CIRCUIT_STATE_Start_Requested	= 10;
	public static final int										CIRCUIT_STATE_Stop_Requested	= 11;

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
		
		this.state																			= CIRCUIT_STATE_Off;
		this.heatRequired																	= null;
	}
	public void addCircuitTask(Ctrl_Calendars.Calendar 				paramCalendar)
	{
		CircuitTask 	circuitTaskItem 													= new CircuitTask(paramCalendar);
		circuitTaskList.add(circuitTaskItem);
	}
	public Long getRampUpTime(Integer tempObjective) 				{  /* OverRidden in Circuit_XXX classes */	return 0L; 	}
	public Long calculatePerformance()								{  /* OverRidden in Circuit_XXX classes */	return 0L; 	}
	public void sequencer()											{  /* OverRidden in Circuit_XXX classes */	}
	public void start()
	{
		LogIt.action(this.name, "Start called");
		this.state																			= CIRCUIT_STATE_Start_Requested;
		this.heatRequired																	= new HeatRequired();
	}
	public void start(CircuitTask 									thisTask)
	{
		LogIt.action(this.name, "Start called with circuitTask");
		this.taskActive																		= thisTask;
		this.state																			= CIRCUIT_STATE_Start_Requested;
		this.heatRequired																	= new HeatRequired();
	}
	public void stop()
	{
		// Called on one of the following conditions
		//   1. Time is up : 					Detected/Called by Circuit_Abstract.scheduleTask
		//   2. Temperature objective reached : Detected/Called by Circuit_XXX.sequencer (thermometer surveillance)
		LogIt.action(this.name, "Stop called");
		this.state																			= CIRCUIT_STATE_Stop_Requested;
		this.heatRequired																	= null;
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
		this.state																			= CIRCUIT_STATE_Off;
		this.heatRequired																	= null;
//		this.taskActive.active																= false; // What happens if the task has been switched to a new one
		taskDeactivate(this.taskActive);
	}
	public void interupt()
	{
//		LogIt.action(this.name, "closing down");
//		this.state																			= CIRCUIT_STATE_Off;
//		this.heatRequired																	= null;
//		this.taskActive.state																= this.taskActive.TASK_STATE_Completed; // What happens if the task has been switched to a new one
//		this.taskActive																		= null;
	}
	public void suspend()
	{
		LogIt.action(this.name, "Suspend called");
		this.state																			= CIRCUIT_STATE_Suspended;
	}						
	public void resume()						
	{						
		LogIt.action(this.name, "Resume called");						
		this.state																			= CIRCUIT_STATE_Resuming;
	}
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
			}
			else																			// Dont know how
			{
				LogIt.error("Circuit_Abstract", "taskActivate", "A task is active when it shouldn't be");
				LogIt.info("Circuit_Abstract", "taskActivate", "Task to activate is occupied... Replaced");
			}
			this.taskActive																	= thisTask;
			this.start();						
			this.taskActive.dateLastRun														= Global.Date.now();
		}
	}
	public void taskDeactivate(CircuitTask thisTask)			// After deactivation, all tasks should be inactive
	{
		LogIt.display("Circuit_Abstract", "taskDeactivate", this.name + " Task Deactivated ");
		this.taskActive.dateLastRun															= Global.Date.now();
		this.taskActive																		= null;
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
		String 												day 							= Global.Date.getDayOfWeek(0);				// day = 1 Monday ... day = 7 Sunday// Sunday = 7, Monday = 1, Tues = 2 ... Sat = 6
		Long 												now								= Global.Time.now();
		Long 												today							= Global.Date.now();
		CircuitTask											taskFound						= null;
		
		// Stop activeTask
		
		if (taskActive != null)
		{
			if (	(now > taskActive.timeEnd) 							// taskActive : Time up
			&& 		(this.state != CIRCUIT_STATE_Stop_Requested) 
			&&		(this.state != CIRCUIT_STATE_Stopping)   
			&&		(this.state != CIRCUIT_STATE_Optimising)   )
			{
				// Time is up for this task and it hasn't yet been asked to stop
				this.stop();
			}
		}

		// Go through all task entries for this circuit
		
		for (CircuitTask circuitTask : circuitTaskList) 													// Go through all tasks
		{	
			if (	(  circuitTask.days.contains(day)	) 
//			&& 		(! circuitTask.active				)	   )
					)
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
				}
			}
		}
		
		if (! Global.isAway())				// We are at home so get going
		{
			if (taskFound != null)
			{
				if (type == CIRCUIT_TYPE_HotWater)
				{
					taskActivate(taskFound);
				}
				else
				{
					if (Global.thermoOutside.reading > Global.tasksBackGround.summerTemp)
					{
						// TODO do nothing or marked as finished
					}
					else
					{
						taskActivate(taskFound);
					}
				}
			}
		}
	}
}
