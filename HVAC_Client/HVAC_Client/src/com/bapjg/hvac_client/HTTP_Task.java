package com.bapjg.hvac_client;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import HVAC_Messages.*;
//Template										variable			= something
//Template										ext/imp				class

public class HTTP_Task 							extends 			AsyncTask <Ctrl_Abstract, Void, Ctrl_Abstract>
{
	public HTTP_Response						callBack;
	public HTTP_Connection						serverConnection;

	public HTTP_Task()
	{
		this.serverConnection										= new HTTP_Connection();
		this.callBack												= null;
	}
	@Override
	protected Ctrl_Abstract doInBackground(Ctrl_Abstract... messageOut) 
	{
		return serverConnection.serverTransaction(messageOut[0]);
	}	
	protected void onProgressUpdate(Void... progress)  { }
	@Override
    protected void onPostExecute(Ctrl_Abstract messageReturn) 
	{             
		callBack.processFinishHTTP(messageReturn);
    }
}
