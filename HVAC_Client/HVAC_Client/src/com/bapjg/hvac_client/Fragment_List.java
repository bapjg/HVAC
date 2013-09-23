package com.bapjg.hvac_client;

import android.app.Fragment;
import android.os.Bundle;

public class Fragment_List extends Fragment
{

	public Fragment_List() 
	{
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_configuration);
		
		System.out.println(Global.henry);
		// TextView field	 						= (TextView) findViewById(R.id.Data);
		// field.setText(Global.henry);
	}

	
}
