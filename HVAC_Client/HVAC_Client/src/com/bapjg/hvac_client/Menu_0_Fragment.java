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
//Template										variable			= something
//Template										ext/imp				class
public class Menu_0_Fragment 					extends 			Fragment 
{
	private 	int					menuLayout;
	
	public Menu_0_Fragment()
	{
		super();
	}
	public Menu_0_Fragment(int menuLayout)																					// layout   : id of the layout file	eg : R.layout.menu_1_temperatures
	{
		super();
		this.menuLayout												= menuLayout;
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
    	View 									thisView 			= inflater.inflate(this.menuLayout, container, false);				// Inflate the menuLayout into container (menu_container)
    	Button									firstButton			= (Button) ((ViewGroup) thisView).getChildAt(0);
    	allButtonsSetup((ViewGroup) thisView);
		((OnClickListener) this).onClick(firstButton);																		// Execute the onClickListener of the first menu button
    	return thisView;
    }
	public void onClick(View myView) 																						// This is the onClick event from the Menu
	{
    	Button 								myButton 				= (Button) myView;
		ViewGroup 							viewParent				= (ViewGroup) myView.getParent();									// Set all textColours to white
		allButtonsSetup(viewParent);
    	myButton.setTextColor(Color.YELLOW);
	}
	public void allButtonsSetup(ViewGroup thisView)
	{
		for (int i = 0; i < thisView.getChildCount(); i++)																	
		{
			Button							buttonChild 			= (Button) thisView.getChildAt(i);
			buttonChild.setOnClickListener((OnClickListener) this);															// Set the OnClickListener to the menuFragment object (ie this)
			buttonChild.setTextColor(Color.WHITE);																			// Colour white
		}
		
	}
}

