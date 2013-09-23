package com.bapjg.hvac_client;

import android.app.Fragment;
import android.os.Bundle;

public class List_Fragment extends Fragment
{

	public List_Fragment() 
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
