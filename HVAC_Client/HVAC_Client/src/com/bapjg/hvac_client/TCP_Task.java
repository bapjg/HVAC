package com.bapjg.hvac_client;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import HVAC_Messages.*;

public class TCP_Task extends AsyncTask <Ctrl_Abstract, Void, Ctrl_Abstract>
{
	public Activity						activity;

	public TCP_Task(Activity activity)
	{
		this.activity										= Global.activity;
	}
	
	@Override
	protected Ctrl_Abstract doInBackground(Ctrl_Abstract... messageOut) 
	{
		if (Global.piConnection.connect())
		{
			return Global.piConnection.piTransaction(messageOut[0]);
		}
		else
		{
			return new Ctrl_Abstract().new NoConnection();

		}
	}	
	protected void onProgressUpdate(Void... progress)  { }
	@Override
    protected void onPostExecute(Ctrl_Abstract result) 
	{             
		if (result instanceof Ctrl_Temperatures)
		{
		}
		else if (result instanceof Ctrl_Abstract.NoConnection)
		{
			Toast.makeText(activity, "No Connection established yet", Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(activity, "A Nack has been returned", Toast.LENGTH_SHORT).show();
		}
    }
	public void displayTemps() 	{ /* OverRidden in instantiated classes */	}
}
