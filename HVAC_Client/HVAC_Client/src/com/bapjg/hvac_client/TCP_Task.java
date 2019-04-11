package com.bapjg.hvac_client;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;
import HVAC_Common.*;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class TCP_Task 											extends 					AsyncTask <Msg__Abstract, Void, Msg__Abstract>
{
	public TCP_Response											callBack;
	public TCP_Connection										piConnection;

	public TCP_Task()
	{
		this.piConnection																	= new TCP_Connection();
		this.callBack																		= null;
	}
	@Override
	protected Msg__Abstract doInBackground(Msg__Abstract... messageOut) 
	{
		return piConnection.piTransaction(messageOut[0]);
	}	
	protected void onProgressUpdate(Void... progress)  { }
	@Override
    protected void onPostExecute(Msg__Abstract messageReturn) 
	{             
		callBack.processFinishTCP(messageReturn);
		piConnection.disconnect();
    }
}
