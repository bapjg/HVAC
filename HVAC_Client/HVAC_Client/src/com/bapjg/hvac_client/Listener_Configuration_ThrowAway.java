package com.bapjg.hvac_client;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Listener_Configuration_ThrowAway implements View.OnClickListener
{
	@Override
	public void onClick(View myView) 
	{
		System.out.println("On click listener active");
		Toast.makeText(Global.appContext, "A Nack has been returned from " + Global.serverURL, Toast.LENGTH_LONG).show();
	}
}
