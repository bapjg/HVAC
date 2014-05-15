package com.bapjg.hvac_client;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import HVAC_Messages.*;
//Template										variable			= something
//Template										ext/imp				class

public class TCP_Task 							extends 			AsyncTask <Ctrl__Abstract, Void, Ctrl__Abstract>
{
	public TCP_Response							callBack;
	public TCP_Connection						piConnection;

	public TCP_Task()
	{
		this.piConnection											= new TCP_Connection();
		this.callBack												= null;
	}
	@Override
	protected Ctrl__Abstract doInBackground(Ctrl__Abstract... messageOut) 
	{
		return piConnection.piTransaction(messageOut[0]);
	}	
	protected void onProgressUpdate(Void... progress)  { }
	@Override
    protected void onPostExecute(Ctrl__Abstract messageReturn) 
	{             
		callBack.processFinishTCP(messageReturn);
		piConnection.disconnect();
    }
}
