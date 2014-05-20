package com.bapjg.hvac_client;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import HVAC_Messages.*;
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class HTTP_Task 											extends 					AsyncTask <Ctrl__Abstract, Void, Ctrl__Abstract>
{
	public HTTP_Response										callBack;
	public HTTP_Connection										serverConnection;

	public HTTP_Task()
	{
		this.serverConnection																= new HTTP_Connection();
		this.callBack																		= null;
	}
	@Override
	protected Ctrl__Abstract doInBackground(Ctrl__Abstract... messageOut) 
	{
		Log.v("App", "sending message");
		return serverConnection.serverTransaction(messageOut[0]);
	}	
	protected void onProgressUpdate(Void... progress)  { }
	@Override
    protected void onPostExecute(Ctrl__Abstract messageReturn) 
	{             
		callBack.processFinishHTTP(messageReturn);
    }
}
