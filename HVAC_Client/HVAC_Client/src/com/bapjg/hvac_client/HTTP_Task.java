package com.bapjg.hvac_client;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import HVAC_Common.*;
//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class HTTP_Task 											extends 					AsyncTask <Msg__Abstract, Void, Msg__Abstract>
{
	public HTTP_Response										callBack;
	public HTTP_Connection										serverConnection;

	public HTTP_Task()
	{
		this.serverConnection																= new HTTP_Connection();
		this.callBack																		= null;
	}
	@Override
	protected Msg__Abstract doInBackground(Msg__Abstract... messageOut) 
	{
		return serverConnection.serverTransaction(messageOut[0]);
	}	
	protected void onProgressUpdate(Void... progress)  { }
	@Override
    protected void onPostExecute(Msg__Abstract messageReturn) 
	{             
		callBack.processFinishHTTP(messageReturn);
    }
}
