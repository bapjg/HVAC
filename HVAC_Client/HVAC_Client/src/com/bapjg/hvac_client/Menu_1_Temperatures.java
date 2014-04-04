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
public class Menu_1_Temperatures 	extends 	Menu_0_Fragment 
									implements 	View.OnClickListener
{
	public Menu_1_Temperatures(int menuLayout)
	{
		super(menuLayout);
	}
	public void onClick(View myView) 									// This is the onClick event from the Menu
	{
		super.onClick(myView);
		
    	Button 								myButton 					= (Button) myView;
    	String								myCaption					= myButton.getText().toString();
    	
    	if (myCaption.equalsIgnoreCase("Temperatures"))
    	{
    		System.out.println("Menu : Temperatures Click again");
    		Global.panelTemperatures.update();
    		System.out.println("Menu : Temperatures Called update");
    	}
	}
}
