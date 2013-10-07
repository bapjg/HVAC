package com.bapjg.hvac_client;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

@SuppressLint("ValidFragment")
public class Menu_0_Fragment extends Fragment // implements View.OnClickListener
{
	private Fragment			 						fragment;
	private int											layout;
	
	public Menu_0_Fragment()
	{
		super();
		this.fragment									= null;
		this.layout										= -1;
	}
	public Menu_0_Fragment(Fragment fragment, int layout)
	{
		super();
		this.fragment									= fragment;
		this.layout										= layout;
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	//View result = inflater.inflate(R.layout.choices_configuration, container, false);
    	View result = inflater.inflate(this.layout, container, false);
    	// The choice list of buttons has been inflated
    	// They are all buttons, so set the OnClickListener to the fragment of RHS
		if (this.fragment != null)
		{
			for (int i = 0; i < ((ViewGroup) result).getChildCount(); i++)
			{
				Button							buttonChild 				= (Button) ((ViewGroup) result).getChildAt(i);
				buttonChild.setOnClickListener((OnClickListener) this.fragment);
			}
		}    	
    	return result;
    }
}

