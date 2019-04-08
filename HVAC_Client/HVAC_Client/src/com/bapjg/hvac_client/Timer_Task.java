//--------------------------------------------------------------65--------------------------93-------------------------------------------------------------------
package com.bapjg.hvac_client;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import HVAC_Common.Ctrl_Temperatures;
import HVAC_Common.Ctrl_Temperatures.Request;

public class Timer_Task 										extends						TimerTask
{
	
	public TCP_Response											callBack;
	
	public Timer_Task(TCP_Response callBack)
	{
		this.callBack																		= callBack;
	}
	
	
	@Override
	public void run()
	{
		TCP_Task												task						= new TCP_Task();
	   	task.callBack																		= callBack;					// processFinish
	   	task.execute(new Ctrl_Temperatures().new Request());
//	   	Global.toaster("45", true); // Comment/Uncomment to force recompilation
	}
}



/*

TimerTask timerTask = new MyTimerTask();
//running timer task as daemon thread
Timer timer = new Timer(true);
timer.scheduleAtFixedRate(timerTask, 0, 10*1000);
System.out.println("TimerTask started");
//cancel after sometime
try {
    Thread.sleep(120000);
} catch (InterruptedException e) {
    e.printStackTrace();
}
timer.cancel();
System.out.println("TimerTask cancelled");
try {
    Thread.sleep(30000);
} catch (InterruptedException e) {
    e.printStackTrace();
}

*/