package com.bapjg.hvac_client;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

@SuppressLint("ValidFragment")
public class Choices_Configuration extends Fragment // implements View.OnClickListener
{
	private Fragment_Configuration 						fragment;
	
	public Choices_Configuration()
	{
		super();
		this.fragment									= null;
	}
	public Choices_Configuration(Fragment_Configuration fragment)
	{
		super();
		this.fragment									= fragment;
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	View result = inflater.inflate(R.layout.choices_configuration, container, false);
    	
		if (this.fragment != null)
		{
			for (int i = 0; i < ((ViewGroup) result).getChildCount(); i++)
			{
				Button							buttonChild 				= (Button) ((ViewGroup) result).getChildAt(i);
				buttonChild.setOnClickListener(this.fragment);
			}
		}    	
    	return result;
    }
}

