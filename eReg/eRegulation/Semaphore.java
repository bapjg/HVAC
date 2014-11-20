package eRegulation;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Semaphore
{

	public ReentrantLock 					semaphore 			= new ReentrantLock();
	public String							name;
	public String							owner;
	public Boolean							fast;

	public Semaphore(String name, Boolean fast)
	{
		this.name												= name;
		this.owner												= "";
		this.fast												= fast;
	}
	public Boolean semaphoreLock(String caller)
	{
		if (this.fast)
		{
			semaphore.lock();
			return true;
		}
		else
		{
			Boolean 							lockResult;
			try
			{
				lockResult 											= semaphore.tryLock(2, TimeUnit.SECONDS);
				owner 												= Thread.currentThread().getName();
			}
			catch (InterruptedException e1)
			{
				System.out.println(Global.DateTime.now() + " Lock on semaphore " + this.name + " failed, called by " + caller);
				return false;
			}
			
			if (!lockResult)
			{
				System.out.println(Global.DateTime.now() + " Lock on semaphore " + this.name + " timed out, called by " + caller + " owned by " + this.owner);
				return false;
			}
			else
			{
				return true;
			}
		}
	}
	public void semaphoreUnLock()
	{
		semaphore.unlock();
		owner													= "";
	}
}
