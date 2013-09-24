package com.bapjg.hvac_client;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class Activity_Configuration extends FragmentActivity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuration);
		
		System.out.println(Global.henry);
		// TextView field	 						= (TextView) findViewById(R.id.Data);
		// field.setText(Global.henry);
	}
	protected View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState) 
	{
		View view = inflator.inflate(R.layout.fragment_list, container, false);
		setContentView(R.layout.activity_configuration);
		
		System.out.println(Global.henry);
		// TextView field	 						= (TextView) findViewById(R.id.Data);
		// field.setText(Global.henry);
		return view;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.configuration, menu);
		return true;
	}

}
