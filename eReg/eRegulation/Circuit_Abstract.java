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
	public static final int			CIRCUIT_STATE_Started 		= 1;
	public static final int			CIRCUIT_STATE_Running 		= 2;
	public static final int			CIRCUIT_STATE_Stopping	 	= 3;
	public static final int			CIRCUIT_STATE_Optimising 	= 4;
	public static final int			CIRCUIT_STATE_Error	 		= -1;

	public static final int			CIRCUIT_STATE_Suspended		= -1;
	public static final int			CIRCUIT_STATE_RampUp 		= -1;

	public Mixer					mixer						= null;
	public TemperatureGradient 		temperatureGradient			= null;				//This will be overridden
	
	public CircuitTask				taskActive					= null;
	public CircuitTask				taskNext					= null;
	
	public ArrayList <CircuitTask> 	circuitTaskList 			= new ArrayList <CircuitTask>();
	public HeatRequired				heatRequired				= new HeatRequired();
	
	public Boolean					willBeSingleCircuit			= false;

	public Circuit_Abstract()
	{	
	}
	public Circuit_Abstract(String name, String friendlyName, String circuitType, String tempMax, String rampUpTime)
	{	
		this.name												= name;
		this.friendlyName										= friendlyName;
		this.circuitType										= circuitType;
		this.tempMax											= Integer.parseInt(tempMax);
		this.rampUpTime											= Long.parseLong(rampUpTime);
		this.state												= CIRCUIT_STATE_Off;
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
		System.out.println("Overriden method called in Abstract");
		return 10000L;
	}
	public Long calculatePerformance()
	{
		System.out.println("Overriden method called in Abstract");
		return 0L;
	}
	public void start()
	{
		System.out.println("Overriden method called in Abstract");
	}
	public void stop()
	{
		System.out.println("Overriden method called in Abstract");
	}
	public void sequencer()
	{
		// Task overridden in sub classes
	}
	public void scheduleTaskNext()
	{
		String day 												= Global.getDayOfWeek();  				// day = 1 Monday ... day = 7 Sunday// Sunday = 7, Monday = 1, Tues = 2 ... Sat = 6

		if (this.taskNext == null) // Is this a good idea. If its not null, could it be re-arranged 
		{
			for (CircuitTask circuitTask : circuitTaskList) 
			{
				if (circuitTask.days.contains(day))
				{
					if (circuitTask.timeStart > Global.getTimeNowSinceMidnight())
					{
						// This task has yet to be performed
						// Should we not check that it is either running or already scheduled
						if (this.taskNext == null)
						{
							this.taskNext						= circuitTask;
							System.out.println("Circuit : " + this.name + " put on next to run list");
						}
						else
						{
							if (circuitTask.timeStart < this.taskNext.timeStart)
							{
								this.taskNext					= circuitTask;
								System.out.println("Circuit : " + this.name + " put on next to run list");
							}
						}
					}
				}
			}
		}
		if (this.taskNext == null) // If it still is null, then we can do the same over again on next DAY
		{
			// day = 1 Monday ... day = 7 Sunday// Sunday = 7, Monday = 1, Tues = 2 ... Sat = 6
			// Must Convert to int, add 1 modulo 7
			// Convert back to string
			String tomorrow 									= Global.getDayOfWeek() + 1; // Rubbish as string
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
			System.out.println("Scheduling active task for " + this.name + " and nextTask is not null");
			//There is a waiting task. Replace active task if it exists
			
			if ((Global.getTimeNowSinceMidnight() > this.taskNext.timeStart - getRampUpTime())			//Which get rampuptime ??? this or next
			&&  (Global.getTimeNowSinceMidnight() < this.taskNext.timeEnd))
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
			}
			
			// At this stage taskActive may (or may not) have been scheduled to taskNext
			// Now see if other tasks are scheduled to run at the same time
			
			if (this.taskActive != null)
			{
				for (Circuit_Abstract circuit : Global.circuits.circuitList)
				{
					this.willBeSingleCircuit 						= true;
					
					if ( ! this.name.equalsIgnoreCase(circuit.name))					// We are looking for other tasks than the current
					{
						System.out.println(this.name + " current circuit");
						System.out.println(circuit.name + " investigated circuit");
						
						
						if (circuit.taskActive != null)
						{
							this.willBeSingleCircuit				= false;			// A task is currently active, so we will not be alone
							return;
						}
						if (circuit.taskNext != null)
						{
							if (circuit.taskNext.timeStart - circuit.getRampUpTime() < this.taskActive.timeEnd)
							{
								this.willBeSingleCircuit			= false;
								return;
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
