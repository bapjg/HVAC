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
public class Menu_0_Fragment extends Fragment 
{
//	public  	Fragment			panelFragment;
	private 	int					menuLayout;
	
	public Menu_0_Fragment(int menuLayout)
	{
		// fragment : object which maintains the panel fragment			eg : panelTemperatures = new Panel_1_Temperatures();
		// layout   : id of the layout file								eg : R.layout.menu_1_temperatures
		super();
//		this.panelFragment								= panelFragment;
		this.menuLayout									= menuLayout;
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	View thisView = inflater.inflate(this.menuLayout, container, false);				// Inflate the menuLayout into container (menu_container)
    	
		for (int i = 0; i < ((ViewGroup) thisView).getChildCount(); i++)					// Set the OnClickListener to the menuFragment object (ie this)
		{
			Button				buttonChild 			= (Button) ((ViewGroup) thisView).getChildAt(i);
			buttonChild.setOnClickListener((OnClickListener) this);
		}
				
		((OnClickListener) this).onClick((Button) ((ViewGroup) thisView).getChildAt(0));	// Execute the onClickListener of the first menu button
    	return thisView;
    }
	public void onClick(View myView) 													// This is the onClick event from the Menu
	{
    	System.out.println("AbstractAbstract : We have arrived in onClick again");
    	Button 								myButton 					= (Button) myView;
		ViewGroup 							viewParent					= (ViewGroup) myView.getParent();	// Set all textColours to white

		for (int i = 0; i < viewParent.getChildCount(); i++)
		{
			Button							buttonChild 				= (Button) viewParent.getChildAt(i);
			buttonChild.setTextColor(Color.WHITE);
		}
		
		((Button) myView).setTextColor(Color.YELLOW);
	}
}

