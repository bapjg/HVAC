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
public class Menu_1_Temperatures extends Menu_0_Fragment implements View.OnClickListener
{
	public Menu_1_Temperatures(Fragment panelFragment, int menuLayout)
	{
		super(panelFragment, menuLayout);
	}
	public void onClick(View myView) 
	{
    	System.out.println("We have arrived in onClick again");
    	Button 								myButton 					= (Button) myView;
    	String								myCaption					= myButton.getText().toString();
    	
		// Set all textColours to white
		ViewGroup 							viewParent					= (ViewGroup) myView.getParent();
		for (int i = 0; i < viewParent.getChildCount(); i++)
		{
			Button							buttonChild 				= (Button) viewParent.getChildAt(i);
			buttonChild.setTextColor(Color.WHITE);
		}
		
		((Button) myView).setTextColor(Color.YELLOW);
    	
    	if (myCaption.equalsIgnoreCase("Temperatures"))
    	{
    		System.out.println("Temperatures Click again");
    		//this.panelFragment.update();	
    	}

	}

}
