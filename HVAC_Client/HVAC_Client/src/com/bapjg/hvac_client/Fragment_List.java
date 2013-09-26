package com.bapjg.hvac_client;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
		
		//System.out.println(Global.henry);
		// TextView field	 						= (TextView) findViewById(R.id.Data);
		// field.setText(Global.henry);
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

	
}
