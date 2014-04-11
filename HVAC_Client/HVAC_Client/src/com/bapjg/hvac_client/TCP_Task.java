package com.bapjg.hvac_client;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import HVAC_Messages.*;

public class TCP_Task extends AsyncTask <Ctrl_Abstract, Void, Ctrl_Abstract>
{
	public TCP_Response					callBack;
	public TCP_Connection				piConnection;

	public TCP_Task()
	{
		this.piConnection									= new TCP_Connection();
		this.callBack										= null;
	}
	@Override
	protected Ctrl_Abstract doInBackground(Ctrl_Abstract... messageOut) 
	{
		return piConnection.piTransaction(messageOut[0]);
	}	
	protected void onProgressUpdate(Void... progress)  { }
	@Override
    protected void onPostExecute(Ctrl_Abstract messageReturn) 
	{             
		callBack.processFinish(messageReturn);
		piConnection.disconnect();
    }
}
