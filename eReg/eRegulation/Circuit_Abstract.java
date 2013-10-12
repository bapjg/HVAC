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
	
	public static final int			CIRCUIT_STATE_Off 			= 0;
	public static final int			CIRCUIT_STATE_Starting 		= 1;
	public static final int			CIRCUIT_STATE_Running 		= 2;
	public static final int			CIRCUIT_STATE_Stopping	 	= 3;
	public static final int			CIRCUIT_STATE_Optimising 	= 4;
	public static final int			CIRCUIT_STATE_Error	 		= -1;

	public static final int			CIRCUIT_STATE_Suspended		= 5;
	public static final int			CIRCUIT_STATE_Resuming 		= 6;

	public Mixer					mixer						= null;
	public TemperatureGradient 		temperatureGradient			= null;				//This will be overridden
	
	public CircuitTask				taskActive					= null;
	public CircuitTask				taskNext					= null;
	
	public ArrayList <CircuitTask> 	circuitTaskList 			= new ArrayList <CircuitTask>();
	public HeatRequired				heatRequired				= null;
	
	public Boolean					willBeSingleCircuit			= false;

//	public Circuit_Abstract()
//	{	
//	}
	public Circuit_Abstract(String name, String friendlyName, String circuitType, String tempMax, String rampUpTime)
	{	
		this.name												= name;
		this.friendlyName										= friendlyName;
		this.circuitType										= circuitType;
		this.tempMax											= Integer.parseInt(tempMax);
		this.rampUpTime											= Long.parseLong(rampUpTime);
		this.state												= CIRCUIT_STATE_Off;
		this.heatRequired										= null;
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
		CircuitTask 	circuitTaskItem 						= new CircuitTask(timeStart, timeEnd, tempObjective, stopOnObjective, days);
		circuitTaskList.add(circuitTaskItem);
	}
	public Long getRampUpTime()
	{
		return 10000L; // Overriddent method
	}
	public Long calculatePerformance()
	{
		return 0L; // Overriddent method
	}
	public void start()
	{
		LogIt.action(this.name, "Start called");
		this.state												= CIRCUIT_STATE_Starting;
		this.heatRequired										= new HeatRequired();
	}
	public void stop()
	{
		LogIt.action(this.name, "Stop called");
		this.state												= CIRCUIT_STATE_Stopping;
		this.heatRequired										= null;
	}
	public void suspend()
	{
		LogIt.action(this.name, "Suspend called");
		this.state												= CIRCUIT_STATE_Suspended;
	}
	public void resume()
	{
		LogIt.action(this.name, "Resume called");
		this.state												= CIRCUIT_STATE_Resuming;
	}
	public void sequencer()										// Task overridden in sub classes
	{
	}
	public void scheduleTaskNext()
	{
		String day 												= Global.getDayOfWeek(0);				// day = 1 Monday ... day = 7 Sunday// Sunday = 7, Monday = 1, Tues = 2 ... Sat = 6

		LogIt.info("Circuit_Abstract", "scheduleTaskNext", "1. Looking for tasks on " + this.name);
		if (this.taskNext == null)
		{
			LogIt.info("Circuit_Abstract", "scheduleTaskNext", "2. Looking for tasks on " + this.name + " and Next is null");
			for (CircuitTask circuitTask : circuitTaskList) 											// Go through all tasks
			{
				LogIt.info("Circuit_Abstract", "scheduleTaskNext", "3. Looking at task on " + this.name + " starting at " + circuitTask.timeStartDisplay + " state is " + circuitTask.state);
				LogIt.info("Circuit_Abstract", "scheduleTaskNext", "3. Looking at task on " + this.name + " contains day " + (circuitTask.days.contains(day)));
				LogIt.info("Circuit_Abstract", "scheduleTaskNext", "3. Looking at task on " + this.name + " state waitin " + (circuitTask.state == circuitTask.TASK_STATE_WaitingToStart));
				LogIt.info("Circuit_Abstract", "scheduleTaskNext", "day is " + day);

				if ((circuitTask.days.contains(day)) && (circuitTask.state == circuitTask.TASK_STATE_WaitingToStart))														// This one is for today
				{
					LogIt.info("Circuit_Abstract", "scheduleTaskNext", "4. Looking at task on " + this.name + " starting at " + circuitTask.timeStartDisplay);
					
					if ((circuitTask.timeStart > Global.getTimeNowSinceMidnight())						// This task has yet to be performed (timeStart > now
					|| (circuitTask.timeEnd > Global.getTimeNowSinceMidnight()))						// Or time End > now
						// Also need to handle the case of restarting the Regulator in the middle of a program
					{
						LogIt.info("Circuit_Abstract", "scheduleTaskNext", "5. Found a task on " + this.name + " starting at " + circuitTask.timeStartDisplay);
						// No allowance is made for rampuptime : not really needed as this is setup long before at around midnight
						// We started the loop with taskNext = null
						// Item 1 might have been put on the list, so we need to take the first one.
						if (this.taskNext == null)		// 												
						{
							this.taskNext						= circuitTask;
							LogIt.info("Circuit_Abstract", "scheduleTaskNext", "6. Circuit " + this.name + " starting at " + circuitTask.timeStartDisplay + " put on the next to run list", true);
						}
						else
						{
							if (circuitTask.timeStart < this.taskNext.timeStart)
							{
								this.taskNext					= circuitTask;
								LogIt.info("Circuit_Abstract", "scheduleTaskNext", "7. Found a task on " + this.name + " starting at " + circuitTask.timeStartDisplay + " replaced current on the next to run list", true);
							}
						}
					}
				}
			}
		}
		else
		{
			// The taskNext is not null, but with a modified calendar could want to rearrange things
		}
		if (this.taskNext == null) // If it still is null, then we can do the same over again on next DAY
		{
			// day = 1 Monday ... day = 7 Sunday// Sunday = 7, Monday = 1, Tues = 2 ... Sat = 6
			// Must Convert to int, add 1 modulo 7
			// Convert back to string
			String tomorrow 									= Global.getDayOfWeek(1);
			// Do the same as above, but get the first activity after midnight
		}
	}
	public void scheduleTaskActive()
	{
		// Purpose is to place taskNext as activeTask at the right time
		// Additionally we need to know if the newly scheduled taskActive will will be alone
		
		// Time up is handled circuit sequencer
		//    either : stop on objective / stop on time
		// Also handles case of Optimising

		if (this.taskNext != null)
		{
			//There is a waiting task. Replace active task if it exists
			
			if ((Global.getTimeNowSinceMidnight() > this.taskNext.timeStart - this.getRampUpTime())			// TimeNow is after starttime - rampup
			&&  (Global.getTimeNowSinceMidnight() < this.taskNext.timeEnd))									// TimeNow is still before stoptime
			{
				System.out.println("============A task has been scheduled");
				System.out.println("Scheduled to start at : " + this.taskNext.timeStart);
				System.out.println("Rampup                : " + getRampUpTime());
				System.out.println("Decision              : " + (this.taskNext.timeStart -  getRampUpTime()));
				System.out.println("Now                   : " + Global.getTimeNowSinceMidnight());
				
				if (this.taskActive != null)
				{
					this.taskActive.state						= CircuitTask.TASK_STATE_Completed;
				}
				this.taskActive									= this.taskNext;
				this.taskNext									= null;
				this.taskActive.state							= CircuitTask.TASK_STATE_Started;
				this.start();
			}
			
			// At this stage taskActive may (or may not) have been scheduled to taskNext
			// Now see if other tasks are planned to run at the same time
			
			if ((this.taskActive != null) &&  (Global.getTimeNowSinceMidnight() > this.taskActive.timeEnd))	// Task has finished its time
			{
				// We should call circuit.stop()
				this.taskActive.state							= CircuitTask.TASK_STATE_WaitingToStart;	// Should be Completed ....
			}
				
			if (this.taskActive != null)												// This is to determine whether task will be alone
			{
				this.willBeSingleCircuit 							= true;
				
				for (Circuit_Abstract circuit : Global.circuits.circuitList)
				{
					if ( ! this.name.equalsIgnoreCase(circuit.name))					// We are looking for other tasks than the current
					{
						if (circuit.taskActive != null)
						{
							this.willBeSingleCircuit				= false;			// A task is currently active, so we will not be alone
						}
						if (circuit.taskNext != null)
						{
							if (circuit.taskNext.timeStart - circuit.getRampUpTime() < this.taskActive.timeEnd)
							{
								this.willBeSingleCircuit			= false;			// A task will be active before we finish
							}
						}
					}
				}
			}
		}
	}
//	public void scheduleTasks()
//	/*
//	 *  This sets up "circuitTask" to be the task selected to run based on date/time
//	 *  If the circuit has no task programmed, "circuitTask" is null (not true, but should it be true)
//	 *  
//	 *  Circuit_Abstract has several implmentations (HW/Radiator/Mixer
//	 *  
//	 *  Circuit_Abstract has a list of tasks (circuitTask)
//	 *  
//	 *  
//	 *  
//	 *  
//	 */
//	{
//		// First calculate time to start next task based on thermal performance.
//		// This will be circuit dependant
//		// Hence timeToStart = timeStart - thrermalrampup
//		/*
//		 if (now > timeStart - thrermalrampup)
//		 {
//		 	if (this.taskActive != null)
//		 	{
//		 	// mAY NEED TO STOP THE TASK OR DO SOMETHING
//		 	}
//		 	
//		 	this.taskActive = this.taskNext;
//		 	this.taskNext = null;
//		 }
//		 
//		 Set the task state to rampup
//		 HW = Wait to get boiler temp > HW temp before pumpon
//		      Determine the max/min boiler temp required
//		 Floor = Start the mixer and set floor temp at 40°
//		      Determine the max/min boiler temp required
//		 Radiator = Dont know what to do
//		      Determine the max/min boiler temp required
//		 
//		 
//		 End of task can be due to
//		      New task with rampup requirements stepping in
//		      targetTemp reached (HW)
//		      timeEnd reached
//		      
//	     At task end allow for optimising
//	     		eg HW carrying on
//		 * 
//		 * 
//		 */
//		
//		String day 												= Global.getDayOfWeek();  				// day = 1 Monday ... day = 7 Sunday// Sunday = 7, Monday = 1, Tues = 2 ... Sat = 6
//		
//		
//		if (taskActive != null)
//		{
//			//There is an active task. No need to schedule anything until it has finished
//			//Unless it is not running (ie we stopped the program mid-schedule
////			LogIt.info("Circuit","scheduleTasks", "activeTask <> null in : " + name);
//			if (Global.getTimeNowSinceMidnight() > taskActive.timeEnd)
//			{
//				taskActive.state								= CircuitTask.TASK_STATE_Completed;
//				// 
//				// If this a temporary task, should be deleted
//				//
//				// LogIt.info("Circuit","scheduleTasks", "checking circuitTask for : " + name + "starting at time " + circuitTask.timeStart/3600/100 + "completed");
//			}
//			else
//			{
//				// Just carry on
//			}
//		}
//		else
//		{
//			for (CircuitTask circuitTask : circuitTaskList) 
//			{
//
//				if (circuitTask.days.contains(day))
//				{
//					//	LogIt.info("Circuit","scheduleTasks", "checking circuitTask for : " + name + "starting at time " + circuitTask.timeStart/3600/100);
//					
//					// Three possibilities : Task is yet to run, task currently running, finished running
//					if (circuitTask.state != circuitTask.TASK_STATE_Completed)
//					{
//						if (Global.getTimeNowSinceMidnight() < circuitTask.timeStart)
//						{
//							circuitTask.state						= circuitTask.TASK_STATE_WaitingToStart;
//							// LogIt.info("Circuit","scheduleTasks", "checking circuitTask for : " + name + "starting at time " + circuitTask.timeStart/3600/100 + "waiting to start");
//						}
//						else if ((circuitTask.timeStart < Global.getTimeNowSinceMidnight()) && ( Global.getTimeNowSinceMidnight() < circuitTask.timeEnd))
//						{
//							circuitTask.state						= circuitTask.TASK_STATE_Started;	// Indication in the Task that it has been started (just informative)
//							
//							// All this is very fudgy/strange/needs to be sorted out
//							
//							state									= CIRCUIT_STATE_Started;				// State of the circuit is started. The sequencer will actually get things moving
//							taskActive								= circuitTask;
//							// LogIt.info("Circuit","scheduleTasks", "checking circuitTask for : " + name + "starting at time " + circuitTask.timeStart/3600/100 + "activating");
//						}
//						else if (Global.getTimeNowSinceMidnight() > circuitTask.timeEnd)
//						{
//							circuitTask.state						= circuitTask.TASK_STATE_Completed;
//							// activeTask will be set to null in the sequencer
//							// LogIt.info("Circuit","scheduleTasks", "checking circuitTask for : " + name + "starting at time " + circuitTask.timeStart/3600/100 + "completed");
//						}
//						else
//						{
//							// We have an error
//							circuitTask.state						= circuitTask.TASK_STATE_Error;
//						}
//					}
//				}
//				else
//				{
//					circuitTask.state								= circuitTask.TASK_STATE_NotToday;
//				}
//			}
//		}
//	}
}
